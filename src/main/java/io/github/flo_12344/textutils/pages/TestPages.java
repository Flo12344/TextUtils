package io.github.flo_12344.textutils.pages;

import com.hypixel.hytale.protocol.packets.interface_.CustomPageLifetime;
import com.hypixel.hytale.protocol.packets.interface_.CustomUICommand;
import com.hypixel.hytale.protocol.packets.interface_.CustomUICommandType;
import com.hypixel.hytale.server.core.entity.entities.player.pages.BasicCustomUIPage;
import com.hypixel.hytale.server.core.ui.builder.UICommandBuilder;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import io.github.flo_12344.textutils.commands.Test2dCommand;
import org.checkerframework.checker.nullness.compatqual.NonNullDecl;

import java.lang.reflect.Field;
import java.util.List;

public class TestPages extends BasicCustomUIPage {
    public TestPages(@NonNullDecl PlayerRef playerRef) {
        super(playerRef, CustomPageLifetime.CanDismiss);
    }

    @Override
    public void build(UICommandBuilder uiCommandBuilder) {
//        try {
//            Field privateField = UICommandBuilder.class.getDeclaredField("commands");
//            privateField.setAccessible(true);
//
//            List<CustomUICommand> commands = (List<CustomUICommand>) privateField.get(uiCommandBuilder);
//            commands.add(new CustomUICommand(CustomUICommandType.Append, (String) null, Test2dCommand.test, (String) null));
//            privateField.set(uiCommandBuilder, commands);
//        } catch (NoSuchFieldException e) {
//            throw new RuntimeException(e);
//        } catch (IllegalAccessException e) {
//            throw new RuntimeException(e);
//        }
    }

}
