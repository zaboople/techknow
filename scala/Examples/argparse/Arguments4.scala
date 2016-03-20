/*
  This is not necessarily *useful*; rather, the objective was to parse arguments in a *functionally pure* manner, which is hard,
  especially when you account for the trickier requirements:
    - Report extra arguments that we didn't ask for, which many parsers don't do
    - Allow unnamed arguments, e.g. a classic command like "grep -e -i expr file" has two unnamed arguments, the expr & file.
    - Allow arguments in any order    
    - Report missing argument parameters, e.g. if "-f" requires a filename.
*/  
println("Testing...")


//TESTS:
{
  val ray=makeRay("-a1", "-a2", "a3", "2")
  val Answers(
      leftover, wrong, List(a1, a2)
    )=
    Arguments(ray)
      .ask("-a1")
      .ask("-a2", 1)
      .answers
  report(leftover, wrong, a1, a2)
}
{
  val ray=makeRay("file", "-a1", "blah", "-a2", "a2", "shitbag", "bumpher")
  val Answers(
      leftover, wrong, List(a1, a2, file)
    )=
    Arguments(ray)
      .ask("-a1", 1)
      .ask("-a2", 1)
      .ask()
      .answers
  report(leftover, wrong, a1, a2, file)
}
{
  val ray=makeRay("file", "smut", "-a1", "blah", "-a2", "a2")
  val Answers(
      leftover, wrong, List(a1, a2, file)
    )=
    Arguments(ray)
      .ask(1)
      .ask("-a1", 1)
      .ask("-a2", 1)
      .answers
  report(leftover, wrong, a1, a2, file)
}
{
  val ray=makeRay("-zz", "-yy", "1")
  val Answers(
      leftover, wrong, List(zz, yy)
    )=
    Arguments(ray)
      .ask("-zz", 1)
      .ask("-yy")
      .answers
  report(leftover, wrong, zz, yy)
}
{
  val ray=makeRay("-ww", "-ww", "-www")
  val Answers(
      leftover, wrong, List(w, ww, ww2, www, wwww)
    )=
    Arguments(ray)
      .ask("-w")
      .ask("-ww")
      .ask("-ww")
      .ask("-www")
      .ask("-wwww")
      .answers
  report(leftover, wrong, w, ww, ww2, www, wwww)
}


//HELPERS: 
def makeRay(ray:String*)={
  print("\nTesting:")
  for (r <- ray) print(" "+r)
  println()
  ray.toArray
}
def report(leftover:List[String], wrong:List[Answer], answers:Answer*)={
  if (leftover.nonEmpty || wrong.nonEmpty) println("INVALID")
  for (v <- leftover) println("  Unexpected: "+v)      
  for (w <- wrong) println("  Wrong: "+w)
  for (a <- answers) println(a)
}


//////////////////////////////////
// Input/Ouputs are Ask/Answer: //
//////////////////////////////////


case class Ask(dashName:Option[String], valMin:Option[Int], valMax:Option[Int]) {
  override def toString="Dash "+dashName+" valMin:"+valMin+" valMax:"+valMax
}

case class Answer(
    dashName:Option[String], 
    vals:List[String],
    exists:Boolean=true, 
    invalidNotEnoughValues:Boolean=false
  ) {
  override def toString=
    "Name:"+dashName.mkString("")+
    " Exists:"+exists+
    " Vals:"+(if (invalidNotEnoughValues) " <MISSING>" else vals.mkString(","))
  def valid = !invalidNotEnoughValues
}
case class Answers(unexpected: List[String], wrongAnswers:List[Answer], allAnswers:List[Answer])

/** This is an "Either"; you either have a name, or a list of values. */
abstract class Argument(dashName:Option[String], values:List[String])
case class Dash(dashName:String)       extends Argument(Some(dashName), Nil)
case class Values(vals:List[String])   extends Argument (null, vals)



////////////////
// ARGUMENTS: //
////////////////



case class Arguments(remains: List[Argument], answerList: List[Answer]){  

  def answers:Answers=Answers(this)

  def ask(q:Ask):Arguments=
    q.dashName match {
      case Some(name)=>
        remains.indexOf(Dash(name)) match {
          //We see -name doesn't exist:
          case -1 => 
            addAnswer(this.remains, Nil, Answer(Some(name), Nil, exists=false)) 
          //We see it does, so find out if it has values:
          case  x => 
            getVals(q, remains.take(x),  remains.slice(x+1, remains.length), Answer(Some(name), Nil))
        }
      case None =>
        //Unnnamed parameters:
        getVals(q, Nil, remains, Answer(None, Nil))  
    }
  
  /**
    * dashName: If defined, this is a named argument, a la "-name". Otherwise it absorbs all remaining
    *           parameters after named arguments are parsed.
    * valMin: If defined, this argument allows values. If greater than 0, it requires values according
    *         to its count.
    * valMax: If defined, this argument is limited to a maximum of valMax values. 
    */
  def ask(dashName:Option[String], valMin:Option[Int], valMax:Option[Int]):Arguments=
    ask(Ask(dashName, valMin, valMax))

  //Shortcut: Unnamed (implied) argument allows valMin to valMax values:
  def ask(valMin:Int, valMax:Int):Arguments=
    ask(None, Some(valMin), Some(valMax))

  //Shortcut: Unnamed (implied) argument allows exactly valMinMax values:
  def ask(valMinMax:Int):Arguments=ask(valMinMax, valMinMax)

  //Shortcut: Unnamed (implied) argument allows values:
  def ask():Arguments=ask(None, Some(0), None)
  
  //Shortcut: Named argument that allows no values:
  def ask(dashName:String):Arguments=
    ask(Some(dashName), None, None)

  //Shortcut: Named argument allows 0 or more values:
  def ask(dashName:String, allowVals:Boolean):Arguments=
    if (allowVals)
      ask(Some(dashName), Some(0), None)
    else
      ask(dashName)

  //Shortcut: Named argument requires exactly valMinMax values:
  def ask(dashName:String, valMinMax:Int):Arguments=
    ask(Some(dashName), Some(valMinMax), Some(valMinMax))
    
  //Shortcut: Named argument requires exactly min values and optionally max:
  def ask(dashName:String, min:Int, max:Option[Int]):Arguments=
    ask(Some(dashName), Some(min), max)

  private def addAnswer(before:List[Argument], after:List[Argument], answer:Answer)=
    Arguments(before ::: after, this.answerList ::: List(answer))

  private def getVals(ask:Ask, before:List[Argument], after:List[Argument], answer:Answer):Arguments={
 
    val (vals, newAfter)=after match {
      case Values(vals) :: more             => (vals, more)
      case _                                => (Nil, after)
    }

    (ask.valMin,ask.valMax) match {
    
      // Technically invalid, but we'll just redo with 0 as minimum:
      case (None, Some(hi))=>      
        getVals(
          Ask(ask.dashName, Some(0), ask.valMax), 
          before, after, answer
        )
        
      // Does not want values:
      case (None, None)=>
        addAnswer(before, after, answer)

      // Unlimited values with a minimum:
      case (Some(lo), None)=>
        addAnswer(
          before, newAfter, 
          Answer(answer.dashName, vals, invalidNotEnoughValues=vals.size<lo)
        )
        
      // Has a min & max, probably 1 & 1:
      case (Some(lo), Some(hi))=>
        val answerVals=vals.take(hi)
        addAnswer(
          append(before, vals.slice(hi, vals.length)), 
          newAfter, 
          Answer(answer.dashName, answerVals, invalidNotEnoughValues=answerVals.size<lo)
        )
    }
  }
  
  /* 
     This adds a vals, list of plain values, to an Argument list, with the understanding
     that if the last Argument is itself a list of plain values, vals should just be appended to that.
  */
  private def append(args:List[Argument], vals:List[String])=
    (args, vals) match {
      case (a,     Nil)    =>a
      case (Nil, leftover) =>List(Values(leftover))
      case (a, leftover)   =>a.last match {
        case Values(v)=>a.dropRight(1) :+ Values(v ::: leftover)
        case _        =>a              :+ Values(leftover)
      }
    }
}

object Answers {
  def apply(a:Arguments):Answers=
    Answers(
      (a.remains match {
        case Nil=>Nil
        case r=>
          for (r1 <- r) yield r1 match {
            case Dash(name)    =>List(name)
            case Values(names) =>names
          }
      }).flatten, 
      a.answerList.filter(!_.valid),
      a.answerList
    )

}

object Arguments {

  def apply(input:Array[String]):Arguments=
    Arguments(make(input, Nil), Nil)
  def apply(input:String*):Arguments= 
    apply(input.toArray)
          
  private def make(input:Array[String], results:List[Argument]):List[Argument] =
    input match {
      case Array() => 
        //We're done, so reverse everything (args & values for args)
        for (x <- results.reverse)
          yield x match {
            case Values(z) =>Values(z.reverse)
            case f  => f
          }
      case _ => 
        make(input.tail, makeArg(results, input.head))
    }
    
  private def makeArg(args:List[Argument], s:String):List[Argument]=
    if (s.startsWith("-"))
      Dash(s) :: args
    else 
      //Note how we're condensing sequences of non-dash stuff into one thing:
      args match {
        case Values(x) :: more =>Values(s :: x)   :: more
        case _                 =>Values(s :: Nil) :: args
      }
  
}
