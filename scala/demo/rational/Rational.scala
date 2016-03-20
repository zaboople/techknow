//DEFINITIONS:

//Main class:
class Rational(n:Int, d:Int) {

  //CONSTRUCTOR STUFF: 
  require(d!=0);

  //Temp variable:
  private val g = gcd(n.abs, d.abs)

  //Makes instance variables visible to other instances & so on
  val numer = n / g
  val denom = d / g
  
  //Auxiliary constructor
  def this(n: Int) = this(n, 1)   

  //Clever greatest common divisor function:
  private def gcd(a: Int, b: Int): Int =
    if (b==0) a 
    else gcd(b, a % b)  

  //PUBLIC METHODS:

  //This is kind of like assertions. Nice, yeah, but it blows up at runtime, not at compile time:
  override def toString=numer +"/"+ denom
  
  def add(that: Rational): Rational =
    new Rational(
      (numer * that.denom) + (that.numer * denom),
      (denom * that.denom)
    )  
  def +(that: Rational)=add(that);
  def + (i: Int): Rational =
    new Rational(numer + i * denom, denom)
    
  def -(that: Rational): Rational =
    new Rational(
      numer * that.denom - that.numer * denom,
      denom * that.denom
    )
  def -(i: Int): Rational =
    new Rational(numer - i  * denom, denom)    
    
      
  def * (that: Rational): Rational =
    new Rational(numer * that.numer, denom * that.denom)
  def * (i: Int): Rational =
    new Rational(numer * i, denom)
  def / (that: Rational): Rational =
    new Rational(numer * that.denom, denom * that.numer)
  def / (i: Int): Rational =
    new Rational(numer, denom * i)    
    
    
  def lessThan(that: Rational):Boolean =
    numer * that.denom < that.numer * denom
  def <(that: Rational)=lessThan(that);
}

//Oh here we go implicit. I don't understand this much yet, but it's semi-evil.
//Note the flag we must enable. The recommendation is use "implicit parameters" instead
//and you won't need the import, but what are those? Dunno:
import language.implicitConversions;
implicit def intToRational(x: Int) = new Rational(x)

//Companion:
object Rational {

  //Allows us to skip "new"
  def apply(n:Int, d:Int):Rational=new Rational(n,d);
  def apply(n:Int):Rational=new Rational(n);  
  
  //Unit test:
  def main(args: Array[String]) {
    var unr=Rational(11,0);
    println(unr);
  }
  
}

//SCRIPT:
var unr=Rational(4, 48);
println(unr);

if (unr < Rational(12, 13))
  println("LT");
else
  println("GT");
  
println (2 * unr)