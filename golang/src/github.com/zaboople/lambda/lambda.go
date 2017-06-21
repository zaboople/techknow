package main

import (
	"fmt"; "math"
)

///////////////////////
// LAMBDA RECEIVERS: //
///////////////////////

/*
	Receives a lambda as a parameter and executes it:
*/
func computeTwo(x float64, y float64, afunction func(a, b float64) float64) float64 {
	return afunction(x, y)
}


/*
	Declares a lambda type, which helps eliminate some repetition. The computeTwo2 function
	does the same as computeTwo but with a simpler definition:
*/
type TwoFloat func(x,y float64) float64
func computeTwo2(x, y float64, f TwoFloat) float64 {
	return f(x, y)
}

//////////////////////
// LAMBDA CREATORS: //
//////////////////////


/*
	Returns a closure. Note that a name like "by" is not required, I'm just being
	pedantic because it gives better readability in most cases:
*/
func adder() func(by int) int {
	sum := 0
	return func(i int) int {
		sum += i
		return sum
	}
}

/*
	Returns a fibonacci closure:
*/
func fibonacci() func() int {
	first  := 0
	second := 1
	return func() int {
		temp:=second
		second += first
		first=temp
		return second
	}
}



func main() {

	/*
		These declarations do the same thing:
			1) Declare a function pointer the easy way
			2) Declare it the pedantic way.
			3) Declare it the pedantic way but notice that I don't have to give variable names (but I should, really)
			3) Declare it using a predefined type and slamming in a variable that appears to fit
	*/
	hypot := func(x, y float64) float64 {
		return math.Sqrt(x*x + y*y)
	}
	var hypot2 func(x, y float64) float64 =
		func(x, y float64) float64 {
			return math.Sqrt(x*x + y*y)
		}
	var hypot3 func(float64, float64) float64 =
		func(x, y float64) float64 {
			return math.Sqrt(x*x + y*y)
		}
	var hypot4 TwoFloat = hypot

	fmt.Println("Executing these two variables:")
	fmt.Println(hypot(3, 4))
	fmt.Println(hypot2(32, 224))

	fmt.Println("Now passing them as lambdas:")
	fmt.Println(computeTwo(122, 17, hypot))
	fmt.Println(computeTwo(122, 17, hypot2))
	fmt.Println(computeTwo(122, 17, hypot3))
	fmt.Println(computeTwo(122, 17, hypot4))

	fmt.Println("Now passing a true anonymous function as lambda:")
	fmt.Println(
		computeTwo(
			13, 17.9,
			func(x, y float64) float64 {
				return math.Sqrt(x*x + y*y)
			}))

	fmt.Println("Now using our closure:")
	add:=adder()
	for x:=1; x<=10; x++ {
		println("Closure call", add(x))
	}

	fmt.Println("Another closure that does fibonacci:")
	fib:=fibonacci()
	for x:=1; x<=45; x++ {
		print(" ",fib())
	}
}


