/*
  This shows how to implement get/set overrides in a class. Or they really aren't overrides per se, 
  just a way to provide the methods. The main trick is implementing "_="; note that the "_" & "=" cannot
  be separated by any whitespace.
 */


class Foo {
  private[this] var h = 12
  private[this] var m = 0
  def hour: Int=     {println("got hour!"); h}
  def hour_=(x: Int) {println("set hour!"); h = x;}
  def minute: Int = m
  def minute_=(x: Int) { m = x }
}

val e=new Foo();
e.hour=10
e.minute=12
println(e.hour+" "+e.minute)
