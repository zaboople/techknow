import scala.concurrent.{Promise,Future,Await}
import scala.concurrent.ExecutionContext.Implicits.global
import java.util.Random

def demo1() {
  // This demoes an idea of a checking on a job every so many seconds
  val random=new Random(System.currentTimeMillis)
  case class Job(checkFor : Int){
    val next=random.nextInt(25)
    println("Next "+next)
    def complete = next == checkFor
  }
  def waitFor(originalJob: Job) : Job = {
    def loop(j: Job): Stream[Job] = j #:: loop(Job(j.checkFor))
    loop(originalJob).filter(
      j => j.complete || {Thread.sleep(500); false}
    ).head
  }
  println(waitFor(new Job(12)))
}

def demo2() {
  // This shows right vs wrong way to use streams:
  def loop(j: Int): Stream[String] =
    s"xxxx${j}" #:: (
      if (j<100000000) loop(j+1)
      else Nil.toStream
    )

  // Bad way will start freezing & thrashing in short order:
  // val list=loop(1)
  // for (item <- list) println(item)

  // Good way runs super fast; only difference is we
  // avoid getting a reference to the head of the stream
  // thus allowing the garbage collector to do its magic:
  for (item <- loop(1)) println(item)
}


def demo3() {
  // Even Stream.range is lazy:
  Stream.range(1, 1000000, 2)
    .map(x => {println(x); Thread.sleep(100); x})
    .takeWhile(_ < 100)
    .last
}

demo3()