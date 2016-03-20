
/**
 * This is one way to do dependency injection, although I'm not sure it's useful.
 */
trait Banker {
  val bankPhrase:String
  def bank(){println(bankPhrase)}
}
trait Booger {
  val boogerPhrase:String
  def boop(){println(boogerPhrase)}
}

/** Here it is: The self-type-annotation. Check the =>, which now means yet another thing. */
trait BoogerNeeder  { 
  this: Booger with Banker=>
  def actup{
    boop
    bank
  }
}

val bn=new BoogerNeeder with Booger with Banker {
    val boogerPhrase="boogity boo"
    val bankPhrase="bankity bank"
  }
  
bn.actup
