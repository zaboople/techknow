package main
import "fmt"
import "flag"
import "strings"

/*
  This implements google's "wordcount" problem, where you count the words in a
	string input.
*/


/**
 * Initialize the big list of breakable chars:
 */
var breakstr="!#.<>~`$%^&*()_+{}[]\\|\":;'? "
var breaks=makebreaks()
func makebreaks() (breaks []rune) {
  breaks=make([]rune, len(breakstr))
	for index, c:=range(breakstr) {
		breaks[index]=c
	}
	return
}


/*
 * The big thing here is that we convert arrays of characters into strings.
 * Note that the type required is "rune". Ah, rune. Not char.
 */
func WordCount(s string) map[string]int {
	result:=map[string]int{}
	currword:=make([]rune, len(s))
	count:=0
	lastIndex:=-1	
	for _,ccc:= range(s){
		if isFunky(ccc, breaks) {
			count++
			update(result, currword, lastIndex)
			lastIndex=-1
		} else {
			lastIndex+=1
			currword[lastIndex]=ccc
		}
	}
	update(result, currword, lastIndex)
	return result
}

func isFunky(achar rune, funky []rune) bool {
	for i:=0; i<len(funky); i++ {
		if funky[i]==achar {
			return true
		}
	}
	return false
}
func update(result map[string]int, reserve []rune, lastIndex int) {
	aword:=string(reserve[:lastIndex+1])
	if aword != "" {
		result[aword] = result[aword] + 1
	}
}

func main() {
	//Get arguments:
	flag.Parse()
	var args []string=flag.Args()
	fmt.Printf("\nArguments: %d: %v\n", len(args), args)
	
	//For each argument print word count:	
	var str string=strings.Join(args, " ")
	fmt.Printf("\nFor sentence: \"%s\"\n\n", str)
	fmt.Printf("Result: %v", WordCount(str))
	
}
