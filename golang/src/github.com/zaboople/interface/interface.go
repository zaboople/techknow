package main

import (
	"fmt"; "math"
)

// This is our interface:
type FlatFloat interface {Flat() float64}

// This implements our interface thanks to "structural typing" which is a lot like duck typing:
type MyFloat float64
func (f MyFloat) Flat() float64 {
	if f < 0 {return float64(-f)}
	return float64(f)
}

// This also implements FlatFloat, but we've added "Stringer", via String(). 
// Note that for Stringer, we didn't use a pointer, because with a pointer, it only works for 
// pointer types; without, it works for either. And since you can't have two functions named String(),
// we went with the one that works in all cases:
type Vertex struct {X, Y float64}
func (v *Vertex) Flat() float64  {return math.Sqrt(v.X*v.X + v.Y*v.Y)}
func (v *Vertex) String() string {return fmt.Sprintf("<Vertex X=%f Y=%f>", v.X, v.Y)}

func main() {
	var a FlatFloat

	f := MyFloat(-math.Sqrt2)
	var g MyFloat=-math.Sqrt2
	a = f  
	fmt.Println(a, f, g)
	
	
	// Plain a=v will fail because Flat is defined on a pointer. 
	// See with interfaces, the coercion back and forth between pointer & not-pointer
	// just doesn't happen.
	v := Vertex{3, 4}
	a = &v 
	fmt.Println(v.Flat(), a.Flat())
	
	//And now back to our Stringer interface. Notice how it only works with a, not v, because
	//we defined String() on a pointer. No casting happens.
	fmt.Println(v, a)
}

