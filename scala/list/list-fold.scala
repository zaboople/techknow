/*
  For a given operation (X,Y)=> fold takes the result of the operation and passes it back in as X, 
  once for each list member, which is passed as Y. Thus fold needs an initial value to pass in as the first X.
  
  Fold is curried, although I'm not sure I see the point.
  
  There are 3 fold functions: foldLeft(), fold() & foldRight(). "/:" is foldLeft.  ":\" is foldRight.
  Whereas foldLeft & foldRight strictly follow the list's order (left to right and vice versa, respectively), 
  fold() itself allows list elements to be invoked in *any* order, so that Scala can choose to parallelize 
  invocations for maximum performance.
  
  Note that foldRight switches (X,Y)=> around to (Y,X)=>, for no apparent reason.
*/


//So the following two do the same thing:
println(
  List(1,2,3,4,5).foldLeft(10)(_*_)
)
println (10*1*2*3*4*5)


//The following two also do the same thing, in a silly way. 
//We're ignoring the list value and just using it as a sort of iteration counter:
println
println(
  List(1,1,1,1,1).foldLeft(1)((x,_)=>x*2)
)
println(
  Math.pow(2,5)
)


//This gives only 50, because it ignores the input being passed along:
println
println(
  List(1,2,3,4,5).foldLeft(1)((_,y)=>10*y)
)


//This is Scala's idea of a "better" way to write fold, using the obnoxious /:, but from what
//I can see everyone on stack overflow hates it. It does make it harder to understand for newbies:
println
println(
  (10 /: List(1,2,3,4,5)) (_*_)
)


//OK let's play with the currying. I can do this I guess. Note how I put the "_"
//in after the fold() call because you have to do that for some dumb reason.
println
val x=List(1,2,3).foldLeft(0)_
println(x(_+_))
println(x(_*_))


/*
  Here, with foldLeft, we're just making a copy of the list, but with foldRight, 
  we reverse it. Note how (list, y) has to be reversed to (y, list) because
  Scala swaps things around for no good reason.
  
  Also note that we had to toss in a "type annotation" on the fold..() method. 
*/
println
val str=List("Hello", "I", "am", "nutso");
println(
  str.foldLeft [List[String]](Nil)((list, y)=>y :: list)
)
println(
  str.foldRight[List[String]](Nil)((y, list)=>y :: list)
)


/* 
  The following duplicate each other's results, 
  one with foldLeft(list), the other foldRight(list.reverse).
  
  (Note you will get *almost* the exact same value without the reverse, but 
   precision loss happens differently when we divide by the largest number
   first instead of last)
*/
println

//Computes a list of 1.0,2.0...9.0:
val nums=List.range(1, 10).map(_*1.0)

println(
  nums.foldLeft(1.0)((x,y) =>{x/y})
)
println(
  nums.reverse.foldRight(1.0)((x,y)=>{y/x})
)

