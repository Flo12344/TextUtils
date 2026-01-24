package io.github.flo_12344.textutils.system;

import com.hypixel.hytale.component.*;
import com.hypixel.hytale.component.query.Query;
import com.hypixel.hytale.component.system.RefChangeSystem;
import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.asset.type.model.config.Model;
import com.hypixel.hytale.server.core.asset.type.model.config.ModelAsset;
import com.hypixel.hytale.server.core.modules.entity.component.BoundingBox;
import com.hypixel.hytale.server.core.modules.entity.component.ModelComponent;
import com.hypixel.hytale.server.core.modules.entity.component.PersistentModel;
import com.hypixel.hytale.server.core.modules.entity.component.TransformComponent;
import com.hypixel.hytale.server.core.modules.entity.tracker.EntityTrackerSystems;
import com.hypixel.hytale.server.core.universe.Universe;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import io.github.flo_12344.textutils.TextUtils;
import io.github.flo_12344.textutils.component.MovingComponent;
import io.github.flo_12344.textutils.component.TextUtils3DTextComponent;
import org.checkerframework.checker.nullness.compatqual.NonNullDecl;
import org.checkerframework.checker.nullness.compatqual.NullableDecl;

public class SetupMove3dTextSystem extends RefChangeSystem<EntityStore, MovingComponent> {
    @NullableDecl
    @Override
    public Query<EntityStore> getQuery() {
        return TextUtils3DTextComponent.getComponentType();
    }

    @NonNullDecl
    @Override
    public ComponentType<EntityStore, MovingComponent> componentType() {
        return MovingComponent.getComponentType();
    }

    @Override
    public void onComponentAdded(@NonNullDecl Ref<EntityStore> ref, @NonNullDecl MovingComponent movingComponent, @NonNullDecl Store<EntityStore> store, @NonNullDecl CommandBuffer<EntityStore> commandBuffer) {
        ModelAsset modelAsset = ModelAsset.getAssetMap().getAsset("Fixed_Hologram");
        Model model = Model.createScaledModel(modelAsset, 1f);
//        commandBuffer.addComponent(ref, PersistentModel.getComponentType(), new PersistentModel(model.toReference()));
        commandBuffer.addComponent(ref, ModelComponent.getComponentType(), new ModelComponent(model));
        Universe.get().sendMessage(Message.raw("ADDED"));
    }

    @Override
    public void onComponentSet(@NonNullDecl Ref<EntityStore> ref, @NullableDecl MovingComponent movingComponent, @NonNullDecl MovingComponent t1, @NonNullDecl Store<EntityStore> store, @NonNullDecl CommandBuffer<EntityStore> commandBuffer) {
    }

    @Override
    public void onComponentRemoved(@NonNullDecl Ref<EntityStore> ref, @NonNullDecl MovingComponent movingComponent, @NonNullDecl Store<EntityStore> store, @NonNullDecl CommandBuffer<EntityStore> commandBuffer) {
//        commandBuffer.removeComponent(ref, PersistentModel.getComponentType());
        commandBuffer.removeComponent(ref, ModelComponent.getComponentType());
        Universe.get().sendMessage(Message.raw("REMOVED"));
    }
}
