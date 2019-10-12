package com.github.hannesknutsson.hungryboii.utilities.managers.abstractions;

import com.github.hannesknutsson.hungryboii.Application;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public abstract class MappingManager<T, Y> implements Manager {

    private static Logger LOG = LoggerFactory.getLogger(MappingManager.class);

    private ConcurrentHashMap<T, Y> registeredKeyValuePairs;

    public boolean register(T key, Y value) {
        boolean success = false;
        if (!Application.isRunning()) {
            getRegisteredObjects().put(key, value);
            success = true;
            LOG.debug("{}Manager registered: {}", getManagerType(), key.toString());
        } else {
            LOG.error("{}Manager can not register {} after application start: {}", getManagerType(), getManagerType(), key.toString());
        }
        return success;
    }

    public boolean unRegister(T toUnRegister) {
        boolean success = false;
        if (!Application.isRunning()) {
            Y removedObject = getRegisteredObjects().remove(toUnRegister);
            success = removedObject != null;
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

    protected Map<T, Y> getRegisteredObjects() {
        if (registeredKeyValuePairs == null) {
            registeredKeyValuePairs = new ConcurrentHashMap<>();
        }
        return registeredKeyValuePairs;
    }
}
