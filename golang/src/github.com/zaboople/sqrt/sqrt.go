package main

import (
	"fmt"; "log"; "math"; "os"; "strconv";
)

func main() {

	// Note that log.Fatal has the consequence of exiting the program:
	input, err := strconv.ParseFloat(os.Args[1], 64)
	if err != nil {
		log.Fatal(err)
	}
	
	// Now compute output:
	var root = Sqrt(math.Abs(input))
	printFloat("Input", input)
	printFloat("Root", root)
	printFloat("Squared", root*root)
}
func printFloat(name string, value float64) {
	fmt.Printf("%12s: %20.32f\n", name, value)
}

func Sqrt(src float64) float64 {
	return doSqrt(src, src/2)
}

func doSqrt(src float64, est float64) float64 {
	var next = est - ((est*est)-src)/(2*est)
	if math.Abs(next-est) < 0.0000000000000000001 {
		return next
	} else {
		return doSqrt(src, next)
	}
}
