package main
/* 
	This reads in a file of digits, counts how many of each digit it finds, and prints the results.
*/
import (
	"fmt"
	"os"
	"strings"
	"strconv"
)

type CounterChan struct {
	count int
	input chan int
	output chan int
}
func (c *CounterChan) accumulate() {
	for range c.input {
		c.count++
	}
	c.output <- c.count
}

func main() {
	fmt.Println("Initializing...")
	
	//1. Create the counters:
	var counters []*CounterChan=make([]*CounterChan, 10)
	var output chan int=make(chan int)
	for i, _ := range counters {	
		cc:=CounterChan{
			0, make(chan int), output,
		}
		go cc.accumulate()
		counters[i]=&cc
	}

	//2. Get our buffering ready: 
	var buff []byte=make([]byte, 1024)
	var stdin *os.File=os.Stdin

	//3. Read from stdin, feeding numbers into their channels:
	for size,error:=stdin.Read(buff); 
			error==nil; 
			size,error =stdin.Read(buff){
		var reader *strings.Reader = strings.NewReader(string(buff[:size]))
		ssize:=reader.Len()
		for i:=0; i<ssize; i++ {
			ch, _, _:=reader.ReadRune()
			index, err:=strconv.ParseInt(string(ch), 10, 64)
			if (err == nil) {
				counters[index].input <- 1
			}
		}
	}

	//4. Close all the input channels so the counters know to stop;
	//   Wait on the counters to signal completion;
	//   Print the results.
	for i, _ := range counters {
		close(counters[i].input)
		<-output
	}
	for i, c := range counters {
		println(i, c.count)
	}

}

