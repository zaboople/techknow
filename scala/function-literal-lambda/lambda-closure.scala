
//Note how I can modify a variable inside the closure:
var total=0;
List(1,2,3,4,5).foreach(total+=_);
println(total)

//This is the long form of the previous, in case you forgot the _ shortcut:
List(1,2,3,4,5).foreach((x:Int)=>total+=x);
println(total)