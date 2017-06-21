package main
import (
	"flag"
	"fmt"
	"math"
	"math/big"
	"strconv"
	"time"
)

// Runs the main program
func main() {
	flag.Parse()
	if (flag.NArg()==0) {
		fmt.Println("Need a number")
	} else if iterations,err:=strconv.Atoi(flag.Arg(0)); err==nil {
		monitorCalc(iterations, "ASY", calcAsync);
		//monitorCalc(iterations, "STR", calcStraight);
	} else {
		fmt.Println("Invalid: "+flag.Arg(0))
	}
}

// This is used as a wrapper for whichever version of our calculation we
// choose to run. It times the calculation and also checks it against a
// a 10,000-digit PI we get from check.go.
func monitorCalc(
		iterations int,
		prefix string,
		calculator func(out chan string, max int, format int),
	){
	begin:=time.Now()
	results:=make(chan string, 10)
	go calculator(results, iterations, iterations)
	count:=0;

	for result:=range results {
		count++
		result :=result[0:iterations]
		tomatch:=digits10k[0:int(math.Min(float64(iterations),10000))]
		match:=tomatch==result
		fmt.Println(result)
		if (!match) {
			fmt.Println(tomatch)
		}
		fmt.Println("Matched", match)
	}
	fmt.Println(prefix, " TIME: ", time.Now().Sub(begin))
}


// This is the fastest version. It offloads most of the work
// to equation_iterator.go & add_async.go.
func calcAsync(output chan string, count int, format int) {
	equationOutput:=makeChanBig(64)
	piOutput:=makeChanBig(1)

	// Start up the accumulator. Internally it will start goroutines
	// and return quickly after that.
	addAsync(piOutput, equationOutput, 4, count)

	// Start running the equation and feeding the accumulator:
	go func() {
		state :=NewEquationIterator()
		for i:=0; i<count; i++ {
			x := state.next()
			equationOutput <- x
		}
		close(equationOutput)
		fmt.Println("Equations complete")
	}()

	// Get our one result and bail:
	output <- (<- piOutput).FloatString(format)
	close(output)
}

// This uses EquationIterator like calcAsync, but does the addition as
// just an iterative calculation. This is slow not just because it lacks
// concurrency, but because it is adding the values consecutively instead
// of dividing them up into "layers".
func calcSynchronous(output chan string, count int, format int) {
	state :=NewEquationIterator()
	number:=new(big.Rat)
	for i:=0; i<count; i++ {
		number.Add(number, state.next())
	}
	output <- number.FloatString(format)
	close(output)
}
