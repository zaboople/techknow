// This is a demonstration of doing validation that sucks because
// there's no way to force left projections to stop evaluating after the first fail, i.e.
//   .right.map(...)
//   .left.map(...)
//   .right.map(...)
//   .left.map(...) 
// The rights will stop happening as soon as we have a left, but the lefts will keep going. 


case class Things(one:Option[String]=None, two:Option[String]=None, three:Option[String]=None) 
println(
  List(
    validate(Things(None)),
    validate(Things(one=Some("hi"))),
    validate(Things(one=Some("hi"), two=Some("fa"))),
    validate(Things(one=Some("hi"), two=Some("fa"), three=Some("so")))
  ).mkString("\n")
)


def validate(things:Things):Either[String, Things]=
  Right(things)
    .right.flatMap(
      t=>if (t.one.isDefined) Right(t) else 
        .filter(_ => t.one.isDefined)
        .toRight(t)
        .left.map(_ => "Missing first")
    )
    .right.flatMap(
      t=>Option(t)
        .filter(_ => t.two.isDefined)
        .toRight(t)
        .left.map(_ => "Missing second")
    )    
    .right.flatMap(
      t=>Option(t)
        .filter(_ => t.three.isDefined)
        .toRight(t)
        .left.map(_ => "Missing third")
    )    
