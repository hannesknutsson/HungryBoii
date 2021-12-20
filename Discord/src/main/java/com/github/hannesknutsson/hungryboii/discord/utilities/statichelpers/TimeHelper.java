package com.github.hannesknutsson.hungryboii.discord.utilities.statichelpers;

import com.github.hannesknutsson.hungryboii.discord.structure.enumerations.Weekday;
import com.github.hannesknutsson.hungryboii.discord.exceptions.TotallyBrokenDudeException;

import java.util.Calendar;

import static com.github.hannesknutsson.hungryboii.discord.structure.enumerations.Weekday.*;

public class TimeHelper {

    public static boolean isWeekend() throws TotallyBrokenDudeException {
        return getDayOfWeek() == SATURDAY || getDayOfWeek() == SUNDAY;
    }

    public static Weekday getDayOfWeek() throws TotallyBrokenDudeException {
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

    public static Weekday parseStringToWeekday(String toParse) {
        toParse = toParse.trim().toLowerCase();
        Weekday actualDay;
        switch (toParse) {
            case "måndag":
            case "monday":
                actualDay = MONDAY;
                break;
            case "tisdag":
            case "tuesday":
                actualDay = TUESDAY;
                break;
            case "onsdag":
            case "wednesday":
                actualDay = WEDNESDAY;
                break;
            case "torsdag":
            case "thursday":
                actualDay = THURSDAY;
                break;
            case "fredag":
            case "friday":
                actualDay = FRIDAY;
                break;
            case "lördag":
            case "saturday":
                actualDay = SATURDAY;
                break;
            case "söndag":
            case "sunday":
                actualDay = SUNDAY;
                break;
            default:
                actualDay = NOT_A_WEEKDAY;
        }
        return actualDay;
    }


}
