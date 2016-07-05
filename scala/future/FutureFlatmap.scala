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

{
  // Let's see flatmap in action:
  println("\nEXAMPLE 1:")
  val (a, b, c)=(makeFuture(4000), makeFuture(2000), makeFuture(1000))
  val q:Future[Int]=a
    .flatMap(_ => b)
    .flatMap(_ => c)

  // This will wait on _all_ the futures, not just the last one:
  println("Awaiting")
  Await.result(q, Duration(4000, MILLISECONDS))

  // This only prints the last value. Also note that even though our future
  // runs to completion, this executes concurrently and will very likely
  // print out in the middle of our next example.
  for (x <- q)
    println("Example 1 Complete (am I overrunning the next example?): "+x)
}



{
  // Now let's do for comprehensions
  // This is us merging 3 futures into 1 that we can wait on:
  println("\n\nEXAMPLE 2:")
  val (h, g, f)=(makeFuture(3000), makeFuture(2000), makeFuture(1000))
  val qqq=
    for (zz <- h; yy <- g; aa <- f)
      yield ()
  println("Program is running")
  Await.result(qqq, Duration(15000, MILLISECONDS))
  println("Program is done")
}

{
  val x=Future{20}
  val y=Future{"hello"}
  val list=List(1, 2, 3)
  for (num <- x; str <- y; i <- list)
    println(str+" "+i)
}