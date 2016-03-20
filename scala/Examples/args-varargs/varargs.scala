def echo(strs: String*)= {
  println("Length: "+strs.length);
  for (str <- strs) println(str)
}
 
echo("one","two","three");
println

//This is the goofy junk we have to stick on 
//so that the function accepts our array of strings
//as a varargs array of strings (that's what the parameter really is):
echo(args:_*)