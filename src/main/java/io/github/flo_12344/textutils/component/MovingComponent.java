package io.github.flo_12344.textutils.component;

import com.hypixel.hytale.codec.builder.BuilderCodec;
import com.hypixel.hytale.component.Component;
import com.hypixel.hytale.component.ComponentType;
import com.hypixel.hytale.server.core.universe.world.World;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import org.checkerframework.checker.nullness.compatqual.NullableDecl;

import java.util.concurrent.Executor;

public class MovingComponent implements Component<EntityStore> {
    public static final BuilderCodec<MovingComponent> CODEC =
            BuilderCodec.builder(MovingComponent.class, MovingComponent::new)
                    .build();

    private static ComponentType<EntityStore, MovingComponent> TYPE;

    public static void init(ComponentType<EntityStore, MovingComponent> type) {
        TYPE = type;
    }

    public static ComponentType<EntityStore, MovingComponent> getComponentType() {
        return TYPE;
    }

    public MovingComponent() {
    }

    @NullableDecl
    @Override
    public Component<EntityStore> clone() {
        return new MovingComponent();
    }
}
