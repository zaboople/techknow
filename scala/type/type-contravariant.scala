/*
  It is probably best to read type-covariance.scala first.

  Adding the "-" makes this class contravariant. As far as what's
  legal and illegal, it's basically the opposite of covariant. 

  Also, there is a <: that is like :> we use in covariants, but it seems useless 
  within a class. More for static methods.
*/
class X[-T](
    //Illegal:
    //val list:List[T]
  ) {

  //Illegal:
  //var list=List[T]();
  //def add(x:T, y:T):Tuple2[T]=(x,y)
  
  //Legal
  def add(x:T, y:T):Set[Any]={
    Set(x,y)
  }
  def addBetter[U <: T](x:U, y:U):Set[U]={
    Set(x,y)
  }

}

//Without the "-" above, these would not work. 
val x:X[String] =new X()
val y:X[String] =new X[AnyRef]()

//For what it's worth:
println(x.add("hi","hello"))

