if [[ $(uname) == CYG* ]]; then
  export GOPATH=$(cygpath -d $(pwd))"\\"
else
  export GOPATH=$(pwd)
fi
progname=$1
shift

# Note on cygwin, make sure to use the DOS-i-fied path to bin/go
# or it will fail bizarrely on an accusation about circular
# imports.
$GOROOT/bin/go install github.com/zaboople/$progname
