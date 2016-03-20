/** This is cute but sort of stupid. I'm not sure I get anything out of doing this. */
import scala.collection.GenTraversableOnce
case class Ranger(a:Appendable, size:Int, filters:List[Int=>Boolean]=Nil){
  a.append("\nConstructor "+size+" "+filters+";")
  def log(name:String, value:Int){
    a.append(" ")
    a.append(name)
    a.append(" ")
  }
  private def ok(i:Int):Boolean=
    filters.foldLeft(true)(
      (ok:Boolean, f:(Int)=>Boolean)=>ok && f(i)
    )
  def foreach(f:Int=>Unit){
    log(" foreach", size)
    for (j <- 0 to size if ok(j))
      f(j)
  }
  def map[T](f:Int => T):IndexedSeq[T]={ 
    log("map", size)
    for (j <- 0 to size if ok(j))
      yield
        f(j)
  }
  def flatMap[T](f:Int => GenTraversableOnce[T]):GenTraversableOnce[T]= {
    log("flatmap", size)
    val res=Nil;
    for (j <- 0 to size if ok(j); 
         q <- f(j).toList)
      yield  q
  }  
  def withFilter(f:Int => Boolean):Ranger = 
    Ranger(a, size, f :: filters)

  
}

val a:Appendable=new java.lang.StringBuilder()

val q=
  for (x <- Ranger(a, 10) if x > 3;
       q <- 1 to 2        if q < 3;
       y <- Ranger(a, x)  if y > 2 && y < 10;
       z <- Ranger(a, y)  if z > 7;
       q="OK "+z)
    yield q

println(a)
println(q.getClass+" "+q)
