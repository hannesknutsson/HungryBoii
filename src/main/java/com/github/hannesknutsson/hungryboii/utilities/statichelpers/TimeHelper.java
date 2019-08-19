package com.github.hannesknutsson.hungryboii.utilities.statichelpers;

import com.github.hannesknutsson.hungryboii.structure.enumerations.Weekday;
import com.github.hannesknutsson.hungryboii.structure.exceptions.ParsingOutdated;
import com.github.hannesknutsson.hungryboii.structure.exceptions.TotallyBrokenDudeException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Calendar;

import static com.github.hannesknutsson.hungryboii.structure.enumerations.Weekday.*;

public class TimeHelper {

    static Logger LOG = LoggerFactory.getLogger(TimeHelper.class);

    public static Weekday getDayOfWeek() throws TotallyBrokenDudeException {
        Calendar myDate = Calendar.getInstance();
        int dayNumber = myDate.get(Calendar.DAY_OF_WEEK) - 2; //Because weeks start on mondays, not sundays. And because arrays start on 0, not 1
        return getDayOfWeek(dayNumber);
    }

    public static Weekday getDayOfWeek(int dayNumber) throws TotallyBrokenDudeException {
        Weekday today;
        switch (dayNumber) {
            case 0:
                today = MONDAY;
                break;
            case 1:
                today = TUESDAY;
                break;
            case 2:
                today = WEDNESDAY;
                break;
            case 3:
                today = THURSDAY;
                break;
            case 4:
                today = FRIDAY;
                break;
            case 5:
                today = SATURDAY;
                break;
            case 6:
                today = SUNDAY;
                break;
            default:
                LOG.error("Failed to determine what day today is??? Computer is supposedly drunk.");
                throw new TotallyBrokenDudeException();
        }
        return today;
    }

    public static Weekday parseStringToWeekday(String toParse) throws ParsingOutdated {
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
