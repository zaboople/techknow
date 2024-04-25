#!/usr/bin/env python3

def stripIndent(ii, trimEnds=True):
  list = ii.split("\n")
  found = 1
  lineCount = len(list);
  while (1):
    list2 = []
    any = 0
    for x in list:
      if x == "":
        list2.append(x)
      elif x[0] == " ":
        any += 1
        list2.append(x[1:])
    if not any:
      break;
    if len(list2)!=lineCount:
      break;
    list = list2;
  while trimEnds and len(list)>0 and list[0] == "":
    del list[0]
  while trimEnds and len(list)>0 and list[-1] == "":
    del list[-1]
  return "\n".join(list);

##############
## TESTING: ##
##############

def debug(label, ii, stripEnds=True):
  print(f"**** {label} ****:")
  print(stripIndent(ii, stripEnds))
  print("<-----")

debug("Big Test 1:", """
    Shabooo
  Hello
    Hi
      Dummy

    Bad
  End


""")
debug("Big Test 2:", """
    5678
    nubule
    1234

      Sterp
    1
""");

debug("One word:", "blah")
debug("One blank:", "")
debug("Many blank lines:", """


""")
debug("Many blank lines, no trim:", """

        dub
    chub

""", False)