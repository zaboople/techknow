package main

import (
	"fmt"; 
)

// So here is our type:
type Planet int

// Now we will define things of that type. "iota" means 0. We could
// repeat the "Planet=iota" definition for each item, but we don't have
// to; it's just assumed.
const (
	jupiter Planet=iota
	saturn
	mars
	earth
	mercury
	venus
	neptune
	uranus
	pluto
)

// Now we'll define a "toString" for Planets.
func (p Planet) String() string {
	switch p {
		case jupiter, neptune, saturn, uranus:
			return "BIG"
		case earth, mars, venus:
			return "MEDIUM"
		case mercury, pluto:
			return "small"
		default:
			return "out of range"
	}
}

// Using our type:
func main() {
	fmt.Println(jupiter)
	fmt.Println(earth)
	fmt.Println(mercury)
	fmt.Println(neptune+11)
}

