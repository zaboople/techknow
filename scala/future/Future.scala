import scala.concurrent.{Await, ExecutionContext, Future, Promise}
import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.{Try, Success, Failure}
import scala.concurrent.duration.{Duration, MILLISECONDS}



// Await.result waits for the future and blows up appropriately if it doesn't work out
try {
  println(
    Await.result(
      Future[Int](throw new Exception("har har")),
      Duration(1000, MILLISECONDS)
    )
  )
} catch {
  case e : Exception => println("Caught!")
}

// This assumes the future will not run indefinitely, which is generally not that unreasonable:
println(
  Await.result(
    Future(1 * 100 + 12), Duration.Inf
  )
)



