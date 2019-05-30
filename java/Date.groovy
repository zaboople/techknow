import java.util.*;
import java.text.*;

println("\nGrabbing midnight in a different time zone")
Calendar cal = Calendar.getInstance();
setMidnight(cal)
System.out.println(cal.getTime())
cal.setTimeZone(TimeZone.getTimeZone("US/Pacific"))
setMidnight(cal)
System.out.println(cal.getTime())

static def setMidnight(Calendar cal) {
    cal.set(Calendar.HOUR_OF_DAY, 0)
    cal.set(Calendar.MINUTE, 0)
    cal.set(Calendar.SECOND, 0)
    cal.set(Calendar.MILLISECOND,0)
}