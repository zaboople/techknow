//3. CONSTANT PATTERNS:
def describe(x: Any) = x match {
  case 5       => "five"
  case true    => "truth"
  case "hello" => "hi!"
  case Nil     => "the empty list"
  case _       => "something else"
}
println(describe(4))

