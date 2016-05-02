import scala.util.{Either,Left,Right}

//This shows how to go two ways with an either and come back together:

val good1=Some("xxx")
val good2=Some("yyyy")
val bad1=None
val bad2=None


def test(s:Option[String]): Int=
  s
    .toRight{
      println("Broken")
      -1
    }.right.map{
      x =>
        println("All good")
        x.length
    }.merge

println(test(good1))
println(test(bad1.orElse(bad2).orElse(good2)))

println(test(bad1.orElse(bad2)))