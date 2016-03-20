//This is a more sensible scheduler that uses an internal queue. The queue is still weird, as it's
//two lists.

import scala.actors.Actor
case object MsgKill
case object MsgStop
case object MsgAssign
case object MsgDone

////////////////////////
//Core classes/traits://
////////////////////////

class Task (val index:Int, val task: Any) {
  val started=System.currentTimeMillis()
  val strIndex="T"+index
  override def toString=strIndex
  def getWait=System.currentTimeMillis-started
}
abstract class Runner {
  def run(task:Any)
}
trait MyTimer {
  var notBusyStart=System.currentTimeMillis
  var totalWaited:Long=0
  var totalExecuted:Long=0
  def startWork={
    val currTime=System.currentTimeMillis
    val myWait=currTime-notBusyStart
    totalWaited+=myWait
    totalExecuted+=1
    myWait
  }
  def finishWork={
    notBusyStart=System.currentTimeMillis
  }
  def averageWait=if (totalExecuted==0) 0 
                  else totalWaited/totalExecuted  
}

///////////////////
// TASK MANAGER: //
///////////////////

//Task manager decides what task will run next. 

class TaskManager extends Actor {

  var tryingToStop=false
  var waitersBusy=0
  var taskCount=0 //FIXME taskCount && tasksAssigned needs to loop around
  var tasksAssigned=0

  //The waiterList and waiters structures contain the same objects. Waiters is just optimized
  //for random access, and waiterList is optimized for easily adding elements.
  class WaiterContainer(val waiter:Waiter) {
    var busy=false
    override def toString=waiter.toString
  }
  var waiterLength=0
  var waiterList:List[WaiterContainer]=List()
  var waiters:Array[WaiterContainer]=Array()

  //When all Waiters are busy, new Tasks are added to taskListReversed. When
  //Waiters are free and taskList is empty, taskListReversed is reversed and the
  //result becomes taskList. taskListReversed is replaced with a new empty list,
  //and Waiters receive new tasks from taskList until it is empty again. 
  //This way we get first-in-first-out Task scheduling. 
  var taskListReversed=List[Task]()
  var taskList=List[Task]()
  
  //Start this actor:
  start
  
  def addRunner(a:Runner)=this ! a
  def addTask(a:Any)     =this ! a
  def stop()             =this ! MsgStop
  def killAll()          =this ! MsgKill
  override def toString="Task Manager"
  
  private def debug(s:String) {
    println("TM:"+s);
  }
  private def debugs(s:String) {
    debug(s)
  }


  private def checkStop {
    if (tryingToStop) {
      if (tasksAssigned==taskCount){
        debug("**** stopping all **** ")
        waiters.foreach(_.waiter ! MsgStop)
        return
      }
      debugs("Stopnot:"+tasksAssigned+"-"+taskCount)
    }
    act
  }
  private def assignOutstanding(wc:WaiterContainer) {
    while (waitersBusy < waiterLength && taskList.size>0) {
      debugs("Off task list")
      if (wc==null)
        assignToWaiter(taskList.head)
      else
        assignToWaiter(taskList.head, wc)
      taskList=taskList.tail
    }
    if (waitersBusy < waiterLength && taskListReversed.size>0) {
      debugs("Reversing")
      taskList=taskListReversed.reverse
      taskListReversed=Nil
      assignOutstanding(wc)
    }
  }
  private def assignToWaiter(t:Task) {
    debug("assigning "+t)
    for (w<-waiters
         if (!w.busy)){
      assignToWaiter(t,w)
      return
    }
  }
  private def assignToWaiter(t:Task, w:WaiterContainer) {
      waitersBusy+=1
      tasksAssigned+=1
      debug("assigned "+t+" to "+w)
      w.busy=true
      w.waiter ! (MsgAssign, t)
  }  
  private def addTask(t:Task) {
    if (waitersBusy<waiterLength)
      assignToWaiter(t)
    else {
      taskListReversed=t::taskListReversed
      debug("BUSY "+taskListReversed.head)
    }
  }
  def act={
    react {
      case (MsgDone, w:Waiter)=>{
        debug("Done "+w)
        waiters(w.index).busy=false
        waitersBusy-=1
        assignOutstanding(waiters(w.index))
        checkStop
      } 
      case newRunner:Runner=>{
        //debug("received new runner")
        waiterList=new WaiterContainer(new Waiter(this, waiterLength, newRunner))::waiterList
        waiterLength+=1
        waiters=waiterList.reverse.toArray
        assignOutstanding(null)
        act
      }
      case MsgStop=>{
        debug("\n\n*** TM received stop request **** \n\n")
        tryingToStop=true
        checkStop
      }
      case MsgKill=>{
        debug("killed")
        waiters.foreach(_.waiter ! MsgKill)
      }
      case a:Any=>{
        taskCount+=1
        debug("New task "+taskCount+" : "+a)
        addTask(new Task(taskCount, a))
        act
      }
    }
  }
}


/////////////
// WAITER: //
/////////////

//A Waiter "waits" on its Runner to run a Task. So when the 
//Runner is running, the Waiter blocks and stops reading
//from its message queue. 
class Waiter (val taskManager:TaskManager, val index:Int, val runner:Runner) extends Actor with MyTimer {
  
  val myname="W"+index
  start

  override def toString=myname
  private def debug(s:String) {
    println(myname+":"+s)
  }
  private def finishUp={
    debug("STOPPED, Avg. wait "+averageWait)  
  }
  
  def act=react{
    case (MsgAssign, t:Task)=>{
      debug("received "+t+" it waited: "+t.getWait+" i waited: "+startWork)
      runner.run(t.task)
      finishWork
      taskManager ! (MsgDone, this)
      act
    }
    case MsgStop=>finishUp
    case MsgKill=>{}
    case a:Any=>throw new RuntimeException("WTF "+a);
  }
}



///////////////////
// TEST HARNESS: //
///////////////////


class MyRunner(val myIndex:Int, val tm:TaskManager) extends Runner {
  def run(task:Any)=
    task match {
      case (aValue:Int)=>{
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
def doRandomCounter=randomizer.nextInt(4)
def startAll={
  val tm=new TaskManager()
  for (x<-0 to 3)
    tm.addRunner(new MyRunner(x, tm))
  var r1=doRandomCounter
  var r2=doRandomCounter
  for (x<-0 to 4000){
    if (x % 128==0){
      r1=doRandomCounter
      r2=doRandomCounter
    }
    Thread.sleep(doRandom(r1))
    tm.addTask(doRandom(r2))
  }
  tm.addTask("Stop")
}

startAll