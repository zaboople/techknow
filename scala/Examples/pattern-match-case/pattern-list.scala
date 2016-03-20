println("\nList match with ::");
def getSecond(vals: List[Int]) = vals match {
  //We're saying "x prepended to (y prepended to an arbitrary list)"
  //That's pretty clever:
  case x :: y  :: _ => y
  case _ => -1
}
println(getSecond(List(1,2,9, 12)))
println(getSecond(List(2,9)))
println(getSecond(List()))


println("\nList match with _*:");
def getSecondAgain(vals:List[Int])=vals match {
  //This gets the same result, but using a different technique. 
  case List(x, y, _*) => y
  case _              => -1
}
println(getSecondAgain(List(1,2,9,12)))
println(getSecondAgain(List(2,9)))
println(getSecondAgain(List()))


println("\nList match with List()");
def isThreeOrFour(vals: List[Int]) = vals match {
  //A bit more boring:
  case List(_,_,_)   => {println("It was three"); true }
  case List(a,b,c,d) => {println("It was four"); true }
  case _             => false
}
println(isThreeOrFour(List(1,2,3,4,5)))
println(isThreeOrFour(List(1,2,3,5)))
println(isThreeOrFour(List(1,2)))