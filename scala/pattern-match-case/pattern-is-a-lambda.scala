//The List.foreach operator/function normally takes
//a function literal/lambda that receives one value at a time. Since
//patterns are actually lambdas (surprise), the following works:
println
List(1,2,3,-5,6) foreach {
  case a if a<0=>println(a.abs);
  case a if a>5=>println("Too big")
  case a       =>println(a)
}

//This is the same thing, but we actually declared the lambda, because Scala demanded it in
//this case. I put the parentheses around (Int) but didn't really have to. With two parameters I would.
println
val lam:(Int)=>Unit={
  case a if a<0=>println(a.abs);
  case a if a>5=>println("Too big")
  case a       =>println(a)
}

List(-10, 10) foreach lam
