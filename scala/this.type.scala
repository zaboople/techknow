// So the this.type trick allows an abstract class/trait to return the type
// of the concrete subclass (whatever it may be).
trait Stinker {
  def hooper: this.type={
    println("shebang")
    this
  }
}

class Stunker extends Stinker {
}

val s:Stunker=
  new Stunker().hooper.hooper.hooper