/*
  So private[this] is mostly the same as private, except that in the latter case, 
  different instances of the same class can access each other's private variables. 
  So in Scala you usually see private[this] because you don't need instances to access each other
  in that way.  
  
  I would expect protected[this] to work similarly, but I'm not sure I really care that much.
  
 */


class Foo {
  private[this] var h = 12
  private[this] var m = 0
    protected[this] var q=1
  /* Illegal
  def xx(f:Foo){
    println(f.h)
  }
  */
}
class Bar {
  private var h = 12
  private var m = 0
  def xx(f:Bar){
    println(f.h)
  }
}

val e=new Foo();
new Bar().xx(new Bar())
