//True-ish. 

//Interesting however because it shows us how flatMap() is used on the first N-1 
//elements of the for comprehension, and then map() cleans up on the last one (i.e. cleans up on N)

case class Bang(a:Appendable, i:Int, legit:Boolean=true){
  a.append("\nConstructor "+i)
  def log(name:String, value:Int){
    a.append(" ")
    a.append(name)
    a.append(" ")
    a.append(value.toString)
  }
  def foreach(f:Int=>Unit){
    log(" foreach", i)
    f(i)
  }
  def map(f:Int => Int):Int ={ 
    log("map", i)
    if (legit)
      Bang(a, f(i)).i
    else
      0
  }
  def flatMap(f:Int => Int):Int = {
    log("flatmap", i)
    if (legit)
      Bang(a, f(i)).i
    else
      0
  }  
  def withFilter(f:Int => Boolean):Bang = {
    log("filter", i)
    if (f(i))
      this
    else
      Bang(a, i, false)
  } 
  
}

//MAP/FLATMAP:
{
  val app=new java.lang.StringBuilder()
  println("\nMap/FlatMap: "+
    (
      for (
          x <- Bang(app, 1);
          y <- Bang(app, x*2* -1);
          if (y % 2 == 0);
          z <- Bang(app, y*2)
        )
        yield {
          app.append(" inside for")
          x+y+z+100
        }
    )
  )
  println("Log: "+app)
}

//FOREACH:
{
  val app=new java.lang.StringBuilder()
  for (
        x <- Bang(app, 1);
        y <- Bang(app, x*1);
        z <- Bang(app, x*2)
    ){
      app.append(" inside for")
    }
  println("\nForeach log: "+app)
}
