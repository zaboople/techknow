//Unapply is the key method in an extractor. It is sort of the opposite
//of apply(), but apply() does not have to be defined.


//Example 1: Typical
println
object EMail {
  def unapply(str: String): Option[(String, String)] = {
    val parts = str split "@"
    if (parts.length == 2) Some(parts(0), parts(1)) else None
  }
}
for (s <- args){
  print("\""+s+"\": ")
  s match {
    case EMail(user, domain) 
      => println("Email, user="+user+", domain="+ domain)
    case _ 
      => println("Not an email address")
  }
}

//Example 2: An extractor that returns boolean, so it needs none of the Option stuff. This is a special case.
println
object LongString {
  def unapply(str:String): Boolean=str.length>12
}
"Gooooblblblble" match {
  case LongString()=>println("Long")
  case _=>println("nope")
}


//Example 3: An extractor that returns a sequence.
println
val splitter="""(\p{Space})+""".r
def splitRev(whole:String)=splitter.split(whole).reverse
object WordExtractor {
  def unapplySeq(whole: String): Option[Seq[String]] =
    Some(splitRev(whole))
}
val WordExtractor(last, nextToLast, _*)="This is a sentence"
println("Last word:\""+last+"\"; next-to-last word:\""+nextToLast+"\"")

//However: Extractor is already built into collections, so here is an even easier way:
println
val Array(n1, n2, _*)=splitRev("Another way to do it")
println("Last word:\""+n1+"\"; next-to-last word:\""+n2+"\"")


//Example 4: Extractors are built into regexes. Whatever is in parentheses in the regex
//           will show up as a list element. Note however that there is no way to handle
//           repeating elements; each pair of parentheses will produce only one element.
//           So "(a)*" matched to "aaaa" produces 1 element, not 4.
println
val plusSign="""\p{Space}*\+\p{Space}*"""
val myregex=("(.*?)"+plusSign+"(.*)").r
val myregex(a, b)="firsty   +secondy"
println(a+" \"plus\" "+b)

