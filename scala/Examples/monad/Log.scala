/** 
 * This is a single value class, one and only one. So it doesn't implement filter/withFilter. But
 * that also allows it to avoid annoying lists and return actual values.
 *
 * This is also stupid, but I've almost implemented it so many times that I might as leave it as a
 * reminder not to ever do it again. It's just as effective to have a lambda function like so:
    def log[T](value:T, prefix:String):T={
      println(prefix+"\t-> "+value)
      value
    }
 */
case class MyLog[T](value:T, prefix:String){
  protected def doWith[B](f:T=>B):B={
    println(prefix+"\t-> "+value)
    f(value)
  }
  def foreach(f:T=>Unit):Unit  =doWith(f)
  def map[B](f:T=>B):B         =doWith(f)
  def flatMap[C](f:T=>C):C     =doWith(f)
}



// This prints, simply: "6". 
val number:Int=
  for (x <- MyLog(1,     "init");
       y <- MyLog(2 * x, "By 2");
       z <- MyLog(x+y+4, "x+y+4");
       q <- MyLog(x+y+z, "x+y+z")
       ) 
    yield q
println("Result: "+number)
