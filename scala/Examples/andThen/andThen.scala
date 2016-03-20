//These have to be defined as vals
val a=(i:Int)=>i+1
val b=(i:Int)=>i*2

//This creates a function that calls b(a(b(a))) 
val q=a andThen b andThen a andThen b
println(q(3))

//This does the same thing. Notice how the components are reversed, though:
val qr=b compose a compose b compose a
println(qr(3))

//Look how we do it on the fly:
println((a andThen b andThen a andThen b)(3))

//This is probably better though, since we can read it as a(b(a(b(3)))
println((a compose b compose a compose b)(3))