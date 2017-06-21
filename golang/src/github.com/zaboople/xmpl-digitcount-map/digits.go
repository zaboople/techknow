package main
/* 
	This reads in a file of digits, counts how many of each digit it finds, and prints the results.
*/
import (
	"fmt"
	"os"
	"strings"
	//"strconv"
)



/* Counters: */
type Counter interface {
	next(nextchar string, mappy map[string]int)
}
type OneCharCounter struct {}
func (occ *OneCharCounter) next(nextchar string, mappy map[string]int) {
	mappy[nextchar]=mappy[nextchar]+1 
}

type MultiCharCounter struct {
	max int
	space string
}
func (occ *MultiCharCounter) next(nextchar string, mappy map[string]int) {
	size:=len(occ.space)
	if (size<occ.max){
		occ.space+=nextchar
	} else {
		occ.space=occ.space[1:size]+nextchar
	}
	if (size>occ.max-2) {
		mappy[occ.space]=mappy[occ.space]+1 
	}
}


/* Our overall accumulator */
type Accumulator struct {
	input chan string
	output chan int
	mappers []Counter
	counts  []map[string]int
}
func makeCounter(ms ...Counter) Accumulator {
	counts:=make([]map[string]int, len(ms))
	for i,_ := range(counts){
		temp:=make(map[string]int)
		counts[i]=temp
	}
	return Accumulator{
		make(chan string), make(chan int), ms, counts,
	}
}
func (c *Accumulator) accumulate() {
	for str:= range c.input {
		for i, m := range c.mappers {
			var crud map[string]int=c.counts[i]
			m.next(str, crud)
		}
	}
	c.output <- 1
}

func processBuffer(buff string, cc *Accumulator) {
	var reader *strings.Reader = strings.NewReader(buff)
	ssize:=reader.Len()
	for i:=0; i<ssize; i++ {
		ch, _, _:=reader.ReadRune()
		cc.input <- string(ch)
	}
}

func main() {
	fmt.Println("Initializing...")
	
	//1. Create the counters:
	cc:=makeCounter(&OneCharCounter{}, &MultiCharCounter{3, ""})
	go cc.accumulate()

	//2. Get our buffering ready: 
	var buff []byte=make([]byte, 1024)
	var stdin *os.File=os.Stdin

	//3. Read from stdin, feeding numbers into their channels:
	for size,error:=stdin.Read(buff); 
			error==nil; 
			size,error =stdin.Read(buff){
		processBuffer(string(buff[:size]), &cc)
	}
	
	//4. Close all the input channels so the counters know to stop;
	//	 Wait on the counters to signal completion;
	//	 Print the results.
	close(cc.input)
	<-cc.output
	for index,_:=range cc.counts {
		fmt.Printf("\n%d\n", index)
		for key,val:=range (cc.counts[index]){
			fmt.Println(key, val)
		}
	}

}

