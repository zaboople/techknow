//Here is my simple generic class:
class MyClass[T](val myVal:T) {
  def get:T=myVal
}

//And here is the existential type. Oh is this weird or what.
def doof(z:MyClass[_])={
  println(z.get.toString)
}

//Both of these work:
val x=new MyClass("hello")
doof(x)
doof(new MyClass("Hi there"))