//1. INFIX
//This means the "operator" is in the middle of its arguments. So in this example "drop" is the operator. It
//is really just a 1 argument function on String. Note how we can chain calls together 
//without parentheses. Infix only works when you have one or fewer arguments.
println("Buffalo Beefalo Ruffalo Tupelo" drop 2 drop 3 drop 4 drop 5);

//In these example, 3 + 1 (infix) evaluates first. Note the second especially
println("Buffalo Beefalo Ruffalo Tupelo" drop 3 + 1);
println("Buffalo Beefalo Ruffalo Tupelo" drop(3) + 1);

//Okay now we'll force it to treat "1" a number to convert to string and append:
println(("Buffalo Beefalo Ruffalo Tupelo" drop 3) + 1);
println("Buffalo Beefalo Ruffalo Tupelo".drop(3) + 1);


//2. PREFIX
//This is prefix, which has the weird unary thing. Both of the below are the same:
println(-2.0)
println((2.0).unary_-)
//So the method is defined with the actual unary_ gunk on it.


//3. POSTFIX
//Postfix is now discouraged because it interferes with semicolon "inference", so it's only
//considered a reasonable practice when creating a "DSL". 

//String.toLowerCase can be considered a postfix "operator" because it takes no arguments
//and has no side effects...but to avoid warnings, you need to do the first line. 
import scala.language.postfixOps
import language.postfixOps
println("Hello" toLowerCase)

//This is also interesting. On Double, the no-argument methods won't allow parentheses, even though it's a no-argument
//method. So this causes an error, "Double does not take parameters":
//  2.2.abs()
//This the "correct" way:
println(2.2.abs)
println(2.2.toDegrees.abs.ceil);

//But that's not true here. Both parens and no-parens work perfectly. (Returning
//"this" makes no difference, just allowed me to play with method chaining):
class XWing {
  def pew():XWing=new XWing();
}
val c=new XWing
println(c.pew.pew.pew);
println(c.pew().pew().pew())
