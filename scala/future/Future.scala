import scala.concurrent.{Await, ExecutionContext, Future, Promise}
import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.{Try, Success, Failure}
import scala.concurrent.duration.{Duration, MILLISECONDS}



// This allows us to extract the real value of the future in cases where we darn well
// want it now.
def getOrDie[T](f : Future[T], ms : Int = 1000) : T =
  Await.result(f, Duration(ms, MILLISECONDS))
try {
  println(
    getOrDie(
      Future[Int](throw new Exception("har har"))
    )
  )
} catch {
  case e : Exception => println("Caught!")
}
println(
  getOrDie(
    Future(1 * 100 + 12)
  )
)


Future(println("wokka")).map(x=>println(x))

Future("hello").map{
  case d:String=>println("string")
  case _ => println("why?")
}

Thread.sleep(1000)


//This is us merging 3 futures into 1 that we can wait on:
val h=x(3000)
val g=x(2000)
val f=x(1000)
val qqq=for (zz <- f.recover(allow); yy <- g.recover(allow); mm<-h.recover(allow)) yield ()
println("Program is running")
Await.result(qqq, Duration(15000, MILLISECONDS))
println("Program is done")