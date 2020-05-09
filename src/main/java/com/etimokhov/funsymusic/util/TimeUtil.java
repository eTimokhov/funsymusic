package com.etimokhov.funsymusic.util;

import org.ocpsoft.prettytime.PrettyTime;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Locale;

@Component
public class TimeUtil {
    private static final PrettyTime prettyTime = new PrettyTime(new Locale("en"));

    public String convertToPrettyTime(Date date) {
        return prettyTime.format(date);
    }

}
