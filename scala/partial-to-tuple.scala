def bork(i:Int, d:Double, s:String)=
  s+": "+(i+d)

val tupf=(bork _).tupled
val tup = (3, 4.1, "hello")
println(tupf(tup))

val curf=(bork _).curried
val a=curf(7)
val b=a(12.1)
val c=b("bye")
println(c)