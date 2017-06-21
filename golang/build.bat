echo off
d:
cd %~dp0

rem Setup go paths
set GOROOT=c:\Programs\Go183
set GOPATH=%~dp0

rem Program name must be first argument:
set progname=%1
SHIFT
echo Building: %progname%

rem Do build:
%GOROOT%\bin\go.exe install github.com/zaboople/%progname%

