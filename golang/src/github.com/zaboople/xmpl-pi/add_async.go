package main
import (
	"fmt"
	"math"
	"math/big"
)

/*
	Welcome to the really hard part: Adding gigantic fractions together, because that's how
	Go expresses very large floating point (but rational) numbers, as fractions. By gigantic,
	we mean very large integers on top and bottom, as in:

	    somethinggazillion/anothergazillion + moregazillion/whatevergazillion

	- except the actual numerator & denominator values would wrap your screen many times each.
	These buggers eat CPU like there's no tomorrow, and we let the Go libraries just do their
	best with them.
*/

func addAsync(
		finalOutput chan *big.Rat,
		equationOutput chan *big.Rat,
		farmSize int,
		equationCount int) {
	fmt.Println("Adder farm size", farmSize)
	adderInput:= make(chan *Pair2, 1+(farmSize*2))
	backPressure:=make(chan bool)
	// Pairs up values from the equation output and feeds them to the adder input:
	go pairerEquations(equationOutput, adderInput, farmSize+1, backPressure)
	// Pairs up values from the adder output and feeds them _back_ to the adder input as well:
	go pairer2(finalOutput, adderInput, backPressure, farmSize, equationCount)
}

//////////////////////////
// EFFECTIVELY PRIVATE: //
//////////////////////////

type Pair2 struct {
	a *big.Rat
	b *big.Rat
	layerIndex int
}
type RatWrap struct {
	value *big.Rat
	layerIndex int
}

// This reads from the equationOutput and writes pairs of values to adderInput. However, it stops
// whenever it reaches bufferLimit, and waits for backPressure to signal that pairInput is not flooded
// and more data can be sent. Refer to pairer2() for how backPressure is signalled back.
func pairerEquations(
		equationOutput chan *big.Rat,
		adderInput chan *Pair2,
		bufferLimit int,
		backPressure chan bool) {
	var waiting *big.Rat=nil
	var count = 0
	for next := range equationOutput {
		if (waiting==nil){
			waiting=next;
		} else {
			count++
			for (count>bufferLimit) {
				<- backPressure
				count-=1
			}
			adderInput <- &Pair2{waiting, next, 0}
			waiting=nil
		}
	}
	if (waiting!=nil) {
		adderInput <- &Pair2{waiting, big.NewRat(0, 1), 0}
	}
	fmt.Println("Equation pairer complete")

	// An insurance policy to ensure backPressure doesn't overflow:
	for _ = range backPressure {}
}


// The result channel is just for returning our singular result.
// The adderInput channel is shared with pairerEquations() and feeds results to adders.
//   It's circular in that we feed adder results back into it as well. Thus we have to
//   track a count of how many values should flow through.
// The backPressure channel lets pairerEquations() know that it won't flood the adderInput
//   channel. If we successfully read a number from pairerEquations(), we send some slack
//   back its way so we can receive another.
// The adderFarmSize is just how many adders can fit in the architecture without wasting
//   way too many cycles on context switches.
// Finally, equationCount allows us to calculate iterations, since as per above, we need
//   a way to know when we're done-done.
func pairer2(
		result chan *big.Rat,
		adderInput chan *Pair2,
		backPressure chan bool,
		adderFarmSize int,
		equationCount int) {

	// Create add output channel and adder coroutines:
	adderOutput:=make(chan *RatWrap, adderFarmSize+1)
	for i:=0; i<adderFarmSize; i++ {
		go adder2(adderOutput, adderInput, i)
	}

	// Calculate how many additions we expect, so we know when to bail
	// and close all the channels. Then create an array of waiting values
	// so that we can pair up by "layer". We'd rather add the easy numbers
	// together first - which is fast - than mix & match with "hard" numbers
	// that slow everybody down.
	layerCount := int(math.Ceil(math.Log2(float64(equationCount))))
	expectCounts := make([]int,layerCount)
	expectTotal := 0
	{
		var factor=equationCount;
		for i:=0; i<layerCount; i++ {
			factor = (factor/2) + (factor%2)
			expectCounts[i]=factor;
			expectTotal += factor
		}
	}
	waiting := make([]*RatWrap,layerCount)


	// Loop through adderOutput, feeding more pairs back to the adders.
	//
	// Whenever we get a value from pairerEquations (layerIndex==0), we ease the backpressure.
	//
	// When we receive the last value for the last layer, we know we're done.
	//
	// If we receive an orphan value where they won't be any more for the given
	// layer, we pair it with a dummy 0 and give it to the adder so it bubbles
	// back to the next layer.
	layersFound:=0
	for next := range adderOutput {
		layerIndex:=next.layerIndex;
		expectCounts[layerIndex]--
		expectTotal--
		//fmt.Println(expectTotal, "adds left")
		if (layerIndex==0) {
			backPressure <- false
		} else if (layerIndex>layersFound) {
			layersFound=layerIndex
			fmt.Println("Layer advanced to", (layersFound+1), "of", layerCount)
		}
		if (waiting[layerIndex]==nil) {
			if (expectCounts[layerIndex]==0){
				if (layerIndex==layerCount-1) {
					result<-next.value
					close(result)
					close(adderOutput)
				} else {
					adderInput <- &Pair2{next.value, big.NewRat(0, 1), layerIndex+1}
				}
			} else {
				waiting[layerIndex]=next
			}
		} else {
			adderInput <- &Pair2{waiting[layerIndex].value, next.value, layerIndex+1}
			waiting[layerIndex]=nil
		}
	}

	close(adderInput)
	close(backPressure)
	fmt.Println("Pairer Complete")
}

func adder2(output chan *RatWrap, input chan *Pair2, addIndex int) {
	count := 0
	for next:=range input {
		output <- &RatWrap{next.a.Add(next.a, next.b), next.layerIndex}
		count++
	}
	fmt.Println("Adder ", addIndex, " Complete ", count)
}

