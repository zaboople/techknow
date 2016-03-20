//This applies multiple inputs to a pattern by making them into a tuple on the fly. 
//Didn't realize this could be done until I saw an example, but it's really pretty obvious.
//Tuples are nice things.

def foo(a:Int, b:Int, c:Int)=(a,b,c) match {
  case (1,2,3) =>println("woop")
  case (_, _, _) =>println(a+" "+b+" "+c+" Weren't satisfactory, feh")
}

println
foo(1,2,4)
foo(1,2,3)