package main

import "fmt"

func main() {
	example1()
	example2()
	example3()
	example4()
}

func example1() {
	fmt.Println("\nExample 1: Cutting up an array")

	p := []int{2, 3, 5, 7, 11, 13}
	printSlice("p", p)

	// Obtains first index thru second -1:
	printSlice("p 1:4", p[1:4])

	// Obtains nothing. Note that for p[x:y], however x MUST be <= y. Otherwise
	// you get a runtime error or panic.
	printSlice("p 0:0", p[0:0])

	// Missing low index implies 0, or in other words, for :N, you get the first N elements:
	printSlice("p :3", p[:3])

	// Missing high index implies len(s):
	printSlice("p 4:", p[4:])
}



/** Interesting because nil is not so much null. */
func example2() {
	fmt.Println("\nExample 2: Nil Nil");
	var z []int
	fmt.Println("Unitialized array:", z, "len:", len(z), "cap:", cap(z))
	if z == nil {
		fmt.Println("Yes it's nil")
	}

	println("Add elements to our uninitialized array:")
	a:=append(z, 12)
	printSlice("One element", a)
	b:=append(a, 23)
	printSlice("Two element", b)
	printSlice("Original is still", z)
}


func example3(){

	//Making a empty array with length & capacity 5, just to make
	//the point that capacity is automatically the same as length
	//if you don't say otherwise:

	fmt.Println("\nExample 3, blah")
	a := make([]string, 5)
	printSlice("whatever", a)

}

func example4(){
	fmt.Println("\nExample 4, Slicing overlap")

	//This array has capacity 5, but 0 length, so
	//you cannot use any valid index with it, which is real strange:
	var orig []string = make([]string, 0, 5)
	printSlice("orig", orig)

	//Slice the first two elements off to get a suddenly useable array:
	copy1 := orig[:2]
	copy1[0]="zero"
	copy1[1]="one"
	printSlice("copy1 0-1", copy1)

	//We are now going to slice slices and so forth, and you'll notice
	//that since everything is a slice of the same original array, they
	//all bleed into each other's data:

	//1. Now even though copy1 is a "slice" of the first two elements of orig's capacity,
	//   it *retains* all 5 of orig's capacity, because we copied from the beginning:
	fmt.Println()
	copy2 := copy1[:5]
	copy2[1]="e"
	copy2[2]="r"
	copy2[3]="o"
	copy2[4]="."
	printSlice("copy2 0-4", copy2)
	printSlice("copy1 0-1", copy1)

	//2. Now copy3 only has a capacity of 3, because it starts slicing at index 2. Also,
	//   even though it is a completely separate slice, it gets bits of the other two copies
	//   mixed into it:
	fmt.Println()
	copy3 := orig[2:5]
	copy3[1]="b"
	printSlice("copy3 2-4", copy3)
	printSlice("copy2 0-4", copy2)
	printSlice("copy1 0-1", copy1)

	//Notice how our original slice still has... NOTHING:
	printSlice("orig", orig)
}

// Concatenating arrays is easy, you just have to know to
// force varargs using "..."
func example5()	{
	a := []string{"hello", "world"}
	b := []string{"goodbyte", "now"}
	c := append(a, b...)
	fmt.Println(c)
}

////////////////
// UTILITIES: //
////////////////

func printSlice(s string, x interface{}){
	fmt.Printf("%-10s %v\n", s, x)
}


/**
* Just in case you thought go could do like java and trade arrays for varargs, no.
* If you pass an array as the second argument, it is treated as a 1-element array
* containing a 1-element array.
*/
func printSlice3(s string, x ...interface{}) {
	fmt.Printf("%-10s len=%d cap=%d %v\n",
		s, len(x), cap(x), x)
}