package main

import (
	"fmt"
	"time"
)

func say(s string, sleep time.Duration) {
	for i := 0; i < 5; i++ {
		time.Sleep(sleep * time.Millisecond)
		fmt.Printf("%s ",s)
	}
}

func main() {
	//The "go" starts an independent thread, but doesn't wait for it.
	//So the program will just exit without it finishing. To resolve
	//this, use channels. Or at least that's one way.
	go say("world", 110) 
	say("hello", 50)
}
