/**
  This can be executed with a standard Scala interpreter at the command line, e.g.
    @> scala OneShortcutTooMany.scala
*/


/**
 0. A class definition & instance. If you're new to Scala, "Any" is roughly analagous to Java's "Object":
 */
class C {
  def foo(a:Any)=
    printf(
      "\nClass=%-18s  Value=%s", a.getClass().getName(), a 
    );
}
val c=new C();


/** 
  1. Simple invocations.
  */
printf("\n\n1.")
c.foo("hello");
c.foo(2.2);


/** 
  2. Three equivalent invocations with Tuple3, going from fully declared to completely inferred. 
     Tuples are arbitrary quickie data structures that are both scientifically important 
     and popular among lazy programmers. (There are also Tuple2, Tuple4, Tuple5... Tuple32 types)
  */
printf("\n\n2.")
c.foo( new Tuple3[Int, Double, String](1, 2.2, "three") )
c.foo( Tuple3(1, 2.2, "three") )
c.foo( (1, 2.2, "three") )  


/** 
  3. Guess whether this will compile,
     keeping in mind that C.foo() takes *only one argument*:
  */
printf("\n\n3.")
c.foo(1, 2.2, "three") 


/**
  4. WHAT? 
     Explanation: Scala's designers want everything to be an object, but they also
     want to preserve the simplicity of traditional primitives, so:
  */  
printf("\n\n4.")
val x=10

//Two things that do the same thing, but the first is "ugly":
x.+(10)
x + 10

//Two things that also do the same thing:
c.foo("hello") 
c foo "hello"  

//And thus three things that do the same thing
c.foo( (1, 2.2, "three") )
c foo (1, 2.2, "three")
c.foo(1, 2.2, "three")

/** 
  When Scala sees multiple arguments passed to a 1-argument, "broadly" typed function, it will assume
  instead that they are a Tuple, if possible. But rarely would this be the intended result; rather, 
  it's a programming mistake that the compiler won't catch and will lead to incomprehensible errors. 
  
  One can demonstrate more examples with AnyRef, lambdas, generic types, 
  and various combinations thereof. And of course it could be 2, 4, 5 - any number of arguments.
  
  It may be clever, but Scala has at least one shortcut too many. 
  */
   