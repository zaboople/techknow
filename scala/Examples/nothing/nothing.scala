
//This is the only use for Nothing: A function whose purpose is to throw
//Exceptions. Otherwise the divide method would have a compile error on 
//the makeException call:
def makeException(msg:String):Nothing={
  println("Error "+msg);
  throw new Exception(msg)
};
def divide(x: Int, y: Int): Int =
  if (y != 0) x / y
  else makeException("can't divide by zero")
  
  
//This works too, of course:
def divide2(x: Int, y: Int): Int =
  if (y != 0) x / y
  else throw new Exception("I'm stupid")  

