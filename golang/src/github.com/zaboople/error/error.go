package main

/*
	The error interface requires an Error() function. We're going to use it with both non-pointers and pointers:
*/

import (
	"fmt"; "time"
)

//Pointer:
type MyError1 struct {When time.Time; What string; instance int}
func (e *MyError1) Error() string {
	e.instance++
	return fmt.Sprintf("MyError1 at %v, %7s %d", e.When, e.What, e.instance)
}

//No-pointer:
type MyError2 struct {When time.Time; What string; instance int}
func (e MyError2) Error() string {
	e.instance++
	return fmt.Sprintf("MyError2 at %v, %7s %d", e.When, e.What, e.instance)
}

/*
 Driver:
*/
func main() {
	if err := goFail(); err != nil {
		//These all print different because Error() is defined pointer-wise:
		fmt.Println(err)
		fmt.Println(err)
		fmt.Println(err)
	}
	if err := goFail2(); err != nil {
		//These all print the same because Error() is not-pointer
		fmt.Println(err)
		fmt.Println(err)
		fmt.Println(err)
	}
	if err := goFail3(); err != nil {
		//These also all print the same, even though goFail3 attempts to return a pointer
		fmt.Println(err)
		fmt.Println(err)
		fmt.Println(err)
	}
}
func goFail() error {
	return &MyError1{time.Now(), "goFail", 0}
}
func goFail2() error {
	return MyError2{time.Now(), "goFail2", 0,} //LOOK EXTRA COMMA NO REASON HA HA HA DERP
}
func goFail3() error {
	return &MyError2{time.Now(), "goFail3", 0}
}