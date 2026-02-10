println("=========")

println("\n*** Fixed width decimal, right justified:")
println(String.format("%10.4f", 100D/2234D))

println("\n*** Only care about getting 4 chars after decimal point:")
println(String.format("%.4f", 100D/34D))
println(String.format("%.4f", 1001111D/34D))
println(String.format("%.4f", 1D/34D))
println(String.format("%,d", 9000000))

println("\n*** Left justify (right-padding):")
println("-> "+ String.format("%-15d", 111) +"<- ");

println("\n*** Right justify:");
println(String.format("%5d", 1))
println(String.format("%5d", 13))

println("\n*** Right justify with zeroes:");
println(String.format("%05d", 1))
println(String.format("%05d", 13))

println("\n*** Left justify a string:")
println("-> "+ String.format("%-8s", "hello") +"<- ");
