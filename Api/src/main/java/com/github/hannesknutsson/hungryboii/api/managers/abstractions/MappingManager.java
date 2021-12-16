package com.github.hannesknutsson.hungryboii.api.managers.abstractions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public abstract class MappingManager<T, Y> implements Manager {

    private static Logger LOG = LoggerFactory.getLogger(MappingManager.class);

    private ConcurrentHashMap<T, Y> registeredKeyValuePairs;

    public void register(T key, Y value) {
        getRegisteredObjects().put(key, value);
        LOG.debug("{}Manager registered: {}", getManagerType(), key.toString());
    }

    public void unRegister(T toUnRegister) {
        getRegisteredObjects().remove(toUnRegister);
        LOG.debug("{}Manager unregistered: {}", getManagerType(), toUnRegister.toString());
    }

    protected Map<T, Y> getRegisteredObjects() {
        if (registeredKeyValuePairs == null) {
            registeredKeyValuePairs = new ConcurrentHashMap<>();
        }
        return registeredKeyValuePairs;
    }
}
