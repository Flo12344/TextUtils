package io.github.flo_12344.textutils.system;

import com.hypixel.hytale.builtin.hytalegenerator.LoggerUtil;
import com.hypixel.hytale.component.*;
import com.hypixel.hytale.component.query.Query;
import com.hypixel.hytale.component.system.tick.EntityTickingSystem;
import com.hypixel.hytale.logger.HytaleLogger;
import com.hypixel.hytale.math.vector.Vector3d;
import com.hypixel.hytale.math.vector.Vector3f;
import com.hypixel.hytale.server.core.asset.type.model.config.Model;
import com.hypixel.hytale.server.core.asset.type.model.config.ModelAsset;
import com.hypixel.hytale.server.core.entity.UUIDComponent;
import com.hypixel.hytale.server.core.modules.entity.component.*;
import com.hypixel.hytale.server.core.modules.entity.tracker.NetworkId;
import com.hypixel.hytale.server.core.universe.world.World;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import io.github.flo_12344.textutils.TextUtils;
import io.github.flo_12344.textutils.component.TextUtils3DTextComponent;
import io.github.flo_12344.textutils.utils.TextManager;
import org.checkerframework.checker.nullness.compatqual.NonNullDecl;
import org.checkerframework.checker.nullness.compatqual.NullableDecl;

import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.CopyOnWriteArrayList;

public class EditText3DSystem extends EntityTickingSystem<EntityStore> {

    @Override
    public void tick(float v, int i, @NonNullDecl ArchetypeChunk<EntityStore> archetypeChunk, @NonNullDecl Store<EntityStore> store, @NonNullDecl CommandBuffer<EntityStore> commandBuffer) {
        var textUtilsEntity = archetypeChunk.getComponent(i, TextUtils3DTextComponent.getComponentType());
        if (textUtilsEntity != null && textUtilsEntity.isEdited()) {
            TransformComponent transform = archetypeChunk.getComponent(i, TransformComponent.getComponentType());
            if (transform == null)
                return;
            RefreshText(
                    store.getExternalData().getWorld(),
                    textUtilsEntity.getText_entities(),
                    textUtilsEntity.getText(),
                    textUtilsEntity.getFont_name(),
                    transform.getPosition(),
                    transform.getRotation(),
                    commandBuffer
            );
            if (!TextManager.textUtilsEntity.containsKey(textUtilsEntity.getId())) {
                var uuid = commandBuffer.getComponent(archetypeChunk.getReferenceTo(i), UUIDComponent.getComponentType());
                TextManager.textUtilsEntity.put(textUtilsEntity.getId(), uuid.getUuid());
            }
            textUtilsEntity.setEdited(false);
        }
    }

    private void RefreshText(World world, CopyOnWriteArrayList<UUID> list, String text, String font, Vector3d pos, Vector3f rot, CommandBuffer<EntityStore> commandBuffer) {
        Store<EntityStore> store = world.getEntityStore().getStore();
        if (!list.isEmpty()) {
            for (var c : list) {
                commandBuffer.removeEntity(world.getEntityRef(c), RemoveReason.REMOVE);
            }
            list.clear();
        }
        int text_pos = 0;
        float width = 0.1f;
        var charArray = text.toCharArray();
        for (char c : charArray) {
            TransformComponent transform = new TransformComponent(pos, rot);
            Vector3d right = new Vector3d(1, 0, 0).rotateY(rot.y);
            Vector3d offset = right.scale((double) -charArray.length / 2 * width + text_pos * width);

            transform.getPosition().add(offset);

            Holder<EntityStore> holder = EntityStore.REGISTRY.newHolder();
            ModelAsset modelAsset = ModelAsset.getAssetMap().getAsset("Char" + Objects.toString((int) c));


            if (modelAsset == null) {
                continue;
            }
            Model model = Model.createScaledModel(modelAsset, 1.0f);

            var uuid = holder.ensureAndGetComponent(UUIDComponent.getComponentType()).getUuid();
            holder.addComponent(TransformComponent.getComponentType(), transform);
            holder.addComponent(PersistentModel.getComponentType(), new PersistentModel(model.toReference()));
            holder.addComponent(ModelComponent.getComponentType(), new ModelComponent(model));
            holder.addComponent(NetworkId.getComponentType(), new NetworkId(store.getExternalData().takeNextNetworkId()));
            holder.addComponent(Intangible.getComponentType(), Intangible.INSTANCE);
            commandBuffer.addEntity(holder, AddReason.SPAWN);
            list.add(uuid);
            text_pos++;
        }
    }

    @NullableDecl
    @Override
    public Query<EntityStore> getQuery() {
        return TextUtils3DTextComponent.getComponentType();
    }
}
