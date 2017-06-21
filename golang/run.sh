if [[ $(uname) == CYG* ]]; then
  export GOPATH=$(cygpath -d $(pwd))
else
  export GOPATH=$(pwd)
fi
progname=$1
shift
go install github.com/zaboople/$progname && bin/$progname "$@"