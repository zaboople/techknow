//This only works if b1 defines both a type, AND a default value:
class b1(val i:Int=2)
class b2(override val i:Int=4) extends b1 
println(new b1().i);
println(new b2().i);

//This only works if c1 defines i as a "def"; 
// - if defined as a "val" c3 will give errors (c2 will be ok since it's a val)
// - if defined as a "var" c2 & c3 will both give errors.
class c1 {
  def i=12;
}
class c2 extends c1 {
  override val i:Int=4
}
class c3 extends c1 {
  override def i:Int=4*13*1
}



