import scala.actors.Actor

//This is a variation on actor 7, but it tries to "tune" itself
//to set up the ideal number of Waiters. That is rather difficult
//though given how we deviously vary the traffic levels, and the resulting
//complexity makes it something of a bust.



// CORE CLASSES / TRAITS

case object MsgKill
case object MsgStop
case object MsgAssign
case object MsgDone

class Task (val index:Int, val task: Any) {
  val started=System.currentTimeMillis()
  val strIndex="T"+index
  override def toString=strIndex
  def getWait=System.currentTimeMillis-started
}
abstract class Runner {
  def run(X:Any)
}
abstract class RunnerCreator {
  def make:Runner
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

//Task manager decides what task will run next. It does not maintain an internal queue;
//instead it hands Tasks to the various Waiters, which offer to run the Tasks when they
//are not busy.

class TaskManager extends Actor {
  class WaiterContainer(val waiter:Waiter) {
    var busy=false
    var toRemove=false
    override def toString=waiter.toString
  }
  var runnerCreator:RunnerCreator=null
  var adjTaskWait:Long=0
  var adjWaiterWait:Long=0
  var adjTasksComplete=0
  var tryingToStop=false
  var waitersBusy=0
  var taskCount=0 //FIXME taskCount && tasksAssigned needs to loop around
  var tasksAssigned=0
  var waiterLength=0
  var waiterList:List[WaiterContainer]=List()
  var waiters:Array[WaiterContainer]=Array()
  var taskListReversed=List[Task]()
  var taskList=List[Task]()
  start

  def setRunnerCreator(rc:RunnerCreator)= this ! rc
  def addRunner(r:Runner)=this ! r
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
  
  // TASK ASSIGNMENT: 
  private def assignOutstanding(wc:WaiterContainer)=
    if (tasksOutstanding)
      assignToWaiter(deQueueTaskList, wc)
  private def assignOutstanding=
    while (waitersBusy < waiterLength && tasksOutstanding) 
      assignToWaiter(deQueueTaskList)
  private def deQueueTaskList={
    val t=taskList.head
    taskList=taskList.tail
    t
  }
  private def tasksOutstanding={
    if (taskList.size==0 && taskListReversed.size>0) {
      taskList=taskListReversed.reverse
      taskListReversed=Nil
    }
    taskList.size>0
  }
  private def assignToWaiter(t:Task) {
    for (w<-waiters
         if (!w.busy)){
      assignToWaiter(t,w)
      return
    }
    throw new Exception("Task missed assignment: "+t)
  }
  private def assignToWaiter(t:Task, w:WaiterContainer) {
    waitersBusy+=1
    tasksAssigned+=1
    w.busy=true
    w.waiter ! (MsgAssign, t)
    debug("assigned "+t+" to "+w+" busy:"+waitersBusy+" of "+waiterLength)
  }  
  private def addTask(t:Task) {
    if (waitersBusy<waiterLength)
      assignToWaiter(t)
    else {
      adjustWaiterCountIf
      taskListReversed=t::taskListReversed
      debug("BUSY "+taskListReversed.head)
    }
  }
  
  //WAITER ADJUSTMENT
  private def adjustWaiterCountIf {
    println("Adjust waiters? Complete:"+adjTasksComplete+" Reversed:"+taskListReversed.size)
    if (runnerCreator!=null) {
      if (waiterLength==0) 
        addWaiter(runnerCreator.make)
      else
      if (adjTasksComplete>32)
        adjustWaiterCount
      else
      if (adjTasksComplete>=waiterLength && taskListReversed.size> 28) 
        adjustWaiterCount
    }
  }
  private def adjustWaiterCount {
    var tc=adjTasksComplete
    taskList.foreach(
      t=>{adjTaskWait+=t.getWait;tc+=1}
    )
    taskListReversed.foreach(
      t=>{adjTaskWait+=t.getWait;tc+=1}
    )
    val avgTaskWait  =adjTaskWait  /tc
    val avgWaiterWait=if (adjTasksComplete==0) 0
                      else adjWaiterWait/adjTasksComplete
    debug(" *** COMPLETED "+adjTasksComplete+"\t AVG WAITER WAIT: "+avgWaiterWait+" WAITERS: "+waiterLength)
    debug(" *** TASKS "+tc+"\t AVG TASK WAIT: "+avgTaskWait)
    if (avgTaskWait>150) {
      if (avgWaiterWait>150) 
        //Too much context switching isn't helping
        removeWaiter
      else
      if (avgWaiterWait<60){
        addWaiter(runnerCreator.make)
        if (avgWaiterWait<5)
          addWaiter(runnerCreator.make)
      }
    }
    adjTaskWait=0
    adjWaiterWait=0
    adjTasksComplete=0
  }
  private def adjustWaiters{
    waiters=waiterList.reverse.toArray
    waiterLength=waiters.length
    assignOutstanding
  }
  private def addWaiter(r:Runner){
    debug("*** WAITER ADDED ***")
    waiterList=new WaiterContainer(new Waiter(this, waiterLength, r))::waiterList
    adjustWaiters
  }
  private def removeWaiter {
    if (waiterList.size>1){
      debug("*** WAITER REMOVED ***")
      waiterList.head.waiter ! MsgStop
      waiterList=waiterList.tail
      adjustWaiters
    }
  }

  
  
  //MESSAGES
  def act={
    react {
      case (MsgDone, w:Waiter, waiterWaited:Long, taskWaited:Long)=>{
        debug("Done "+w)
        if (waitersBusy==waiterLength){
          adjTaskWait  +=taskWaited
          adjWaiterWait+=waiterWaited
          adjTasksComplete+=1
        }
        waitersBusy-=1
        //Be careful that this waiter wasn't removed.
        //If he's out of range, or doesn't match his index,
        //he's bad.
        if (w.index<waiters.length) {
          val wc=waiters(w.index)
          if (wc.waiter==w) {
            wc.busy=false
            assignOutstanding(wc)
          }
        }
        adjustWaiterCountIf
        checkStop
      } 
      case rc:RunnerCreator=>{
        runnerCreator=rc
        act
      }
      case newRunner:Runner=>{
        addWaiter(newRunner)
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
      val taskWait=t.getWait
      val myWait=startWork
      debug("received "+t+" it waited: "+taskWait+" i waited: "+myWait)
      runner.run(t.task)
      finishWork
      taskManager ! (MsgDone, this, myWait, taskWait)
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
        //println("\nR"+myIndex+" sleeping "+aValue)
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
def startAll={
  val randomizer=new java.util.Random(System.currentTimeMillis())
  def doRandom(count:Int)=
    (for (x <- 1 to count) 
      yield (1+randomizer.nextInt(10))).foldLeft(1)(_*_)
  def doRandomCounter=randomizer.nextInt(4)
  var runnerIndex= -1
  val tm=new TaskManager()
  tm.setRunnerCreator(new RunnerCreator(){
      def make={
        runnerIndex+=1
        new MyRunner(runnerIndex, tm)
      }
  })
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
