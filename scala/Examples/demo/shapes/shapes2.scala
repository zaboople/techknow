import java.awt.Point;

trait Points {
  def points:List[Point]
}

///////////////////////////////////////////////////
// CONVENIENCE CONVERSION FROM TUPLES TO POINTS: //
///////////////////////////////////////////////////

trait TuplePoints extends Points {
  def tuples:List[Tuple2[Int,Int]]
  //This bit of nastiness is so TupleIncrements can play ball. Couldn't fix it with
  //abstract overrides because then you can't override in a constructor:
  protected def opportuplity=tuples
  def points=
    for (t <- opportuplity)
      yield new Point(t._1, t._2)
}
//Provides the ability to specify each point relative to the previous point:
trait TupleIncrements extends TuplePoints {
  override protected def opportuplity=tupelo((0,0), tuples, Nil)
  private def tupelo(
      curr:Tuple2[Int,Int], 
      told:List[Tuple2[Int,Int]], 
      tnew:List[Tuple2[Int,Int]]
    ):List[Tuple2[Int,Int]]={
    if (told.isEmpty)
      tnew
    else {
      val t=told.head;
      val u=(curr._1+t._1, curr._2+t._2);
      tupelo(u, told.tail, u::tnew)
    }
  }
}


/////////////////////
// DRAWING CLASSES //
// so to speak     //
/////////////////////

//Takes a series of points and figures out how to draw a line between
//them on a monospaced character mode (say, courier new font) terminal. 
//Note how SortedPoints is declared after Interpolator, yet... Um, well
//it SHOULD go first, but no, interpolator does. I dunno. Look at my 
//traits.scala example for a more typical experience. I do not understand
//why it works differently here. I am really puzzled.
trait Outline extends Interpolator with SortedPoints {
  def draw()={
    var x=0; var y=0;
    for (p <- points){
      if (y!=p.y) x=0
      drawUntil(y, p.y, println)
      drawUntil(x, p.x, print(" "))
      print(".");
      x=p.x+1;
      y=p.y;
    }
    println();
  }
  private def drawUntil(i:Int, iMax:Int, doIt: => Unit):Unit=
    if (i<iMax) {
      doIt;
      drawUntil(i+1, iMax, doIt);
    }
}

//The following two traits should really be packaged private with the previous trait:
trait SortedPoints extends Points {
  abstract override def points=super.points.sortWith(
    (p1,p2)=>
      (p1.y<p2.y) || (p1.y==p2.y && p1.x<p2.x)
  );
}
trait Interpolator extends Points {
  abstract override def points=doInter(super.points, Nil);
  def doInter(list:List[Point], newlist:List[Point]):List[Point]=
    doInter(list.size, list, newlist)
  def doInter(originalSize:Int, list:List[Point], newlist:List[Point]):List[Point]={
    val a=list.head
    val l1=list.tail
    if (l1.isEmpty)
      if (originalSize>2)
        //Generates the wraparound from last to first:
        interpolate(a, newlist.last, newlist) 
      else
        a :: newlist
    else 
      doInter(originalSize, l1, interpolate(a, l1.head, newlist))
  }

  private class Factor(val num:Int) {
    val abs=num.abs;
    val one=if (num==0) 0 else num/abs;
  }
  def interpolate(p1:Point, p2:Point, results:List[Point]):List[Point]={
    val result=p1 :: results;
    val fx=new Factor(p2.x-p1.x); 
    val fy=new Factor(p2.y-p1.y);
    if (fx.abs<=1 && fy.abs<=1)
      result;
    else
    if (fx.num==0)
      interp(p1, p2, result, null, new Point(0, fy.one))
    else
    if (fy.num==0)
      interp(p1, p2, result, null, new Point(fx.one, 0))
    else
    if (fx.abs > fy.abs) 
      if (fx.num % fy.num != 0)
        result
      else 
        interp(p1, p2, result, null, new Point(fx.one * fx.abs/fy.abs, fy.one))
    else 
    if (fy.num % fx.num != 0)
      result
    else
      interp(p1, p2, result, null, new Point(fx.one, fy.one * fy.abs/fx.abs))
  }
  def interp(p1:Point, p2:Point, result:List[Point], test:Point, incr:Point):List[Point]=
    if (p2.equals(test))
      result
    else {
      val p=new Point(p1.x+incr.x, p1.y+incr.y)
      if (test==null)
        interp(p, p2,         result, p, incr)
      else
        interp(p, p2, test :: result, p, incr)
    }
}


//////////////////////////////
// POLYGON OUTLINE CLASSES: //
//////////////////////////////


//If you make TuplePoints & Outline backwards it won't work. This sort of makes sense since
//Outline doesn't define the "points" def
class AnyShape(val tuples:List[Tuple2[Int,Int]]) 
  extends TuplePoints with Outline

class IncrementShape(val tuples:List[Tuple2[Int,Int]]) 
  extends TupleIncrements with Outline 

class Rectangle(x:Int, y:Int, width:Int, height:Int) extends TuplePoints with Outline  {
  def tuples=List(
    (x,y), (x+width, y), (x+width, y+width), (x, y+width)
  )
}

///////////
// TEST: //
///////////

new AnyShape(List((0,0),(16,0),(8,16))).draw();
new Rectangle(9, 4, 12, 15).draw();
new IncrementShape(List((3, 3), (5, 5), (-5, 10))).draw();
new IncrementShape(List((6, 3), (18, 6))).draw(); 


