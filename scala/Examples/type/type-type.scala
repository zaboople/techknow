//This is neat:
type T=String
new T("goodbye")
def x:T="whoops"

//This is useless:
val y="hello"
type V=y.type

//Illegal. It says I am trying to create an instance of "y.type". Pfft.
//new V("what")

//Illegal:
//val q=y.type match {
//  case String=>1
//  case _=>2
//}

//Illegal. You can't print a type. Argh.
//println(y.type)
//println(T)
//println(V)

//Illegal: "hello" is a literal, not an object. 
//          Yeah it's not java
//type q="hello".type