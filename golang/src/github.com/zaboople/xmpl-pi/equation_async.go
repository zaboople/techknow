package main

import (
	"math/big"
	"fmt"
)

// Originally intended to make equation evaluation as concurrent
// as possible. Ends up not achieving much of anything, and creating
// more stack+heap memory for the sake of it.
//
// This might be more useful if I work out a method for avoiding the
// highly compute-intensive addition operations.
func runEquationAsync(equationOutput ChanBig, count int) {

	// These are our factor constants for b,c,d,e in the equation;
	// Reference factors.go:
	factors := NewFactors()

	/////////////////////////////////////
	//1. Make channels for our functions:
	divisorA:=makeChanBig(8)
	divisorB:=make(chan int64, 4)
	divisorC:=make(chan int64, 4)
	divisorD:=make(chan int64, 4)
	divisorE:=make(chan int64, 4)

	equationA:=makeChanBig(4)
	equationB:=makeChanBig(4)
	equationC:=makeChanBig(4)
	equationD:=makeChanBig(4)
	equationE:=makeChanBig(4)

	/////////////////////////////////////
	//2. Start all our goroutines.

	// The former drives the latter:
	go makePowers(divisorA, 16, count)
	go chanBigDivides(equationA, divisorA, factors.bigD1)

	// makeMultiples drives chanDivides:
	go makeMultiples(divisorB, divisorC, divisorD, divisorE, 8, count)
	go chanDivides(equationB, divisorB, 1, factors.bigD4)
	go chanDivides(equationC, divisorC, 4, factors.bigD2)
	go chanDivides(equationD, divisorD, 5, factors.bigD1)
	go chanDivides(equationE, divisorE, 6, factors.bigD1)

	// Everything comes out of chanDivides/chanBigDivides into equation()
	go equation1(equationOutput, equationA, equationB, equationC, equationD, equationE, count)
}

//////////////////////////
//                      //
// EFFECTIVELY PRIVATE: //
//                      //
//////////////////////////


// 1. SIMPLEST VERSION:
func equation1(output ChanBig, a ChanBig, b ChanBig, c ChanBig, d ChanBig, e ChanBig, count int) {
	for i:=0; i<count; i++ {
		output <- doEquation(<-a, <-b, <-c, <-d, <-e)
	}
	fmt.Println("Equations complete ", count)
	close(output)
}


// 2 THE MORE CONCURRENT BUT NOT REALLY BETTER VERSION //
func equation2(output ChanBig, a ChanBig, b ChanBig, c ChanBig, d ChanBig, e ChanBig, count int) {

	type EquationInput struct {
		a *big.Rat
		b *big.Rat
		c *big.Rat
		d *big.Rat
		e *big.Rat
	}

	// 1 Setup. Lately it seems like running a large farm is a
	// a bad idea that just wastes memory, as equations calculate more
	// than fast enough to keep the downstream adders busy.
	farmSize:=1
	forkInput:=make(chan *EquationInput, 20)
	signalBack:=make(chan bool, 20)

	// 2 Fork a series of helpers that run the equation:
	for i:=0; i<farmSize; i++ {
		go func() {
			for values:=range forkInput {
				output <- doEquation(values.a, values.b, values.c, values.d, values.e)
				signalBack<-true
				//fmt.Print("e")
			}
		}()
	}

	// 3 Fork a manager that pumps data to the helpers:
	go func() {
		for i:=0; i<count; i++ {
			forkInput <- &EquationInput{<-a, <-b, <-c, <-d, <-e}
		}
		close(forkInput)
	}()

	// 4 Wait on the helpers to finish:
	for i:=0; i<count; i++ {
		<- signalBack
	}
	fmt.Println("Equations complete ", count)
	close(signalBack)

	// 5 We are done:
	close(output)
}

// This basically does a * (b - c - d - e)
func doEquation(a *big.Rat, b *big.Rat, c *big.Rat, d *big.Rat, e *big.Rat) *big.Rat {
	return bigMultiply(
		a,
		bigSubtract(
			bigSubtract(
				bigSubtract(b, c), d,
			),
			e,
		),
	)
}

// Feeds output to chanBigDivides():
func makePowers(output ChanBig, base int64, count int) {
	multiple:=makeBig(base)
	res:=makeBig(1)
	output <- res
	for i:=1; i<count; i++ {
		res=makeBig(0).Mul(res, multiple)
		output <- res
	}
	fmt.Println("Powers complete")
	close(output)
}
func chanBigDivides(output ChanBig, divisors ChanBig, numerator *big.Rat) {
	for divisor := range divisors {
		output <- bigDivide(numerator, divisor)
		//fmt.Print("D")
	}
}

// Feeds consecutive multiples to chanDivides. Straight math is kinda dumb to dedicate
// 4 channels plus a goroutine for, but it's the easiest way to set up
// chanDivides() to sweep thru without redundancy.
func makeMultiples(d1, d2, d3, d4 chan int64, base int64, count int) {
	// 0 as a divisor might sound bad, but chanDivides() always adds a value to
	// the raw divisor we send:
	var res int64=0
	d1 <- res
	d2 <- res
	d3 <- res
	d4 <- res
	for i:=1; i<count; i++ {
		res+=base
		d1 <- res
		d2 <- res
		d3 <- res
		d4 <- res
		//fmt.Print("m")
	}
	fmt.Println("Multiples complete")
	close(d1)
	close(d2)
	close(d3)
	close(d4)
}
func chanDivides(output ChanBig, divisors chan int64, addToDivisor int64, numerator *big.Rat) {
	for divisor := range divisors {
		output <- bigDivide(numerator, makeBig(divisor+addToDivisor))
		//fmt.Print("d")
	}
}



