package io.github.flo_12344.textutils.utils;

import com.hypixel.hytale.component.*;
import com.hypixel.hytale.math.vector.Vector3d;
import com.hypixel.hytale.math.vector.Vector3f;
import com.hypixel.hytale.server.core.entity.UUIDComponent;
import com.hypixel.hytale.server.core.modules.entity.component.*;
import com.hypixel.hytale.server.core.modules.entity.tracker.NetworkId;
import com.hypixel.hytale.server.core.universe.world.World;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import io.github.flo_12344.textutils.component.TextUtils3DTextComponent;

import javax.annotation.Nonnull;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TextManager {
    public static ConcurrentHashMap<String, UUID> text3dUtilsEntity = new ConcurrentHashMap<>();

    public static void SpawnText3dEntity(Vector3d pos, Vector3f rot, @Nonnull World world, String text, String _label, String font, float size) {
        world.execute(() -> {
            Store<EntityStore> store = world.getEntityStore().getStore();
            Holder<EntityStore> holder = EntityStore.REGISTRY.newHolder();

            TransformComponent transform = new TransformComponent(pos, rot);

            holder.addComponent(TransformComponent.getComponentType(), transform);
            holder.addComponent(NetworkId.getComponentType(), new NetworkId(store.getExternalData().takeNextNetworkId()));
            var uuid = holder.ensureAndGetComponent(UUIDComponent.getComponentType());
            holder.addComponent(Intangible.getComponentType(), Intangible.INSTANCE);
            holder.addComponent(TextUtils3DTextComponent.getComponentType(),
                    new TextUtils3DTextComponent(font, text, _label, size));
            store.addEntity(holder, AddReason.SPAWN);
            TextManager.text3dUtilsEntity.put(_label, uuid.getUuid());
        });
    }

    public static void MoveText3dEntity(String id, World world, Store<EntityStore> store, Vector3d pos) {
        TransformText3dEntity(id, world, store, new Vector3f(), pos);
    }

    public static void RotateText3dEntity(String id, World world, Store<EntityStore> store, Vector3f rot) {
        TransformText3dEntity(id, world, store, rot, new Vector3d());
    }

    public static void TransformText3dEntity(String id, World world, Store<EntityStore> store, Vector3f rot, Vector3d pos) {
        var text_entity = world.getEntityRef(TextManager.text3dUtilsEntity.get(id));
        var textUtilsEntity = store.getComponent(text_entity, TextUtils3DTextComponent.getComponentType());
        TransformComponent transform = store.getComponent(text_entity, TransformComponent.getComponentType());
        if (transform == null)
            return;
        transform.getPosition().add(pos);
        transform.getRotation().add(rot);
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
}
