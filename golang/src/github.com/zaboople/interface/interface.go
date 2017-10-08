package main

/*
	This illustrates, among other things, go's quirky behavior regarding pointers and not-pointers.
	For regular functions, go will NOT automatically coerce types from *XX to XX and vice versa; but
	for methods, go says, hm, yeah, ok I know what you want. But just wait for interfaces...
*/

import (
	"fmt"; "math"
)

/*
	METHODS ON A STRUCT.
*/
type Vertex struct {
	X, Y float64
}
func (v *Vertex) ToOrigin() float64 {
	return math.Sqrt(v.X*v.X + v.Y*v.Y)
}
func (v Vertex) SetXWrong(x float64) Vertex {
	//This is essentially doing immutability. The original value
	//is unchanged because we received A COPY:
	v.X=x
	return v
}
func (v *Vertex) SetXRight(x float64) {
	//This uses a pointer, so we're mutable. Note however that it does
	//work with non-pointer types, as it gets "coerced". Just be warned
	//that such coercion tends to fail with interfaces.
	v.X=x
}
func (v *Vertex) Add(v2 *Vertex) {
	v.X+=v2.X
	v.Y+=v2.Y
}


/*
	METHODS ON A TYPE.
*/
type MyFloat float64
func (f MyFloat) Abs() MyFloat {
	if f < 0 {
		return -f
	}
	return f
}


func main() {

	// 1. STRUCT METHODS AND POINTERS:
	{
		fmt.Println("\nCalling methods on a pointer or not-a-pointer:")
		v := &Vertex{3, 4}
		fmt.Println("Pointer:", v, v.ToOrigin())
		z := Vertex{4, 5}
		fmt.Println("Not Pointer:", z, z.ToOrigin())
		//Notice how we MUST pass a pointer, because the coercion only happens
		//with the the method target:
		v.Add(&z)
		fmt.Println("Added:", v)
	}

	// 2. TYPE METHODS AND POINTERS:
	{
		fmt.Println("\nMethod on a type:")
		var mf MyFloat=-78
		fmt.Println("Value", mf, "Abs", mf.Abs())

		// And throw in pointers. Notice how the pointer can only
		// be assigned a value by using *, and the value can only be
		// printed using *, but the Abs() method call implicitly dereferences
		// because blurgh
		fmt.Println("\nPointers to type:")
		mf2:=&mf
		*mf2=-1214
		fmt.Println("Value", mf,   "Abs", mf.Abs())
		fmt.Println("Value", *mf2, "Abs", mf2.Abs())
		var mf3 *MyFloat=mf2
		*mf3=-3
		fmt.Println("Pointer", mf2, mf3, &mf)
		fmt.Println("Value", *mf2, *mf3, mf)
	}

	// 3. STRUCT POINTER/NOT-POINTER AND EFFECT ON STATE:
	{
		fmt.Println("\nCalling a stateful method via pointer and not-pointer:");
		vv:=Vertex{1,2}
		fmt.Println("Start with:", vv)
		fmt.Println("SetXWrong changes temporary:", vv.SetXWrong(899))
		fmt.Println("See we're still the same:", vv)
		vv.SetXRight(899)
		fmt.Println("Now we've changed permanently:", vv)

		fmt.Println("\nJust verifying that not-pointer methods behave the same when passed a pointer:")
		v2:=&Vertex{1,1}
		v3:=v2.SetXWrong(2)
		fmt.Println("Copying the pointer?", v2, v3)
	}

}

