package main

/* 
	Also see: http://golang.org/pkg/fmt/ 
*/

import ("fmt")


func main() {
	
	//Int is d: Why not i? Meh
	j:=2405
	fmt.Printf("%d\n", j)
	
	// Float is f. Here you have two choices: 
	// 1) Just do %f and let it work itself out
	// 2) Demand that you get a specific number of decimal places and good luck;
	//    This is the RIGHT way - you almost always want the "-" because without it 
	//    you tend to get unexpected blank spaces at the front:
	k:=1.131445
	fmt.Printf("%-4.10f\n", k)
	
	//Sprintf doesn't print, it just returns the string:
	ll:=fmt.Sprintf("%d %f", j, k)
	fmt.Println(ll)
	
	//Right justified strings:
	fmt.Printf("%10s%10s%10s\n", "hello", "werp", "dang",)
	
	//Left justified strings:
	fmt.Printf("%-10s%-10s%-10s\n", "hello", "werp", "dang",)
}
