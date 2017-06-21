/* 
  Generates an image. If you run this from a shell script be careful because
  it prints to stdout.
*/
package main

import (
	//"bytes"
	//"encoding/base64"
	//"fmt"
	"image"
	"image/png"
	//"io"
	"os"
)

func Show(f func(int, int) [][]uint8) {
	const (
		dx = 256
		dy = 256
	)
	m := image.NewNRGBA(image.Rect(0, 0, dx, dy))
	data := f(dx, dy)
	for y := 0; y < dy; y++ {
		for x := 0; x < dx; x++ {
			v := data[y][x]
			i := y*m.Stride + x*4
			m.Pix[i]   = v+10
			m.Pix[i+1] = v+30
			m.Pix[i+2] = v+50
			m.Pix[i+3] = 255
		}
	}
	png.Encode(os.Stdout, m)
}

func generator(dx, dy int) [][]uint8 {
	ddy := make([][]uint8, dy)
	y:=0
	for y<len(ddy){
		ddx := make([]uint8, dx)
		x:=0
		for x<len(ddx)	{
			ddx[x]=uint8(x*x*y*y)
			x+=1
		}
		ddy[y]=ddx
		y+=1
	}	
	return ddy
}


func main() {
	Show(generator)
}


