import scala.concurrent.{Await, ExecutionContext, Future, Promise}
import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.{Try, Success, Failure}
import scala.concurrent.duration.{Duration, MILLISECONDS}

// The scaladoc for Future.flatMap() & map() is dead-silent about what these do.
// 1. flatMap() allows you to "aggregate" futures together, invisibly. This allows
//    you to do cute things in for comprehensions as well.
// 2. map() is for converting the value inside the future.
def makeFuture(ms:Int)=Future{
  println("Sleeping "+ms)
  Thread.sleep(ms)
  println("Slept    "+ms)
  ms
}


val (a, b, c)=(makeFuture(4000), makeFuture(2000), makeFuture(1000))
val q:Future[Int]=c
  .flatMap(_ => b)
  .flatMap(_ => a)

// This will wait on _all_ the futures, not just the last one:
println("Awaiting")
Await.result(q, Duration(4100, MILLISECONDS))

// This only prints the last value
for (x <- q)
  println("Complete: "+x)