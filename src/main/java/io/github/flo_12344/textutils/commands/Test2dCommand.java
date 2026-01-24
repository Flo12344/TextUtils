package io.github.flo_12344.textutils.commands;

import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.server.core.command.system.CommandContext;
import com.hypixel.hytale.server.core.command.system.basecommands.AbstractPlayerCommand;
import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.world.World;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import io.github.flo_12344.textutils.pages.TestPages;
import org.checkerframework.checker.nullness.compatqual.NonNullDecl;

public class Test2dCommand extends AbstractPlayerCommand {
    public static String test = "Group {\n" +
            "    Anchor: (Width: 400, Height: 200);\n" +
            "    Background: #1a1a2e(0.95);\n" +
            "    LayoutMode: Top;\n" +
            "    Padding: (Full: 20);\n" +
            "\n" +
            "    Label #Title {\n" +
            "        Text: \"Tutorial Level 1\";\n" +
            "        Anchor: (Height: 40);\n" +
            "        Style: (FontSize: 24, TextColor: #ffffff, Alignment: Center);\n" +
            "    }\n" +
            "\n" +
            "    Label #Subtitle {\n" +
            "        Text: \"Static Display - No Events\";\n" +
            "        Anchor: (Height: 30);\n" +
            "        Style: (FontSize: 16, TextColor: #888888, Alignment: Center);\n" +
            "    }\n" +
            "\n" +
            "    Label #Info {\n" +
            "        Text: \"Press ESC to close\";\n" +
            "        Anchor: (Height: 25);\n" +
            "        Style: (FontSize: 14, TextColor: #666666, Alignment: Center);\n" +
            "    }\n" +
            "}";

    public Test2dCommand() {
        super("testUI", "");
    }

    @Override
    protected void execute(@NonNullDecl CommandContext commandContext, @NonNullDecl Store<EntityStore> store, @NonNullDecl Ref<EntityStore> ref, @NonNullDecl PlayerRef playerRef, @NonNullDecl World world) {
        Player player = (Player) store.getComponent(ref, Player.getComponentType());
        TestPages page = new TestPages(playerRef);
        player.getPageManager().openCustomPage(ref, store, page);
    }
}
