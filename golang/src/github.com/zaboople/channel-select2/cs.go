package main

import "fmt"

func main() {
	test()
}

func populate(output, whenDone chan int, id int) {
	for i:=0; i<100; i++{
		i+=1
		output <- i
	}
	whenDone <- id
}

func test() {
	out1 := make(chan int)
	out2 := make(chan int)
	out3 := make(chan int)
	whenDone  := make(chan int)


	//Be aware: This for loop cannot have conditions. Anything more
	//will lock up cold and die.
	go func() {
		for {
			select {
			
				// These are fairly routine:
				case x := <-out1 :
					fmt.Printf(" 1:%-5d", x)
				case x := <-out2 :
					fmt.Printf(" 2:%-5d", x)					
				// But note! We can ignore the actual value received:
				case <-out3 :
					fmt.Printf(" 3:     ")
					
				// We can also attach a "default:" but it will just be hugely noisy
				// and eat CPU, so forget that:
				//default : fmt.Printf(".")
			}
		}
	}()
	go populate(out1, whenDone, 1)
	go populate(out2, whenDone, 2)
	go populate(out3, whenDone, 3)	

	fmt.Println("\tFIRST",  <-whenDone)
	fmt.Println("\tSECOND", <-whenDone)
	fmt.Println("\tTHIRD",  <-whenDone)
}

