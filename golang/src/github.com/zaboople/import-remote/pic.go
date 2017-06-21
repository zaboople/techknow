/** 
	This works great on the go playground, and generates cool pictures, but pic2 is better.
	This just shows how we can automatically download libraries we want to use. If they exist.
*/
package main

import "code.google.com/p/go-tour/pic"

func main() {
	pic.Show(Pic)
}


func Pic(dx, dy int) [][]uint8 {
	println(dx, dy)
	ddy := make([][]uint8, dy)
	y:=0
	for y<len(ddy){
		ddx := make([]uint8, dx)
		x:=0
		for x<len(ddx)	{
			ddx[x]=uint8((x-79931)*x/(y+100))
			x+=1
		}
		ddy[y]=ddx
		y+=1
	}	
	return ddy
}

