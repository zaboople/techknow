package main

import (
	"fmt"; "image/color"; "strconv"
)

func main() {
	fmt.Println(ToString(123123))
	fmt.Println(ToString(12.22))
	fmt.Println(ToString("what i'm a string"))
	fmt.Println(ToString(color.RGBA{12,23,34,45}))
}

func ToString(any interface{}) string {

	//Here we have a weird-ass looking type cast:
	if v, ok := any.(int); ok {
		return "Caught: " + fmt.Sprintf("%d", v)
	}
	
	// Now here's a weird looking switch on a type; this is ONLY
	// allowed in a switch statement.
	switch v := any.(type) {
		case int:
			return strconv.Itoa(v)
		case float64:
			return strconv.FormatFloat(v, 'f', 10, 64)
		case color.RGBA:
			rgba:=any.(color.RGBA) //type casting again
			return fmt.Sprintf("R%d G%d B%d A%d", rgba.R, rgba.G, rgba.B, rgba.A)
		case string:
			return any.(string) //type casting again
	}
	
	return "???"
}

