echo off
d:
cd %~dp0

set progname=%1
SHIFT
call build.bat %progname%
if %errorlevel% neq 0 exit /b %errorlevel%
SET GOMAXPROCS=200

rem Run the program:
bin\%progname% %1 %2 %3 %4 %5 %6 %7 %8 %9

