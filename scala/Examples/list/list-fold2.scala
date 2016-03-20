/*
  The book Programming in Scala describes the two functions (amended here with some printing), flattenLeft
  and flattenRight, which give the same result, but one is implemented with foldLeft and the other with
  foldRight. The authors declare that flattenLeft is less efficient, giving an utterly cryptic explanation.
  So:

  If you watch this example run and look at the printout, you will see that for flattenRight, each
  iteration acts like this:
    3-element-list ::: increasingly long list
  yet for flattenLeft, it works like this:
    increasingly long list ::: 3-element-list
  
  We already should know that the fastest way to concatenate two lists is by using :: to add each element
  of the first to the beginning of the second. Thus the number of :: operations for each function is:
    flattenRight performs: 5*3      = 15 "::" operations
    flattenLeft  performs: 3+6+9+12 = 30 "::" operations
  
  And thus.

*/
def flattenLeft[T](xss: List[List[T]]) =
  (List[T]() /: xss) ((x,y)=>
    {print(x+" ::: "+y+"\n"); x ::: y})
def flattenRight[T](xss: List[List[T]]) =
  (xss :\ List[T]()) ((x,y)=>
    {print(x+" ::: "+y+"\n"); x ::: y})
  
val eep=List(
  List(1,2,3)
 ,List(4,5,6)
 ,List(7,8,9)
 ,List(10,11,12)
 ,List(13,14,15)  
)
println("\nUsing foldLeft:")
println("Result: "+flattenLeft(eep))
println("\nUsing foldRight:")
println("Result: "+flattenRight(eep))