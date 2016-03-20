//This shows how to disassemble a case class instance
//into a series of variables

//1. A basic example:
println
case class Doink(dink:Boolean, derp:Int, doop:String)
val doink=Doink(true, 12, "Hello")
val Doink(a,b,c)=doink 
println(a+"-"+b+"-"+c)

//Note that for more safety you can declare the types of the new variables:
val Doink(p:Boolean,d:Int,q:String)=doink


//2. Here we'll do it in a for loop, which is pretty nutty:
println
val list=List[Doink](
  Doink(true,   1, "hello"),
  Doink(false, 21, "blargh"),
  Doink(true, -9,  "weasel")
)
for (Doink(a,b,c)<-list)
  println ("We got "+a+""+b+""+c)


//3. And a map, which is actually a bunch of Tuples
println
val map=Map("a"->1, "b"->2, "c"->3)
for ((s,n)<-map)
  print(" Pair: "+s+" "+n);
println
//Like I say, it's Tuples. Let's declare the type:
for ((s:String,n:Int)<-map)
  print(" Pair: "+s+" "+n);
println  


//4. Let's get a little more clever with lists. We're
//   not just going to get list members, but a sub-list out
//   of this:
println
val list2=List(6,5,4,3,2,1)
val first :: second :: rest = list2;
println("First="+first+", Second="+second+", Rest="+rest)


//5. We can do arrays too; here, split returns an array. Note however that if we don't get say 1 or 3
//   elements instead of exactly two, we will get an exception.
println
val Array(xx,yy)="foo@bar" split "@" 
println("xx="+xx+" yy="+yy)


