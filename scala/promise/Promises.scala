/**
  The difference between Promise & Future is:

  When created directly, a Future starts executing as soon as it can. You can't tell it to hold off.

  A Promise doesn't run any logic. You create a Promise, attach some logic to its Future (yes it
  has a built in Future), and nothing happens. You can then pass it to other logic hoping somebody
  calls Promise.success() or Promise.failure() on it, which will automatically trigger the logic
  you attached to that Future.

  So a promise is mostly just a fancy lambda callback system; perhaps you could describe it as
  "standardized". Meh. It really doesn't make sense as a return value, since why not return a
  Future? It's better as an input value. It isn't making a promise, it's making a request.

  So is there really a point in using Promises? Why not just Futures?
**/
import scala.concurrent.{Promise,Future,Await}
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.{Duration,MILLISECONDS}
val duration = Duration(100, MILLISECONDS)

case class TaxCut(reduction: Int){
  println("Made a tax cut "+reduction);
}

def findOut(p: Promise[TaxCut]) {
  Future {
    println("Starting the new legislative period.")
    Thread.sleep(2000)
    println("We reduced the taxes...")
    p.success(TaxCut(20))
    //This never prints out because nobody waits on this future
    Thread.sleep(5000)
    println("We still hate you")
  }
}

//None of this is going to block:
val promise=Promise[TaxCut]()
promise.future.onSuccess {
  case z => println("Success: "+z)
}
promise.future.onFailure {
  case z=> println("Fail "+z)
}
findOut(promise)

// Normally you would wait using Await (further down),
// but this gives a better idea of what's happening:
for (i <- 1 to 100 if !promise.isCompleted){
  println("Waiting...")
  Thread.sleep(150)
}
// This doesn't do anything, since it's already done
Await.ready(promise.future, Duration(20500, MILLISECONDS))