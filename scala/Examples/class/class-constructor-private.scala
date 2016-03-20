//This cannot be instantiated directly from outside
class X private(val a:Int, val b:Int)  
//Illegal:
//val c=new X(1,2)

//This can, but the values are inaccessible
class Y (private val a:Int, private val b:Int)  
val c=new Y(1,2)
//Illegal:
//println(c.a)

//Here's how we make an available constructor when the constructor is private.
//Note that this() cannot do anything but call a different this(). It cannot
class Z private (private val a:Int, private val b:Int)  {
  def this(start: Int)=this(start/2, start%2)
  def half=a
  def remainder=b
  //Illegal:
  //def this(start: Int)={}
}
val z=new Z(13);
println(z.half+" "+z.remainder)

//This is another way, which takes advantage of apply() but
//note how you can't use "new" with this method. This is
//also explained in class-constructor-no-new.scala:
object Z {
  def apply(start: Int)=new Z(start/2, start%2)
}
val x=Z(14)
println(x.half+" "+x.remainder)
