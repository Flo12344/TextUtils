package io.github.flo_12344.textutils.system;

import com.hypixel.hytale.component.*;
import com.hypixel.hytale.component.query.Query;
import com.hypixel.hytale.component.system.RefChangeSystem;
import com.hypixel.hytale.server.core.entity.UUIDComponent;
import com.hypixel.hytale.server.core.universe.world.World;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import io.github.flo_12344.textutils.component.Text3dDeleterComponent;
import io.github.flo_12344.textutils.component.TextUtils3DTextComponent;
import io.github.flo_12344.textutils.utils.TextManager;
import org.checkerframework.checker.nullness.compatqual.NonNullDecl;
import org.checkerframework.checker.nullness.compatqual.NullableDecl;

public class DeleterText3dSystem extends RefChangeSystem<EntityStore, Text3dDeleterComponent> {
    @NullableDecl
    @Override
    public Query<EntityStore> getQuery() {
        return Query.and(TextUtils3DTextComponent.getComponentType(), Text3dDeleterComponent.getComponentType());
    }

    @NonNullDecl
    @Override
    public ComponentType<EntityStore, Text3dDeleterComponent> componentType() {
        return Text3dDeleterComponent.getComponentType();
    }

    @Override
    public void onComponentAdded(@NonNullDecl Ref<EntityStore> ref, @NonNullDecl Text3dDeleterComponent textComponent, @NonNullDecl Store<EntityStore> store, @NonNullDecl CommandBuffer<EntityStore> commandBuffer) {
        var comp = commandBuffer.getComponent(ref, TextUtils3DTextComponent.getComponentType());
        for (var c : comp.getText_entities()) {
            commandBuffer.removeEntity(store.getExternalData().getWorld().getEntityRef(c), RemoveReason.REMOVE);
        }
        TextManager.textUtilsEntity.remove(comp.getId());
        commandBuffer.removeEntity(ref, RemoveReason.REMOVE);
    }

    @Override
    public void onComponentSet(@NonNullDecl Ref<EntityStore> ref, @NullableDecl Text3dDeleterComponent textComponent, @NonNullDecl Text3dDeleterComponent t1, @NonNullDecl Store<EntityStore> store, @NonNullDecl CommandBuffer<EntityStore> commandBuffer) {

    }

    @Override
    public void onComponentRemoved(@NonNullDecl Ref<EntityStore> ref, @NonNullDecl Text3dDeleterComponent textComponent, @NonNullDecl Store<EntityStore> store, @NonNullDecl CommandBuffer<EntityStore> commandBuffer) {
    }
}
