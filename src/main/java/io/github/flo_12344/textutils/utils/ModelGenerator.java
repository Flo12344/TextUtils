package io.github.flo_12344.textutils.utils;

import com.google.gson.JsonObject;
import com.hypixel.hytale.server.core.universe.Universe;

import java.util.HashMap;
import java.util.List;

public class ModelGenerator {
    static HashMap<String,String> loaded_models;
    static HashMap<String,String> pending_models;

    public boolean genCharaxterModel(String character, String world, String font, String color){
        var w = Universe.get().getWorld(world);
        if(w == null)
            return false;

        String to_load = character + "_" + color + "_" + font;
        JsonObject json = new JsonObject();

        pending_models.put(world,to_load);

        return true;
    }
}
