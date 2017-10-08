package main
import (
	"fmt"; "time"; "flag"; "strconv"
)

/* NOTE: IT IS USUALLY IMPORTANT TO SET GOMAXPROCS=200 IN YOUR SHELL */

/////////////
// DRIVER: //
/////////////

func main() {
	if iterations:=getIterations(); iterations!=nil {
		drive(*iterations)
	}
}
func getIterations() *int {
	flag.Parse()
	if (flag.NArg()==0) {
		fmt.Println("Need a number")
	} else if iterations,err:=strconv.Atoi(flag.Arg(0)); err==nil {
		if (iterations<0) {
			fmt.Println("Must be at least 0")
		} else {
			return &iterations
		}
	} else {
		fmt.Println("Invalid: "+flag.Arg(0), err)
	}
	return nil
}
func drive(iterations int) {
	type funcname struct {function CheckPrime; name string}
	funcs:=[]funcname{
		//funcname{hardPrimeCheckChan, "h"},
		//funcname{mediumPrimeCheckChan, "m"},
		funcname{mediumHardPrimeCheckChan, "~"},
		//funcname{simplePrimeCheckChan, "-"},
	}

	waitfor:=make(chan int)
	fmt.Println("Iterations", iterations)
	for _,f:=range funcs {
		go calc(waitfor, iterations, f.name, f.function);
	}
	for range funcs {
		<- waitfor
	}
}

type CheckPrime func(input chan int, output chan int)

func calc(
		waitfor chan int, iterations int, prefix string, checker CheckPrime,
	){
	begin:=time.Now()
	toCheck:=make(chan int, 2000)
	success:=make(chan int, 2000)
	go checker(toCheck, success)

	go func() {
		for i:=0; i<iterations; i++ {
			toCheck<-i
		}
		close(toCheck)
	}()

	for i:=range success {
		fmt.Println(prefix, " ", i, " ")
	}

	fmt.Println("\n", prefix, "TIME: ", time.Now().Sub(begin), "\n")
	waitfor <- 1
}



//////////////////////////////
// FASTEST SOLUTION SO FAR: //
//////////////////////////////

func mediumHardPrimeCheckChan(vals chan int, ifPrime chan int) {
	already:=[]int{}

	check:=func (n int)  bool {

		// Compare to known primes:
		limit:=len(already)
		for i:=0; i<limit; i++ {
			compare:=already[i]
			if (compare > 1) {
				if (n % compare==0){
					return false;
				}
			}
		}

		// Add to known and return true:
		already=append(already, n)
		return true
	}

	for v:=range vals {
		if (check(v)){
			ifPrime <- v
		}
	}
	close(ifPrime)
}




/////////////////////////////
// fast but not fastest:   //
/////////////////////////////

func mediumPrimeCheckChan(vals chan int, ifPrime chan int) {
	primes:=make([]bool, 1000000)
	max:=0
	check:=func (n int)  bool {
		limit:=n/2
		for i:=2; i<=limit; i++ {
			eligible:=i > max || primes[i]
			if (eligible){
				//fmt.Print("e", i, " ", n, " ")
				if (n % i==0) {
					return false
				}
			}
		}
		primes[n]=true
		max=n
		return true
	}

	for v:=range vals {
		if (check(v)){
			ifPrime <- v
		}
	}
	close(ifPrime)
}

///////////////////////////////////////////////
// HARD SOLUTION, DOESN'T PERFORM THAT WELL: //
///////////////////////////////////////////////

func hardPrimeCheckChan(input chan int, ifPrime chan int) {

	primes:=&Primes{
		make([]bool, 100000), 0,
	}
	primes.primes[2]=true;
	primes.primes[3]=true;
	askContains:=&AskContains{
		0, make(chan uint8, 10),
	}
	chanAskContains:=make(chan *AskContains, 10)
	chanAdd:=make(chan int, 100)
	chanAddMax:=make(chan int, 100)
	go primes.check(chanAskContains, chanAdd, chanAddMax)


	for v:=range input {
		prime:=true
		for i:=2; prime && i<=v/2; i++ {
			askContains.value=i
			chanAskContains <- askContains
			answer:=<- askContains.answer
			if (answer==CONTAINS_YES) {
				//fmt.Println("\ncheck ", i)
				prime=v % i!=0
			} else if (answer==CONTAINS_DUNNO) {
				//fmt.Print(" DUNNO ", i)
				if (simplePrimeCheck(i)) {
					//fmt.Print(" dammit ", i)
					prime=v % i!=0
					chanAddMax <- i
				}
			}
		}
		if (prime) {
			ifPrime <- v
			chanAdd <- v
		} else {
			//fmt.Print("NO", v, " ")
		}
	}
	close(ifPrime)
}




const CONTAINS_NO=0
const CONTAINS_YES=1
const CONTAINS_DUNNO=2

type AskContains struct {
	value int
	answer chan uint8
}
type Primes struct {
	primes []bool
	max int
}
func (this *Primes) check(askContains chan *AskContains, add chan int, addMax chan int) {
	for {
		select {
			case next:=<-askContains:
				//fmt.Print("c", next.value, " ")
				if (next.value > this.max) {
					//fmt.Print("DUNNO", next.value)
					next.answer<-CONTAINS_DUNNO
				} else if (this.contains(next.value)) {
					//fmt.Println("YES", next.value)
					next.answer<-CONTAINS_YES
				} else {
					//fmt.Println("NO", next.value)
					next.answer<-CONTAINS_NO
				}
			case addMe:=<-add:
				//fmt.Print("+")
				this.add(addMe)
			case max:=<-addMax:
				if (this.max< max) {
					//fmt.Print("mm", max)
					this.max=max
				}
		}
	}
}
func (this *Primes) contains (i int) bool {
	//fmt.Println("\n CONTAINS? ", i, this.primes[i])
	return this.primes[i]
}
func (this *Primes) add (i int) {
	this.primes[i]=true
	if (i>this.max) {
		this.max=i
	}
}



////////////////////////////////////////////
// SIMPLE SOLUTION, PERFORMS PRETTY GOOD: //
////////////////////////////////////////////

func simplePrimeCheckChan(vals chan int, ifPrime chan int) {
	for v:=range vals {
		if (simplePrimeCheck(v)){
			ifPrime <- v
		}
	}
	close(ifPrime)
}


func simplePrimeCheck(n int)  bool {
	limit:=n/2
	for i:=2; i<=limit; i++ {
		if (n % i==0) {
			return false
		}
	}
	return true
}
