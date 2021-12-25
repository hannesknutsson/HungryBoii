package com.github.hannesknutsson.hungryboii.discord.utilities.statichelpers;

import com.github.hannesknutsson.hungryboii.discord.structure.enumerations.Weekday;
import com.github.hannesknutsson.hungryboii.discord.exceptions.TotallyBrokenDudeException;

import java.util.Calendar;

import static com.github.hannesknutsson.hungryboii.discord.structure.enumerations.Weekday.*;

public class TimeHelper {

    public static boolean isWeekend() throws TotallyBrokenDudeException {
        return getDayOfWeek() == SATURDAY || getDayOfWeek() == SUNDAY;
    }

    private static Weekday getDayOfWeek() throws TotallyBrokenDudeException {
        Calendar myDate = Calendar.getInstance();
        int dayNumber = myDate.get(Calendar.DAY_OF_WEEK);
        return getDayOfWeek(dayNumber);
    }

    private static Weekday getDayOfWeek(int dayNumber) throws TotallyBrokenDudeException {
        Weekday today;
        switch (dayNumber) {
            case 2:
                today = MONDAY;
                break;
            case 3:
                today = TUESDAY;
                break;
            case 4:
                today = WEDNESDAY;
                break;
            case 5:
                today = THURSDAY;
                break;
            case 6:
                today = FRIDAY;
                break;
            case 7:
                today = SATURDAY;
                break;
            case 1:
                today = SUNDAY;
                break;
            default:
                throw new TotallyBrokenDudeException();
        }
        return today;
    }
}
