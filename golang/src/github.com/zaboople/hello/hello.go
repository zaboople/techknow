package main

import "github.com/zaboople/hello_utils"

/** 
 * This is going to automatically import functions - shout1(), shout2() - from sister classes in this
 * package, which is kind of weird. If a naming conflict occurs the compiler will bark. It also imports
 * a supposed foreign utility - hello_utils.Twerpy() - in a parallel directory, which is a little
 * bit easier to understand.
 */
func main() {
	shout1()
	shout2()
	hello_utils.Twerpy()
}