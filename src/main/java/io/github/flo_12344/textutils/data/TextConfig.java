package io.github.flo_12344.textutils.data;

import com.google.gson.JsonObject;
import com.hypixel.hytale.codec.Codec;
import com.hypixel.hytale.codec.KeyedCodec;
import com.hypixel.hytale.codec.builder.BuilderCodec;
import com.hypixel.hytale.codec.codecs.array.ArrayCodec;
import com.hypixel.hytale.codec.codecs.map.MapCodec;
import com.hypixel.hytale.math.vector.Vector3d;
import com.hypixel.hytale.math.vector.Vector3f;
import com.hypixel.hytale.server.core.modules.interaction.interaction.config.data.ListCollector;
import com.nimbusds.jose.util.JSONArrayUtils;
import io.github.flo_12344.textutils.component.TextUtils3DTextComponent;

import java.text.ParseException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

public class TextConfig {
    public static final BuilderCodec<TextConfig> CODEC =
            BuilderCodec.builder(TextConfig.class, TextConfig::new)
                    .append(new KeyedCodec<>("Texts", new MapCodec<>(Text3dData.CODEC, HashMap::new)),
                            (textConfig, s) -> textConfig.textEntity.putAll(s),
                            (textConfig) -> textConfig.textEntity).add()
                    .build();

    private ConcurrentHashMap<String,Text3dData> textEntity = new ConcurrentHashMap<>();

    public void AddText(String id,Text3dData text){
        textEntity.put(id, text);
    }

    public void EditTextTransform(String id, Vector3d pos, Vector3f rot)
    {
        textEntity.get(id).position = pos;
        textEntity.get(id).rotation = rot;
    }

    public ConcurrentHashMap<String, Text3dData> getTextEntity() {
        return textEntity;
    }
}
