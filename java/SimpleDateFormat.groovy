import java.text.*;
public class Goober {

// The idea is to create a date for midnight at a given location. When we print it.
// we will get a value ahead/behind midnight by X hours based on how far away our
// time zone is.

    public static void main(String[] args) {
        println("\nISO - X is the magical thing")
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssX");
        println(sdf.parse("2013-03-15T10:00:01+0000"))
        println(sdf.parse("2013-03-15T10:00:02Z"))
        println(sdf.parse("2013-03-15T10:00:03-0500"))

        println("\nNo time zone in date string, alternate route:")
        sdf = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss");
        sdf.setTimeZone(TimeZone.getTimeZone("US/Eastern"))
        println(sdf.parse("12-01-2012 00:00:00"))
        sdf.setTimeZone(TimeZone.getTimeZone("US/Central"))
        println(sdf.parse("12-01-2012 00:00:00"))
        sdf.setTimeZone(TimeZone.getTimeZone("US/Mountain"))
        println(sdf.parse("12-01-2012 00:00:00"))
        sdf.setTimeZone(TimeZone.getTimeZone("US/Pacific"))
        println(sdf.parse("12-01-2012 00:00:00"))

        println("Parse default format (don't believe the hype in DateFormat.MEDIUM): ")
        println(
          new SimpleDateFormat("EEE MMM d HH:mm:ss zzz yyyy")
            .parse(
              new Date().toString()
            )
        )
    }

}