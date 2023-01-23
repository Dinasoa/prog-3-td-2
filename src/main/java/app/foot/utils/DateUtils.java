package app.foot.utils;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Date;

public class DateUtils {
    public String parseDate(Instant toParse){
        SimpleDateFormat date = new SimpleDateFormat("yyyy-MM-dd");
        return date.format(Date.from(toParse));
//        return toParse.toString().substring(0,10);
    }
}
