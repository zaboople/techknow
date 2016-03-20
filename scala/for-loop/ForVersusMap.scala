// Illustrates how for turns into map + flatMap
// The two print statements give the exact same result:

val n1=List(9,10,11)
val n2=List(5,6,7,8)
val n3=List(1,2,3,4)

println()
println(
  for (x <- n1; y <- n2; z<-n3)
    yield compute(x, y, z)
)

println()
println(
  n1.flatMap(
    x=>n2.flatMap(
      y=>n3.map(
        z => compute(x, y, z)
      )
    )
  )
)

def compute(x:Int, y:Int, z:Int)=
  x+" "+y+" "+z+"=>"+(x * y / z )