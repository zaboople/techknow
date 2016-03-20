///////////////////
// MAIN PROGRAM: //
///////////////////

/*
 * Objective: Traverse a list of lists of lists... and print everything found that is not a list.
 */
val input=
  List(
    List(1, 7, List(2222, 2233, 3333, List(1,2,333.33))),
    2,
    List(
      List(List(23,32,3), 83747, List(4, List(99))),
      33333,
      "rrrr"
    )
  )
find(input)
println()
findTailRecursive(input)


////////////////
// SOLUTIONS: //
////////////////


// SOLUTION 1: Non-recursive: //
def find(list: List[Any]) {
  var li=list
  while (!li.isEmpty)
    li=find2(li)
}
def find2(list:List[Any]):List[Any]={
  var result=list.tail
  for (li <- check(list.head))
    result=check(li) :: result
  result
}

// SOLUTION 2: Tail-Recursive: not any more efficient, just harder to understand:
@scala.annotation.tailrec 
def findTailRecursive(list:List[Any]):List[Any]=
  if (list==Nil)
    Nil
  else 
    findTailRecursive(
      list.flatMap(check(_))
    )


////////////////
// UTILITIES: //
////////////////

def check(x:Any): List[Any]=
  x match {
    case li:List[Any]=> 
      li
    case x => 
      println(x)
      Nil
  }
