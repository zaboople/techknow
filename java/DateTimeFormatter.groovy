import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.regex.Pattern
import java.util.Date
import java.time.format.DateTimeFormatter
import java.time.ZonedDateTime
import java.time.Instant
import java.time.ZoneId

class DateMaster {

    // This allows for retarded time zone name, time zone offset with/without ':', the "T" in the middle (or not)
    private final static DateTimeFormatter dtfAnything =
        DateTimeFormatter.ofPattern("yyyy-MM-dd['T'][' ']HH:mm:ss[XXX][XX][X][' 'zzzz]")
    private final static DateTimeFormatter dfFormatWithOffset=
        DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss[XXXX]")

    private static void testDate(String sdate) {
        System.out.println("\nTesting "+sdate)
        try {
            ZonedDateTime date=ZonedDateTime.parse(sdate, dtfAnythingy)
            Date oldDate=Date.from(date.toInstant());
            System.out.println("ZonedDateTime:    "+date)
            System.out.println("With nice offset: "+dfFormatWithOffset.format(date))
            System.out.println("java.util.Date:   "+oldDate)

        } catch (Exception e) {
            e.printStackTrace(System.out)
        }
    }
    public static void main(String[] args) {
        testDate("2018-05-01T15:55:20Z")
        testDate("2018-05-01T15:55:20+0500")
        testDate("2018-05-01T15:55:20-0500")
        testDate("2018-11-01T13:30:00-0700")
        testDate("2018-05-01 15:55:20-05:15")
        testDate("2018-05-01T15:55:20 America/New_York")
        testDate("2019-01-09T00:00:00-0600")
        ZoneId zone=ZoneId.of("GMT");
        println(zone)
    }
}
