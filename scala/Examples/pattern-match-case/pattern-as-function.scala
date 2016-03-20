//FIXME EXPLAIN partial function
val second: List[Int] => Int = {
  //This is a list of three elements, when we only need two - but the third element
  //is a standin for a list. We're saying "x prepended to (y prepended to a list)"
  case x :: y  :: _ => y
  case _ => throw new RuntimeException("Nope");
}

println(second(List(1,2)))