//Normally tuples are written using the syntax in the first statement, but as best
//as I can tell the second is equivalent:
println
println("Verify map syntax is tuple syntax:");
val x=("abc", 123)
val y="abc"->123
println(x == y)
println(x.getClass()+" "+y.getClass())

//Here is a tuple of tuples
println
println("Tuple of tuples:");
val q=( 
  ( (1,2), (3, 4, 5) ), 
  ( (5,6), (7, "hi") )
)
println(q._2._2._2)

//Back to the syntax.... turns out yes, you can make a map using tuples.
println
println("Map made with tuples:");
val map1=Map((1,2))
val map2=Map(1->2)
println(map1 +" "+map2)
println(map1 == map2)
println((map1 get 1).get);

//This does not create a Tuple4. It creates nested tuple2's that look like this:
//  (((Int, Int), Int), Int) = (((1,2),3),4) 
println
println("Nested tuples using map syntax:");
val dink=1->2->3->4
println(dink._2)
println(dink._1._1._2)