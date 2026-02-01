package io.github.flo_12344.textutils.registry;

import io.github.flo_12344.textutils.utils.FontConfig;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class FontRegistry {
    private static ConcurrentHashMap<String, List<FontConfig>> fontMap = new ConcurrentHashMap<>();

}
