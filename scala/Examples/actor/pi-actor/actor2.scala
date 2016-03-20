//PI=3.14159 26535 89793 23846 26433 83279 50288 41971 69399 37510 58209 74944 59230 78164 06286 20899 86280 34825 34211 70679 

///////////////
// PI LOGIC: //
///////////////

import java.math.BigDecimal
import java.math.RoundingMode
def bigDScale=100;
def makeBigD(i:Long)=new BigDecimal(i).setScale(bigDScale);
val bigD16=makeBigD(16L)
val bigD4=makeBigD(4L)
val bigD2=makeBigD(2L)
val bigD1=makeBigD(1L)
val bigD0=makeBigD(0L)
def bigDivide(x:BigDecimal, y:BigDecimal)=x.divide(y, RoundingMode.HALF_UP)
def makePiIteration(i:Int):BigDecimal={
  val a=bigDivide(bigD1, bigD16.pow(i))
  val b=bigDivide(bigD4, makeBigD((8*i)+1))
  val c=bigDivide(bigD2, makeBigD((8*i)+4))
  val d=bigDivide(bigD1, makeBigD((8*i)+5))
  val e=bigDivide(bigD1, makeBigD((8*i)+6))
  a.multiply(
    b.subtract(c).subtract(d).subtract(e)
  )
} 
def makePi(tries:Int):BigDecimal={
  //The recursive version loses data
  var curr=bigD0
  var i=0L
  for (i <- 0 to tries) 
    curr=curr.add(makePiIteration(i))
  curr
}

//////////////////
// ACTOR LOGIC: //
//////////////////

import scala.actors.Actor
class MsgStop
class Scheduler(val operators:List[Actor], val jobs:List[Any]) extends Actor {
  var instances=operators.length
  var currIndex= -1
  def act {
    operators.foreach(c=>this ! c.start())
    while (instances>0)
      react {
        case c:Actor=>{
          currIndex+=1;
          if (currIndex<jobs.length)
            c ! (jobs(currIndex), this)
          else {
            c ! new MsgStop
            instances-=1;
          }
          act
        }
        case d:Any=>throw new Exception("WTF "+d)
      }
  }
}
class Calculator(val name:String) extends Actor {
  def act=react {
    case (tries:Int, scheduler:Actor)=>{
      println(name+" "+tries+":\n\t"+makePi(tries))
      scheduler ! this
      act
    }
    case m:MsgStop=>
    case d:Any=>throw new Exception("WTF "+d)
  }
}

////////////////
// EXECUTION: //
////////////////

new Scheduler(
  List(new Calculator("A"), new Calculator("B"), new Calculator("C")),
  List(1, 10, 15, 20)
).start


println("Actual \n\t3.1415926535897932384626433832795028841971693993751058209749445923078164062862089986280348253421170679") 