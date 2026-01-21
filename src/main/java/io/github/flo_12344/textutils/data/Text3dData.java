package io.github.flo_12344.textutils.data;

import com.hypixel.hytale.codec.Codec;
import com.hypixel.hytale.codec.KeyedCodec;
import com.hypixel.hytale.codec.builder.BuilderCodec;
import com.hypixel.hytale.math.vector.Vector3d;
import com.hypixel.hytale.math.vector.Vector3f;

public class Text3dData {
    public static final BuilderCodec<Text3dData> CODEC = BuilderCodec.builder(Text3dData.class, Text3dData::new)
            .append(new KeyedCodec<>("Position", Vector3d.CODEC),
                    (text3dData, vector3, extraInfo) -> text3dData.position = vector3,
                    (text3dData, extraInfo) -> text3dData.position).add()
            .append(new KeyedCodec<>("Rotation", Vector3f.CODEC),
                    (text3dData, vector3f, extraInfo) -> text3dData.rotation = vector3f,
                    (text3dData, extraInfo) -> text3dData.rotation).add()
            .append(new KeyedCodec<>("Text", Codec.STRING),
                    (text3dData, text, extraInfo) -> text3dData.text = text,
                    (text3dData, extraInfo) -> text3dData.text).add()
            .append(new KeyedCodec<>("World", Codec.STRING),
                    (text3dData, text, extraInfo) -> text3dData.world = text,
                    (text3dData, extraInfo) -> text3dData.world).add()
            .build();

    Vector3d position;
    Vector3f rotation;
    String text;
    String world;

    private Text3dData(){}

    public Text3dData(Vector3d position, Vector3f rotation, String text, String world) {
        this.position = position;
        this.rotation = rotation;
        this.text = text;
        this.world = world;
    }

    public Vector3d getPosition() {
        return position;
    }

    public Vector3f getRotation() {
        return rotation;
    }

    public String getText() {
        return text;
    }

    public String getWorld() {
        return world;
    }
}
