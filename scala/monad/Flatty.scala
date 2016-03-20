/** 
 * This is a single value class, one and only one. So it doesn't implement filter/withFilter. But
 * that also allows it to avoid annoying lists and return actual values.
 */
abstract class Flat[T](value:T){
  protected def doWith[A](f:T=>A):A  
  def foreach(f:T=>Unit):Unit  =doWith(f)
  def map[B](f:T=>B):B         =doWith(f)
  def flatMap[C](f:T=>C):C     =doWith(f)
}

case class MyFlat[T](value:T) extends Flat(value) {
  protected override def doWith[B](f:T=>B):B=
    f(value)
}


// This prints, simply: "6". 
val number:Int=
  for (x <- MyFlat(1);
       y <- MyFlat(2);
       z <- MyFlat(3))
    yield x+y+z       
println(number)


// This prints List(12), because we brought lists into the for comprehension. 
// Note that if MyFlat() were the last item in the for - say, by removing 
// the r<- declaration - this wouldn't even compile because of how we'd mixed & 
// matched listy things with not-listy things.
val numbers=
  for (x <- MyFlat(1);
       q <- List(5);
       y <- MyFlat(2);
       r <- List(4))
    yield x+y+q+r
println(numbers)      
