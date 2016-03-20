//Note that most of these operations are available on maps too, they just do something slightly different.

//1 Create a set and add stuff to it
val set=Set(10, 20, 30)
val x=set + 1 + 2 
println(x)

//2 Shows the += & -= operators. Note that it adds 7, not 3 & 4. 
var y=set;
y+=3 + 4
y-=10
println(y)

//3 How do I add (union) actual sets together? Here:
println ( Set(1,2,3) ++ Set(4,5,6) )

println

//4 As far as removing elements... note that the ++ executes before the --, left to right:
println ( Set(1,2,3) ++ Set(4,5,6) -- Set(2,6) )

//5 Hey it can accept lists
println ( Set(1,2,3) ++ List(4,5,6) -- List(2,6) )

//6 Oh look, ++= --=
var mm=Set(1,2,3,4)
mm++=Set(7,8,9)
mm--=Set(1,7)
println(mm);