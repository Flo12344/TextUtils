package io.github.flo_12344.textutils.system;

import com.hypixel.hytale.component.*;
import com.hypixel.hytale.component.query.Query;
import com.hypixel.hytale.component.system.RefChangeSystem;
import com.hypixel.hytale.server.core.entity.UUIDComponent;
import com.hypixel.hytale.server.core.universe.Universe;
import com.hypixel.hytale.server.core.universe.world.World;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import io.github.flo_12344.textutils.component.TextUtils3DTextComponent;
import io.github.flo_12344.textutils.utils.TextManager;
import org.checkerframework.checker.nullness.compatqual.NonNullDecl;
import org.checkerframework.checker.nullness.compatqual.NullableDecl;

public class Text3dSystem extends RefChangeSystem<EntityStore, TextUtils3DTextComponent> {
    @NullableDecl
    @Override
    public Query<EntityStore> getQuery() {
        return TextUtils3DTextComponent.getComponentType();
    }

    @NonNullDecl
    @Override
    public ComponentType<EntityStore, TextUtils3DTextComponent> componentType() {
        return TextUtils3DTextComponent.getComponentType();
    }

    @Override
    public void onComponentAdded(@NonNullDecl Ref<EntityStore> ref, @NonNullDecl TextUtils3DTextComponent textComponent, @NonNullDecl Store<EntityStore> store, @NonNullDecl CommandBuffer<EntityStore> commandBuffer) {
        var uuid = commandBuffer.ensureAndGetComponent(ref, UUIDComponent.getComponentType());
        TextManager.textUtilsEntity.put(textComponent.getId(), uuid.getUuid());
        Universe.get().getLogger().atInfo().log("%s : %s", textComponent.getId(), uuid.getUuid().toString());
    }

    @Override
    public void onComponentSet(@NonNullDecl Ref<EntityStore> ref, @NullableDecl TextUtils3DTextComponent textComponent, @NonNullDecl TextUtils3DTextComponent t1, @NonNullDecl Store<EntityStore> store, @NonNullDecl CommandBuffer<EntityStore> commandBuffer) {
        var uuid = commandBuffer.ensureAndGetComponent(ref, UUIDComponent.getComponentType());
        TextManager.textUtilsEntity.put(textComponent.getId(), uuid.getUuid());
        Universe.get().getLogger().atInfo().log("%s : %s", textComponent.getId(), uuid.getUuid().toString());
    }

    @Override
    public void onComponentRemoved(@NonNullDecl Ref<EntityStore> ref, @NonNullDecl TextUtils3DTextComponent textComponent, @NonNullDecl Store<EntityStore> store, @NonNullDecl CommandBuffer<EntityStore> commandBuffer) {
        TextManager.textUtilsEntity.remove(textComponent.getId());
        World world = store.getExternalData().getWorld();
        textComponent.getText_entities().forEach(uuid -> {
            var t = world.getEntityRef(uuid);
            commandBuffer.removeEntity(t, RemoveReason.REMOVE);
        });
    }
}
