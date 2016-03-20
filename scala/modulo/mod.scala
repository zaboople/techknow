//Demonstrates that the remainder is either the numerator when the divisor is larger, or the remainder.
//So 3 % 10 = 3, strange as that is.
for {x <- 1 to 10
     y <- 1 to 10}
    println(x+" % "+y+"\t="+(x%y));