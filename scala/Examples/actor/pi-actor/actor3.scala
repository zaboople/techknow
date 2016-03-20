val piString="3.141592653589793238462643383279502884197169399375105820974944592307816406286208998628034825342117067982148086513282306647093844609550582231725359408128481117450284102701938521105559644622948954930381964428810975665933446128475648233786783165271201909145648566923460348610454326648213393607260249141273724587006606315588174881520920962829254091715364367892590360011330530548820466521384146951941511609433057270365759591953092186117381932611793105118548074462379962749567351885752724891227938183011949129833673362440656643086021394946395224737190702179860943702770539217176293176752384674818467669405132000568127145263560827785771342757789609173637178721468440901224953430146549585371050792279689258923542019956112129021960864034418159813629774771309960518707211349999998372978049951059731732816096318595024459455346908302642522308253344685035261931188171010003137838752886587533208381420617177669147303598253490428755468731159562863882353787593751957781857780532171226806613001927876611195909216420198938095257201"

//This is important because it determines our accuracy:
def bigDScale=300;


////////////////////////////////////////////////
// The PI algorithm. This does not calculate  //
// individual digits, but iterations.         //
////////////////////////////////////////////////

import java.math.BigDecimal
import java.math.RoundingMode
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

///////////////////////////////////////////////
// General purpose scheduler. It runs thru a //
// list of jobs, handing one to an actor     //
// whenever an actor becomes available, and  //
// informing a separator "finisher" actor    //
// when all jobs are done.                   //
///////////////////////////////////////////////

import scala.actors.Actor
class MsgStop
class Scheduler(val operators:List[Actor], val jobs:List[Any], val finisher:Actor) extends Actor {
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
    finisher ! new MsgStop
  }
}

///////////////////////////////
// CALCULATOR & ACCUMULATOR: //
///////////////////////////////

class Calculator(val name:String, val accumulator:Actor) extends Actor {
  var used=0
  def act=react {
    case (tries:Int, scheduler:Actor)=>{
      val value=makePiIteration(tries)
      used+=1
      print(name+":"+tries+" ")
      accumulator ! value    //Sends result to accumulator
      scheduler   ! this  //Tells scheduler we're ready for another
      act
    }
    case m:MsgStop=>println("\n"+name+":ran "+used+" ")
    case d:Any=>throw new Exception("WTF "+d)
  }
}
val accu = new  Actor {
  var bdList: List[BigDecimal]=List();
  def act=react {
    case (value:BigDecimal)=>{
      bdList= value :: bdList
      act
    }
    case m:MsgStop=>doResult(bdList)
    case d:Any=>throw new Exception("WTF "+d)
  }
}.start

def doResult(bdList:List[BigDecimal]) {
  println("Calculating final...")
  val toAdd=bdList.sortBy(z=>z)
  var resultBD=bigD0
  toAdd.foreach(x=>resultBD=resultBD.add(x))
  val result=resultBD.toString
  println("\nResult \n\t"+result)
  println("\nActual \n\t"+piString)
  print("\nAccurate to: ")
  var i=0
  var accurate= -1
  for (i<-0 to Math.min(result.length, piString.length)-1
        if (accurate==i-1 && result.charAt(i)==piString.charAt(i))
      )
    accurate=i
  println(accurate+" digits \n\t"+result.substring(0, accurate+1))
}


/////////////////////////////////////////////
// Now we create the scheduler and run it: //
/////////////////////////////////////////////

new Scheduler(
  List(new Calculator("A", accu)
     , new Calculator("B", accu)
     , new Calculator("C", accu)
     , new Calculator("D", accu)
     , new Calculator("E", accu)
  )
  ,(0 to 80).toList
  ,accu
).start

