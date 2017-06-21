package main

import (
	"fmt"
	"os"
)


func main() {
	var buff []byte=make([]byte, 1024)
	var stdin *os.File=os.Stdin
	fmt.Println("hello")
	for true {
		size,error:=stdin.Read(buff)
		if error!=nil {
			fmt.Println("Failed")
		} else{
			os.Stdout.Write(buff[:size])//Check it, slicing
			fmt.Println(size)
		}
		if size!=cap(buff) {
			break
		}
	}

}

