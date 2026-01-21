package io.github.flo_12344.textutils.commands;

import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.command.system.CommandContext;
import com.hypixel.hytale.server.core.command.system.arguments.system.RequiredArg;
import com.hypixel.hytale.server.core.command.system.arguments.types.ArgTypes;
import com.hypixel.hytale.server.core.command.system.basecommands.CommandBase;
import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.universe.Universe;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import io.github.flo_12344.textutils.TextUtils;
import io.github.flo_12344.textutils.component.MovingComponent;
import io.github.flo_12344.textutils.component.TextUtils3DTextComponent;
import io.github.flo_12344.textutils.utils.TextManager;
import org.checkerframework.checker.nullness.compatqual.NonNullDecl;

import java.util.Objects;

public class Move3dTextCommand extends CommandBase {
    RequiredArg<String> label;

    public Move3dTextCommand() {
        super("Move3dText", "");
        label = withRequiredArg("label", "", ArgTypes.STRING);
    }

    @Override
    protected void executeSync(@NonNullDecl CommandContext context) {
        var _label = label.get(context);
        if (!TextManager.textUtilsEntity.containsKey(_label)) {
            context.sendMessage(Message.raw(String.format("Unknown TextUtilsEntity label: %s", label)));
            return;
        }

//        Ref<EntityStore> entity = TextManager.textUtilsEntity.get(_label);


        if (!context.isPlayer())
            return;

        var player = context.senderAs(Player.class);
        var store = player.getWorld().getEntityStore().getStore();
       
        player.getWorld().execute(() -> {
            var entity = player.getWorld().getEntityRef(TextManager.textUtilsEntity.get(_label));
            if (store.getComponent(entity, MovingComponent.getComponentType()) == null)
                store.addComponent(entity, MovingComponent.getComponentType(), new MovingComponent());
            else
                store.removeComponent(entity, MovingComponent.getComponentType());
        });

    }
}
