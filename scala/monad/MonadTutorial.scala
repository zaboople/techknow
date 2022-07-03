/***************************************************************************************
  These examples are written in Scala. If you want to experiment, they run directly in
  the Scala interpreter. Download a recent release of scala, get your path/environment
  variables straightened out, and from the command line, type something like this:

      scala MonadTutorial.scala

  Each example is defined as function. To run one, just edit the file and add a call to
  the function you want to see, like so:

      exampleMonadFunctions

  Our pointlessIntroduction() function is already enabled and will print a message if
  you run this right now.
****************************************************************************************/

// Print the intro:
pointlessIntroduction

def pointlessIntroduction() {
  println("""
    |  You've probably had bad experiences with Monad tutorials before, and so it would
    |  be good to explain why:
    |
    |  1. They start by promising, "Don't worry, you won't need to understand monad math!"
    |     and then do what? Babble on and on about monad math, of course.
    |  2. To compensate for that offense, the author promises examples, which sounds
    |     encouraging until you find out that the examples are in Haskell, and so you
    |     figure, "Okay, I'm sure can figure this Haskell thing out..." Nope.
    |  3. They never tell you what a monad actually is, maybe because they have no idea, or
    |     maybe because their priorities are out of whack, or whatever....
    |
    |  So:
    |  1. There will be no monad math. We super-double-trouble-pinky-swear-it.
    |  2. We're going to use Scala instead of Haskell, and yes, you've heard bad things
    |     about Scala and most of them are true, but in this context Scala is a very good
    |     teaching language, and really, just about anything compares well to Haskell. If you
    |     can handle C# & Java, you can probably figure out the minimum of Scala we use here.
    |  3. We're actually going to tell you what a Monad is, in plain English. We'd do it in
    |     plain Spanish if we knew Spanish, but we do not. Same for Russian and so forth.
  """.stripMargin)
}

def exampleLambda() {
  println("""
    |  Part 0: Lambdas for the unfamiliar, or just unfamiliar with Scala, for
    |          completeness' sake
    |
    |  By this point in history most programmers know what lambdas are, including a
    |  few who don't know they know. You can think of a lambda as a
    |    - function pointer (hello C programmers!)
    |    - callback
    |    - inline function
    |    - anonymous function
    |    - "lightweight" anonymous inner class (java is so silly)
    |    - function literal (just to confuse people)
    |
    |  The basic idea is you ought to be able to assign functions to variables, and even
    |  pass functions to other functions, thus allowing them to "call you back" - and
    |  so a "callback". When you see a "=>" in scala, that's a lambda. This example
    |  uses a lambda that takes an Int as input and returns a String:
  """.stripMargin)

  // Here, "f" is a lambda:
  def beforeAfter(f: Int=>String){
    println("  Before calling f(10)...")
    val result:String = f(10)
    println("     You said: \""+result+"\".")
  }

  println("Lambda in a variable...")
  val myFunction:(Int=>String) =
    (x:Int) => {
      "-> I say "+(x+3)+" ! <-"
    }
  beforeAfter(myFunction)

  println("Same lambda completely inline...")
  beforeAfter(x=>{
    "-> I say "+(x+3)+" ! <-"
  })

}


def exampleMonadFunctions() {
  println("""
    |  Part I: WELL TELL ME WHAT A MONAD IS ALREADY
    |  We made a promise, so let's keep it: What the heck is a Monad? Here's what:

    |      A Monad is a list. There. That's it. Really!

    |  More specifically, a Monad is an ultra-concise definition of a List, or we might
    |  say, "Listiness", because some people get very fussy about what a "list" is
    |  and/or isn't, not to mention what a monad is and/or isn't. So as a sort of legal
    |  disclaimerese:

    |      Monads are listy!

    |  The conciseness of monads is handy, because if something wants to be listy - maybe
    |  even something not often thought of as listy - it can do so without a lot of messy
    |  programming; yes, you can make your own monads! After all, they're supposed to be
    |  useful.

    |  A Java programmer might think of a Monad as an interface, although in Scala
    |  there's no official Monad type/class/interface/etc. They're just sort of implicitly
    |  duck-typed: If it quacks, flaps, and meanders like a Monad, it it is one, and
    |  the compiler silently recognizes your Monadness. So to make a Monad you need to
    |  write the Five Essential Monad Functions, or in object-oriented terms... methods:

    |    identity: Effectively a constructor, in object-oriented terms; it's a way
    |              to shovel stuff into our Monad so it's ready to go. In scala this
    |              is called "apply".

    |  The latter 4 are all defined in terms of lambdas:

    |    foreach:  Calls your lambda once for each thingie in the monad.
    |    map:      For transforming a list(y) of X into a list(y) of Y, where the
    |              function input is a lambda that does the transform.
    |    filter:   Filters the list into a smaller list; the function input is a
    |              boolean lambda that gives a thumbs up/down on each member.
    |    flatmap:  A weird one: Suppose your call to map() results in a list of
    |              lists, and you'd like to "flatten" out the nesting into just
    |              a plain list. Flatmap does this. And... That's easy to understand, but
    |              you're probably thinking, "I get why the others are important, but
    |              why this one?" Bear with us.

    |  Notice how listy those are? Yeah. Anyhow, we could use some examples.
  """)


  println("  identity:            "+List.apply(1,2,3))
  println("  identity(shorthand): "+List(1,2,3))

  // Are strings listy? Why, yes, they are:
  val list:String="hello world"

  print("  foreach: ")
  list.foreach(c=>print(c+"-"))
  println()

  println("  map:     " + list.map(c => c.toInt))
  println("  filter:  " + list.filter(c => c.toInt > 110))
  println("  flatMap: " + list.flatMap(c => List(c.toInt, c.toInt + 1)))
  println()
  println("Note: flatMap only does one level of flattening (it's not recursive):")
  println("  flatMap: " + list.flatMap(c => List(List(c.toInt, c.toInt + 1))))
}

def exampleForComp(){
  println("""
    |  For-comprehensions (AKA do-comprehension, list-comprehension, etc.) look like
    |  familiar for-each "loops" that you see in other languages, but they aren't quite
    |  the same. The compiler actually de-sugars FC's into calls to the 5 Monad functions we
    |  demonstrated earlier.
    |
    |  Admittedly, at first glance FC's look like a clever but non-essential parlor trick.
    |  We'll eventually see how we can leverage for-comprehensions as a general-purpose
    |  remedy for "callback heck", but first some basic examples:
    |
    |  * Example 1: Flattening for loops *
    |  For-comprehensions allow you to "flatten" what would normally be a nested for-loop:
  """.stripMargin)

  println("Harder way:")
  for (i <- List(0,1,2))
    for (j <- List(2,3,4))
      for (k <- List(5,6,7))
        print((i+j+k)+" ")
  println

  println("Easier way:")
  for (i <- List(0,1,2);
       j <- List(2,3,4);
       k <- List(5,6,7))
    print((i+j+k)+" ")
  println

  println("Compiler actually de-sugars this to apply's & foreach's:")
  List.apply(0,1,2).foreach(i=>
    List.apply(2,3,4).foreach(j=>
      List.apply(5,6,7).foreach(k=>
        print((i+j+k)+" ")
  ) ) )
  println

  println("""
  |  * Example 2: Combining for & "if" *
  |  For comprehensions also allow you to include "if" statements inline,
  |  sometimes called "guard expressions".
  """.stripMargin)
  println("Harder way:")
  for (i <- Range(0,30))
     if (i % 5 ==0)
        for (j <- Range(0,8))
          if (j % 2 ==0)
            print((i+j)+" ")
  println

  println("Easier way:")
  for (i <- Range(0,30) if i % 5 ==0;
       j <- Range(0,8) if j % 2 ==0)
    print((i+j)+" ")
  println

  println("De-sugaring invokes 2 monad functions this time: foreach & filter")
  Range.apply(0,30)
    .filter(i => i % 5 ==0)
    .foreach(i=>
      Range.apply(0,8)
        .filter(j => j % 2 ==0)
        .foreach(j=>
          print((i+j)+" ")
        )
    )
  println()

  println("""
    |  * Example 3: Yield *
    |  An FC can act as an expression that returns a value, using the "yield" keyword.
    |  That "return value" is always another listy monad, and this brings map & flatmap
    |  into play. As type-specifics go, Scala's collection library has rather quirky
    |  inheritance that can make the outgoing type unpredictable-ish... but it's listy.
  """.stripMargin)

  println("Mashing 3 listy things into one listy thing:")
  val listyThing: Seq[String]=
    for (i <- Range(0,10) if i % 5 ==0;
         j <- Range(0,4) if j % 2 ==0;
         k <- List("a","b","c"))
      yield k+(i+j)
  println(listyThing)
  println()

  println("Desugared version now shows flatmap & map (no foreach):")
  val another=
    Range.apply(0,10)
      .filter(i => i % 5 == 0)
      .flatMap(i =>
        Range.apply(0,4)
          .filter(j => j % 2 == 0)
          .flatMap(j =>
            List.apply("a","b","c")
              .map(k => k+(i+j))
          )
      )
  println(another)
  println("""
    |  That de-sugaring answers our earlier question: Why do monads need flatmap?
    |  If you replace "flatMap" with "map" above (feel free to try it) you'll end up with a
    |  triply-nested monad mess. The flattening gives us a more useful result.

    |  And that's pretty much it for Monad syntax & semantics. Was that hard? Meh, not
    |  real hard, no, but... So now you're thinking, yeah, it's a nice parlor trick, like a
    |  poodle jumping through a hoop (golf clap etc.), but where's that Monad Magic we heard
    |  all about?
  """)
}


/*********************************************************************************************
  PART II: CALLBACK HECK.
*********************************************************************************************/
exampleCallbackHeck
def exampleCallbackHeck(){
  println("""
    |  Part II. Callback Heck
    |
    |  So we discovered lambdas, and got really excited about them, started using them
    |  everywhere, and it's really great... until we had callbacks in our callbacks
    |  calling our callbacks and a big fat mess. What to do?
    |
    |  First, a concrete example:
    |
    |  Example 1: File I/O
    |  This is a decent use case for lambdas, since it involves the whole thing of
    |  make-sure-to-close-your-output/inputstream, often done as try-finally. First we've
    |  defined a fake "OutStream" class (so we don't make a mess of your hard drive) and
    |  a handy lambda wrapper for making it easier to use:
  """.stripMargin)

  // Our fake OutputStream:
  class OutStream(filename:String) {
    private var buffer:List[String]=List()
    def open=
      println("Opening "+filename+"...")
    def print(s:Any)=
      buffer=s.toString :: buffer
    def close(){
      System.out.print("Contents of "+filename+": ")
      for (s <- buffer) System.out.print(s+" ")
      System.out.println()
    }
  }
  // The lambda wrapper for above:
  def withFileOutput(fileName:String, f:(OutStream)=>Unit)={
    val out=new OutStream(fileName)
    out.open
    try f(out)
    finally out.close
  }

  println("A test drive of withFileOutput() lambda: ")
  withFileOutput(
    "/home/phlagphth/file.txt", (out:OutStream)=>{
      out.print("hello")
      out.print("world")
      out.print("good")
      out.print("morning/afternoon.")
    }
  )

  println("""
    |  Example 2: The big fat mess
    |  What if we want to open a bunch of files at once? This example
    |  shows the ensuing nestedness of callbacks:
  """.stripMargin)
  withFileOutput("p2.txt", p2=>{
    withFileOutput("p3.txt", p3=>{
      withFileOutput("p5.txt", p5=>{
        withFileOutput("p7.txt", p7=>{
          for (i <- Range(0, 100)) {
            if (i % 2 ==0) {p2.print(i)}
            else
            if (i % 3 ==0) {p3.print(i)}
            else
            if (i % 5 ==0) {p5.print(i)}
            else
            if (i % 7 ==0) {p7.print(i)}
          }
        })
      })
    })
  })

  println("""
    |  Example 3: Using a poor person's monad to simplify
    |
    |  Now we'regoing to make the previous example more concise by adding a dirt poor,
    |  barefoot, lazy good-for-nothing person's monad. Note how this is is just a proxy
    |  to withFileOutput() but with a very minimal foreach-based wrapper: It only has
    |   1. identity AKA apply() because you get that for free with case classes,
    |      which are just a very slight enhancement on plain ol' classes
    |   2. foreach
    |  You'll also notice that this Monad is only listy in the sense that it is exactly
    |  one-thing-long - hmph! There's nothing wrong with a list of exactly one, though.
  """.stripMargin)

  case class OutStreamNad(fileName:String) {
    def foreach(functionPtr:(OutStream)=>Unit)=
      withFileOutput(fileName, functionPtr)
  }

  // Behold: Flattening! Our callback heck looks much less hecky, even though we're
  // doing the same thing.
  for (p2 <- OutStreamNad("p2.txt");
       p3 <- OutStreamNad("p3.txt");
       p5 <- OutStreamNad("p5.txt");
       p7 <- OutStreamNad("p7.txt");
       i  <- Range(0, 100)){
      if (i % 2 ==0) {p2.print(i)}
      else
      if (i % 3 ==0) {p3.print(i)}
      else
      if (i % 5 ==0) {p5.print(i)}
      else
      if (i % 7 ==0) {p7.print(i)}
  }
}

def exampleFull() {
  println("""
  |  PART V: A full-fledged monad
  |
  |  So OutputStream's are easy because there isn't usually a return value;
  |  InputStream's are all about return values, so how would we do that?
  |  We'll make a full-fledged monad then. But even this time, it's kind
  |  of trashy and cheap:
  |    - withFilter() really doesn't make sense in this context, but it's
  |      here for the sake of completeness; because of it, I'm allowing nulls,
  |      which will some people will find upsetting.
  |    - flatMap() isn't flattening, because, well, it doesn't need to, even
  |      though it is being triggered by our for comprehension.
  """.stripMargin)

  // Here is our fake input stream:
  class FakeInputStream(filename:String) {
    private var curr:Int=0
    def open()=
      println("[Opening "+filename+"...]")
    def read()={
      curr=curr+1
      filename+"<"+curr+" input>"
    }
    def close()=
      println("[Closing "+filename+"]")
  }

  // Monadic FakeInStream wrapper:
  case class InStreamNad[T](filename:String) {
    val instr=new FakeInputStream(filename)

    def foreach(f: (FakeInputStream)=>Unit):Unit=
      if (filename!=null)
        withStream(()=>f(instr))
    def map(f: (FakeInputStream)=>T):T=
      if (filename!=null)
        withStream(()=>f(instr))
      else
        null.asInstanceOf[T]
    def flatMap(f: (FakeInputStream)=>T):T=
      map(f)
    def withFilter(f: (FakeInputStream)=>Boolean):InStreamNad[T]=
      if (f(instr)) this
      else InStreamNad(null)

    // I've kept my lambda-helper strictly internal this time:
    private def withStream[Q](f: ()=>Q):Q={
      instr.open()
      val t:Q=f()
      instr.close()
      t
    }
  }

  println("Reading from three files at once...")
  println(
    for (
        s<-InStreamNad("file1.txt");
        t<-InStreamNad("file2.txt");
        u<-InStreamNad("file3.txt")
      )
      yield List(
        s.read(),
        t.read(), t.read(),
        u.read(), u.read(), u.read()
      )
  )
  println
  println("Reading from three files at once, but throwing in")
  println("an \"if\" guard that shuts it all down...")
  println(
    for (
        s<-InStreamNad("file1.txt") if false;
        t<-InStreamNad("file2.txt");
        u<-InStreamNad("file3.txt")
      )
      yield List(
        s.read(),
        t.read(), t.read(),
        u.read(), u.read(), u.read()
      )
  )
}
