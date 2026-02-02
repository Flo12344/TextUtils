package io.github.flo_12344.textutils.commands;

import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.command.system.CommandContext;
import com.hypixel.hytale.server.core.command.system.arguments.system.OptionalArg;
import com.hypixel.hytale.server.core.command.system.arguments.system.RequiredArg;
import com.hypixel.hytale.server.core.command.system.arguments.types.ArgTypes;
import com.hypixel.hytale.server.core.command.system.basecommands.AbstractPlayerCommand;
import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.world.World;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import io.github.flo_12344.textutils.pages.TestPages;
import io.github.flo_12344.textutils.utils.FontManager;
import io.github.flo_12344.textutils.utils.FormattingUtils;
import org.checkerframework.checker.nullness.compatqual.NonNullDecl;


public class Text2dCommand extends AbstractPlayerCommand {


    public Text2dCommand() {
        super("text2d", "");
        addSubCommand(new NewPageCommand());
    }

    @Override
    protected void execute(@NonNullDecl CommandContext ctx, @NonNullDecl Store<EntityStore> store, @NonNullDecl Ref<EntityStore> ref, @NonNullDecl PlayerRef playerRef, @NonNullDecl World world) {
        ctx.sendMessage(Message.raw("This is a limited example for UI custom font for mods (might add more stuff in the future)"));
    }

    public static class NewPageCommand extends AbstractPlayerCommand {
        RequiredArg<String> text;
        RequiredArg<String> font_id;
        OptionalArg<Float> size;

        protected NewPageCommand() {
            super("page", "");
            text = withRequiredArg("text", "", ArgTypes.STRING);
            font_id = withRequiredArg("font", "", ArgTypes.STRING);
            size = withOptionalArg("size", "", ArgTypes.FLOAT);
        }

        @Override
        protected void execute(@NonNullDecl CommandContext ctx, @NonNullDecl Store<EntityStore> store, @NonNullDecl Ref<EntityStore> ref, @NonNullDecl PlayerRef playerRef, @NonNullDecl World world) {
            var lines = FormattingUtils.parseFormattedText(text.get(ctx));
            String page_data = "";
            page_data += "Group {\n";
            page_data += "LayoutMode: Top;\n";
            var group = "";
            int scale = (int) (FontManager.INSTANCE.getFontSettings(font_id.get(ctx)).glyph_size);
            float resize = ctx.provided(size) ? size.get(ctx) : 1;
            for (var to_use : lines) {
                group += "Group {\n";
                group += "LayoutMode: Left;\n";
                for (var t : to_use) {
                    for (char c : t.getText().toCharArray()) {
                        int width = (int) ((FontManager.INSTANCE.getFontSettings(font_id.get(ctx)).fm.charWidth(c) - scale) * resize);
                        group += "Group {\n";
                        group += "Anchor: (Width: %s, Height: %s, Horizontal: %s);\n".formatted(scale * resize, scale, width / 2);
                        group += "Background: " + FormattingUtils.getUIColor(t.getColor()) + ";\n";
                        group += "MaskTexturePath: \"Textutils/" + font_id.get(ctx) +
                                "/U" + String.format("%04X", (int) c) + ".png\";\n";
                        group += "}\n";
                    }
                }
                group += "}\n";
            }
            group += "}\n";
            page_data += group;

            var player = ctx.senderAs(Player.class);
            TestPages page = new TestPages(playerRef, page_data);
            player.getPageManager().openCustomPage(ref, store, page);
        }
    }

}
