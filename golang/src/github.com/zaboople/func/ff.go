package main

/* 
	Also refer to the lambda directory and the varargs directory. And maybe others.
*/
import ("fmt")



/* 
	Basic function that returns a value:
*/
func doobie1(x int, y int) int {
	return x+y
}

/* 
	A shortcut to previous:
*/
func doobie2(x, y int) int {
	return x+y
}

/* 
	Hey we can return multiple values, like, uh, uh a tuple! Yeah:
*/
func doobie3(x, y int) (int, int) {
	return x+y, x*y
}

/* 
	Hey we can name our return values. Then we don't have to say anything but "return",
	although we could say "return blah,blah" if we wanted:
*/
func doobie4(x, y int) (a int, b int) {
	a=x+y
	b=x*y
	return
}

func main() {
	fmt.Println(doobie1(1, 2))
	fmt.Println(doobie2(1, 2))
	fmt.Println(doobie3(1, 2))
	fmt.Println(doobie4(1, 2))
}


