package io.github.flo_12344.textutils.commands;

import com.hypixel.hytale.component.AddReason;
import com.hypixel.hytale.component.Holder;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.math.vector.Vector3d;
import com.hypixel.hytale.math.vector.Vector3f;
import com.hypixel.hytale.math.vector.Vector3i;
import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.asset.type.model.config.Model;
import com.hypixel.hytale.server.core.asset.type.model.config.ModelAsset;
import com.hypixel.hytale.server.core.command.system.CommandContext;
import com.hypixel.hytale.server.core.command.system.arguments.system.OptionalArg;
import com.hypixel.hytale.server.core.command.system.arguments.system.RequiredArg;
import com.hypixel.hytale.server.core.command.system.arguments.types.ArgTypes;
import com.hypixel.hytale.server.core.command.system.basecommands.CommandBase;
import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.modules.entity.component.*;
import com.hypixel.hytale.server.core.modules.entity.tracker.NetworkId;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import io.github.flo_12344.textutils.TextUtils;
import io.github.flo_12344.textutils.component.TextUtils3DTextComponent;
import io.github.flo_12344.textutils.data.Text3dData;
import io.github.flo_12344.textutils.utils.TextManager;

import javax.annotation.Nonnull;
import java.util.UUID;

public class New3dTextCommand extends CommandBase {
    OptionalArg<Vector3f> rotation;
    OptionalArg<String> label;
    RequiredArg<Vector3i> position;
    RequiredArg<String> text;

    public New3dTextCommand() {
        super("NewText3D", "");
        position = withRequiredArg("position", "", ArgTypes.VECTOR3I);
        text = withRequiredArg("text", "", ArgTypes.STRING);
        rotation = withOptionalArg("rotation", "", ArgTypes.ROTATION);
        label = withOptionalArg("label", "", ArgTypes.STRING);
    }

    @Override
    protected void executeSync(@Nonnull CommandContext context) {

        if (!context.isPlayer())
            return;

        var player = context.senderAs(Player.class);
        Store<EntityStore> store = player.getWorld().getEntityStore().getStore();
        String content = text.get(context);
        if (content.startsWith("\""))
            content = content.substring(1, content.lastIndexOf("\""));
        final Vector3d pos = position.get(context).toVector3d();
        final Vector3f rot = context.getInput(rotation) == null ? new Vector3f() : rotation.get(context);
        final String _label = context.getInput(label) == null ? UUID.randomUUID().toString() : label.get(context);
        if (TextManager.textUtilsEntity.containsKey(_label)) {
            context.sendMessage(Message.raw(String.format("TextUtilsEntity with label %s already exist.", _label)));
            return;
        }

        TextManager.SpawnText(pos, rot, player.getWorld(), content, _label);
    }
}
