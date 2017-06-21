package main

import "fmt"


func main() {
	fibDriver()
}

func fibDriver() {
	receiver := make(chan int)
	sender   := make(chan int)
	
	go func() {
		for i := 0; i < 30; i++ {
		
			//One way to do it:
			x:= <- receiver
			fmt.Println(x)
			
			//A shortcut to do the same:
			fmt.Println(<- receiver)
		}
		sender <- 0     //Signal quit
		sender <- 11112 //Provide a value to go with it
	}()
	
	fibonacci(receiver, sender)
}
func fibonacci(output, input chan int) {
	z:=0
	x, y := 0, 1
	for {
		select {

			// OUTPUT: Only triggered if we're allowed to send output, but if it triggers, 
			// the output is already sent. What we execute is just follow-through:
			case output <- x:
				x, y = y, x+y

			// INPUT: We're ignoring the first value received; we could do
			//      case xxx <- input:
			// but hey we don't want to.
			case <-input:
				fmt.Println("Quitting on: ", <-input)
				return
				
			// This will go absolutely bonkers because most of the time we have neither
			// input nor output:
			default: 
				z++
				if (z % 1000 == 0) {
					fmt.Print(".")
				}
		}
	}
}
