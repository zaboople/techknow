val toStart=Array(
  "v0", "v1", "v2"
  ,"-a0", 
  "-a1", 
  "-a2", "v21", "v22", 
  "-a3", "v31", "v32", 
  "-a3.1",
  "-a4", "v41"
  ,"-a5"
)

debug("HUGE", toStart)
println
val args=Arguments(toStart)
val (hasA1, rest)=args.has("-a1")
val (List(hasA2,hasA3,hasAz), rest2)=rest.has("-a2", "-a3", "-az")
println
println(hasA2+" "+hasA3+" "+hasAz)
println
println("YEAH "+rest2)



def debug(name:String, arr:String*):Unit=
  debug(name, arr.toArray)
def debug(name:String, arr:Array[String]=Array()):Unit=
  println("\nTEST: "+name+"\n"+Arguments(arr))


type Argument=Either[String,List[String]];

case class Arguments(all: List[Argument]){
  override def toString={
    val sb=new StringBuilder();
    all.foreach(x=> sb.append("\n"+x))
    sb.toString
  }

  def has(dashArgs:String*):(List[Boolean], Arguments)=
    has(dashArgs.toArray, Nil)
  private def has(dashArgs:Array[String], results:List[Boolean]):(List[Boolean], Arguments)=
    dashArgs match {
      case Array() => (results, this)
      case _       => {
        val (result,next)=has(dashArgs.head)
        next.has(dashArgs.tail, result :: results)
      }
    }

  def has(dashArg:String)=
    indexOf(dashArg) match {
      case -1 => (false, this)
      case  0 => (true, Arguments(all.tail))
      case  x => (true, Arguments(all.take(x) ::: all.slice(x+1, all.length-x)))
    }
  private def indexOf(dashArg:String)=all.indexOf(Left(dashArg))

  
  /*
  def hasVals(dashArg:String, required:Boolean):(Boolean, List[String], Arguments)={
    val (has, rest)=has(dashArg)
    has match {
      case false => (false, Nil, this)
      case true  => 
  }
  */
}

object Arguments {
  
  def apply(input:Array[String]):Arguments=

    //Reverses make() and the argument values for each:
    Arguments(
      for (x <- make(Nil, input).reverse)
        yield x match {
          case Left(y)  =>Left(y)
          case Right(z) =>Right(z.reverse)
        }
    )

  private def make(args:List[Argument], input:Array[String]):List[Argument] =
    input match {
      case Array() => args
      case _ => make(
        makeArg(args, input.head), input.tail
      )
    }
    
  private def makeArg(args:List[Argument], s:String):List[Argument]=
    if (s.startsWith("-"))
      //New dash arg:
      Left(s) :: args
    else 
      args match {
        //Plain arg, very first of any:
        case Nil=>Right(s :: Nil) :: Nil
        //Plain arg, next of several plain args; or first:
        case _  =>
          args.head match {
            case Right(x)=> Right(s :: x)   :: args.tail
            case _ =>       Right(s :: Nil) :: args
          } 
      }
  
}


    
