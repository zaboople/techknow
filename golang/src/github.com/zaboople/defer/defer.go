package main

// Defer is essentially a try-finally. It allows you to provide a guarantee of execution
// no matter what happens, assuming a panic didn't occur before you provided the defer.

import (
	"fmt"
)
func main() {
	testDefer()
	testPanic()
}

///////////////////////////////////////////
// 1. Just tests deferring and unwinding //
// the stack: there are no panics here.  //
///////////////////////////////////////////

func testDefer() {
	fmt.Println("\nTESTING DEFER: ")
	defer fmt.Println("testDefer")
	def1()
	def2()
	def3()
	def4()
}
func def1() {
	defer fmt.Println("def1")
	def2()
}
func def2() {
	defer fmt.Println("def2")
	def3()
}
func def3() {
	defer fmt.Println("def3")
	def4()
}
func def4() {
	defer fmt.Println("\ndef4")
}

////////////////////////////////
// 2. Defer, recover & panic: //
////////////////////////////////

func testPanic() {
	fmt.Println("\nTESTING PANIC")
	incontrol()
	fmt.Println("Returned normally from incontrol.")
}
func incontrol() {
	//Defer requires a function, so here's a lambda:
	defer func() {
		if r := recover(); r != nil {
			fmt.Println("Recovered in incontrol", r)
		}
	}()
	fmt.Println("incontrol() calling outofcontrol().")
	outofcontrol(0)
	fmt.Println("Returned normally from outofcontrol().")
}
func outofcontrol(i int) {
	defer fmt.Println("Defer in outofcontrol", i)
	if i > 3 {
		fmt.Println("Panicking on:", i)
		panic(fmt.Sprintf("%v", i))
	}
	fmt.Println("Recursing in outofcontrol", i)
	outofcontrol(i + 1)
}
