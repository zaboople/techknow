# Note: This works fine under cygwin until you use dashes
cd $(dirname $0) || exit
./build.sh "$@" && ./bin/"$@" "$@"