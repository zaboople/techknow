println();

/*
  This creates a new variable in a new context. It is not the same "pi". 
  If it was "case Pi" then this would fail because Scala assumes lowercase
  means a new variable, uppercase means an existing thing.
*/
val pi=math.Pi
math.E match {
  case pi => println("Different pi: "+pi);
}
println("Original pi: "+pi);


//This will not compile because Pi is uppercase. Uncomment and try it if you want:
/*
val x=math.E match {
  case Pi => "strange math? Pi = "+ Pi
}
println(x);
*/