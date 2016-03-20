/**
  This uses a special if statement embedded in the pattern. 
  
  Note how the if statement doesn't require parentheses around its arguments. Normal if statements need parens.
  You can add parentheses in if you want, however.
**/

def check(foo:Tuple2[Int,Int])=foo match{
  case (a,b) if a>0 && b>0 => println("Both positive")
  case (a,b) if a<0 && b<0 => println("Both negative")
  case _ => println("One is negative, one is positive")
}

check((9,   10))
check((-9, -10))
check((9,  -10))
