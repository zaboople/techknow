//1. INLINE LAMBDAS
//A series of inlined lambdas, each a simplification of the previou:
println
List(1,2,3).foreach((x:Int)=>println(x+2))
println
List(1,2,3).foreach((x)=>println(x+3))
println
List(1,2,3).foreach(x=>println(x+4))

//Here's an even better example:
println
def isPalindrome(x: String) = x == x.reverse
//All three are the same:
def find1(s: Seq[String]) = s filter isPalindrome
def find2(s: Seq[String]) = s filter isPalindrome _
def find3(s: Seq[String]) = s filter(x=>isPalindrome(x))
println(
  find1(List("bcb", "Worp", "aa")).toString+" "+
  find2(List("bcb", "Worp", "aa")).toString+" "+
  find3(List("bcb", "Worp", "aa")).toString
)