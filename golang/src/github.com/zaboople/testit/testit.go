package main
import (
	"fmt"
)

func main() {
	//fmt.Println("\nYEAH", countCycles(25), "\n")
	fmt.Println("\nYEAH", countCycles(12), "\n")
	fmt.Println(1%2)
}

func countCycles(count int) int {
	total:=0
	mod:=0
	for count+mod>1 {
		fmt.Println(">", count, mod)
		mod+=count % 2
		count=(count / 2)
		if (mod==2) {
			count++
			mod=0
		}
		total += count
		fmt.Println(count, mod)
	}
	if (mod!=0){
		total++
	}
	return total
}