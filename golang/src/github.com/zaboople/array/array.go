package main

import "fmt"

/*
  Also refer to the "slicing" program
*/
func main() {

	fmt.Println("\n")

	//Notice how this prints a bunch of blank spaces for nonexistent members:
	{
		var a = [20]string{"hello", "world"}
		fmt.Println("Note the extra characters: \n", len(a), a, "\n")
	}

	// The first two are arrays because they specify length - including the ... which says
	// figure the length out; the second two are slices because they don't specify length
	{
		var a [2]string = [2]string{"array", "world"}
		var b [3]string = [...]string{"array", "world", "again"}
		var c []string  = b[1:2]
		var d []string  = []string{"slice", "world", "again"}
		fmt.Println(
			"Two arrays and two slices - all look like arrays:\n",
			len(a), a, "\n",
			len(b), b, "\n",
			len(c), c, "\n",
			len(d), d, "\n",
			"\n")
	}

	// Looping thru an array:
	{
		fmt.Println("Using len()")
		p := []int{2, 3, 5, 7, 11, 13}
		for i := 0; i < len(p); i++ {
			fmt.Printf("p[%d] == %d\n", i, p[i])
		}
	}


	// Notice how orig acts as a sort of *pointer, both
	// on = operator and function call:
	{
		fmt.Println("\nPointering:")
		orig := []int{2, 3, 5, 7, 11, 13}
		fmt.Println("orig:", orig)

		copy := orig
		copy[2]=9000
		fmt.Println("copy:", copy)
		fmt.Println("orig:", orig)

		var f=func(x []int) {
			x[2]=1000
		}
		f(orig)
		fmt.Println("copy:", copy)
		fmt.Println("orig:", orig)
	}

	// Notice how slicing an array affects the array, and some weird stuff
	// with this : operator:
	{
		println()
		x := [...]string{"Лайка", "Белка", "Стрелка"}
		// What? Slice the whole thing? Ok
		s := x[:]
		s[1] = "booger"
		fmt.Println(x)
		fmt.Println(s)
	}

}

