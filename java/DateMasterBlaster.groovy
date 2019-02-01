import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.regex.Pattern
import java.util.Date
import java.time.format.DateTimeFormatter
import java.time.ZonedDateTime
import java.time.Instant
import java.time.ZoneId

class DateMaster {

    /** This should capture most variations on java's crappy DateFormat.FULL/MEDIUM */
    private final static Pattern patternFull=
        Pattern.compile(".*?, .*?, \\d\\d\\d\\d .*")
    private final static DateTimeFormatter formatFull2=
        DateTimeFormatter.ofPattern("[EEEE][EEE], [MMMM][MMM] d, yyyy h:mm:ss a [zzzz][zzz][zz][z]")

    /** This captures most ISO variations */
    private final static Pattern patternISO=
        Pattern.compile("\\d\\d\\d\\d-\\d\\d-\\d\\dT\\d\\d:\\d\\d:\\d\\d.*")
    private final static DateTimeFormatter formatISO=
        DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss[XXXXX][XXXX][XXX][XX][X]")

    private final static DateFormat formatFull=DateFormat.getDateTimeInstance(DateFormat.FULL, DateFormat.FULL);

    private final static DateTimeFormatter dfFormatWithOffset=
        DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss[XXXX]")
    private final static DateTimeFormatter dfFormatWithZone=
        DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss z")

    public static ZonedDateTime parseDate(String date) {
        if (patternFull.matcher(date).matches())
            //return formatFull.parse(date)
            return ZonedDateTime.parse(date, formatFull2)
        else
        if (patternISO.matcher(date).matches()) {
            return ZonedDateTime.parse(date, formatISO)
        }
        else
            throw new RuntimeException("Date doesn't match any parsers: "+date)
    }

    private static void testDate(String sdate) {
        System.out.println("\nTesting "+sdate)
        try {
            ZonedDateTime date=parseDate(sdate)
            Date oldDate=Date.from(date.toInstant());
            String oldDateFull=formatFull.format(oldDate);
            System.out.println(String.format(
                "Got ZonedDateTime: %s ... java.util.Date: %s ... full formatted %s", date, oldDate, oldDateFull
            ))
            String withOffset=dfFormatWithOffset.format(date)
            System.out.println("With nice offset: $withOffset")

        } catch (Exception e) {
            e.printStackTrace(System.out)
        }
    }
    public static void main(String[] args) {
        testDate("2018-05-01T15:55:20Z")
        testDate("2018-05-01T15:55:20+0500")
        testDate("2018-05-01T15:55:20-0500")
        testDate("2018-11-01T13:30:00-0700")
        testDate("2018-05-01T15:55:20-05:15")
        testDate("2018-05-01T15:55:20 America/New_York")
        testDate("Friday, May 25, 2018 8:53:52 AM CDT")
        testDate("Monday, Jan 1, 2018 8:53:52 AM CDT")
        testDate("Mon, Jan 1, 2018 8:53:52 AM CDT")
        testDate("Mon, January 1, 2018 8:53:52 AM CDT")
        testDate("Mon, January 1, 2018 8:53:52 AM Central Daylight Time")
        testDate("Friday, July 6, 2018 12:00:00 AM CDT");
        testDate("Friday, July 6, 2018 12:00:00 AM America/Chicago");

        testDate(DateFormat.getDateTimeInstance(DateFormat.FULL, DateFormat.FULL).format(new java.util.Date()))

        testDate("2019-01-09T00:00:00-0600")
        ZoneId zone=ZoneId.of("GMT");
        println(zone)
    }
}
