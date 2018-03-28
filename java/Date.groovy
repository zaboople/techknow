import java.util.*;
import java.text.*;

SimpleDateFormat sdf

// The idea is to create a date for midnight at a given location. When we print it.
// we will get a value ahead/behind midnight by X hours based on how far away our
// time zone is.

println("\nUsing UTC time zone in date string:")
sdf = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss z", Locale.ENGLISH);
println(sdf.parse("12-01-2012 00:00:00 -0500"))
println(sdf.parse("12-01-2012 00:00:00 -0600"))
println(sdf.parse("12-01-2012 00:00:00 -0700"))
println(sdf.parse("12-01-2012 00:00:00 -0800"))

println("\nNo time zone in date string, alternate route:")
sdf = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss", Locale.ENGLISH);
sdf.setTimeZone(TimeZone.getTimeZone("US/Eastern"))
println(sdf.parse("12-01-2012 00:00:00"))
sdf.setTimeZone(TimeZone.getTimeZone("US/Central"))
println(sdf.parse("12-01-2012 00:00:00"))
sdf.setTimeZone(TimeZone.getTimeZone("US/Mountain"))
println(sdf.parse("12-01-2012 00:00:00"))
sdf.setTimeZone(TimeZone.getTimeZone("US/Pacific"))
println(sdf.parse("12-01-2012 00:00:00"))
