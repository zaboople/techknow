package main

import ("fmt"; "sort")

func main() {test()}

func makeStr() []string {
	return []string {"Z", "A", "B", "C", "X", "Y"}
}

func test(){
	{
		fmt.Println("\nShortcut to string sort:")
		aa:=makeStr()
		fmt.Println(aa)
		sort.Strings(aa)
		fmt.Println(aa)
	}
	
	{
		fmt.Println("The sort.XXXXSlice types are kind of dumb because they modify the array that you pass to them.")
		fmt.Println("Also note that they count as interface sort.Interface. Wait, an interface named \"Interface\"?")
		fmt.Println("Um, yeah...")
		
		fmt.Println("Original, StringSlice, sort.Interface - all the same array:")
		bb:=makeStr()
		var copy sort.StringSlice=sort.StringSlice(bb)
		var dumb sort.Interface=copy
		fmt.Println(bb, copy, dumb)
	
		fmt.Println("All after sort:")
		copy.Sort()
		fmt.Println(bb, copy, dumb)
		
		fmt.Println("Simpler approach:")	
		cc:=makeStr()
		sort.StringSlice(cc).Sort()
		fmt.Println(cc)
	}
	{
		fmt.Println("\nReverse sorting is completely bizarre.")
		dd:=makeStr()
		
		fmt.Println("First we get an instance of something that *will* sort in reverse *when* we tell it to sort:")
		var ddi sort.Interface=sort.Reverse(sort.StringSlice(dd))
		fmt.Println(dd, ddi, ddi.Len())
		
		fmt.Println("Now we actually do the reverse sort by calling sort.Sort(). No, sort.Interface doesn't have")
		fmt.Println("a sort function. So we have to use sort.Sort(), which of course doesn't return anything.")
		sort.Sort(ddi)
		fmt.Println(dd, ddi)
	}

	{
		fmt.Println("\nSwapping:")
		ee:=makeStr()
		eei:=sort.StringSlice(ee)
		sort.Sort(eei)
		fmt.Println(ee, eei)
		eei.Swap(0, 5)
		fmt.Println(ee, eei)
	}
}