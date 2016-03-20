//3. FUNCTIONS THAT TAKE LAMBDAS AS INPUT:


/**
 * Example 1: Takes arguments, returns a value:
 */
println("\nExample 1")
def encodeHello(func:(String, Int)=>Int):Int=
  func("Hello", 1)+1000

println (
  encodeHello((name:String, start:Int)=>name.length+start)
)

// Shorter:
println (
  encodeHello((name, start)=>name.length+start+10)
)

// Even shorter, not necessarily readable:
println (
  encodeHello(_.length+_ + 100)
)


/**
 *  Example 2: No arguments, no return value
 */
println("\nExample 2")
def test(aFunction: => Unit)={
  println("About to...")
  for (i <- 1 to 3) aFunction
  println("Done.")
}
//Note how I can use curlies for two statements, parens for only one.
test{
  print("  And... ")
  println("  Hey I am a big lambda executing ")
}
test(
  println("  Hey I am another big lambda executing ")
)


/**
 *  Example 3: No arguments, returns a value:
 */
println("\nExample 3")
def test2(aFunction: => Int)={
  println("About to...")
  for (i <- 1 to 3) println("  Lambda returns: "+aFunction)
  println("Done.")
}
var i=10
test2{i+=1; i}
