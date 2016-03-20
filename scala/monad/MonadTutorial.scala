//////////////////////////////////////////////////////////////////////////////////////////////////////////////
//                          MY BIG TUTORIAL ON MONADS AND FOR COMPREHENSIONS                                //
//////////////////////////////////////////////////////////////////////////////////////////////////////////////

  
/*************************************************************************************************************  
  PART 0: INTRODUCTION
  
  These examples are written in Scala. If you want to experiment, they run directly in the Scala interpreter. 
  Download a recent release of scala, get your path/environment variables straightened out, and from the command 
  line, type something like this:
        
      scala.bat MyBigMonadTutorial.scala
      scala.sh MyBigMonadTutorial.scala
      
  The following lines invoke all of the examples in one fell swoop; comment them out as you like:
*/

exI1
exI2
exI3
exII1
exII2
exIV

/*************************************************************************************************************  
  PART I: FOR COMPREHENSION ESSENTIALS 
  
  Scala for-comprehensions have 3 main features:
    1) Combined for loops
    2) Combined conditional ("if") logic
    3) Yield. 
*/

def exI1(){
  println("\nExample I.1: Combining for loops")

  // First, the traditional nested loop, and then the "monadic" flat way:
  for (i <- Range(0,4))
    for (j <- Range(0,4))
      for (k <- Range(0,4))
        print((1+i+j+k)+" ")
  println

  for (i <- Range(0,4);
       j <- Range(0,4);
       k <- Range(0,4))
    print((1+i+j+k)+" ")
  println    
}

def exI2(){  
  println("\nExample I.2: Combining for & if")              
  for (i <- Range(0,50) if i % 5 ==0;
       j <- Range(0,10) if j % 2 ==0)
    print((i+j)+" ")
  println   
}

def exI3(){
  println("\nExample I.3: Yield")

  // This prints "Vector(a7, b7, c7, a9, b9, c9, a11, b11, c11, a13, b13, c13)":
  val listyThing:Seq[String]=
    for (i <- Range(0,10) if i % 5 ==0;
         j <- Range(0,10) if j % 2 ==0;
         k <- List("a","b","c"))
      yield k+(i+j)
  println(listyThing) 
  
  // So with "yield", a for loop becomes an expression, i.e. it evaluates to a result. 
  // In this case the result has the "Vector" type, which like Range & List is... listy.   
  // Now you know all the interesting things that for-comprehensions can do. 
}



/************************************************************************************************************
  PART II: LAMBDAS
  
  Most programmers already know lambdas nowadays* but to get to the next part, I need a good
  example use case for lambdas: Opening and closing a file, which usually involves 
  a lot of tedious setup-and-teardown boilerplate with interesting stuff in the middle. 
  
  1) To avoid writing to your hard drive, I've made a fake output-stream-like class called OutStream.
  2) Then I've defined writeFile(), which sets up an OutStream, calls your function with it, then closes it.
   
  *If you are an old beardy C programmer, they're really just function pointers.
*/

class OutStream(filename:String) {
  private var buffer:List[String]=List()
  def open=
    println("Starting exhausting boilerplate file-opening stuff for "+filename+"...")
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


def exII1() {
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

def exII2(){
  // Now we'll open a bunch of files and write to all of them at once,
  // which results in horrible nested messiness:
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
  PART IV: MINOR MAGIC 

  Now I'm going to make the previous example more concise by adding a a desperately dirt poor, barefoot, lazy 
  good-for-nothing person's monad, which is perfectly adequate:
*/

def exIV() {

  println("\nExample IV.1: Using a poor person's monad to simplify lambda calls")

  //Dirt poor & barefoot as promised. Note how this is is just a proxy to writeFile()
  //but with a very minimal wrapper:
  case class OutStreamNad(fileName:String) {
    def foreach(functionPtr:(OutStream)=>Unit)=
      writeFile(fileName, functionPtr)
  }

  // Recall all the indention and parenthefication from example III.2; this does the same thing with less.
  // The trick is that Scala automatically translates all of the "xxx <- yyyy"'s  into foreach()'s. 
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

/*
  PART V: We're going to get more exotic now. Instead of "output streams", we're going to do "input streams".
    As before, it's sort of fake but it illustrates the point. 
*/
class RandomInStream(filename:String) {
  private val data:Seq[Int]={
    val random=new java.util.Random(System.currentTimeMillis())
    for (i <- 1 to 10)
      yield random.nextInt(100)
  }
  def open=println("Starting exhausting boilerplate file-opening stuff for "+filename+"...")
  def read():Seq[Int]=data
  def close=println("...Finished exhausting boilerplate file-closing stuff for "+filename)
}



case class RandomInStreamNadder(filename:String) {
  def foreach(fptr:(RandomInStream)=>Unit)=
    doWith(fptr)
  def map[T](fptr:(RandomInStream)=>T):T= //This is only needed if we're last in the for-comp.
    doWith(fptr)  
  def flatMap[T](fptr:(RandomInStream)=>T):T=
    doWith(fptr)

  private def doWith[T](fptr:(RandomInStream)=>T):T={
    val instr=new RandomInStream(filename)
    instr.open
    try fptr(instr)
    finally instr.close
  }
}

val q=
  for (
        x1 <- RandomInStreamNadder("in1");
        x2 <- RandomInStreamNadder("in2");
        y1 <- x1.read() if y1>2;
        y2 <- x2.read() if y2>4
      )
    yield y1+":"+y2
println(q)

