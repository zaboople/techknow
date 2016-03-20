//Here is my simple generic class:
class MyClass[+T](val myVal:T) {
  def get:T=myVal
}

//Note how this function takes an Any version of MyClass. It has 
//to specify something, so that's what it does.
def doof(z:MyClass[Any])={
  println(z.get.toString)
}

//This won't work unless we use the +. We've made it clear here that 
//MyClass is of type String:
val x=new MyClass("hello")
doof(x)

//This actually works without the +. That much is unexplained.
doof(new MyClass("Hi there"))