package io.github.flo_12344.textutils.system;

import com.hypixel.hytale.component.*;
import com.hypixel.hytale.component.query.Query;
import com.hypixel.hytale.component.system.tick.EntityTickingSystem;
import com.hypixel.hytale.math.vector.Vector3d;
import com.hypixel.hytale.math.vector.Vector3f;
import com.hypixel.hytale.server.core.asset.type.model.config.Model;
import com.hypixel.hytale.server.core.asset.type.model.config.ModelAsset;
import com.hypixel.hytale.server.core.entity.UUIDComponent;
import com.hypixel.hytale.server.core.modules.entity.component.*;
import com.hypixel.hytale.server.core.modules.entity.tracker.NetworkId;
import com.hypixel.hytale.server.core.universe.world.World;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import io.github.flo_12344.textutils.component.TextUtils3DTextComponent;
import io.github.flo_12344.textutils.utils.FontManager;
import org.checkerframework.checker.nullness.compatqual.NonNullDecl;
import org.checkerframework.checker.nullness.compatqual.NullableDecl;

import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArrayList;

public class EditText3DSystem extends EntityTickingSystem<EntityStore> {

    @Override
    public void tick(float v, int i, @NonNullDecl ArchetypeChunk<EntityStore> archetypeChunk, @NonNullDecl Store<EntityStore> store, @NonNullDecl CommandBuffer<EntityStore> commandBuffer) {
        var textUtilsEntity = archetypeChunk.getComponent(i, TextUtils3DTextComponent.getComponentType());
        if (textUtilsEntity != null && textUtilsEntity.isEdited()) {
            textUtilsEntity.setEdited(false);
            TransformComponent transform = archetypeChunk.getComponent(i, TransformComponent.getComponentType());
            if (transform == null)
                return;

            var list = textUtilsEntity.getText_entities();
            var world = store.getExternalData().getWorld();

            if (!list.isEmpty()) {
                for (var c : list) {
                    commandBuffer.removeEntity(world.getEntityRef(c), RemoveReason.REMOVE);
                }
                list.clear();
            }
            if (!textUtilsEntity.isVisible())
                return;
            int text_pos = 0;
            var charArray = textUtilsEntity.getText().toCharArray();
            float width;
            if (Objects.equals(textUtilsEntity.getFont_name(), "")) {
                width = 0.1f;
            } else {
                width = (float) FontManager.INSTANCE.getFontSettings(textUtilsEntity.getFont_name()).max_width / 64;
            }

            for (char c : charArray) {
                TransformComponent chara_transform = transform.clone();
                Vector3d right = new Vector3d(1, 0, 0).rotateY(transform.getRotation().y);
                Vector3d offset = right.scale((double) -charArray.length / 2 * width + text_pos * width);

                chara_transform.getPosition().add(offset);

                Holder<EntityStore> holder = EntityStore.REGISTRY.newHolder();
                ModelAsset modelAsset;
                if (Objects.equals(textUtilsEntity.getFont_name(), "")) {
                    modelAsset = ModelAsset.getAssetMap().getAsset("Char" + Objects.toString((int) c));
                } else {
                    modelAsset = ModelAsset.getAssetMap().getAsset(FontManager.getCharFileAsString(c, textUtilsEntity.getFont_name()));
                }


                if (modelAsset == null) {
                    continue;
                }
                Model model = Model.createScaledModel(modelAsset, 1.0f);

                var uuid = holder.ensureAndGetComponent(UUIDComponent.getComponentType()).getUuid();
                holder.addComponent(TransformComponent.getComponentType(), chara_transform);
                holder.addComponent(PersistentModel.getComponentType(), new PersistentModel(model.toReference()));
                holder.addComponent(ModelComponent.getComponentType(), new ModelComponent(model));
                holder.addComponent(NetworkId.getComponentType(), new NetworkId(store.getExternalData().takeNextNetworkId()));
                holder.addComponent(Intangible.getComponentType(), Intangible.INSTANCE);
                commandBuffer.addEntity(holder, AddReason.SPAWN);
                list.add(uuid);
                text_pos++;
            }


        }
    }


    @NullableDecl
    @Override
    public Query<EntityStore> getQuery() {
        return TextUtils3DTextComponent.getComponentType();
    }
}
