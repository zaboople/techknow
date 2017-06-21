package main

import "fmt"

func main() {
	{
		//This is the original:
		var orig Vertex= Vertex{1, 2}
		
		//This is a pointer:
		var copy1 *Vertex = &orig
		copy1.X = 10
		
		//This is just getting a value:
		var copy2 Vertex=*copy1
		copy2.X=100
		copy2.Y=200
		
		//So now we see that copy1 matches original, but copy2 does not:
		fns("Orig", orig)
		fns("Copy1", copy1)
		fns("Copy2", copy2)
		
		//Pass a pointer to a function that will change it:
		//Note that the pointer is required here because the function
		//demands one. For plain functions, go will not coerce types
		//from Vertex to *Vertex and vice versa - but wait until yous see methods...
		incrX(&orig)
		fns("Orig again", orig)
	
		//Here we see that copy3 will get true on == because it does compare by value:
		var copy3=copy2
		fns("Copy3", copy3)
		fns("Copy2==Copy3", copy2==copy3)
	
		//And correspondingly, when we change copy3 the == fails:
		copy3.X=300
		fns("Copy2", copy2)
		fns("Copy3", copy3)
		fns("Copy2==Copy3", copy2==copy3)
	}
	{
		fmt.Println("\nNow we'll modify an int pointer, incrementing it:")
		var i=0;
		incr(&i); fns("i", i)
		incr(&i); fns("i", i)
		incr(&i); fns("i", i)
	
		//Now we'll copy an int pointer to another
		//First off, we'll actually get go to print a pointer;
		//this is actually interesting because you'll see that
		//the pointers are 8 bits apart:
		fns("i", incrFrom(&i))
		fns("i", incrFrom(&i))
		fns("i", incrFrom(&i))
		//Now let's be sensible and print what it points to:
		fns("i", *incrFrom(&i))
		fns("i", *incrFrom(&i))
		fns("i", *incrFrom(&i))
		//Now let's go nuts again:
		fns("i", *incrFrom(incrFrom(incrFrom(&i))))
	}
	
	{
		//The new() function allocates a pointer to our object, with all values defaulted.
		//But we can use other means to do the exact same thing:
		v1:=new(Vertex)
		v2:=&Vertex{}	
		fmt.Println("\nTwo ways to do new:", v1, v2, "Pointer compare:", v1==v2, "Value compare:", *v1==*v2)

		//Note how an uninitialized struct is not the same as uninitialized pointer:
		var v3 *Vertex;
		fns("Using uninitialized pointer", v3)
		var v4 Vertex;
		fns("Using uninitialized struct", v4)
	}
	
	{
		// Note here that (*q).X requires parentheses because *q.X says, "Give me 
		// the value that q.X points to," and X is not a pointer:
		v := Vertex{22,11}
		q := &v
		fmt.Println("\nWatch pointer coercion magic:", v.X, q.X, (*q).X)
	}

}

// LITTLE INCREMENTING FUNCTIONS: //

func incrX(v *Vertex) { v.X+=1 }
func getXPlus1(v Vertex) int { return v.X+1 }
func incr(i *int)		 { *i+=1	}
func incrFrom(i *int) (newptr *int){
	var ii=*i+1
	return &ii
}

// AWESOME PRINTING: //

/** 
 * A good example of how interface{} is analogous to java's Object. 
 * - When printing an struct, "%v" is the designated format, with "%+v" 
 *	 including the struct value names. 
 * - The "%v" syntax also handles pointers to structs pretty well, 
 *	 but for int pointers it prints the address instead of the value (derp!). 
 * - We're using a "range" here to loop through an array. Google is cute about
 *	 this, allowing us to obtain both an index & value simultaneously instead
 *	 of forcing us to choose between two different kinds of loops. If we only
 *	 want one or the other we could replace them with "_".
 */
func fns(name string, values ...interface{}) {
	fmt.Printf("%-14s", name+":")
	for index, value := range values {
		if (index>0) { fmt.Print(", ") }
		fmt.Printf("%+v", value)
	}
	fmt.Println()
}
