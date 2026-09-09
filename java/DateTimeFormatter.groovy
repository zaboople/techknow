import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.regex.Pattern;
import java.util.Date;
import java.time.format.DateTimeFormatter;
import java.time.ZonedDateTime;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.LocalDate;
import java.time.Instant;
import java.time.ZoneId;

class DateMaster {

    private final static DateTimeFormatter dfFormatWithOffset=
        DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS[XXXX]");

    private static void testLocalDateToDate(){
        println("\nTesting local date to date...");
        def df = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        Date date = Date.from(LocalDate.parse("2021-04-13", df).atStartOfDay(
                ZoneId.of("America/Chicago")
            ).toInstant());
        println("Got date $date")
    }

    private static void testCreateDate() {
        println("\nTest create ZonedDateTime...");
        final DateTimeFormatter dtfFormatAMPM=
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss a");

        ZoneId zi = ZoneId.of("America/Chicago");
        ZonedDateTime zdt = ZonedDateTime.ofInstant(
            Instant.ofEpochMilli(System.currentTimeMillis()),
            zi
        );
        println("Created date: $zdt");
        println("Formatted: "+dtfFormatAMPM.format(zdt));
    }

    private static void testParseDifferentDates(String sdate) {
        System.out.println("\nTesting parse Date "+sdate)
        // This allows for retarded time zone name, time zone offset
        // with/without ':', the "T" in the middle (or not):
        final DateTimeFormatter dtfAnything = DateTimeFormatter.ofPattern(
            "yyyy-MM-dd['T'][' ']HH:mm:ss[.SSS][' '][XXX][XX][X][zzzz]"
        );
        try {
            ZonedDateTime date=ZonedDateTime.parse(sdate, dtfAnything)
            debug(date)
        } catch (Exception e) {
            e.printStackTrace(System.out)
        }
    }

    private static void debug(ZonedDateTime date) {
        Date oldDate=Date.from(date.toInstant());
        System.out.println("ZonedDateTime:    "+date)
        System.out.println("With nice offset: "+dfFormatWithOffset.format(date))
        System.out.println("java.util.Date:   "+oldDate)
    }

    private static void testLocalDate(String dateStr) {
        println("\nTesting local date: $dateStr");
        final DateTimeFormatter dtfFormatYMD=
            DateTimeFormatter.ofPattern("yyyy-MM-dd").withZone(ZoneId.of("GMT"));

        LocalDate localDate=LocalDate.parse(dateStr, dtfFormatYMD);
        ZonedDateTime zdt=ZonedDateTime.of(
          localDate, LocalTime.of(0, 0, 0, 0), ZoneId.of("GMT")
        );
        debug(zdt)
        //System.out.println("Local date: "+dfFormatWithOffset.format(localDate));
    }

    private static void testZones(String[] zones) {
      println("\nTesting zones...")
      for (z in zones) {
        ZoneId zone=ZoneId.of(z);
        println("Zone: From $z got $zone");
      }
    }

    private static void testFormatZone() {
        println("\nTesting format zone...");
        final DateTimeFormatter dtfFormat=
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss a z");
        println(dtfFormat.format(ZonedDateTime.now()));
    }

    public static void main(String[] args) {
        testParseDifferentDates("2018-05-01T15:55:20Z")
        testParseDifferentDates("2018-05-01T15:55:20.723+0500")
        testParseDifferentDates("2018-05-01T15:55:20-0500")
        testParseDifferentDates("2018-11-01T13:30:00-0700")
        testParseDifferentDates("2018-05-01 15:55:20-05:15")
        testParseDifferentDates("2018-05-01T15:55:20 America/New_York")
        testParseDifferentDates("2019-01-09T00:00:00-0600")
        testParseDifferentDates("2019-01-09T00:00:00 -06:00")
        testParseDifferentDates("2019-01-01 00:00:01.001 -04:00")
        testZones("GMT", "America/Chicago");
        testLocalDate("2019-01-13");
        testLocalDate("2019-07-13");
        testCreateDate();
        testLocalDateToDate()
        testFormatZone();
    }
}
