RANGE=4
number=$RANDOM
let "number %= $RANGE"
echo $number