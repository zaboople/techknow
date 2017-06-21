package main

import ("fmt")


/*
	Notice how i is printed as 1000 every time, whereas iActual has the iteration number. Yay closures. 
	To make iActual work, it has to be outside my go func(); inside, it will just do the same as i.
*/
func main() {
	ch := make(chan string, 150)
	for i:=0; i<1000; i++ {		
		iActual:=i;
		go func(){
			ch <- fmt.Sprintf("%d:%d ", i, iActual)
		}()
	}
	for i:=0; i<1000; i++ {
		fmt.Printf(<-ch)
	}
}

