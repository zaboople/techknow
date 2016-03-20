::#!
rem This basically invokes scala with the script itself. Scala ignores the #! !# demarcators
rem and then %0 says run this file with %* as parameters. So it's a script that's sort of 
rem calling itself
@echo off
call scala %0 %*
pause
goto :eof
::!#
Console.println("Hello, world!")
argv.toList foreach Console.println
