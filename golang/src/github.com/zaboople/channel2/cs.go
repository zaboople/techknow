package main

import ("fmt";"math")

func main() {
	test()
}

type Status struct {
	name int
	count float64
}
func populate(whenDone chan int, output chan Status, id int) {
	status:=Status{id,0}
	for i:=0; i<100; i++{
		status.count=math.Sqrt(float64(i))
		output <- status
	}
	//Don't forget to close the channel, man! If you don't then the
	//printer() function will never exit its for-loop.
	close(output)
	whenDone <- id
}
func printer(whenDone chan int, output chan Status, id int) {
	for zz:=range output {
		fmt.Printf("%d %f\n", zz.name, zz.count)
	}
	whenDone <- id
}

func test() {
	runcount:=50
	var outs  []chan Status =make([]chan Status, runcount)
	for i:=0; i< runcount; i++ {
		//The buffer size tends to determine the output order.
		//Basically populate() is throttled by printer().
		outs[i] =make(chan Status, 1)
	}
	var dones chan int=make(chan int, runcount*2)
	for i:=0; i< runcount; i++ {
		go printer(dones, outs[i], i+runcount)
	}
	for i:=0; i< runcount; i++ {
		go populate(dones, outs[i], i)	
	}
	for i:=0; i< runcount*2; i++ {
		zzz:=<-dones
		fmt.Printf("DONE %d ", zzz+1)
	}
}

