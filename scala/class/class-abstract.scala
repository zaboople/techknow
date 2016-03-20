abstract class AbstractThing {
  //This is an abstract method. Note how extenders
  //do not have to use "override" when implementing it:
  def contents: List[String]
}

class Thing extends AbstractThing {
  //Yes this is a function
  def contents=List("12", "123", "1234");
}
class OtherThing extends AbstractThing {
  //Note how a val (a field) overrides a function (well, a method)
  val contents=List("a12", "b123", "c1234");
}
class AnotherThing extends AbstractThing {
  //Wow, a var can do it too, and this gets weird:
  var contents=List("p12", "f123", "c234");
  contents=List("a", "b", "c");
}


val c:AbstractThing=new Thing();
println(c.contents);

val d:AbstractThing=new OtherThing();
println(d.contents);

val e:AbstractThing=new AnotherThing();
println(e.contents);//Shows a b c