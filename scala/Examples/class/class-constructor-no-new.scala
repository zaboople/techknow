//This shows how we can use a "companion" class to
//create a static pseudo-constructor that eliminates the
//need for a "new". 

//Note: You can also do this with case classes

class Dinkus(i:Int) {
  def height=2*i;
  def weight=10*i;
}
object Dinkus {
  def apply(x:Int)=new Dinkus(x);
}

val dink=Dinkus(2);
println(dink.height+" "+dink.weight);

