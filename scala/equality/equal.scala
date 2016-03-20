/**
  This shows the difference between eq and ==. 
  The eq operator/function was created so you could get guaranteed
  "reference equality", i.e. the same as Java's ==.
  
  Also note there is an "ne" function for reference "inequality".
**/

//True, because values in the Lists match:
println(List(1,2,3) == List(1,2,3))

//False, because these are different objects:
println(List(1,2,3) eq List(1,2,3))


//True: Java will do everything it can to reuse the same String, so
//this is actually comparing one string object with itself:
println("foo" eq "foo")

//False: Here is how we trick java into giving us two different objects:
val a=new String("foo"); val b=new String("foo");
println(a eq b)
