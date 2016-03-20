/*
  This also applies to traits, btw.
  Covariance like this is not allowed in Java. 
  Working with a covariant is extremely restrictive, to 
  prevent type errors.
*/
//Adding the "+" makes this class covariant. 

class X[+T](val list:List[T]) {
  def head=list.head
  def all=list
  
  //Illegal; T can never be a method parameter. Nope, never.
  //def add(t:T){}
  
  //Workaround: a "lower bound".
  def add[U >: T](u:U):X[U]=new X(u :: list)
}


//Without the "+" above, this would not work:
val x:X[AnyRef] =new X(List("strang", "str", "ding"))
println(x.head)
println(x.add("Hello").all)

//Shockingly, this works. But what you're actually getting is a list
//of a different type. 
println(x.add(1).all)
val what:Any=x.add(1).head
println(what.getClass())