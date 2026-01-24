package io.github.flo_12344.textutils.utils;

import com.hypixel.hytale.component.*;
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

import javax.annotation.Nonnull;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

public class TextManager {
    public static ConcurrentHashMap<String, UUID> textUtilsEntity = new ConcurrentHashMap<>();

    public static void SpawnText(Vector3d pos, Vector3f rot, @Nonnull World world, String text, String _label) {
        world.execute(() -> {
            Store<EntityStore> store = world.getEntityStore().getStore();
            Holder<EntityStore> holder = EntityStore.REGISTRY.newHolder();
            
            TransformComponent transform = new TransformComponent(pos, rot);

            holder.addComponent(TransformComponent.getComponentType(), transform);
            holder.addComponent(NetworkId.getComponentType(), new NetworkId(store.getExternalData().takeNextNetworkId()));
            holder.ensureComponent(UUIDComponent.getComponentType());
            holder.addComponent(Intangible.getComponentType(), Intangible.INSTANCE);
//            TextManager.textUtilsEntity.put(_label, holder.ensureAndGetComponent(UUIDComponent.getComponentType()).getUuid());
            store.addComponent(store.addEntity(holder, AddReason.SPAWN),
                    TextUtils3DTextComponent.getComponentType(),
                    new TextUtils3DTextComponent("", text, _label));
//            if (!from_load)
//                TextUtils.INSTANCE.config.get().AddText(_label, new Text3dData(pos, rot, text, world.getName()));
        });
    }
}
