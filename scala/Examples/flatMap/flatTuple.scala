println
val map=Map(1->2, 2->3, 3->4)
println("\nStart with a map: "+map)

println("\nFlatten the tuples into individual values:")
map.flatMap(
    x=>x.productIterator
  ).foreach(x=>print("  Value:"+x))
println	

println("\nConvert the tuples into strings, which get flattened into a list of characters:")
map.flatMap(
    x=>x.toString
  ).foreach(x=>println("  "+x))
println
