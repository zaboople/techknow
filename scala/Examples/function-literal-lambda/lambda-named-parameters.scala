/* 
  This demonstrates a lambda with no parameters. Specifically it demonstrates a special subtle trick 
  called "named parameters". The difference is in the f1 & f2 function declarations, 
  where f1 says (named parameters):
    string: => String
  but f2 says:
    string:() => String
  The former allows for a much simpler syntax when it comes time to provide the actual lambda.
 
  Notes:

  1. When Scala saw my recursion, it demanded that f1 & f2 return a value. So I declared
  them as "Unit", which is interesting, because you normally don't have to say that.

  2. Strange behavior of the "stringer" variable:
  f1 says:
    val s=stringer; 
  f2 says:
    val s=stringer(); 
  -f1 will not compile with the f2 version; we have to leave off the parentheses. However, 
      somehow scala infers that this is a function invocation! When I say f1(stringer)
      it infers that I am passing stringer, not invoking it, but that's
      easier to understand because of f1's parameter type declaration.
  -f2 will run forever with the f1 version; without the parens, s gets assigned
      the function itself instead of the string it would return. Well, duh, of course. Maybe
      type declarations aren't so bad...

   3. These are "tail-recursive", verify by setting the max increment to 10000 or so instead of 1000.
*/

//Example 1 (better)
def f1(stringer: => String):Unit={
  val s=stringer;
  if (s!=null){
    print(" "); print(s);
    f1(stringer); 
  }
}
var i=0;
f1 { 
  i+=1
  if (i==1000) null else ">"+i
} 

//Example 2 (worse) (doesn't use named parameters):
def f2(stringer:() => String):Unit={
  val s=stringer();
  if (s!=null){
    print(" "); print(s);
    f2(stringer); 
  }
}
var j=0;
f2(()=>{
  j+=1 
  if (j==1000) null else ">"+j
}) 
