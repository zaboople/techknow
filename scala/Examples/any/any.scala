//Any is the root.
//  AnyVal is below Any, and is for what would be primitives in Java
//  AnyRef is below Any and for everything else, and is basically the same as java.lang.Object:

val a:AnyVal=4;
val b:Any=a;

val c:AnyRef="Hello";
val d:Any=c;

val e:AnyRef="Hi";
val f:java.lang.Object=e;
val g:AnyRef=f;
