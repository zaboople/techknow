/** 
    Scala's map is broken because it not only allows you to store nulls, but when you retrieve them you get 
    Some(null), which is stupid. So first I'm going to implement a wrapper that fixes some of this:
  */

object Mapper {
  /** Allows me to create a mapper using Mapper(a->a1, b->b1, ....) */
  def apply[A,B](stuff: Tuple2[A,B]*):Mapper[A,B]=
    Mapper(stuff.toMap)
}
case class Mapper[A,B](map:Map[A,B]){
  def get(name:A)=
    map.get(name) match {
      case Some(null) =>None
      case x  => x
    }
  lazy val values=map.values.filter(
      _ match {
        case null =>false
        case _    =>true
      }
    )
}
  



/** And using it the broken way: */
{
  val map=Map("hello"->null, "some"->1, "what"->null)
  println("\nBROKEN")
  print("  Loop... ")
  for (x <- map.values)
    print(x)  
  print("\n  Get...")
  println(map get "hello")
  println(map get "nothing")
}

/** Fixed using my wrapper: */
{
  val map=Mapper("hello"->null, "some"->1, "what"->null)
  println("\nFIXED")
  print("  Loop... ")
  for (x <- map.values)
    print(x)  
  print("\n  Get...")
  println(map get "hello")
  println(map get "nothing")
}
