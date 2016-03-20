import scala.actors.Actor

//This is a rather bizarre implementation of a queueless task scheduler. Tasks that cannot be run
//are passed around as messages until they can be run, so Scala's Actor messaging system
//basically acts as the queue. Why? I dunno, it's really kinda stupid.

case object MsgKill
case object MsgStop
case object MsgOffer
case object MsgSetWaiter
case object MsgDone
case object MsgAccept
case object MsgPassBack


class Task (val index:Int, val task: Any) {
  val started=System.currentTimeMillis()
}
abstract class Runner {
  def run(X:Any)
}

///////////////////
// TASK MANAGER: //
///////////////////

//Task manager decides what task will run next. It does not maintain an internal queue;
//instead it hands Tasks to the various Waiters, which offer to run the Tasks when they
//are not busy. 

class TaskManager extends Actor {
  var tryingToStop=false
  var currTask=0
  var taskCount=0 //FIXME taskCount needs to loop around
  var taskAcceptedCount=0 //FIXME same deal
  var currWaiter=0
  var waiterLength=0
  var waiterList:List[Waiter]=List()
  var waiters:Array[Waiter]=Array()
  start
  
  def addRunner(a:Runner)=this ! a
  def addTask(a:Any)=this ! a
  def stop()=this ! MsgStop
  def killAll()={this ! MsgKill}
  override def toString="Task Manager"
  
  private def nextWaiter {
    currWaiter+=1
    if (currWaiter==waiterLength)
      currWaiter=0
  }
  private def offerToWaiter(t:Task) {
    if (t.index!=currTask+1) 
      waiters(currWaiter) ! (MsgPassBack, t)
    else {
      debug("offering "+t.index)
      waiters(currWaiter) ! (MsgOffer, t)
    }
  }
  private def checkStop {
    if (tryingToStop) {
      if (taskAcceptedCount==taskCount){
        debug("**** stopping all **** ")
        waiters.foreach(_ ! MsgStop)
        return
      }
      debugs("Stopnot:"+taskAcceptedCount+"-"+taskCount)
    }
    act
  }
  private def debug(s:String) {
    println("\nTM:"+s);
  }
  private def debugs(s:String) {
    print(" TM:"+s);
  }
  def act={
    react {
      case (MsgPassBack, w:Waiter, t:Task)=>{
        debugs("<-"+t.index)
        offerToWaiter(t)
        nextWaiter
        act
      }
      case (MsgAccept, w:Waiter, t:Task)=>{
        val newTime=System.currentTimeMillis
        debug("accepted "+t.index)
        currTask=t.index
        taskAcceptedCount+=1
        nextWaiter
        checkStop
      }
      case (newRunner:Runner)=>{
        //debug("received new runner")
        val w=new Waiter(this, waiterLength, newRunner)
        waiterLength+=1
        waiterList=w::waiterList
        waiters=waiterList.reverse.toArray
        if (waiterLength>1) {
          waiters(waiterLength-2) ! (MsgSetWaiter, w)
          w ! (MsgSetWaiter, waiters(0))
        }
        act
      }
      case MsgStop=>{
        debug("\n\n*** TM received stop request **** \n\n")
        tryingToStop=true
        checkStop
      }
      case MsgKill=>{
        debug("killed")
        waiters.foreach(_ ! MsgKill)
      }
      case a:Any=>{
        taskCount+=1
        debug("New task "+taskCount+" : "+a)
        offerToWaiter(new Task(taskCount, a))
        act
      }
    }
  }
}


//////////////////////////
// WAITER & RUNWRAPPER: //
//////////////////////////

//Waiter receives tasks and either offers to run them, or passes them on if busy. 
//When a task is accepted, Waiter hands it to RunWrapper,
//which actually blocks to run the task. Waiter continues receiving messages.

class Waiter (val taskManager:TaskManager, val index:Int, val runner:Runner) extends Actor {
  val runWrapper=new RunWrapper(this, runner)
  var nextWaiter:Waiter=this
  var busy=false;
  var toldToStop=false
  var notBusyStart=System.currentTimeMillis
  start
  
  private def debug(s:String) {
    println("\nW"+index+": "+s)
  }
  private def debugs(s:String){
    print(" W"+index+s)
  }
  def act=react{
    case (MsgSetWaiter, w:Waiter)=>{
      nextWaiter=w
      act
    }
    case (MsgOffer, t:Task)=>{
      if (!busy){
        debug("accepted, task wait: "+(System.currentTimeMillis()-t.started)+" index: "+t.index
             +" my wait: "+(System.currentTimeMillis()-notBusyStart))
        busy=true
        taskManager ! (MsgAccept, this, t)
        runWrapper ! t
      }
      else {
        debugs("!->"+t.index+"!->"+nextWaiter.index)
        nextWaiter ! (MsgOffer, t)
      }
      act
    }
    case MsgDone=>{
      notBusyStart=System.currentTimeMillis()
      busy=false
      if (!toldToStop) act
      else debug("STOPPED")
    } 
    case (MsgPassBack, t:Task)=>{
      if (busy) {
        debugs("->"+t.index+"->"+nextWaiter.index)
        nextWaiter !  (MsgPassBack, t)
      }
      else {
        debugs("^"+t.index)
        taskManager ! (MsgPassBack, this, t)
      }
      act
    }
    case MsgStop=>{
      //We will not receive this until all outstanding Tasks have been accepted
      runWrapper ! MsgStop
      if (busy) {
        toldToStop=true
        act
      }
      else
        debug("STOPPED")
    }
    case MsgKill=>{
      debug("killed")
      runWrapper ! MsgKill
    }
    case a:Any=>throw new RuntimeException("WTF "+a);
  }
}


class RunWrapper(val waiter: Waiter, val runner:Runner) extends Actor {
  start
  def act=react{
    case t:Task=>{
      runner.run(t.task)
      waiter ! MsgDone
      act
    }
    case MsgStop=>println("RunWrapper stopped")
    case MsgKill=>println("RunWrapper killed")
  }
}



///////////////////
// TEST HARNESS: //
///////////////////


class MyRunner(val myIndex:Int, val tm:TaskManager) extends Runner {
  def run(task:Any)=
    task match {
      case (aValue:Int)=>{
        println("\nR"+myIndex+" sleeping "+aValue)
        if (aValue>10000)
          println("\n\n\n****MyRunner OH BOY**** "+aValue+"\n\n\n")
        Thread.sleep(aValue.toLong)
      }
      case (s:String)=>
        tm.stop
      case x:Any=>
        println("MyRunner UNKNOWN "+x)
    }
}


val randomizer=new java.util.Random(System.currentTimeMillis())
def doRandom(count:Int)=
  (for (x <- 1 to count) 
     yield (1+randomizer.nextInt(10))).foldLeft(1)(_*_)
      

def startAll={
  val tm=new TaskManager()
  for (x<-0 to 5)
    tm.addRunner(new MyRunner(x, tm))
  for (x<-0 to 500){
    Thread.sleep(doRandom(randomizer.nextInt(4)))
    tm.addTask(doRandom(randomizer.nextInt(5)))
  }
  tm.addTask("Stop")
}

startAll