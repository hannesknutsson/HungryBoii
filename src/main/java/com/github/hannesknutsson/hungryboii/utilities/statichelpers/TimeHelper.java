package com.github.hannesknutsson.hungryboii.utilities.statichelpers;

import java.util.Calendar;

public class TimeHelper {

    public static int getDayOfWeek() {
        Calendar myDate = Calendar.getInstance();
        return myDate.get(Calendar.DAY_OF_WEEK) - 2;
    }
}
