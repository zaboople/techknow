package main

import "fmt"

func main() {

	//Here's a while loop defined as a for loop:
	sum := 1
	for sum < 2000 {
		fmt.Print(sum, " ")
		sum += sum + 13
	}
	fmt.Println(sum)

	// How about we just do a classic for ok:
	for i:=0; i<10; i++ {
		fmt.Print(i, " ")
	}
	fmt.Println()

	for i:=0; i<10; i++ {
		fmt.Print(i, " ")
	}
	fmt.Println()

	//Now let's go thru the iterations on range + array:
	{
		//A rather funky string array declaration:
		strings := [...]string{"a", "b", "c", "d"}

		//Looping through the (slightly) hard way followed
		//by the (slightly) easier way:
		for i := range strings {
			fmt.Println("Array index", i, "is", strings[i])
		}
		for i, v := range strings {
			fmt.Println("Array index", i, "is", v)
		}

		//If you don't want the index use _ to ignore it:
		for _, v := range strings {
			fmt.Print(v+" ")
		}

		// But if you don't care at all, use _ with = instead of :=
		for _ = range strings {
			fmt.Print(" ")
		}
		fmt.Println()
	}

	//OK how about range + map:
	{
		//The slightly harder way:
		capitals := map[string]string{"France": "Paris", "Italy": "Rome", "Japan": "Tokyo"}
		for key := range capitals {
			fmt.Println("Map item: Capital of", key, "is", capitals[key])
		}

		//The slightly easier way:
		for key2, val := range capitals {
			fmt.Println("Map item: Capital of", key2, "is", val)
		}
	}

	// The simplest for loop has no conditions and runs forever.
	// Of course we cheat and bail ourselves out:
	{
		for {
			if 1>0 {break}
		}
	}


}
