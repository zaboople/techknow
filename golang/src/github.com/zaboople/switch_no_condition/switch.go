package main

import (
	"fmt"
)

/** 
 * Note: The name of this file is irrelevant - it's the package that determines the name of the 
 * program.
 * 
 * This is a switch that has no condition, which is allowed, and cuts back
 * on if/else/else/if/else/if business
 */
func main() {
	i:=3
	j:=4
	k:=5
	switch {
	case i<10:
		switch {
		case j<10:
			fmt.Println("Whatta")
			fmt.Println("Butta")
		case k<10:
			fmt.Println("Not gonna")
		}
	case j<10:
		fmt.Println("Good afternoon.")
	case k<10:
		fmt.Println("Good what?")
	default:
		fmt.Println("Good evening.")
	}
}
