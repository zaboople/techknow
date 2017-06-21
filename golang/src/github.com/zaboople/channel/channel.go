package main

/**
	A channel can be written to by goroutines, and then other logic
	can "synchronize" on that channel and wait for them to finish.
	
	Note that channels are created with make(), for whatever reason. 
	Also, we don't have to use pointers to channels, as they're a primitive
	type (we can, but hey).  

	Note how we're using sleeps to force one thread to take longer
	than the other; the order of results read from the channel is based
	on who writes first. And if we don't wait for someone to return, then 
	too bad, whatever they did is killed. 	
*/

import ("fmt"; "time")

func main() {
	array := []int{7, 2, 8, -9, 4, 0, 3}
	halfway:=len(array)/2
	
	//Make a channel and let stuff write to it. Note that we can add
	//a buffer amount to the make() call, we just don't need one.
	ch := make(chan int)
	go sum(ch, array[halfway:], 1000)
	go sum(ch, array[:halfway], 500)

	//Nothing happens because it writes last, we only read twice, whatever, it dies:
	go sum(ch, array, 11500)

	x, y := <-ch, <-ch
	fmt.Println("Result", x, "+", y, "=", x+y)
}
func sum(ch chan int, array []int, sleep time.Duration) {
	fmt.Println("Summing ", array)
	time.Sleep(sleep * time.Millisecond)
	sum := 0
	for _, v := range array {
		sum += v
	}
	ch <- sum 
	fmt.Println("Done with ", array)
}


