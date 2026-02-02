package io.github.flo_12344.textutils.component;

import com.hypixel.hytale.codec.builder.BuilderCodec;
import com.hypixel.hytale.component.Component;
import com.hypixel.hytale.component.ComponentType;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import org.checkerframework.checker.nullness.compatqual.NullableDecl;

import java.util.UUID;

public class CharacterComponent implements Component<EntityStore> {
    private static ComponentType<EntityStore, CharacterComponent> TYPE;

    public static void init(ComponentType<EntityStore, CharacterComponent> type) {
        TYPE = type;
    }

    public UUID parent = null;

    public static ComponentType<EntityStore, CharacterComponent> getComponentType() {
        return TYPE;
    }

    public CharacterComponent() {
    }

    public CharacterComponent(UUID parent) {
        this.parent = parent;
    }

    @NullableDecl
    @Override
    public Component<EntityStore> clone() {
        return new CharacterComponent(parent);
    }

}
