package main
/*
  This is interesting, because making arrays and our sleep time behave kind of funny in unison.
	You may find more .'s or less based on how you tweak either.
*/

import ("fmt"; "crypto/rand"; "time")

func main() {test()}


func doThings(throttle chan int) {
	//Notice how we can ignore the value a channel returns by just saying "<-throttle":
	randoms := make([]byte, 100000)
	x:=0
	for {		
		x++
		if len(throttle)>0 {
			fmt.Println("\n<--- Throttle down!", x)
			<-throttle
			<-throttle
			fmt.Println("\n<--- Throttle up!", x)
		} else if (x % 255 ==0) {
			fmt.Printf(".")
		}
		if _, err:=rand.Read(randoms); err!=nil {
			fmt.Println("failed!", randoms[0]);
		}	
	}
}

func test(){
	// The situation can get out of control quickly if we don't give space to the channel.
	repeat:=10
	var throttle chan int=make(chan int, 21)	
	throttle<-1
	go doThings(throttle)
	
	fmt.Println("Hello, getting started....\n")
	for stopper:=0; stopper<repeat; stopper++{	

		fmt.Println("\n---> Bring throttle up!", stopper, repeat-1)
		throttle <- 1 //start
		//time.Sleep(1 * time.Millisecond)

		fmt.Println("\n\n---> Bring throttle down!", stopper, repeat-1)
		throttle <- 1 //stop
		time.Sleep(2 * time.Millisecond)
		
	}
}