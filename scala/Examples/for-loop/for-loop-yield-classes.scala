//This makes the point that the class returned by yield depends on what
//you are looping through. It might seem like class-in-is-class-out but then you
//get to our third example...

//1. This yields an array, because we looped over an array. If you ask for 
//the class it gives you a piece of gook, of course:
println("\nArray:");
val ray:Array[Int]=Array(1,2,3,4)
println(ray.getClass());
val nums:Array[Int]=
  for (y <- ray)
    yield y
println(nums.getClass())


//2. This yields a List because we looped over a List. Well the actual type is 
//some internal thing though - "scala.collection.immutable.$colon$colon". Nice:
println("\nList:");
val glop=List(1,2,3,4,5,6,7);
println(glop.getClass());
val nummys:List[Int]=
  for (p <- glop) yield p
println(nummys.getClass())


//3. So the "to" operator makes a Range. It doesn't have a parameterized type because it's always Int.
//HOWEVER, our yield creates a Seq which is actually a Vector:
println("\nRange...");
val xxx:Range=0 to 100
println(xxx.getClass())
val zippy:Seq[Int]=
  for (x <- xxx) 
    yield x
println(zippy.getClass())


