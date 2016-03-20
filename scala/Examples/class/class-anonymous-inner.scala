//This demonstrates not only anonymous inner but also tosses in a trait:
import scala.collection.mutable.{SynchronizedMap, HashMap}
val mapper=new HashMap[String, String] 
  with SynchronizedMap[String, String] {
      override def apply(x:String) =
        "Arf!"
    }
mapper+=("1" -> "one", "2"->"two");

println(mapper("1"))
println(mapper("2"))