package io.github.flo_12344.textutils.commands;

import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.command.system.CommandContext;
import com.hypixel.hytale.server.core.command.system.arguments.system.RequiredArg;
import com.hypixel.hytale.server.core.command.system.arguments.types.ArgTypes;
import com.hypixel.hytale.server.core.command.system.basecommands.CommandBase;
import com.hypixel.hytale.server.core.entity.entities.Player;
import io.github.flo_12344.textutils.component.MovingComponent;
import io.github.flo_12344.textutils.utils.TextManager;
import org.checkerframework.checker.nullness.compatqual.NonNullDecl;

public class Remove3dTextCommand extends CommandBase {
    RequiredArg<String> label;

    public Remove3dTextCommand(){
        super("Remove3dText","");
        label = withRequiredArg("label", "", ArgTypes.STRING);
    }

    @Override
    protected void executeSync(@NonNullDecl CommandContext context) {
        var _label = label.get(context);
        if(!TextManager.textUtilsEntity.containsKey(_label))
        {
            context.sendMessage(Message.raw(String.format("Unknown TextUtilsEntity label: %s", label)));
            return;
        }
        TextManager.to_delete.add(_label);

    }
}
