case class RandomInput(filters:List[(Int)=>Boolean] = Nil){
  private val random=new java.util.Random(System.currentTimeMillis)
  
  private def check(value:Int):Boolean=
    filters.foldLeft(true)(
      (ok:Boolean, f:Int=>Boolean)=>ok && f(value)
    )
  private def doWith[A](f:Int=>A):IndexedSeq[A]=
    for (i<- Range(0,10); 
         q=random.nextInt(10) if check(q))
      yield f(q)

  def foreach(f:Int=>Unit):Unit=
    doWith(f)
  def map[B](f:Int=>B):IndexedSeq[B]=
    doWith(f)
  def flatMap[C](f:Int=>C):IndexedSeq[C]=
    doWith(f).flatten
  def withFilter(f:Int=>Boolean)=
    RandomInput(f::filters)
}

val x=
  for (line  <- RandomInput() if line > 2;
       line2 <- RandomInput() if line2 < 3)
    yield line+line2
print(x)
