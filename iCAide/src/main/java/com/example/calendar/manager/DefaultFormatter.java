package com.example.calendar.manager;

import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import android.support.annotation.NonNull;

import com.example.calendar.manager.CalendarUnit.CalendarType;

/**
 * Created by Blaz Solar on 26/04/15.
 */
public class DefaultFormatter implements Formatter {

    private final DateTimeFormatter dayFormatter;
    private final DateTimeFormatter weekHeaderFormatter;
    private final DateTimeFormatter monthHeaderFormatter;

    public DefaultFormatter() {
        this("E", "yyyy'年第'w'周'", "yyyy'年'M'月'");
    }

    public DefaultFormatter(@NonNull String dayPattern, @NonNull String weekPattern, @NonNull String monthPattern) {
        dayFormatter = DateTimeFormat.forPattern(dayPattern);
        weekHeaderFormatter = DateTimeFormat.forPattern(weekPattern);
        monthHeaderFormatter = DateTimeFormat.forPattern(monthPattern);
    }

    @Override public String getDayName(@NonNull LocalDate date) {
        return date.toString(dayFormatter);
    }

    @Override public String getHeaderText(@CalendarType int type, @NonNull LocalDate from, @NonNull LocalDate to) {
        switch (type) {
            case CalendarUnit.TYPE_WEEK:
                return from.toString(weekHeaderFormatter);
            case CalendarUnit.TYPE_MONTH:
                return from.toString(monthHeaderFormatter);
            default:
                throw new IllegalStateException("Unknown calendar type");
        }
    }
}
