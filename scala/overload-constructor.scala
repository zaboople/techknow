class Stinker(puketron:String) {

  //Here is the magic part:
  def this()=this("Default")
  def this(splang:Int)=this("Number: "+splang)

  override def toString="Stinker: "+puketron
}

println(new Stinker("hi there"))
println(new Stinker())
println(new Stinker(12))