case class Validator[T](t:T, isValid:Boolean=true, errors:List[String]=Nil) {
  def is(test:T => Option[String]):Validator[T]=
    test(t)
      .map(
        s=>Validator(t, false, s::errors)
      )  
      .getOrElse(
        Validator(t, isValid, errors)
      )
  /** Executes only if we are valid, else ignored */
  def and(test:T => Option[String]):Validator[T]=
    if (isValid) is(test) else this
  def get: Option[T]=
    if (isValid) Some(t) else None
  def either: Either[List[String], T]=
    if (isValid) Right(t) else Left(errors)
  def failed: List[String]=
    if (isValid) Nil else errors
}

///////////////
// EXAMPLES: //
///////////////

case class Things(one:Option[String]=None, two:Option[String]=None, three:Option[String]=None) 

  
def validate2(t:Things)=
  Validator(t)
    .is(
      _=>if (t.one.isDefined) None else (Some("Needs one"))
    )
    .and(
      _=>if (t.two.isDefined) None else (Some("Needs two"))
    )
    .and(
      _=>if (t.three.isDefined) None else (Some("Needs three"))
    )
    .failed
println(
  "Validate if previous:\n"+
  List(
    validate2(Things(None)),
    validate2(Things(one=Some("hi"))),
    validate2(Things(one=Some("hi"), two=Some("fa"))),
    validate2(Things(one=Some("hi"), two=Some("fa"), three=Some("so")))
  ).mkString("\n")
)


def validate3(t:Things)=
  Validator(t)
    .is(
      _=>t.one.flatMap(_=>None).map(_=>"Needs one")
    )
    .is(
      _=>t.two.filter(_.contains("fa")).flatMap(_=>None).map(_=>"Needs two")
    )
    .is(
      _=>if (t.three.isDefined) None else (Some("Needs three"))
    )
    .failed
println(
  "Validate all no matter what:\n"+ 
  List(
    validate3(Things(None)),
    validate3(Things(one=Some("hi"))),
    validate3(Things(one=Some("hi"), two=Some("fa"))),
    validate3(Things(one=Some("hi"), two=Some("fa"), three=Some("so")))
  ).mkString("\n")
)
    
    
    

def validate4(t:Things)=
  Validator(t)
    .is(
      _=>if (t.one.isDefined) None else (Some("Needs one"))
    )
    .and(
      _=>if (t.two.isDefined) None else (Some("Needs two"))
    )
    .is(
      _=>if (t.three.isDefined) None else (Some("Needs three"))
    )
    .failed
println(
  "Validate one & three no matter what, two only if one fails\n"+
  List(
    validate4(Things(None)),
    validate4(Things(one=Some("hi"))),
    validate4(Things(one=Some("hi"), two=Some("fa"))),
    validate4(Things(one=Some("hi"), two=Some("fa"), three=Some("so")))
  ).mkString("\n")
)
    