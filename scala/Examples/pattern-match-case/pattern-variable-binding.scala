//This uses the magical "@" to achieve variable "binding".
//There are occasional situations where inside a pattern, we want to bind variables
//to parts that are otherwise impossible to get at.

val lol=List(
  List(1,2,3), 
  List(2,3,4)
)

//1. Here is the standard pattern match situation:
println
lol match {
  case (nums1 @ n1::_trash1) :: (nums2 @ n2::_trash2) :: _trash3=>{
    println("Found "+n1+" at start of "+nums1)  
    println("Found "+n2+" at start of "+nums2)
  }
  case _=>
    println("No")
}


//2. Here is a shortcut to the above:
println
val  (nums1 @ n1::_trash1) :: (nums2 @ n2::_trash2) :: _trash3=lol
println("Found "+n1+" at start of "+nums1)  
println("Found "+n2+" at start of "+nums2)



