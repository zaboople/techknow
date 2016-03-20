
//This shows wildcards with the "|"-or trick documented in another file.

def checkit(zz:Tuple3[Int,Int,Int])=zz match  {
  case Tuple3(_, _, 3) | 
       Tuple3(_, 3, _) | 
       Tuple3(3, _, _) => println(zz +" has at least one 3 in it.")
  case _ => println(zz + " doesn't have any 3's in it")
}
checkit((1,2,4))
checkit((3,2,4))
checkit((3,3,4))
checkit((9,5,3))