//Tries to obtain a value and defaults to 9 if that fails.
val i=
  try {
    22.0 / Integer.parseInt(args(0));
  } catch {
    //Note that you don't have to put a "break" at the end of
    //a case, unlike stupid switch. You also need to put the 
    //general case at the end or it will intercept the specific case's
    //targeted exception type:
    case ex:NumberFormatException=>
      println("Number format exception happened: "+ex); 
      11
    case ex:Exception=>
      println("Unexpected exception happened: "+ex);
      9.1
  }
println(i);


//The latter function generates a warning, both are just awful,
//and each returns a different result, which is nuts. Scala
//recommends avoiding both:
def f(): Int = try { return 1 } finally { return 2 }
println(f());
/*
def g(): Int = try { 0+1;     } finally { (1+1);   }
println(g());
*/