package main

import (
	"fmt"; "time";
)

/////////////
// DRIVER: //
/////////////

func main() {
	i:=0
	testWeekdayArithmetic(&i)
	testSecondArithmetic(&i)
	testSleep(&i)
	testParseDate(&i)
	testParseTime(&i)
	testConvertTime(&i)
}

//////////////////////
// REUSEABLE STUFF: //
//////////////////////

/* 
	For reference, this is sort of the canonical representation for time formatting;
		Day of week =1, Month =2, Hour=3, Minute=4, Second=5, Year=2006 or 6, TZ offset=7 and ZONE=MST
	Example: Mon Jan 2 3:04:05 PM -0700 MST 2006

	And notice how I am using 15 for 3 for 24 hour time which is bizarre.
*/
const parseLong   = "Jan 2, 2006 at 15:04 (MST)"
const printLong   = "2006-01-02 3:04pm MST"

/* This is a legitimate location: */
const myTown      ="America/Chicago"

/* 
	This is formatting for the best sub-second granularity. Note that 000... is sub-seconds, for what that's worth,
	not 789123 etc. You get 3 orders of magnitude: milli/micro/nano, so you need 9 zeroes. If you want separators
	between the zeroes, you're screwed, because that will break them.
*/
func longerPrint(name string, t time.Time){
	const printLonger = "2006-01-02 3:04:05.000000000pm MST"
	fmt.Printf("%-20s %s\n", name+":", t.Format(printLonger))
}

/* 
	Prints all the time & time zone info:
*/
func printAll(t time.Time) {
	name, offset := t.Zone()
	println("Time: "+t.Format(printLong))
	println("Zone, Offset (in seconds):", name, offset)
	println("Location:", t.Location().String())
}

/* 
  Clever way to print headings on tests. I like this.
*/
func heading(index *int, title string) {
	*index++
	fmt.Printf("\n******************************\n%d. %s\n", *index, title)
}

////////////
// TESTS: //
////////////

func testWeekdayArithmetic(index *int) {

	heading(index, "Weekday arithemetic")
	println("Warning: Weekday will blow up on subtraction if you go less than zero.")

	//Utility:
	format := func(name string, day time.Weekday){
		fmt.Printf("%-22s %s\n", name, day)
	}
	
	today := time.Now().Weekday()
	format("Today is", today)

	tomorrow:=time.Now().Add(time.Hour*24)
	format("Tomorrow is", tomorrow.Weekday())

	dayAfterTomorrow:=tomorrow.Add(time.Hour*24)
	format("Day after tomorrow is", dayAfterTomorrow.Weekday())
}

func testSecondArithmetic(index *int) {
	heading(index, "Second arithemetic")

	now := time.Now()
	longerPrint("Starting with", now)

	//Clever lambda/closure stuff!
	var f=func(amount time.Duration, name string) {
		for i:=1; i<3; i++{
			now=now.Add(amount)
			longerPrint(name, now)
		}
	}
	f(time.Nanosecond, "Add a nanosecond")
	f(time.Microsecond, "Add a microsecond")
	f(time.Millisecond, "Add a millisecond")
}

func testSleep(index *int) {
	heading(index, "Sleep")
	longerPrint("Current time", time.Now())

	time.Sleep(1* time.Nanosecond)
	longerPrint("Sleep 1 ns", time.Now())
	
	time.Sleep(1* time.Microsecond)
	longerPrint("Sleep 1 micros", time.Now())
	
	time.Sleep(1* time.Millisecond)
	longerPrint("Sleep 1 ms", time.Now())
}


func testParseDate(index *int) {
	heading(index, "Parsing a date (no time)")
	println("The challenge here is to create a local date from an assumed local date string.")

	const shortForm = "2006-Jan-02"
	const toParse   = "2013-Feb-03"
	println("Parsing:", toParse)

	fmt.Println("\nThe obvious solution parses to UTC, but when converted to local:")
	if tt, err := time.Parse(shortForm, toParse); err!=nil {
		println("Error parsing: "+toParse, err.Error())
	} else {
		fmt.Println(tt.Local().Format(printLong))		
	}

	//This works much better:
	fmt.Println("\nSolution is to parse \"in location\":")
	if location, err:=time.LoadLocation(myTown); 
			err!=nil {
		println("Error loading location"+myTown)
	}	else if localTime, err := time.ParseInLocation(shortForm, toParse, location); 
			err!=nil {
		println("Error parsing: "+toParse, err.Error())
	} else {
		fmt.Println(localTime.Format(printLong))		
	}	
}

func testParseTime(index *int) {
	heading(index, "Parsing Time")

	println("\nLet's fail a parse blatantly:")
	const badTime="Mar 3, 2013 at 11:01 (UTC) awef"
	println("Parsing:", badTime)
	if mytime, err := time.Parse(parseLong, badTime); err != nil {
		println("Failed to parse, error:", err.Error())
	}	else {
		printAll(mytime)
	}				
		
	println("\nLet's parse a localized time:")
	const originalTime="May 3, 2013 at 11:01 (CDT)"
	println("Parsing:", originalTime)
	mytime, err := time.Parse(parseLong, originalTime)
	if err != nil {
		println("Failed to parse", err.Error())
	}	else {
		printAll(mytime)
		println("Verifying with Local():")
		printAll(mytime.Local())
	}
}
func testConvertTime(index *int) {
	heading(index, "Converting the current time")
	current:=time.Now()

	println("\nThe current time is:")
	printAll(current)

	println("\nThe current time in UTC:")
	printAll(current.UTC())

	println("\nThe current time in an imaginary time zone called \"DERP\", 60 seconds east from UTC:")
	printAll(
		current.In(
			time.FixedZone(
				"DERP", 60)))
	
	println("\nThe current time in an alternate location, based on the ISO location standard:")	
	locationName:="America/Los_Angeles"
	println("Converting to location: "+locationName)
	if loc, err:=time.LoadLocation(locationName); 
			err != nil {
		println("Failed to parse location:", err.Error())
	} else {
		printAll(current.In(loc))
	}
}
