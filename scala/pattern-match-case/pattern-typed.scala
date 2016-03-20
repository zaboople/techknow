//Note how we put in wildcards where parameterized/generic types would go on our Map. 
//Scala is irritating in that it will demand a type from you, and when you give one, 
//it starts barking warnings about type erasure. 

//1. Basic types demo:
def getSize(x: Any) = x match {
  case s: String        => s.length
  case m: Map[_, _]     => m.size
  case l: List[_]       => l.size
  
  //Here, dinkus is actually a variable, but you can't do much with it. 
  //One thing can do is create a Map[dinkus,dinkus]().
  case a: Array[dinkus] => {Map[dinkus,dinkus](); a.size}
  
  //The usual catch-all thing:
  case _                => 1

  //This will generate a type erasure warning:
  //case m: Map[Int,Int]   => m.size
}
println(getSize("booger"))
println(getSize(Map(1->10, 2->20, 3->30, 4->40)))
println(getSize(List('a', 'b', 'c', 'd', 'e')))
println(getSize(Array('a', 'b', 'c', 'd', 'e')))


//2. BTW, this is stupid, but it works. 
println
def doof(x: Any):Unit = 
  x match {
    case m:Int   => 
    case _       =>
  }
val x:Unit=doof()
//These give "()" and "void"
println(x);
println(x.getClass())
