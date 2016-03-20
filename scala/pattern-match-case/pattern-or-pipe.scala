//This shows a pattern with multiple values, so you can 
//say "1 or 2 or 3"
def toYesOrNo(choice: Int): String = choice match {
    case 1 | 2 | 3 => "yes"
    case 0 => "no"
    case _ => "error"
}

println(toYesOrNo(3))