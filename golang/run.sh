# Note: This may not work under cygwin or windows or unix or whatever because
# 1. flag.Arg(0) is different depending on environment; on cygwin, at least, it's the name of the program.
#    Probably on Unix/Linux/Gnu too.
# 2. Other weirdness

cd $(dirname $0) || exit
./build.sh "$@" && ./bin/"$@" "$@"
