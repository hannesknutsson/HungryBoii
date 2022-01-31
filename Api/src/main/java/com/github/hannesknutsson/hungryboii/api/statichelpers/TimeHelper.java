package com.github.hannesknutsson.hungryboii.api.statichelpers;

import com.github.hannesknutsson.hungryboii.api.enumerations.Weekday;

import java.util.Calendar;

import static com.github.hannesknutsson.hungryboii.api.enumerations.Weekday.FRIDAY;
import static com.github.hannesknutsson.hungryboii.api.enumerations.Weekday.MONDAY;
import static com.github.hannesknutsson.hungryboii.api.enumerations.Weekday.NOT_A_WEEKDAY;
import static com.github.hannesknutsson.hungryboii.api.enumerations.Weekday.SATURDAY;
import static com.github.hannesknutsson.hungryboii.api.enumerations.Weekday.SUNDAY;
import static com.github.hannesknutsson.hungryboii.api.enumerations.Weekday.THURSDAY;
import static com.github.hannesknutsson.hungryboii.api.enumerations.Weekday.TUESDAY;
import static com.github.hannesknutsson.hungryboii.api.enumerations.Weekday.WEDNESDAY;


public class TimeHelper {

    public static Weekday getDayOfWeek() {
        Calendar myDate = Calendar.getInstance();
        int dayNumber = myDate.get(Calendar.DAY_OF_WEEK);
        return getDayOfWeek(dayNumber);
    }

    private static Weekday getDayOfWeek(int dayNumber) {
        return switch (dayNumber) {
            case 2 -> MONDAY;
            case 3 -> TUESDAY;
            case 4 -> WEDNESDAY;
            case 5 -> THURSDAY;
            case 6 -> FRIDAY;
            case 7 -> SATURDAY;
            case 1 -> SUNDAY;
            default -> NOT_A_WEEKDAY;
        };
    }
}
