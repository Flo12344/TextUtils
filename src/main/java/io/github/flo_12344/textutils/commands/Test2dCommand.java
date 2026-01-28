package io.github.flo_12344.textutils.commands;

import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.server.core.command.system.CommandContext;
import com.hypixel.hytale.server.core.command.system.basecommands.AbstractPlayerCommand;
import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.io.ServerManager;
import com.hypixel.hytale.server.core.plugin.registry.AssetRegistry;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.Universe;
import com.hypixel.hytale.server.core.universe.world.World;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import io.github.flo_12344.textutils.pages.TestPages;
import org.checkerframework.checker.nullness.compatqual.NonNullDecl;

import java.util.ArrayList;
import java.util.List;

public class Test2dCommand extends AbstractPlayerCommand {
    public static String test = "Group {" +
            "@Mytex = PatchStyle( TexturePath : \"Items/Textutils/Blocky/U0021.png\");\n" +
            "Background: @Mytex;\n" +
            "    Anchor: (Left: 20, Top: 20, Width: 64, Height: 64);\n" +
            "}";

    public Test2dCommand() {
        super("testUI", "");
    }

    @Override
    protected void execute(@NonNullDecl CommandContext commandContext, @NonNullDecl Store<EntityStore> store, @NonNullDecl Ref<EntityStore> ref, @NonNullDecl PlayerRef playerRef, @NonNullDecl World world) {
        List<Character> done = new ArrayList<>();
        test = "";
        for (char c : "Ta gueuuuuuule!!!".toCharArray()) {
            if (done.contains(c))
                continue;
            test += "@U" + String.format("%04X", (int) c) + " = PatchStyle( TexturePath : \"Items/Textutils/Blocky/U" + String.format("%04X", (int) c) + ".png\");\n";
            done.add(c);
        }
        test += "Group {\n";
        test += "LayoutMode: Center;\n";

        var group = "Group {\nLayoutMode: Left;\n";
        for (char c : "Ta gueuuuuuule!!!".toCharArray()) {
            group += "Group {\n";
            group += "Anchor: (Width: 64, Height: 64);\n";
            group += "Background: @U" + String.format("%04X", (int) c) + ";\n";
            group += "}\n";
        }
        group += "}\n";
        group += "}\n";
        test += group;


        Player player = (Player) store.getComponent(ref, Player.getComponentType());
        TestPages page = new TestPages(playerRef);
        player.getPageManager().openCustomPage(ref, store, page);
    }
}
