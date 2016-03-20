/** 
 * This is an overrideable wrapper for once-only stuff. 
 */
abstract class Optionish[A](value:A, filters:List[A=>Boolean]=Nil){

  private def ok:Boolean=
    filters.foldLeft(true)(
      (ok:Boolean, f:(A)=>Boolean)=>ok && f(value)
    )

  protected def doWith[T](f:A=>T):T
    
  def foreach(f:A=>Unit)=
    if (ok) doWith(f)
  def map[T](f:A=>T):Option[T]=
    if (ok) Some(doWith(f))
    else None
  def flatMap[T](f:A=>T):Option[T]= 
    if (ok){
      var res:T=doWith(f)
      while (res.isInstanceOf[Some[_]]) 
        res=res.asInstanceOf[Some[T]].get //weird
      if (res==None) None
      else Some(res)
    }
    else None
  def withFilter(f:A=>Boolean):Once[A]=Once(value, f :: filters)

}


/** Now here is a sample override: */
case class Once[A](value:A, filters:List[A=>Boolean]=Nil) extends Optionish[A](value, filters) {
  protected override def doWith[T](f:A=>T)={
    print(">"+value)
    val x=f(value)
    println(value+"<")
    x
  }    
}

/**
 * And finally some examples uses of such:
 */
println("\n\n1.")
val q=
  for (a:String <- Once("-a-");
       b <- Some(99);
       c <- Once("-b-") if a=="-a-";
       number1 <- 1 to 3;
       d <- Some("-d-") if number1 > 1;
       f <- Once("<"+a+b+c+">"))
    yield {
      val ee=a+" "+b+" "+c+" "+number1+" "+d+" "+f
      println("\nI'm gonna yield this: "+ee)
      ee
    }
println(q.getClass+" "+q)

println("\n\n2.")
for (x <-Once("Nothing"))
  println("Alrighty")



println("\n\n3.")
case class OnceList[A <: List[Int]](value:A) extends Optionish[A](value) {
  protected override def doWith[T](f:A=>T)={
    print(">"+value.size)
    val x=f(value)
    println(value+"<")
    x
  }    
}
val zz=
  for (i <- OnceList(List(1,2,3)) if i.size < 1)
    yield i
println(zz.getClass+" "+zz)