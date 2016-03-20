//Example 1: How apply & update are used to create native-looking
//           functionality:
//This:
{
  //Constructor:
  val ss=Array apply("1", 1, 1.1);
  
  //Reference
  println(ss apply 2);
  
  //Update
  ss update(2, 199)
  
  //Reference
  println(ss apply 2);
}
//Is the same as this:
{
  //Constructor:
  val ss=Array("1", 1, 1.1)
  
  //Reference
  println(ss(2))
  
  //Update
  ss(2)=199
  
  //Reference
  println(ss(2))
}



