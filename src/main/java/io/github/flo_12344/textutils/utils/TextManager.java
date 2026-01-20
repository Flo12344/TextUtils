package io.github.flo_12344.textutils.utils;

import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.CopyOnWriteArrayList;

public class TextManager {
    public static ConcurrentHashMap<String, Ref<EntityStore>> textUtilsEntity = new ConcurrentHashMap<>();
    public static CopyOnWriteArrayList<String> to_delete = new CopyOnWriteArrayList<>();
}
