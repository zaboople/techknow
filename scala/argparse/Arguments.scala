//FIXME reverse
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
test("None")
test("V-One", "a")
test("V-Two", "a", "b")
test("V-Three", "a", "b", "c")
test("A-One", "-a1")
test("A-Two", "-a1", "-a2")
test("HUGE", toStart)

def test(name:String, arr:String*)=
  println("\nTEST: "+name+"\n"+Arguments(arr.toArray))

def test(name:String, arr:Array[String]=Array())=
  println("\nTEST: "+name+"\n"+Arguments(arr))



case class Arguments(input:Array[String]){

  
  val all:List[Argument]=
    //Reverses make() and the argument values for each:
    for (
        x <- make(Nil, new Argument(None, Nil), input)
          .reverse
      )
      yield Argument(x.name, x.vals.reverse)

  override def toString=all.toString
  
  private def make(
      args:List[Argument], 
      arg:Argument, 
      input:Array[String]
    ):List[Argument] =
    input match {

      case Array() => arg :: args

      case _ => 
        val s=input.head
        if (s.startsWith("-"))
  
          //New argument:
          make(
            arg match {
              case Argument(None, Nil)=> args 
              case _  => arg :: args
            }
            ,Argument(s, Nil)
            ,input.tail
          ) 
  
        else 
          
          //New argument value. Either very first input - no name - or add to existing
          make(
            args
            ,arg match {
              case Argument(None, Nil)=> Argument(None, s)
              case _ => Argument(arg.name, s :: arg.vals)
            }
            ,input.tail
          )
  
    }
  
}


    
case class Argument(name:Option[String],  vals:List[String]){
  override def toString="\nName: "+name+" -> Vals:"+vals
}    
object Argument {
  def apply(name:String, vals:List[String]):Argument= 
    Argument(Some(name), vals)
  def apply(name:String, s:String):Argument= 
    Argument(Some(name), s :: Nil)
  def apply(name:Option[String], s:String):Argument= 
    Argument(name, s :: Nil)
}    
