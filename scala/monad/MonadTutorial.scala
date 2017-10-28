//////////////////////////////////////////////////////////////////////////////////////////////////////////////
//                          MY BIG TUTORIAL ON MONADS AND FOR COMPREHENSIONS                                //
//////////////////////////////////////////////////////////////////////////////////////////////////////////////


/*************************************************************************************************************
  PART 0: INTRODUCTION

  These examples are written in Scala. If you want to experiment, they run directly in the Scala interpreter.
  Download a recent release of scala, get your path/environment variables straightened out, and from the command
  line, type something like this:

      scala.bat MonadTutorial.scala
      scala.sh MonadTutorial.scala

  Each example is defined as function; you can uncomment the line before it to run it.
*/


/*************************************************************************************************************
  PART I: FOR COMPREHENSION ESSENTIALS

  Scala for-comprehensions have 3 main features:
    1) Combined for loops
    2) Combined conditional ("if") logic
    3) Yield.
  And: They only work with monads.
*/

//exampleI1
def exampleI1(){
  println("\nExample I.1: Combining for loops")

  // First, the traditional nested loop, and then the "monadic" flat way:
  for (i <- Range(0,4))
    for (j <- Range(0,4))
      for (k <- Range(0,4))
        print((i+j+k)+" ")
  println

  for (i <- Range(0,4);
       j <- Range(0,4);
       k <- Range(0,4))
    print((i+j+k)+" ")
  println
}

//exampleI2
def exampleI2(){
  println("\nExample I.2: Combining for & if")
  for (i <- Range(0,50) if i % 5 ==0;
       j <- Range(0,10) if j % 2 ==0)
    print((i+j)+" ")
  println
}

//exampleI3
def exampleI3(){
  println("\nExample I.3: Yield")

  // This prints "Vector(a0, b0, c0, a2, b2, c2, a4, b4, c4, a5, b5, c5, a7, b7, c7, a9, b9, c9)":
  val listyThing:Seq[String]=
    for (i <- Range(0,10) if i % 5 ==0;
         j <- Range(0,6) if j % 2 ==0;
         k <- List("a","b","c"))
      yield k+(i+j)
  println(listyThing)

  // This is the exact same thing! In fact the scala compiler "desugars" for comprehensions
  // into the map/flatMap/filter functions just like we're doing here. But this way
  // is not as easy to read:
  val another:Seq[String]=
    Range(0,10)
      .filter(i => i % 5 == 0)
      .flatMap(i =>
        Range(0,6)
          .filter(j => j % 2 == 0)
          .flatMap(j =>
            List("a","b","c")
              .map(k => k+(i+j))
          )
      )
  println(another)

  // What's with this flatMap vs map thing? What happens if we just use plain map()? This happens:
  // Vector(Vector(List(a0, b0, c0), List(a2, b2, c2), List(a4, b4, c4)), Vector(List(a5, b5, c5), List(a7, b7, c7), List(a9, b9, c9)))
  val oneMore:Seq[Seq[Seq[String]]]=
    Range(0,10)
      .filter(i => i % 5==0)
      .map(i=>
        Range(0,6)
          .filter(j => j % 2==0)
          .map(j=>
            List("a","b","c")
              .map(k => k+(i+j))
          )
      )
  println(oneMore)

  // So with "yield", a for loop becomes an expression, i.e. it evaluates to a result.
  // In this case the result has the "Vector" type, which like Range & List is... listy.
  // Now you know all the interesting things that for-comprehensions can do.
}



/************************************************************************************************************
  PART II: LAMBDAS

  Most programmers already know lambdas nowadays* but to get to the next part, I need a good
  example use case for lambdas: Opening and closing a file, which usually involves
  a lot of tedious setup-and-teardown boilerplate with interesting stuff in the middle.

  1) To avoid writing to your hard drive, I've made a fake output-stream-like class called "OutStream".
  2) Then I've defined writeFile(), which sets up an OutStream, calls your lambda with it, then closes it.

  *If you are an old beardy C programmer, they're really just function pointers.
*/

class OutStream(filename:String) {
  private var buffer:List[String]=List()
  def open=
    println("Starting boilerplate file-opening stuff for "+filename+"...")
  def print(s:Any)=
    buffer=s.toString :: buffer
  def close(){
    System.out.print(" "+filename)
    for (s <- buffer) System.out.print(s+" ")
    System.out.println()
  }
}

def writeFile(fileName:String, functionPtr:(OutStream)=>Unit)={
  val out=new OutStream(fileName)
  out.open
  try functionPtr(out)
  finally out.close
}

// exampleII1
def exampleII1() {
  println("\nExample II.1: Lambda - simple use case")
  writeFile(
    "/home/phlagphth/file.txt", (out:OutStream)=>{
      out.print("hello")
      out.print("world")
      out.print("good")
      out.print("morning/afternoon.")
    }
  )
}

//exampleII2
def exampleII2(){
  // Now we'll open a bunch of files and write to all of them at once,
  // which results in semi-horrible nested messiness:
  println("\nExample II.2: Lambda - complex use case")
  writeFile("p2.txt", p2=>
    writeFile("p3.txt", p3=>
      writeFile("p5.txt", p5=>
        writeFile("p7.txt", p7=>
          for (i <- Range(0, 100))
            if (i % 2 ==0) p2.print(i)
            else
            if (i % 3 ==0) p3.print(i)
            else
            if (i % 5 ==0) p5.print(i)
            else
            if (i % 7 ==0) p7.print(i)
        )
      )
    )
  )
}

/*************************************************************************************************************
  PART III: MINOR MONAD MAGIC

  Now I'm going to make the previous example more concise by adding a a desperately dirt poor, barefoot, lazy
  good-for-nothing person's monad, which is perfectly adequate:

*/

exampleIII
def exampleIII() {

  println("\nExample III.1: Using a poor person's monad to simplify lambda calls")

  // Dirt poor & barefoot as promised. Note how this is is just a proxy to writeFile()
  // but with a very minimal wrapper:
  case class OutStreamNad(fileName:String) {
    def foreach(functionPtr:(OutStream)=>Unit)=
      writeFile(fileName, functionPtr)
  }

  // Recall all the indention and parenthefication from example III.2; this does the same thing with less.
  // The trick is that Scala automatically translates all of the "xxx <- yyyy"'s into our foreach()'s.
  for (p2 <- OutStreamNad("p2.txt");
       p3 <- OutStreamNad("p3.txt");
       p5 <- OutStreamNad("p5.txt");
       p7 <- OutStreamNad("p7.txt");
       i  <- Range(0, 100))
    if (i % 2 ==0) p2.print(i)
    else
    if (i % 3 ==0) p3.print(i)
    else
    if (i % 5 ==0) p5.print(i)
    else
    if (i % 7 ==0) p7.print(i)
}

/********************************************************************************************
  PART V:

  Now we'll attempt a full-fledged monad with all the bells & whistles.

  Once is similar to Option/Some/None. It works ok without relying on external classes,
  and for tutorial purposes we want to limit what the reader must ponder. So, apologies
  if it seems kinda hokey.
*/
case class Once[A](value:A=null, ok:Boolean=true){

  // This is an overly cute way to control what gets printed by toString()
  override def productArity=if (ok) 1 else 0

  def foreach(f:A=>Unit):Unit=
    if (ok)
      f(value)
  def map[T](f:A=>T):Once[T]=
    if (ok)
      Once(f(value))
    else
      Once(null.asInstanceOf[T], false)
  def flatMap[T](f:A=>T):Once[T]=
    if (ok) {
      val res=f(value)
      // Now for wacky "flattening" logic:
      if (res.isInstanceOf[Once[_]]){
        val o=res.asInstanceOf[Once[T]]
        Once(o.value, o.ok)
      }
      else
        Once(res)
    }
    else
      Once(null.asInstanceOf[T], false)
  def withFilter(f:A=>Boolean):Once[A]=
    Once(value, ok && f(value))

}

exampleIV()
def exampleIV() {
  println("\n* Basic for-yield:")
  println(
    for (
        x <- Once(5);
        y <- Once(2);
        z <- List(10, 20)
      )
      yield x * y * z
  )

  println("\n* Should do nothing:")
  for (
      x <- Once(12) if x > 12;
      y <- Once(3);
      z <- Once(x + y)
    )
    println(x * y * z)

  println("\n* Yield an empty Once:")
  println(
    for (
        x <- Once(12);
        y <- Once(3);
        z <- Once(4)  if x > 100
      )
      yield x * y * z
  )
}
