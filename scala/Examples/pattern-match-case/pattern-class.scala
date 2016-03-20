/*
  Why define a class as a case class?
    - Because of pattern matching
  But wait there's more:
    - You don't have to type "new" because you get an automatic Factory method
    - All parameters are automatically val arguments, i.e. instance variables
    - ==, toString, hashCode are all automatically implemented and walk all the val's in the class
    - An automatic copy method is added (see below)
*/

//Note the use of sealed. That means all the subclasses of Expr are defined here, so Scala knows
//whether you've specified all the possible classes that can match Expr in a match-case:
sealed abstract class Expr
case class Var(name: String) extends Expr
case class Number(num: Double) extends Expr
case class UnOp(operator: String, arg: Expr) extends Expr
case class BinOp(operator: String, left: Expr, right: Expr) extends Expr

//1 CHECK OUT THE BUT-WAIT-THERE'S-MORE STUFF:

//The automatic new:
val bo1=BinOp("***", Number(123), Number(345))

//Automatic val
println("The automatic val")
println(bo1.operator)

//Automatic copy:
val bo2=bo1.copy(left=Number(2222));

//Automatic toString
println("The automatic toString")
println(bo1+" "+bo2)

//Automatic ==
//We create a new instance that is the same as bo1, so == gives true on comparison:
println("The automatic ==")
val bo3=bo2.copy(left=Number(123))
println(bo1==bo3);


//2. CHECK OUT THE CASE CLASS PATTERN MATCHING:
def doMatch(e:Expr)= e match {

  //Boring:
  case Number(x)=>println("Yes it's a number: "+x);
  
  //Look, nested patterns:
  case BinOp("*", Number(a), Number(b))=>println("Multiplying: "+a+" times "+b+"="+(a*b));
  
  //Our standard default wildcard, which is usually needed:
  case _        =>println("I dunno what this is "+e)
}

doMatch(Number(2))
doMatch(BinOp("*", Number(12), Number(13)))

