package io.github.flo_12344.textutils.commands;

import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.asset.type.model.config.Model;
import com.hypixel.hytale.server.core.asset.type.model.config.ModelAsset;
import com.hypixel.hytale.server.core.command.system.CommandContext;
import com.hypixel.hytale.server.core.command.system.arguments.system.RequiredArg;
import com.hypixel.hytale.server.core.command.system.arguments.types.ArgTypes;
import com.hypixel.hytale.server.core.command.system.basecommands.AbstractPlayerCommand;
import com.hypixel.hytale.server.core.command.system.basecommands.CommandBase;
import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.modules.entity.component.BoundingBox;
import com.hypixel.hytale.server.core.modules.entity.component.ModelComponent;
import com.hypixel.hytale.server.core.modules.entity.component.PersistentModel;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.Universe;
import com.hypixel.hytale.server.core.universe.world.World;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import io.github.flo_12344.textutils.TextUtils;
import io.github.flo_12344.textutils.component.MovingComponent;
import io.github.flo_12344.textutils.component.TextUtils3DTextComponent;
import io.github.flo_12344.textutils.utils.TextManager;
import org.checkerframework.checker.nullness.compatqual.NonNullDecl;

import java.util.Objects;

public class Move3dTextCommand extends AbstractPlayerCommand {
    RequiredArg<String> label;

    public Move3dTextCommand() {
        super("Move3dText", "");
        label = withRequiredArg("label", "", ArgTypes.STRING);
    }

    @Override
    protected void execute(@NonNullDecl CommandContext context, @NonNullDecl Store<EntityStore> store, @NonNullDecl Ref<EntityStore> ref, @NonNullDecl PlayerRef playerRef, @NonNullDecl World world) {
        var _label = label.get(context);
        if (!TextManager.textUtilsEntity.containsKey(_label)) {
            world.sendMessage(Message.raw(String.format("Unknown TextUtilsEntity label: %s", _label)));
            return;
        }
        var entity = world.getEntityRef(TextManager.textUtilsEntity.get(_label));
        ModelAsset modelAsset = ModelAsset.getAssetMap().getAsset("Fixed_Hologram");
        if (modelAsset == null) {
            return;
        }
        Model model = Model.createScaledModel(modelAsset, 1f);
        world.execute(() -> {
            if (store.getComponent(entity, MovingComponent.getComponentType()) == null) {

//                store.addComponent(entity, PersistentModel.getComponentType(), new PersistentModel(model.toReference()));
//                store.addComponent(entity, ModelComponent.getComponentType(), new ModelComponent(model));
                store.addComponent(entity, MovingComponent.getComponentType(), new MovingComponent());
            } else {
//                store.removeComponent(entity, PersistentModel.getComponentType());
//                store.removeComponent(entity, ModelComponent.getComponentType());
                store.removeComponent(entity, MovingComponent.getComponentType());
            }
        });

    }

}
