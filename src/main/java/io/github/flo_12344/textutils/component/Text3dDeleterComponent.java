package io.github.flo_12344.textutils.component;

import com.hypixel.hytale.codec.Codec;
import com.hypixel.hytale.codec.KeyedCodec;
import com.hypixel.hytale.codec.builder.BuilderCodec;
import com.hypixel.hytale.component.Component;
import com.hypixel.hytale.component.ComponentType;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import org.checkerframework.checker.nullness.compatqual.NullableDecl;

public class Text3dDeleterComponent implements Component<EntityStore> {
    public static final BuilderCodec<Text3dDeleterComponent> CODEC =
            BuilderCodec.builder(Text3dDeleterComponent.class, Text3dDeleterComponent::new)
                    .build();
    private static ComponentType<EntityStore, Text3dDeleterComponent> TYPE;

    public static void init(ComponentType<EntityStore, Text3dDeleterComponent> type) {
        TYPE = type;
    }

    public static ComponentType<EntityStore, Text3dDeleterComponent> getComponentType() {
        return TYPE;
    }

    @NullableDecl
    @Override
    public Component<EntityStore> clone() {
        return new Text3dDeleterComponent();
    }
}
