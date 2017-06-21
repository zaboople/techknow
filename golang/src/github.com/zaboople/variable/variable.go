package main

import (
	"fmt"
)


func main() {
	
	//Type inference:
	a:=1
	
	//This does the same thing, but it takes more keyboarding and gets us what? Nothing:
	var b=-2
	
	//So I want a type declaration. Well that only works with var:
	var c int32=-32
	
	//We don't have to initialize it. We can assign a value later but why bother, it's 0:
	var d int
	
	//This number is so large, the compiler will complain about overflow errors for this value unless 
	//you declare it like this. No, := will not work here.
	var e int64=12898798798 
	
	//Well now we can use := because the compiler infers the type. Meh.
	f:=e+1
	
	//Assigning multiple things at once
	j,k,jj,kk:=1,2,3,4

	//RE-assigning multiple things at once, but := shouldn't work here, because it's for INITIAL
	//assignment, right? No, it's okay if just ONE of them is new; here, it's "ll":
	j,k,jj,ll:=1,2,3,4

	//Assigning multiple things with type declared only works if they're all the same type:
	var g,h int=1,3
	
	//Assigning multiple things at once with mixed types works if you don't declare the type though:
	m, n := 1, "hi"
	var mm, nn = 1, "hello"
	
	//Hey just use const if you don't need to change it. It's just like var.
	const o int=1
	const oo="blah"
	
	//We'll get compiler errors if we don't do anything with these:
	printAll(a, b, c, d, e, f, g, h, j, k, jj, kk, ll, m, n, mm, nn, o, oo)
}

func printAll(vals ...interface{}) {
	for i:=0; i<len(vals); i++ {
		fmt.Println(vals[i])
	}
}