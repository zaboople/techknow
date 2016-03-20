//This demonstrates Option and its subclasses Some & None. 
//Note that we are using this layer typical Java evil
//over Scala's much more strict Map behavior.
println

def getValue(o:Option[String]):String=
  o match {
    case Some(a)=>a
    case None=>null //Evil. This is literally null, not an object.
  }
val map=Map("Yo"->"Yo whatever", "Hi"->"How are you", "Hello"->"Hello yourself")
println(getValue(map.get("Yo")))
println(getValue(map.get("blerg")))

//This slight revision is more convenient:
def getValue(name:String, values:Map[String,String]):String=
  values.get(name) match {
    case Some(a)=>a
    case None=>null
  }
println(getValue("Hi", map))


