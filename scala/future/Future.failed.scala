import scala.concurrent.{Await, ExecutionContext, Future, Promise}
import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.{Try, Success, Failure}
import scala.concurrent.duration.{Duration, MILLISECONDS}

// Getting the failed projection of a future essentially means
// "I expect this future to fail", so that when you ask for its result,
// you get the exception as a return value, rather than the exception
// blowing up all over you.
//
// However, if your future actually succeeded, then when you try to get
// the value of the failed projection it will blow up all over you.
//
// This is sort of handy for testing.
val f=Future{
  if (true)
    throw new NullPointerException("Ha ha splat")
  12
}
// Get the failed projection:
val fail=f.failed
val failResult=Await.result(
  fail, Duration(1000, MILLISECONDS)
)
println(failResult)