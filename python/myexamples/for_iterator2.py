import itertools;
import operator;

# A lot going on here:
# 1. We're building an infinite expression using itertools.repeat()
# 2. We're using () instead of [] around a comprehension so that it doesn't
#    get turned into a list, which would run out of memory. You don't have
#    make a list just because you used a comprehension!
# 3. We're using itertools.accumulate(), which is great for doing things
#    like generating a series - it's not a reducer
# 4. We're using itertools.takewhile() to take the first few items. Using
#    and if-guard in an outer comprehension would lock up, unfortunately
#    (tried it):
for y in itertools.takewhile(
        lambda x: x<1000,
        itertools.accumulate((
            ord(letter)
            for word in itertools.repeat("Close")
            for letter in word
        ), operator.add)
    ):
    print(y);