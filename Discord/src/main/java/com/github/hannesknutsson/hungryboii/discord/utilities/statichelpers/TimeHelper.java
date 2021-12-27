package com.github.hannesknutsson.hungryboii.discord.utilities.statichelpers;

import com.github.hannesknutsson.hungryboii.discord.structure.enumerations.Weekday;

import java.util.Calendar;

import static com.github.hannesknutsson.hungryboii.discord.structure.enumerations.Weekday.*;

public class TimeHelper {

    public static boolean isWeekend() {
        return getDayOfWeek() == SATURDAY || getDayOfWeek() == SUNDAY;
    }

    private static Weekday getDayOfWeek() {
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
