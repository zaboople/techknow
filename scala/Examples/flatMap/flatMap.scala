println
println("A boring flatmap that just does some multiplication:")
println(
  List(
    List(1,2,3,4,5) map (_*10),
    List(6,7,8,9,10) map (_*11)
  ) flatMap (x=>x)
)

println
println("Another boring flatmap that uses _:, and also comingles types. Regrettably, the comingling is very limited;")
println("Array & Option could not be mixed with List & Seq.")
println(
  List(
    List(1,2,3,4,5)
    ,
    Seq(6,7,8,9,10)
  ) flatMap (x=>x map(_*2))
)


println
println("Not fully flattened, because we triple-nested lists. flatMap() only goes 1 deep")
println(
  List(
    List(List(1,2,3)), 
    List(List(3,4,5))
  ).flatMap(x=>x)
)


/**
   This will not work because not everything is the same type:
    println(
        List(List(1, 2, 3), 4, 5) flatMap(x=>x) 
    )
    
    This will not work because this is not a list of list-y things.
    println(List(1,3,4,6).flatMap(x=>x))
*/


println
println("This demonstrates how flatMap turns Option[Int] into Int. It erases the Option part")
println("""because it "flattens" the structure. Note how the None's dissapear. """)
val curd=List(Some(1), Some(2), Some(3), None, Some(4), None)
println(curd.flatMap(x=>x))

