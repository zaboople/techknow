//Note how I can import more than one thing:
import java.util.{List, ArrayList}

//Or just one:
import java.util.Date

//Or goodness forbid, all
import java.text._

object JavaCall {

  def main(args: Array[String]) {

    //Note use of parameterized types:
    val list= new ArrayList[String];
    val list2= new java.util.ArrayList[String];
    
    //This only works for functions with one parameter, best as
    //I can tell (update - considered bad form when there are side effects):
    list add "hello";
    
    //This is the more typical syntax, just like java:
    list.add("world");
    list.add(0, "goodbye");

  }

}