cd $(dirname $0) || exit
./build.sh "$@" && ./bin/"$@" "$@"