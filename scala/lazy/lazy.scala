class A {
  lazy val b={println("Initializing A.b ..."); 12}
       val c={println("Initializing A.c ..."); 13}
}
println("Creating new A")
val xxx=new A();
println("A.b initialized yet? No, but A.c probably is.");
println("A.b: "+xxx.b)
println("A.c: "+xxx.c)