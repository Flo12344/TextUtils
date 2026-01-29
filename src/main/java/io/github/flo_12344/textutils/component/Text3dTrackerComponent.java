package io.github.flo_12344.textutils.component;

import com.hypixel.hytale.codec.Codec;
import com.hypixel.hytale.codec.KeyedCodec;
import com.hypixel.hytale.codec.builder.BuilderCodec;
import com.hypixel.hytale.component.Component;
import com.hypixel.hytale.component.ComponentType;
import com.hypixel.hytale.math.vector.Vector3i;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import org.checkerframework.checker.nullness.compatqual.NullableDecl;

import java.util.UUID;
import java.util.concurrent.CopyOnWriteArrayList;

public class Text3dTrackerComponent implements Component<EntityStore> {
    public static final BuilderCodec<Text3dTrackerComponent> CODEC =
            BuilderCodec.builder(Text3dTrackerComponent.class, Text3dTrackerComponent::new)
                    .append(new KeyedCodec<>("Tracked", Codec.UUID_STRING),
                            (text3dTrackerComponent, uuid) -> text3dTrackerComponent.tracked = uuid,
                            text3dTrackerComponent -> text3dTrackerComponent.tracked).add()
                    .append(new KeyedCodec<>("Offset", Vector3i.CODEC),
                            (text3dTrackerComponent, vector3i) -> text3dTrackerComponent.offset = vector3i,
                            text3dTrackerComponent -> text3dTrackerComponent.offset).add()
                    .build();
    private UUID tracked;
    private Vector3i offset;
    private TrackerType type;

    public enum TrackerType {FIXED, HEAD, NAMEPLATE}

    private static ComponentType<EntityStore, Text3dTrackerComponent> TYPE;

    public static ComponentType<EntityStore, Text3dTrackerComponent> getComponentType() {
        return TYPE;
    }

    public static void init(ComponentType<EntityStore, Text3dTrackerComponent> type) {
        TYPE = type;
    }

    public Text3dTrackerComponent() {
    }

    public Text3dTrackerComponent(UUID tracked, Vector3i offset) {
        this.offset = offset;
        this.tracked = tracked;
    }

    public UUID getTracked() {
        return tracked;
    }

    public void setTracked(UUID tracked) {
        this.tracked = tracked;
    }

    public Vector3i getOffset() {
        return offset;
    }

    public void setOffset(Vector3i offset) {
        this.offset = offset;
    }

    @NullableDecl
    @Override
    public Component<EntityStore> clone() {
        return new Text3dTrackerComponent(tracked, offset);
    }
}
