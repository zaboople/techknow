//This is partly ripped off from the Scala book:

/*
  Core definition. Note that this could be a trait; all that "abstract class" does is disallow
  inheritance from other classes (but not traits). Better performance too I guess:
*/
abstract class IntQueue {
  def get(): Int
  def put(x: Int):IntQueue
  def each(x:(Int=>Unit))
  def getAll()={
    val sb=new StringBuilder();
    each(x=>sb.append(" "+x));
    sb.toString();
  }
}

/*
  Intermediate definition. This is where we get most functionality from. 
*/
import scala.collection.mutable.ArrayBuffer
class BasicIntQueue extends IntQueue {
  private val buf = new ArrayBuffer[Int]
  def get() = buf.remove(0)
  def put(x: Int):IntQueue={ buf += x; this }
  def each(x:(Int=>Unit))=
    buf.foreach(x(_))
}


/*
  Here are the cute parts
*/
trait Doubling extends IntQueue {
  abstract override def put(x: Int):IntQueue=super.put(2 * x) 
}
trait Incrementing extends IntQueue {
  abstract override def put(x: Int):IntQueue=super.put(x + 1) 
}
trait Filtering extends IntQueue {
  abstract override def put(x: Int):IntQueue= 
    if (x >= 0) super.put(x)
    else this
}

///////////
// TESTS //
///////////

/* 
  1. Boring:
*/
println (
  (new BasicIntQueue with Doubling).put(1).put(2).put(3).getAll()
)

/*
  2. Much more interesting -
     These give different results, based on Scala's "stacking" protocol. The core concept
     is that rightmost goes first:
*/

//1. Incrementing happens before Filtering, so the negative # makes it in as 0.
println (
  (new BasicIntQueue with Filtering with Incrementing).put(-1).put(2).put(3).getAll()
)
//2. Filtering goes first, so the -1 never gets in:
println (
  (new BasicIntQueue with Incrementing with Filtering).put(-1).put(2).put(3).getAll()
)
