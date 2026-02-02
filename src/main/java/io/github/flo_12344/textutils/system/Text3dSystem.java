package io.github.flo_12344.textutils.system;

import com.hypixel.hytale.component.*;
import com.hypixel.hytale.component.query.Query;
import com.hypixel.hytale.component.system.HolderSystem;
import com.hypixel.hytale.component.system.RefChangeSystem;
import com.hypixel.hytale.component.system.tick.EntityTickingSystem;
import com.hypixel.hytale.math.util.TrigMathUtil;
import com.hypixel.hytale.math.vector.Vector3d;
import com.hypixel.hytale.server.core.asset.type.model.config.*;
import com.hypixel.hytale.server.core.entity.UUIDComponent;
import com.hypixel.hytale.server.core.modules.entity.component.*;
import com.hypixel.hytale.server.core.modules.entity.tracker.NetworkId;
import com.hypixel.hytale.server.core.universe.Universe;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import io.github.flo_12344.textutils.component.Text3dDeleterComponent;
import io.github.flo_12344.textutils.component.Text3dTrackerComponent;
import io.github.flo_12344.textutils.component.TextUtils3DTextComponent;
import io.github.flo_12344.textutils.registry.TextUtilsHologramRegistry;
import io.github.flo_12344.textutils.utils.FontManager;
import io.github.flo_12344.textutils.utils.FormattingUtils;
import io.github.flo_12344.textutils.utils.TextManager;
import org.checkerframework.checker.nullness.compatqual.NonNullDecl;
import org.checkerframework.checker.nullness.compatqual.NullableDecl;

import java.util.Objects;
import java.util.logging.Level;

public class Text3dSystem {
    public static class DeleterText3dSystem extends RefChangeSystem<EntityStore, Text3dDeleterComponent> {
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
            TextUtilsHologramRegistry.get().onEntityRemoved(comp.getId());
            commandBuffer.removeEntity(ref, RemoveReason.REMOVE);
        }

        @Override
        public void onComponentSet(@NonNullDecl Ref<EntityStore> ref, @NullableDecl Text3dDeleterComponent textComponent, @NonNullDecl Text3dDeleterComponent t1, @NonNullDecl Store<EntityStore> store, @NonNullDecl CommandBuffer<EntityStore> commandBuffer) {

        }

        @Override
        public void onComponentRemoved(@NonNullDecl Ref<EntityStore> ref, @NonNullDecl Text3dDeleterComponent textComponent, @NonNullDecl Store<EntityStore> store, @NonNullDecl CommandBuffer<EntityStore> commandBuffer) {
        }
    }

    public static class Text3dSpawned extends HolderSystem<EntityStore> {
        @Override
        public void onEntityAdd(@NonNullDecl Holder<EntityStore> holder, @NonNullDecl AddReason addReason, @NonNullDecl Store<EntityStore> store) {
            var textUtils = holder.getComponent(TextUtils3DTextComponent.getComponentType());
            var uuid = holder.getComponent(UUIDComponent.getComponentType()).getUuid();
            if (!TextUtilsHologramRegistry.get().contains(textUtils.getId())) {
                TextUtilsHologramRegistry.get().onEntityAdded(textUtils.getId(), uuid);
            }


        }

        @Override
        public void onEntityRemoved(@NonNullDecl Holder<EntityStore> holder, @NonNullDecl RemoveReason removeReason, @NonNullDecl Store<EntityStore> store) {
            var textUtils = holder.getComponent(TextUtils3DTextComponent.getComponentType());
            if (TextUtilsHologramRegistry.get().contains(textUtils.getId())) {
                TextUtilsHologramRegistry.get().onEntityRemoved(textUtils.getId());
            }
        }

        @NullableDecl
        @Override
        public Query<EntityStore> getQuery() {
            return TextUtils3DTextComponent.getComponentType();
        }
    }

    public static class EditText3DSystem extends EntityTickingSystem<EntityStore> {

        @Override
        public void tick(float v, int i, @NonNullDecl ArchetypeChunk<EntityStore> archetypeChunk, @NonNullDecl Store<EntityStore> store, @NonNullDecl CommandBuffer<EntityStore> cbf) {
            var textUtilsEntity = archetypeChunk.getComponent(i, TextUtils3DTextComponent.getComponentType());
            if (textUtilsEntity == null && !textUtilsEntity.isEdited()) {
                return;
            }
            textUtilsEntity.setEdited(false);
            TransformComponent transform = archetypeChunk.getComponent(i, TransformComponent.getComponentType());
            if (transform == null)
                return;

            var list = textUtilsEntity.getText_entities();
            var world = store.getExternalData().getWorld();

            if (!list.isEmpty()) {
                for (var c : list) {
                    var ref = world.getEntityRef(c);
                    if (ref != null)
                        cbf.removeEntity(ref, RemoveReason.REMOVE);
                }
                list.clear();
            }
            if (!textUtilsEntity.isVisible())
                return;
            int text_pos = 0;

            var formated = FormattingUtils.parseFormattedText(textUtilsEntity.getText());

            float width = (float) FontManager.INSTANCE.getFontSettings(textUtilsEntity.getFont_name()).max_width / 64;
            width *= textUtilsEntity.getSize();

            int text_len = formated.stream()
                    .mapToInt(str -> str.getText().length())
                    .sum();

            for (var str : formated) {
                for (char c : str.getText().toCharArray()) {
                    TransformComponent chara_transform = transform.clone();
                    Vector3d right = new Vector3d(1, 0, 0).rotateY(transform.getRotation().y);
                    Vector3d offset = right.scale((double) -text_len / 2 * width + text_pos * width);

                    chara_transform.getPosition().add(offset);

                    Holder<EntityStore> holder = EntityStore.REGISTRY.newHolder();
                    ModelAsset modelAsset = ModelAsset.getAssetMap().getAsset(FontManager.getCharFileAsString(c, textUtilsEntity.getFont_name()));


                    if (modelAsset == null) {
                        Universe.get().getLogger().at(Level.WARNING).log("Missing character for %s", c);
                        continue;
                    }
                    Model model = new Model(modelAsset.getId(), textUtilsEntity.getSize(), modelAsset.generateRandomAttachmentIds(), modelAsset.getDefaultAttachments(), modelAsset.getBoundingBox(), modelAsset.getModel(), modelAsset.getTexture(), FormattingUtils.getGradientSet(str.getColor()), FormattingUtils.getGradientId(str.getColor()), modelAsset.getEyeHeight(), modelAsset.getCrouchOffset(), modelAsset.getAnimationSetMap(), modelAsset.getCamera()
                            , modelAsset.getLight(), modelAsset.getParticles(), modelAsset.getTrails(), modelAsset.getPhysicsValues(), modelAsset.getDetailBoxes(), modelAsset.getPhobia(), modelAsset.getPhobiaModelAssetId());
                    var uuid = holder.ensureAndGetComponent(UUIDComponent.getComponentType()).getUuid();
                    holder.addComponent(TransformComponent.getComponentType(), chara_transform);
                    holder.addComponent(PersistentModel.getComponentType(), new PersistentModel(model.toReference()));
                    holder.addComponent(ModelComponent.getComponentType(), new ModelComponent(model));
                    holder.addComponent(NetworkId.getComponentType(), new NetworkId(store.getExternalData().takeNextNetworkId()));
                    holder.addComponent(Intangible.getComponentType(), Intangible.INSTANCE);

                    cbf.addEntity(holder, AddReason.SPAWN);


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

    public static class TrackerText3DSystem extends EntityTickingSystem<EntityStore> {

        @Override
        public void tick(float v, int i, @NonNullDecl ArchetypeChunk<EntityStore> archetypeChunk, @NonNullDecl Store<EntityStore> store, @NonNullDecl CommandBuffer<EntityStore> cbf) {
            var ref = archetypeChunk.getReferenceTo(i);
            var textUtilsEntity = cbf.getComponent(ref, TextUtils3DTextComponent.getComponentType());
            var tracker = archetypeChunk.getComponent(i, Text3dTrackerComponent.getComponentType());
            TransformComponent transform = archetypeChunk.getComponent(i, TransformComponent.getComponentType());
            if (transform == null)
                return;

            var target = store.getExternalData().getWorld().getEntityRef(tracker.getTracked());
            if (target == null) {
                cbf.removeComponent(ref, Text3dTrackerComponent.getComponentType());
                return;
            }
            TransformComponent target_trsf = cbf.getComponent(target, TransformComponent.getComponentType());
            HeadRotation head = cbf.getComponent(target, HeadRotation.getComponentType());

            if (head != null) {
                transform.setRotation(head.getRotation());
                transform.getRotation().add(0, TrigMathUtil.PI, 0);
            }

            transform.setPosition(target_trsf.getPosition());
            transform.getPosition().add(tracker.getOffset());
            int text_pos = 0;
            float width;
            if (Objects.equals(textUtilsEntity.getFont_name(), "")) {
                width = 0.1f;
            } else {
                width = (float) FontManager.INSTANCE.getFontSettings(textUtilsEntity.getFont_name()).max_width / 64;
            }
            var arr = textUtilsEntity.getText_entities();
            var size = arr.size();
            for (var uuid : arr) {
                var c = store.getExternalData().getRefFromUUID(uuid);
                Vector3d right = new Vector3d(1, 0, 0).rotateY(transform.getRotation().y);
                Vector3d offset = right.scale((double) -size / 2 * width + text_pos * width);
                TransformComponent t = store.getComponent(c, TransformComponent.getComponentType());
                t.setPosition(offset.add(transform.getPosition()));
                t.setRotation(transform.getRotation());
                text_pos++;
            }


        }


        @NullableDecl
        @Override
        public Query<EntityStore> getQuery() {
            return Text3dTrackerComponent.getComponentType();
        }
    }
}
