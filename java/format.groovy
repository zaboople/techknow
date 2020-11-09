println("=========")

println("\n*** Fixed width decimal, right justified:")
println(String.format("%10.4f", 100D/2234D))

println("\n*** Only care about getting 4 chars after decimal point:")
println(String.format("%.4f", 100D/34D))
println(String.format("%.4f", 1001111D/34D))
println(String.format("%.4f", 1D/34D))

println("\n*** Left justify:")
println(String.format("%-15d", 111)+"hey");
