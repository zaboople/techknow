import java.awt.Point;

//This is the crappier version

trait Interpolator {
  def toInterpolate:List[Point]
  def interpolated=doInter(toInterpolate, Nil);
  class Factor(num:Int) {
    val abs=num.abs;
    val one=if (num==0) 0 else num/abs;
  }
  def doInter(list:List[Point], newlist:List[Point]):List[Point]=
    doInter(list.size, list, newlist)
  def doInter(originalSize:Int, list:List[Point], newlist:List[Point]):List[Point]={
    val a=list.head
    val l1=list.tail
    if (l1.isEmpty)
      if (originalSize>2)
        interpolate(a, newlist.last) ::: newlist 
      else
        a :: newlist
    else 
      doInter(originalSize, l1, interpolate(a, l1.head) ::: newlist) 
  }
  def interpolate(p1:Point, p2:Point):List[Point]={
    val result=List(p1);
    val x=p2.x-p1.x; val y=p2.y-p1.y
    val fx=new Factor(x); val fy=new Factor(y);
    if (fx.abs<=1 && fy.abs<=1)
      result;
    else
    if (x==0)
      interp(p1, p2, result, null, new Point(0, fy.one))
    else
    if (y==0)
      interp(p1, p2, result, null, new Point(fx.one, 0))
    else
    if (fx.abs > fy.abs) 
      if (x % y != 0)
        result
      else 
        interp(p1, p2, result, null, new Point(fx.one * fx.abs/fy.abs, fy.one))
    else 
    if (y % x != 0)
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


trait SortedPoints {
  def toSort:List[Point]
  def sorted:List[Point]=toSort sortWith(
    (p1,p2)=>
      (p1.y<p2.y) || (p1.y==p2.y && p1.x<p2.x)
  );
}

trait Shape extends SortedPoints with Interpolator {
  def points:List[Point]
  def toInterpolate=points
  def toSort=interpolated
  def draw()={
    var x=0; var y=0;
    for (p <- sorted){
      if (y!=p.y) x=0
      drawUntil(y, p.y, println)
      drawUntil(x, p.x, print(" "))
      print(".");
      y=p.y;
      x=p.x+1;
    }
    println();
  }
  private def drawUntil(i:Int, iMax:Int, doIt: => Unit):Unit=
    if (i<iMax) {
      doIt;
      drawUntil(i+1, iMax, doIt);
    }
}


trait TuplePoints {
  def tuples:List[Tuple2[Int,Int]]
  def points=
    for (t <- tuples)
      yield new Point(t._1, t._2)
}
trait TupleIncrements {
  def increments:List[Tuple2[Int,Int]]
  def tuples=tupelo((0,0), increments, Nil)
  def tupelo(
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


class AnyShape(val tuples:List[Tuple2[Int,Int]]) 
  extends Shape with TuplePoints 

class IncrementShape(val increments:List[Tuple2[Int,Int]]) 
  extends Shape with TuplePoints with TupleIncrements 

class Rectangle(x:Int, y:Int, width:Int, height:Int) extends Shape with TuplePoints {
  def tuples=List(
    (x,y), (x+width, y), (x+width, y+width), (x, y+width)
  )
}


new AnyShape(List((0,0),(16,0),(8,16))).draw();
new Rectangle(3, 4, 12, 15).draw();
new IncrementShape(List((3, 3), (5, 5), (-5, 10))).draw();
new IncrementShape(List((3, 3), (6, 120))).draw(); 


