//Cannot initialize y to null. Can't even do like java
//and say, no, it's in the next statement, right there, 
//just give me - no. Must be initialized.
var y="";

//Also not like Java: Comes out as "Unit"
val x=(y="22");
println(x);
//This will always come out true, so you get a warning if you run it:
//println(x==());

//You MUST put a space in between = & -1. Otherwise it takes them
//as one operator, and I have no idea what that will even mean.
val z= -1


/////////////////////////////////////////////////////
//How to do multiple assignments in one statement: //
/////////////////////////////////////////////////////

//Part 1:
//You cannot say val p=1,d=2,q=3
//This alternative actually takes advantage of pattern matching. Yes
//it looks sort of stupid:
println
val (p,d,q)=(1,2,3)
println(p)
println(d)
println(q)

//Part 2:
//This *looks* like x, y & z are assigned the same
//instance of Thing, but no, they aren't. 
println
class Thing {
  var a,b= -1
}
val aa,bb,cc=new Thing()
aa.a=12
bb.a=2
println(aa.a)
println(bb.a)
println(cc.a)
