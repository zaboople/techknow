// So the this.type trick allows an abstract class/trait to return the type
// of the concrete subclass (whatever it may be).
trait Stinker {
  val i=1
  def hooper: this.type=
    this
  override def toString:String=" "+i
}

class Stunker extends Stinker {
  val j=2
  override def toString:String=""+j+" "+super.toString
}

val s:Stunker=
  new Stunker().hooper.hooper.hooper
println(s)
println(new Stunker().hooper)