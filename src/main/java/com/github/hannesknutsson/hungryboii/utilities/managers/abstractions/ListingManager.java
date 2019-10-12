package com.github.hannesknutsson.hungryboii.utilities.managers.abstractions;

import com.github.hannesknutsson.hungryboii.Application;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public abstract class ListingManager<T> implements Manager {

    private static Logger LOG = LoggerFactory.getLogger(ListingManager.class);

    private List<T> registeredObjects;

    public boolean register(T toRegister) {
        boolean success = false;
        if (!Application.isRunning()) {
            getRegisteredObjects().add(toRegister);
            success = true;
            LOG.debug("{}Manager registered: {}", getManagerType(), toRegister.toString());
        } else {
            LOG.error("{}Manager can not register {} after application start: {}", getManagerType(), getManagerType(), toRegister.toString());
        }
        return success;
    }

    public boolean unRegister(T toUnRegister) {
        boolean success = false;
        if (!Application.isRunning()) {
            success = getRegisteredObjects().remove(toUnRegister);
            if (success) {
                LOG.debug("{}Manager unregistered: {}", getManagerType(), toUnRegister.toString());
            } else {
                LOG.error("{}Manager failed to unregister {}: {}", getManagerType(), getManagerType(), toUnRegister.toString());
            }
        } else {
            LOG.error("{}Manager can not unregister {} after application start: {}", getManagerType(), getManagerType(), toUnRegister);
        }
        return success;
    }

    protected List<T> getRegisteredObjects() {
        if (registeredObjects == null) {
            registeredObjects = new CopyOnWriteArrayList<>();
        }
        return registeredObjects;
    }
}
