package io.github.flo_12344.textutils.registry;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class TextUtilsRegistry {
    private static ConcurrentHashMap<String, List<UUID>> entityMap = new ConcurrentHashMap<>();

    public static void registerTextInstance(String modId, UUID entityInstanceId) {
        entityMap.computeIfAbsent(modId, _ -> new ArrayList<>()).add(entityInstanceId);
    }

    public static List<UUID> getTextInstances(String modId) {
        return entityMap.getOrDefault(modId, Collections.emptyList());
    }

    public static void unregisterTextInstance(String modId, UUID entityInstanceId) {
        entityMap.getOrDefault(modId, Collections.emptyList()).remove(entityInstanceId);
    }
}
