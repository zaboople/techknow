echo off
d:
cd %~dp0

set inputname=%1
SHIFT

set progname=%1
SHIFT
call build.bat %progname%
if %errorlevel% neq 0 exit /b %errorlevel%

rem Run the program:
type %inputname% | bin\%progname% %1 %2 %3 %4 %5 %6 %7 %8 %9

