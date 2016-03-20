var lip=List(1,2,3);

//Concat a list:
lip=lip ::: List(2,3,4);
println(lip);

//"Cons" a list, add one element to beginning
lip=123 :: lip;
println(lip);

/* 
  This is an interesting way to create a list. 
    - Note the need for Nil,
      because "::" is a function of its right-hand operand, not left. 
      Anything that ends in ":" works that way.
    - Also note that Nil is not equivocal to "null"; it is a special
      subclass of List.
*/
val dip=12 :: 13 :: 14 :: Nil;
println(dip);