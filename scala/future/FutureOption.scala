import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Await
import scala.concurrent.duration.Duration

// This handles the problem of
//   f -> Future[Option]
//   g -> Future[Option]
// where we call g() if f() returns None. This looks easy at
// first, but we end up with <Future|Option> inside a Future
// and before long we're nesting for comprehensions
// and/or map-flatMaps in grotesque ways.

// 1. Our general-purpose wrappers that we use to chain the functions:
def begin[T, Input](f: (Input)=> Future[Option[T]], u: Input): Future[Option[T]] =
  continue(None, f, u)
def continue[T, U](o: Option[T], f: (U)=> Future[Option[T]], u: U): Future[Option[T]] =
  o match {
    case x: Some[T] => Future.successful(x)
    case _ => f(u)
  }
def finish[T, U](o: Option[T], f: (U)=> Future[T], u: U): Future[T] =
  o.map(Future.successful).getOrElse(f(u))

// 2. Our "imaginary" functions that struggle to get us what we want:
def futNone(i:Int): Future[Option[Int]]=
  Future{None}
def futSome(i:Int): Future[Option[Int]]=
  Future{Some(i)}
def fut(i:Int): Future[Int]=
  Future{i}

// 3. Some sample usages:
for {
    option <- begin(futNone, 1)
    option <- continue(option, futNone, 1)
    option <- continue(option, futSome, 1)
    option <- continue(option, futNone, 1)
    foo <- finish(option, fut, 1)
  }
  println("Example 1: "+foo)
Thread.sleep(100)
// Here there are three chains:
for {
    x <- begin(futNone, 0);
    x <- continue(x, futNone, 0);
    x <- finish(x, fut, 1)

    x <- begin(futSome, x+1)
    x <- finish(x, fut, -1)

    x <- begin(futSome, x+1)
    x <- finish(x, fut, -1)
  }
  println("Example 2: "+x)
Thread.sleep(100)

