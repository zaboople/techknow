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

public class DateConversions {

    private final static DateTimeFormatter dtfFormatYMD=
        DateTimeFormatter.ofPattern("yyyy-MM-dd").withZone(ZoneId.of("GMT"));
    private final static DateTimeFormatter dtfFormatAMPM=
        DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss a");

    public static void main(String[] args) {
        ZonedDateTime zdt1 = fromDateString("2001-04-03");
        System.out.println(zdt1);
        System.out.println(toDate(zdt1));
        System.out.println(toNewDate(toDate(zdt1)));

        ZonedDateTime zd2 = fromDateTimeString("2001-04-03 15:18:03 -0600");
        System.out.println(zd2);
        System.out.println(toDate(zd2));
        System.out.println(toStartOfDay(toDate(zd2)));
    }

    private static Date toDate(ZonedDateTime zdt) {
        return Date.from(zdt.toInstant());
    }

    private static ZonedDateTime toNewDate(Date date) {
        return ZonedDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault());
    }

    private static ZonedDateTime toStartOfDay(Date date) {
        return ZonedDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault())
            .toLocalDate().atStartOfDay(ZoneId.systemDefault());
    }

    private static ZonedDateTime toStartOfDay(ZonedDateTime zdt) {
        return zdt.toLocalDate().atStartOfDay(ZoneId.systemDefault());
    }

    private static ZonedDateTime fromDateString(String dateStr){
        var formatr = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return LocalDate.parse(dateStr, formatr)
            .atStartOfDay(ZoneId.systemDefault());
    }

    private static ZonedDateTime fromDateTimeString(String dateStr){
        var formatr = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss XXXX");
        return ZonedDateTime.parse(dateStr, formatr);
    }
}
