import java.text.*

// This parses both valid ISO versions of timestamp
SimpleDateFormat sdf2=new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssX", Locale.US)
println(sdf2.parse("2019-04-01T09:04:01Z"))
println(sdf2.parse("2019-04-01T09:04:01-0600"))