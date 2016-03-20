println("Testing...")
test3

def test2={
  println
  testResults(
    Arguments(
      Array("-a1", "-a2", "beep", "beep2", "-a3", "a3value", "-a4", "end1", "end2")
    )
    .ask("-a1")
    .ask("-a2", 1)
    .ask("-a4")
    .ask("-a3", true)
    .ask()
  )
}
def test3={
  println
  testResults(
    Arguments(
      "a1", "a2", "a3"
    )
    .ask("-a1")
    .ask("-a2", 1)
    .ask
    .ask(1)
  )
}

def testResults(a:Arguments)={
  println("Answers: "+a.answers)
  println("Remains: "+a.remains)
}



//////////////////////////////////////////
// Core classes: Argument, Ask, Answer: //
//////////////////////////////////////////

/** This is an "Either"; you either have a name, or a list of values. */
abstract class Argument(name:String, values:List[String])
case class Dash(name:String) extends Argument(name, Nil)
case class Values(values:List[String]) extends Argument (null, values)

case class Ask(dashName:Option[String], valMin:Option[Int], valMax:Option[Int]) {
  override def toString="Dash "+dashName+" valMin:"+valMin+" valMax:"+valMax
}

case class Answer(
    dashName:Option[String], 
    vals:List[String],
    invalidNoSuchName:Boolean=false, 
    invalidNotEnoughValues:Boolean=false, 
    invalidTooManyValues:Boolean=false
  ){
  override def toString="\nName: "+dashName+" ; vals: "+vals+" ; valid: "+valid+"\n"
  def valid = !(invalidNoSuchName || invalidNotEnoughValues || invalidTooManyValues)
}
object Answer {
  protected def apply(dashName:String, vals:List[String]):Answer=Answer(Some(dashName), vals)
}

////////////////
// ARGUMENTS: //
////////////////


case class Arguments(remains: List[Argument], answers: List[Answer]){  
  override def toString={
    val sb=new StringBuilder();
    remains.foreach(x=> sb.append("\n"+x))
    sb.toString
  }
  
  def ask(q:Ask):Arguments=
    q.dashName match {
      case Some(name)=>
        remains.indexOf(Dash(name)) match {
          //We see -name doesn't exist:
          case -1 => addAnswer(this.remains, Nil, Answer(Some(name), Nil, invalidNoSuchName=true)) 
          //We see it does, so find out if it has values:
          case  x => getVals(q, remains.take(x),  remains.slice(x+1, remains.length), Answer(Some(name), Nil))
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
    * valMax: If defined, this argument allows a maximum of valMax values. It is illegal to 
    *         to have a defined valMax when valMin is undefined.
    */
  def ask(dashName:Option[String], valMin:Option[Int], valMax:Option[Int]):Arguments=
    ask(Ask(dashName, valMin, valMax))

  //Shortcut: Unnamed (implied) argument allows valMin to valMax values:
  def ask(valMin:Int, valMax:Int):Arguments=
    ask(None, Some(valMin), Some(valMax))

  //Shortcut: Unnamed (implied) argument allows exactly valMinMax values:
  def ask(valMinMax:Int):Arguments=ask(valMinMax, valMinMax)

  //Shortcut: Unnamed (implied) argument allows values:
  //Warning: This NEEDS to have parentheses after the name. Otherwise it becomes a black hole
  //where ask dissapears but ask() works.
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
    
  //Shortcut: Each named argument allows no values:
  def ask(dashNames:String*):Arguments=askMany(
      for (name <- dashNames.toList)
        yield Ask(Some(name), None, None)
    )


  private def askMany(qs:Seq[Ask]):Arguments=qs match {
    case Seq() => this
    case _     => this ask qs.head askMany qs.tail
  }

  private def addAnswer(before:List[Argument], after:List[Argument], answer:Answer)=
    Arguments(before ::: after, this.answers ::: List(answer))

  private def getVals(ask:Ask, before:List[Argument], after:List[Argument], answer:Answer):Arguments={
 
    val (vals, newAfter)=after match {
      case Values(vals) :: more             => (vals, more)
      case _                               => (Nil, after)
    }

    ask.valMin match {
      case None=>
        ask.valMax match {
          case Some(hi)=>
            throw new Exception(
              "Illegal: Your Ask() definition specifies no lower bound (such as 0) but a defined upper bound of: "+hi
            )
          case None => 
            if (vals == Nil || newAfter == Nil)
              //When vals is Nil, as expected;
              //When newAfter is Nil, these could be end-of-line no-parameter vals, let another validation handle that:
              addAnswer(before, after, answer)
            else
              addAnswer(before, newAfter, Answer(answer.dashName, vals, invalidTooManyValues=true))
        }
          
      case Some(lo)=>
        addAnswer(
          before, newAfter, 
          Answer(
            ask.dashName, vals,
            invalidNotEnoughValues=vals.size < lo,
            invalidTooManyValues= (ask.valMax != None && vals.size > ask.valMax.get)
          )
        )
    }
  }
}


object Arguments {

  def apply(input:Array[String]):Arguments=
    Arguments(make(input, Nil), Nil)
  def apply(input:String*):Arguments=
    Arguments(input.toArray)
  
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
