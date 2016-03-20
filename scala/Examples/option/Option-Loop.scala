/**
 * In all of the cases below I'm demonstrating how you can loop through the "values" (or really, value) 
 * in an Option.
 */

def a(x:Int):Option[Int]=Some(x+1)
def b(x:Int):Option[Int]=Some(x+2)
def c(x:Int):Option[Int]=Some(x+3)
def d(x:Int):Option[Int]=Some(x+4)

println("\nUsing a chain of foreach's to print one result:");
//Note that x is simply "re-bound" each time and the old value is lost.
a(0).foreach(
  x=>b(x).foreach(
    x=>c(x).foreach(
      x=>d(x).foreach (
        x=>println(x)
      )
    )
  )
)

println("\nSimplifying the foreach's with _:");
a(0).foreach(
  b(_).foreach(
    c(_).foreach(
      d(_).foreach (
        println(_)
      )
    )
  )
)

println("\nSimplifying the foreach's with a for-comprehension:");
//Here again, x is "re-bound".
for {x <- a(0)
     x <- b(x)
     x <- c(x)
     x <- d(x)}
  println(x)


println("\nUsing foreach again, but gathering the computations up as I go:");
a(0).foreach(
  x=>b(x).foreach(
    y=>c(y).foreach(
      z=>d(z).foreach (
        q=>println(x,y,z,q)
      )
    )
  )
)

println("\nUsing for-comprehension again, but gathering the computations up as I go:");
for {x <- a(0)
     y <- b(x)
     z <- c(y)
     zz<- d(z)}
  println(x,y,z,zz)  
