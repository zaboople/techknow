/**
 * Implements a list of random numbers the hard way:
 */
{
  import scala.collection.GenTraversableOnce
  case class RandomList(size:Int, filters:List[Int=>Boolean]=Nil){
    private val random=new java.util.Random(System.currentTimeMillis)
    private def ok(i:Int):Boolean=
      filters.foldLeft(true)(
        (good:Boolean, f:(Int)=>Boolean)=>good && f(i)
      )
    def walk=(1 to size).map(random.nextInt(_)).filter(ok(_))
    def foreach(f:Int=>Unit)=
      walk.foreach(f(_))
    def map[T](f:Int => T):GenTraversableOnce[T]=
      walk.map(f(_))
    def flatMap[T](f:Int => GenTraversableOnce[T]):GenTraversableOnce[T]=
      walk.flatMap(f(_))
    def withFilter(f:Int => Boolean):RandomList =
      RandomList(size, f :: filters)
  }
  {
    val q=
      for {
          a <- List(10, 20, 30)
          x <- RandomList(a)  if x > 3
          y <- RandomList(x)  if y > 2 && y < 30
          z <- RandomList(y)  if z > 3
        }
        yield z
    println(q)
  }
}

/**
 * Aaaaand: The easier way:
 */
{
  val random2 = new java.util.Random(System.currentTimeMillis)
  def grab(size: Int) = (1 to size).map(random2.nextInt(_)).toList
  val q=
    for {
        a <- List(10, 20, 30)
        x <- grab(a)  if x > 3
        y <- grab(x)  if y > 2 && y < 30
        z <- grab(y)  if z > 3
      }
      yield z
  println(q)
}
