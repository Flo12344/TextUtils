package io.github.flo_12344.textutils.system;

import com.hypixel.hytale.component.AddReason;
import com.hypixel.hytale.component.Holder;
import com.hypixel.hytale.component.RemoveReason;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.component.query.Query;
import com.hypixel.hytale.component.system.HolderSystem;
import com.hypixel.hytale.server.core.asset.type.model.config.Model;
import com.hypixel.hytale.server.core.asset.type.model.config.ModelAsset;
import com.hypixel.hytale.server.core.entity.UUIDComponent;
import com.hypixel.hytale.server.core.modules.entity.component.ModelComponent;
import com.hypixel.hytale.server.core.modules.entity.component.PersistentModel;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import io.github.flo_12344.textutils.component.TextUtils3DTextComponent;
import io.github.flo_12344.textutils.utils.TextManager;
import org.checkerframework.checker.nullness.compatqual.NonNullDecl;
import org.checkerframework.checker.nullness.compatqual.NullableDecl;

public class Text3dSpawned extends HolderSystem<EntityStore> {
    @Override
    public void onEntityAdd(@NonNullDecl Holder<EntityStore> holder, @NonNullDecl AddReason addReason, @NonNullDecl Store<EntityStore> store) {
        var textUtils = holder.getComponent(TextUtils3DTextComponent.getComponentType());
        var uuid = holder.getComponent(UUIDComponent.getComponentType()).getUuid();
        if (!TextManager.textUtilsEntity.containsKey(textUtils.getId())) {
            TextManager.textUtilsEntity.put(textUtils.getId(), uuid);
        }

        
    }

    @Override
    public void onEntityRemoved(@NonNullDecl Holder<EntityStore> holder, @NonNullDecl RemoveReason removeReason, @NonNullDecl Store<EntityStore> store) {
        var textUtils = holder.getComponent(TextUtils3DTextComponent.getComponentType());
        if (!TextManager.textUtilsEntity.containsKey(textUtils.getId())) {
            TextManager.textUtilsEntity.remove(textUtils.getId());
        }
    }

    @NullableDecl
    @Override
    public Query<EntityStore> getQuery() {
        return TextUtils3DTextComponent.getComponentType();
    }
}
