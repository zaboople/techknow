package main

import ("fmt")

func main() {test()}

////////////////
// CONSTANTS: //
////////////////

const	(
	/* We will use values 0 thru maxValue for computation: */
	maxValue=1000
	/* This is the length of the chain of processors we need to handle maxValue. This really should be computable: */
	maxProcessors=30
	/* As we pump data to our processors, this is the maximum buffer size: */
	channelCap=1000
)

////////////////
// COMBINERS: //
////////////////

/*
  A general-purpose function type: Take two numbers, mash 'em together, and give a result:
*/
type Combiner func(float64, float64) float64

var	adder Combiner=
	func(x,y float64) float64 {return x+y}
var	subtracter Combiner=
	func(x,y float64) float64 {return x-y}
var	funkotron Combiner=
	func(x,y float64) float64 {
		if (y==0) {
			return x
		} else {
			return 1+(x/(y+.1))
		}
	}

/////////////////
// GOROUTINES: //
/////////////////

/*
	Reads pairs of numbers from input, combines them per the the combiner
	function, and writes to output until input is exhaused. If only one number is received,
	it's written to output without modification. Closes the output channel when 
	complete. 
	This function can be chained with additional instances of itself.
*/
func combine(name int, combiner Combiner, input chan float64, output chan float64) {
	first:=false
	i1:=0.0
	for x:=range input {
		fmt.Printf("%dInput: %f\n", name, x)
		if (!first) {
			first=true
			i1=x
		} else {
			result:=combiner(i1, x)
			fmt.Printf("%dOutput: %f\n", name, result)
			output <- result
			first=false
			i1=0
		}
	}	
	fmt.Printf("%dLast: %f\n", name, i1)
	if (i1 != 0){
		output <- i1
	}
	close(output)
}



/*
	Prints output from a channel and sends a signal to a second channel when done.
	Mean to be chained with Combiners.
*/
func printer(output chan float64, exitSignaler chan float64) {
	for zz:=range output {
		fmt.Printf("Yeah %f\n", zz)
	}
	exitSignaler <- 1
	close(exitSignaler)
}


/////////////
// DRIVER: //
/////////////

func test() {

	// 0. Determine how we we will combine numbers:
	var combiner Combiner=adder

	
	// 1. Assemble our channels and start up a goroutine for each, reading from channel N and writing to N+1:
	var datas []chan float64=make([]chan float64, maxProcessors)
	dataChannelCount:=cap(datas)
	for i:=0; i<dataChannelCount; i++ {
		datas[i]=make(chan float64, channelCap)
		if (i > 0){
			go combine(i, combiner, datas[i-1], datas[i])
		}
	}
	

	// 2. But who reads from the last channel? The printer. This also needs a spare channel to force the main
	//    goroutine to wait:
	var exitSignaler chan float64=make(chan float64)
	go printer(datas[dataChannelCount-1], exitSignaler)
	

	// 3. Pump data into the first channel:
	for i:=0; i< maxValue; i++ {
		if (i % (maxValue / 10) ==0) {
			fmt.Println("Pumping:", i)
		}
		datas[0] <- float64(i)
	}
	close(datas[0])
	fmt.Println("Initial pumping operation complete")
	

	// 4. Now wait for the signal, and also compute the result the "easy" way for verification:
	for x:=range exitSignaler {
		fmt.Println("Exit signal received:", x)
	}
	fmt.Printf("Simple %f\n", simpleCombiner(maxValue, combiner))
}

///////////////////
// VERIFICATION: //
///////////////////

/*
  This is just a test function to verify the others do what's expected:
*/
func simpleCombiner(max float64, combiner Combiner) (x float64) {
	var i float64
	for i=0; i<max; i++ {
		x=combiner(x,i)
	}
	return
}
