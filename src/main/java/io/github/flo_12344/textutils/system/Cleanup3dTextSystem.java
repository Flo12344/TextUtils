package io.github.flo_12344.textutils.system;

import com.hypixel.hytale.component.*;
import com.hypixel.hytale.component.query.Query;
import com.hypixel.hytale.component.system.tick.EntityTickingSystem;
import com.hypixel.hytale.component.system.tick.TickingSystem;
import com.hypixel.hytale.math.vector.Vector3d;
import com.hypixel.hytale.math.vector.Vector3f;
import com.hypixel.hytale.server.core.asset.type.model.config.Model;
import com.hypixel.hytale.server.core.asset.type.model.config.ModelAsset;
import com.hypixel.hytale.server.core.modules.entity.component.Intangible;
import com.hypixel.hytale.server.core.modules.entity.component.ModelComponent;
import com.hypixel.hytale.server.core.modules.entity.component.PersistentModel;
import com.hypixel.hytale.server.core.modules.entity.component.TransformComponent;
import com.hypixel.hytale.server.core.modules.entity.tracker.NetworkId;
import com.hypixel.hytale.server.core.universe.world.World;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import io.github.flo_12344.textutils.component.TextUtils3DTextComponent;
import io.github.flo_12344.textutils.utils.TextManager;
import org.checkerframework.checker.nullness.compatqual.NonNullDecl;
import org.checkerframework.checker.nullness.compatqual.NullableDecl;

import java.util.Objects;

public class Cleanup3dTextSystem extends TickingSystem<EntityStore> {

    @Override
    public void tick(float v, int i, @NonNullDecl Store<EntityStore> store) {
        if(TextManager.to_delete.isEmpty()){
            return;
        }
        World world = store.getExternalData().getWorld();
        world.execute(()->{
            TextManager.to_delete.forEach(s -> {
                    var t =TextManager.textUtilsEntity.get(s);
                    store.getComponent(t, TextUtils3DTextComponent.getComponentType()).getText_entities().forEach(entityStoreRef -> {
                        store.removeEntity(entityStoreRef, RemoveReason.REMOVE);
                    });
                    store.removeEntity(TextManager.textUtilsEntity.get(s), RemoveReason.REMOVE);
                    TextManager.textUtilsEntity.remove(s);
            });
            TextManager.to_delete.clear();
        });
    }
}
