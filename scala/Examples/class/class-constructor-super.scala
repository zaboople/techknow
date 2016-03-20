class x(val name:String)
println(new x("Made up").name);

//Invoke super constructor with "Hi":
class y extends x("Hi")
//The empty parens are required:
println(new y().name);

//Here's another version:
class z(val zeep:String) extends x(zeep)
println(new z("puke").zeep)