/** 
  I am going to use these two lambdas throughout:
*/
val a=(i:Int)=>i+1
val b=(i:Int)=>i*2



/**
  Now I'm going to show two ways to create a function that does this:
    b(a(b(a(3))))
  Notice that compose & andThen are opposites, so that the a's & b's are in reverse order.
  One might argue that compose() provides a more readable result.
 */
println("\nExample 1")
val qr=b compose a compose b compose a
println(qr(3))
val qs=a andThen b andThen a andThen b
println(qs(3))


/**
 Now I'm just doing the same thing on the fly. Is this easier to read than a(b(a(b(3))))? Meh.
 */
println("\nExample 2")
println((a andThen b andThen a andThen b)(3))
println((a compose b compose a compose b)(3))


/**
  I'm going to try to make this concept a little more succinct using a helper function & varargs:
 */
println("\nExample 3")
def composeAll[A](fs:List[(A)=>A]):(A)=>A = 
  if (fs==Nil)
    (a:A)=>a
  else
    composeAll(fs.tail) compose fs.head //Sorry I'm too lazy too be tail-recursive
def composeAll[A](fs:(A)=>A*):(A)=>A =
  composeAll(fs.toList)

println(composeAll(a,b,a,b)(4))