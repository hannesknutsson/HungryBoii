package com.github.hannesknutsson.hungryboii.utilities.managers;

import java.util.ArrayList;
import java.util.List;

public class EventManager {

    private static List<Object> eventList;

    public static void registerEventHandler(Object toRegister) {
        getRegisteredEventHandlers().add(toRegister);
    }

    public static boolean unregisterEventHandler(Object toUnregister) {
        if (!ApplicationManager.isRunning()) {
            return getRegisteredEventHandlers().remove(toUnregister);
        }
        return false;
    }

    protected static List<Object> getRegisteredEventHandlers() {
        if (eventList == null) {
            eventList = new ArrayList<>();
        }
        return eventList;
    }
}
