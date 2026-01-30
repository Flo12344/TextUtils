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

}
