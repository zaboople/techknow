//This does not give us a field. Since we don't use it, it's lost.
class a1(x: List[Int])  

//This does, because we put "val" on it. 
class a2(val x: List[Int])  

//This also does, but with var.
class a3(var x: List[Int])  

//First one will generate an error, latter two are ok:
//println(new a1(List(1,2)).x);
println(new a2(List(1,2)).x);
println(new a3(List(1,4)).x);


