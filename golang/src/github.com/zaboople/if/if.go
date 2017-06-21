package main

import "fmt"

func main() {

	// This is legal, but x is restricted to the if statement and cannot be
	// addressed outside of it:
	if x:=0; x < 0 {
		fmt.Println("< 0: ", x)
	} else {
		fmt.Println("not < 0: ", x)
	}

	// Cute/tricky variation that looks like the previous but notice how
	// we quietly bypassed our restriction:
	x:=-1; if x < 0 {
		fmt.Println("< 0: ", x)
	}
	fmt.Println("x is: ", x)
}
