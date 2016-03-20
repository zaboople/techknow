def sum(a: Int, b: Int, c: Int) = a + b + c

//This redefines a sort of function that provides one parameter
val sum4=sum(4, _:Int, _:Int)

//So now we call that with only two params:
println(
  sum4.apply(1, 3)
)

//Which is the long form for this trick... since apply is implied. Play ply apply plywood why....
println(
  sum4(1, 3)
)