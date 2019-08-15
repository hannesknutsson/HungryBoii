package com.github.hannesknutsson.hungryboii.utilities.managers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class EventManager {

    private static Logger LOG = LoggerFactory.getLogger(EventManager.class);

    private static List<Object> eventList;

    public static void registerEventHandler(Object toRegister) {
        boolean success = false;
        if (!ApplicationManager.isRunning()) {
            getRegisteredEventHandlers().add(toRegister);
            LOG.info("Command registered: {}", toRegister);
        } else {
            LOG.error("Can not register event after application start: {}", toRegister);
        }
    }

    public static boolean unregisterEventHandler(Object toUnregister) {
        boolean success = false;
        if (!ApplicationManager.isRunning()) {
            success = getRegisteredEventHandlers().remove(toUnregister);
            if (success) {
                LOG.info("Event unregistered: {}", toUnregister);
            } else {
                LOG.error("Failed to unregister event: {}", toUnregister);
            }
        } else {
            LOG.error("Can not unregister event after application start: {}", toUnregister);
        }
        return success;
    }

    static List<Object> getRegisteredEventHandlers() {
        if (eventList == null) {
            eventList = new ArrayList<>();
        }
        return eventList;
    }
}
