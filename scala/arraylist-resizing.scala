val limit=args(0).toDouble
var i:Double=16
var tossed=i
while (i<limit) {
  tossed=tossed+i
  i=i*1.5
  println("Size %,12.0f Tossed %,12.0f".format(i, tossed))
}
val wasted=(i-limit)+tossed
println("Limit was %,12.0f".format(limit))
println("Wasted total %,12.0f ".format(wasted))
println("Fraction %,5.5f ".format(limit/wasted))