#!/usr/bin/env python3

import locale
from myutils import comment

comment("""
    This is a hack for getting all the system locales.
    locale_alias is an undocumented variable. Be careful, a lot
    of command line programs like less/head/tail will corrupt
    the output of this script
""")
names = sorted(set([
    (value.split(".")[0])
    for key, value in locale.locale_alias.items()
    if value.find(".") > -1
]))

x = 1234567.8
for name in names:
    print(f"{name}:", end=" ")
    locale.setlocale(locale.LC_ALL, name)

    conv = locale.localeconv()          # get a mapping of conventions
    try:
        print(
            locale.format_string(
                "%s%.*f",
                (conv['currency_symbol'], conv['frac_digits'], x),
                 grouping=True
            )
        )
    except UnicodeEncodeError as ex:
        print(ex)

