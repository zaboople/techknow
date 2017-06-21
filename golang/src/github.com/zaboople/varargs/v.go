package main

import (
	"fmt"; 
)

func main() {
	showAny(1, 2, 3, "hello", "world")
	showInt(5, 4, 6)
}


/*
 This takes arguments of any type. 
*/
func showAny(xx ...interface{}){
	println("\nInterface type:")
	s:=len(xx)
	for i:=0; i<s; i++ {
		fmt.Println(xx[i])
	}
}

/* 
  This only takes ints
*/
func showInt(xx ...int){
	println("\nint type:")
	s:=len(xx)
	for i:=0; i<s; i++ {
		fmt.Println(xx[i])
	}
}