package io.github.flo_12344.textutils.registry;

import io.github.flo_12344.textutils.TextUtils;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class TextUtilsHologramRegistry {
    private final ConcurrentHashMap<String, UUID> modStorage;
    private static TextUtilsHologramRegistry INSTANCE = new TextUtilsHologramRegistry();

    public TextUtilsHologramRegistry() {
        this.modStorage = new ConcurrentHashMap<>();
    }

    public static TextUtilsHologramRegistry get() {
        return INSTANCE;
    }

    public void onEntityAdded(String entityKey, UUID instanceId) {
        modStorage.put(entityKey, instanceId);
        TextUtilsRegistry.registerTextInstance(TextUtils.MODID, instanceId);
    }

    public UUID getUUID(String entityKey) {
        return modStorage.get(entityKey);
    }

    public boolean contains(String entityKey) {
        return modStorage.containsKey(entityKey) && modStorage.get(entityKey) != null;
    }

    public List<String> getKeys() {
        return modStorage.keySet().stream().toList();
    }

    public void onEntityRemoved(String instanceId) {
        if (!contains(instanceId))
            return;
        TextUtilsRegistry.unregisterTextInstance(TextUtils.MODID, modStorage.get(instanceId));
        modStorage.remove(instanceId);
    }
}
