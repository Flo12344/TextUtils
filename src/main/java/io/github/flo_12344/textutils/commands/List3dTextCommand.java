package io.github.flo_12344.textutils.commands;

import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.command.system.CommandContext;
import com.hypixel.hytale.server.core.command.system.arguments.types.ArgTypes;
import com.hypixel.hytale.server.core.command.system.basecommands.CommandBase;
import io.github.flo_12344.textutils.component.TextUtils3DTextComponent;
import io.github.flo_12344.textutils.utils.TextManager;
import org.checkerframework.checker.nullness.compatqual.NonNullDecl;

import java.util.Iterator;

public class List3dTextCommand extends CommandBase {
    public List3dTextCommand() {
        super("List3dText", "");
    }

    @Override
    protected void executeSync(@NonNullDecl CommandContext commandContext) {
        commandContext.sendMessage(Message.raw("Available Text3D :"));
        for (Iterator<String> it = TextManager.textUtilsEntity.keys().asIterator(); it.hasNext(); ) {
            var t = it.next();
            commandContext.sendMessage(Message.raw(t));
        }
    }
}
