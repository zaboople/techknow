/** 
 * ultraFlat flattens the crap out of darn near anything.
 */
def ultraFlat(i:Iterable[_]):Iterable[_]={
  def taker(x:Any):scala.collection.GenTraversableOnce[Any] = 
    x match {
      case z:Iterable[_]=> 
        (
          for(y <- z) 
            yield taker(y)
        ).flatMap(q=>q)
      case z:Array[_]=>{
        val q:Iterable[_]=z
        taker(q)
      }
      case z:Some[_]  => z
      case z:Product  => ultraFlat(z.productIterator.toIterable)
      case z =>  Some(z)
    }
  i.flatMap(taker)
}

println(ultraFlat(
  Map(1 -> 
    List(
      1,2, Array(987,654),
      Map(82828 -> 2833833),
      (10,11,12)
    )
  )
))
println(
  ultraFlat(
    Seq(
      List(122, 7, 9), 
      Seq(2), 
      Some(3), 
      Map(1111->2222, 3333->4444, 0->22),
      List(
        List(
          1,2,List(3), 
          Array(3838,4848, Some(22))
        )
      )
    )
  )
)

println(ultraFlat(Map(1->2, 3->4)))

println(ultraFlat(
  List((1,2,3,3,4,4,5,5,5,5,5))
))

println(ultraFlat(
  List(
    (1,2,3), (444,55,566)
  )
))

println("\nLooks wrong:")
println(ultraFlat(
  (1,2).productIterator.toIterable
))


println("\nLooks right:")
println(ultraFlat(List(
  (1,2)
)))
