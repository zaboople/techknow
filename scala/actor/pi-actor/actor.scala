//This was a first try. The later ones are better.

///////////////
// PI Logic  //
///////////////
import java.math.BigDecimal
import java.math.RoundingMode
def bigDScale=100;
val bigD4=makeBigD(4L)
val bigD0=makeBigD(0L)
def makeBigD(i:Long)=
  new BigDecimal(i).setScale(bigDScale);
def makeBigD(i:Long, j:Long, k:Long):BigDecimal=
  new BigDecimal(i).multiply(new BigDecimal(j)).multiply(new BigDecimal(k)).setScale(bigDScale)
def makePI(times:Long):BigDecimal=
  makeBigD(3L).add(
    makePIRec2(times)
  )
def makePIRec2(tries:Long):BigDecimal={
  //The recursive version loses data
  var curr=bigD0
  var i=0L
  for (i <- 1L to tries) {
    val incr=2L + (4L*(i-1));
    val d1=bigD4.divide( 
      makeBigD(incr,incr+1,incr+2), RoundingMode.HALF_UP
    );
    val d2=bigD4.divide(
      makeBigD(incr+2,incr+3,incr+4), RoundingMode.HALF_UP
    );
    curr=curr.add(d1.subtract(d2))
  }
  curr
}


/////////////
// ACTORS: //
/////////////

import scala.actors.Actor
class MsgStop
class Scheduler(val operators:List[Actor], val jobs:List[Any]) extends Actor {
  var instances=operators.length
  var currIndex= -1
  def act {
    operators.foreach(c=>this ! c.start())
    while (instances>0)
      receive {
        case c:Actor=>{
          currIndex+=1;
          if (currIndex<jobs.length)
            c ! (jobs(currIndex), this)
          else {
            c ! new MsgStop
            instances-=1;
          }
        }
        case d:Any=>throw new Exception("WTF "+d)
      }
  }
}



class Calculator(val name:String) extends Actor {
  var keepGoing=true;
  def act {
    while (keepGoing) 
      react {
        case (tries:Long, scheduler:Actor)=>{
          println(name+" "+tries+":\n\t"+makePI(tries))
          scheduler ! this
          act
        }
        case m:MsgStop=>keepGoing=false
        case d:Any=>throw new Exception("WTF "+d)
      }
  }
}

////////////////
// EXECUTION: //
////////////////

new Scheduler(
  List(new Calculator("A"), new Calculator("B"), new Calculator("C")),
  List(10L, 100L, 1000L, 10000L, 100000L, 300000L, 800000L, 1000000L)
).start

//PI=3.14159 26535 89793 23846 26433 83279 50288 41971 69399 37510 58209 74944 59230 78164 06286 20899 86280 34825 34211 70679 
