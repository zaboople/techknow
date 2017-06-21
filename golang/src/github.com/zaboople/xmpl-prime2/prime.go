package main
import (
	"fmt"; "time"; "flag"; "strconv"; "regexp"
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
	} else if regex,err:=regexp.CompilePOSIX(","); err!=nil {
		fmt.Println("Bad Regex!", err)
	} else if iterations,err:=strconv.Atoi(regex.ReplaceAllString(flag.Arg(0), "")); err==nil {
		if (iterations<0) {
			fmt.Println("Must be at least 0")
		} else {
			return &iterations
		}
	} else {
		fmt.Println("Invalid: "+flag.Arg(0), "\nParsing error:", err)
	}
	return nil
}

func drive(iterations int) {
	type funcname struct {function CheckPrime; name string}
	funcs:=[]funcname{
		funcname{sieveCheck, "~"},
	}
	waitfor:=make(chan int)
	fmt.Println("Max", iterations)
	for _,f:=range funcs {
		go calc(waitfor, iterations, f.name, f.function);
	} 
	for range funcs {
		<- waitfor 
	}
}
type CheckPrime func(input int, output chan int)

func calc(
		waitfor chan int, iterations int, prefix string, checker CheckPrime,
	){
	begin:=time.Now()
	success:=make(chan int, 10000)
	go checker(iterations, success)		
	primeCount:=0
	for i:=range success {
		fmt.Println(prefix, " ", i, " ")
		primeCount++
	}	
	fmt.Println("\n", prefix, "FOUND", primeCount, "IN TIME: ", time.Now().Sub(begin), "\n")	
	waitfor <- 1
}


func sieveCheck(max int, ifPrime chan int) {
	primes:=make([]bool, max)	
	for i,_:=range primes {
		primes[i]=true
	}
	for i:=2; i<max; i++ {
		if (primes[i]) {
			ifPrime <- i
		}
		for x:=i+i; x<max; x+=i {
			primes[x]=false
		}
	}
	close(ifPrime)
}