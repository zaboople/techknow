println

val arg1 = if (args.length > 0) args(0) else ""
arg1 match {
  case "a" => println("pepper")
  case "b" => println("salsa")
  case "c" => println("bacon")
  case _   => println("huh?")
}


val arg2 = if (args.length > 1) args(1) else ""
val res=arg2 match {
  case "a" => 1
  case "b" => 2
  case "c" => 3
  case _   => 4
}
println(arg2+"->"+res);
