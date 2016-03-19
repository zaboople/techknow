//Here's a decent example:

val n1=List(1,2,3,4)
val n2=List(5,6,7,8)

println(
  for (x <- n1; y <- n2)
    yield y / x
)

println(
  n1.flatMap(
    x=> 
      n2.map(
        y => y/x
      )
  )
)

