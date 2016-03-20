//Without the "annotation", we would end up with Int collections. But we force the members
//to become Doubles instead with this trick:

println(
  Set[Double](12)
  +
  ""
  +
  List[Double](1,2,3)
)

//This would not work for List[String](1,2,3), however