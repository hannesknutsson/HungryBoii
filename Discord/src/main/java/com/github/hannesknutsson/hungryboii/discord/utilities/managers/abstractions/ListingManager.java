package com.github.hannesknutsson.hungryboii.discord.utilities.managers.abstractions;

import com.github.hannesknutsson.hungryboii.discord.Application;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public abstract class ListingManager<T> implements Manager {

    private static Logger LOG = LoggerFactory.getLogger(ListingManager.class);

    private List<T> registeredObjects;

    public void register(T toRegister) {
        getRegisteredObjects().add(toRegister);
        LOG.debug("{}Manager registered: {}", getManagerType(), toRegister.toString());
    }

    public void unRegister(T toUnRegister) {
        getRegisteredObjects().remove(toUnRegister);
        LOG.debug("{}Manager unregistered: {}", getManagerType(), toUnRegister.toString());
    }

    protected List<T> getRegisteredObjects() {
        if (registeredObjects == null) {
            registeredObjects = new CopyOnWriteArrayList<>();
        }
        return registeredObjects;
    }
}
