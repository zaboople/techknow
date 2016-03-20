//Three examples here:


//1. Here is a curried function:
def adder(x: Int)(y: Int)(z: Int) = x + y + z

//And a weird-looking invocation
println(
  adder(1)(2)(3)
)
//Didn't realize it but you can put brackets around function params:
println(
  adder{1}{2}{3}
)

//Invoking the adder partway and then finishing:
{
  println
  val toUse=adder(1)(2)_
  println(toUse(4))
  println(toUse(5))
  println(toUse(6))
}
{
  println
  val toUse=adder(10)_
  println(toUse(20)(30))
  println(toUse(30)(40))
  println(toUse(40)(50))
}


//2. This accomplishes the same result as currying, except one difference.
//   You don't have to use the stupid "_" to get the partway thing
println
def adder2(a:Int)= 
          (b:Int)=> 
          (c:Int)=>a+b+c
println(adder2(1)(2)(3))
val x=adder2(1)
println(x(2)(3))
println(x(3)(4))


//3. Thought I should demonstrate that you can have more than one parameter
//   per segment
def adder2(a: Int, x:Int)(y: Int)(z: Int) = a - (x + y + z)