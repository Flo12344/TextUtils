package io.github.flo_12344.textutils.system;

import com.hypixel.hytale.component.*;
import com.hypixel.hytale.component.query.Query;
import com.hypixel.hytale.component.system.tick.EntityTickingSystem;
import com.hypixel.hytale.math.vector.Vector3d;
import com.hypixel.hytale.server.core.asset.type.model.config.Model;
import com.hypixel.hytale.server.core.asset.type.model.config.ModelAsset;
import com.hypixel.hytale.server.core.modules.entity.component.Intangible;
import com.hypixel.hytale.server.core.modules.entity.component.ModelComponent;
import com.hypixel.hytale.server.core.modules.entity.component.PersistentModel;
import com.hypixel.hytale.server.core.modules.entity.component.TransformComponent;
import com.hypixel.hytale.server.core.modules.entity.tracker.NetworkId;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import io.github.flo_12344.textutils.component.MovingComponent;
import io.github.flo_12344.textutils.component.TextUtils3DTextComponent;
import org.checkerframework.checker.nullness.compatqual.NonNullDecl;
import org.checkerframework.checker.nullness.compatqual.NullableDecl;

import java.util.Objects;

public class Move3dTextUpdateSystem extends EntityTickingSystem<EntityStore> {
    @Override
    public void tick(float v, int i, @NonNullDecl ArchetypeChunk<EntityStore> archetypeChunk, @NonNullDecl Store<EntityStore> store, @NonNullDecl CommandBuffer<EntityStore> commandBuffer) {
        store.getExternalData().getWorld().execute(()->{
            var textUtilsEntity = archetypeChunk.getComponent(i, TextUtils3DTextComponent.getComponentType());
            TransformComponent transform = archetypeChunk.getComponent(i, TransformComponent.getComponentType());
            if(transform == null)
                return;
            int text_pos = 0;
            float width = 0.1f;
            var arr = textUtilsEntity.getText_entities();
            var size = arr.size();
            for (var c : arr) {

                Vector3d right = new Vector3d(1,0,0).rotateY(transform.getRotation().y);
                Vector3d offset = right.scale((double) -size / 2 * width + text_pos * width);
                TransformComponent t = store.getComponent(c, TransformComponent.getComponentType());
                t.setPosition(offset.add(transform.getPosition()));
                t.setRotation(transform.getRotation());
                text_pos++;
            }
        });
    }

    @NullableDecl
    @Override
    public Query<EntityStore> getQuery() {
        return Query.and(TextUtils3DTextComponent.getComponentType(), MovingComponent.getComponentType());
    }
}
