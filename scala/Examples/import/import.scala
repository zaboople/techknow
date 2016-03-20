//Note how we redefine Map() by importing a the mutable version,
//which now overrules the immutable one whenever we say "Map(x)"

//Not so dangerous:
import scala.collection.mutable
val map=mutable.Map(1->2)
println(map.getClass());

//So we still get a regular immutable map:
val map1=Map(1->2)
println(map1.getClass());

//Dangerous:
import scala.collection.mutable.Map
val map2=Map(1->2)
println(map2.getClass());

//Stupid, but gets us out of the problem we just created:
import scala.collection.immutable
val map3=immutable.Map(1->2)
println(map3.getClass());
