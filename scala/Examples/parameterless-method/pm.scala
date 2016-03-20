//Height & width are parameterless methods. They are not supposed to have side effects
//but that is only standard practice. But we follow it here.
class Bum(base:Int) {
  def height:Int=( 2 * 2 * 12 * base)
  def width :Int=( height/2 )
  override def toString=("Base:"+base+" width:"+width+" height:"+height)
}

val bum=new Bum(12);
println("Height:"+bum.height+" Width:"+bum.width);
println(new Bum(13));