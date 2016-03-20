//2. DECLARED LAMBDAS

//2.1 This is a function treated as a val. Note that you could replace "val" with "def" here
//and get the same result, however.
println
val increase=(x:Int)=>print(x+1+" ");`
List(10,20,30).foreach(increase(_)); println
List(10,20,30).foreach(increase)   ; println
List(10,20,30) foreach increase    ; println

//This is a more formal definition of the previous. It declares the type of the lambda:
println
val increase2:Int=>Unit=
  (x:Int)=>{print(x+1+" ");}
List(1,2,3).foreach(increase2(_)); println
List(1,2,3).foreach(increase2)   ; println
List(1,2,3) foreach increase2    ; println


//2.2 Consider delays in execution:
//  -derpie is a val, so print() happens immediately; the value of derpie is thus: Unit
//  -derpie1 is a def, so print() happens when we try to put it in the List(), which becomes a: List[Unit]
//  -derpie2 is a lambda, so print() happens when we directly invoke the lambda via foreach()
println

val derpie          =     print("hi ")
def derpie1         =     print("ho ")
val derpie2:()=>Unit= ()=>print("hum ")

List(derpie1,derpie1,derpie1)
List(derpie2,derpie2,derpie2).foreach(_()) //or you could say foreach(x=>x())
List(derpie2,derpie2,derpie2).foreach _    //even simpler
println


//2.3 Look at this, a function that returns a lambda. 
println
def makeFunc1(base: Int)=
  (value: Int) => value + base
println(makeFunc1(1000)(11))

//Same thing without inferred types:
def makeFunc2(base: Int):Int=>Int = 
  (value: Int) => value + base
println(makeFunc2(1100)(12))



//2.4 This is a very strange way to write functions. The two vals are declared as lambdas:
//  fromInt: take Int and return Base
//  toInt:   take Base and return Int
//Then to make it even weirder, they are defined using an array and a map, which thanks to all 
//the automatic weirdness, act as functions that take the original input and "apply" it to themselves
//as functions.
//Also note that the val's could also be def's, for all I can see.
println
val fromInt: Int    => String = Array("A", "T", "G", "C")
val toInt:   String => Int    = Map("A" -> 0, "T" -> 1, "G" -> 2, "C" -> 3)
println(fromInt(2))
println(toInt(fromInt(2)))

