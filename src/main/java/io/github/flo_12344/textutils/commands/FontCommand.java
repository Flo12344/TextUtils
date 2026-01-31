package io.github.flo_12344.textutils.commands;

import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.command.system.AbstractCommand;
import com.hypixel.hytale.server.core.command.system.CommandContext;
import com.hypixel.hytale.server.core.command.system.arguments.system.OptionalArg;
import com.hypixel.hytale.server.core.command.system.arguments.system.RequiredArg;
import com.hypixel.hytale.server.core.command.system.arguments.types.ArgTypes;
import com.hypixel.hytale.server.core.command.system.arguments.types.ListArgumentType;
import com.hypixel.hytale.server.core.command.system.basecommands.AbstractPlayerCommand;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.world.World;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import io.github.flo_12344.textutils.TextUtils;
import io.github.flo_12344.textutils.component.TextUtils3DTextComponent;
import io.github.flo_12344.textutils.runtime.FontRuntimeManager;
import io.github.flo_12344.textutils.utils.FontConfig;
import io.github.flo_12344.textutils.utils.FontManager;
import io.github.flo_12344.textutils.utils.TextManager;
import org.checkerframework.checker.nullness.compatqual.NonNullDecl;
import org.checkerframework.checker.nullness.compatqual.NullableDecl;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.logging.Level;

public class FontCommand extends AbstractPlayerCommand {
    public FontCommand() {
        super("font", "", false);
        addSubCommand(new ListCommand());
        addSubCommand(new InitCommand());
        addSubCommand(new RangeCommand());
        addSubCommand(new CustomRangeCommand());
        addSubCommand(new RemoveCommand());
    }

    @Override
    protected void execute(@NonNullDecl CommandContext ctx, @NonNullDecl Store<EntityStore> store, @NonNullDecl Ref<EntityStore> ref, @NonNullDecl PlayerRef playerRef, @NonNullDecl World world) {
    }

    public static class ListCommand extends AbstractPlayerCommand {
        protected ListCommand() {
            super("list", "List all the usable fonts");
        }

        @Override
        protected void execute(@NonNullDecl CommandContext ctx, @NonNullDecl Store<EntityStore> store, @NonNullDecl Ref<EntityStore> ref, @NonNullDecl PlayerRef playerRef, @NonNullDecl World world) {
            FontManager.INSTANCE.getLoaded_font().forEach((s, fontSettings) -> {
                String out = s + " -> { Range loaded :" + FontConfig.getLoadedRangesAsString(fontSettings.loaded) + "}";
                ctx.sendMessage(Message.raw(out));
            });
        }
    }

    public static class InitCommand extends AbstractPlayerCommand {
        RequiredArg<String> font_name;
        OptionalArg<String> font_id;
        OptionalArg<Float> size;

        protected InitCommand() {
            super("init", "init a font from the font folder of TextUtils");
            font_name = withRequiredArg("font_name", "Font file name with or without extension", ArgTypes.STRING);
            font_id = withOptionalArg("font_id", "Name used to access the font", ArgTypes.STRING);
            size = withOptionalArg("size", "", ArgTypes.FLOAT);
        }

        @Override
        protected void execute(@NonNullDecl CommandContext ctx, @NonNullDecl Store<EntityStore> store, @NonNullDecl Ref<EntityStore> ref, @NonNullDecl PlayerRef playerRef, @NonNullDecl World world) {
            var font = font_name.get(ctx).replaceAll("\"", "");
            var id = font_id.get(ctx) == null ? font.replaceAll("\\.ttf|\\.otf", "").replaceAll(" ", "_") : font_id.get(ctx);
            var _size = size.provided(ctx) ? size.get(ctx) : 32f;

            try {
                if (!FontManager.INSTANCE.InitFromUserCommands(FontManager.FONT_DIR + File.separator + font, id, _size)) {
                    ctx.sendMessage(Message.raw("Failed to load %s".formatted(font)));
                    return;
                }
            } catch (IOException | FontFormatException e) {
                ctx.sendMessage(Message.raw(e.getMessage()));
            }
            ctx.sendMessage(Message.raw("Font %s loaded as %s".formatted(font, id)));
        }
    }

    public static class RangeCommand extends AbstractPlayerCommand {
        RequiredArg<String> font_id;
        RequiredArg<List<String>> range_name;

        protected RangeCommand() {
            super("range", "Generate a glyphs for the specified range and font (ranges are based on https://www.unicodepedia.com/groups/)");
            font_id = withRequiredArg("font_id", "Id of the desired font", ArgTypes.STRING);
            range_name = withRequiredArg("range_name", "one or multiples of the following (separated by ',')" + FontConfig.getAllOptions(), new ListArgumentType<>(ArgTypes.STRING));
        }

        @Override
        protected void execute(@NonNullDecl CommandContext ctx, @NonNullDecl Store<EntityStore> store, @NonNullDecl Ref<EntityStore> ref, @NonNullDecl PlayerRef playerRef, @NonNullDecl World world) {
            if (!FontManager.INSTANCE.IsFontLoaded(font_id.get(ctx))) {
                ctx.sendMessage(Message.raw("Font not found"));
            }
            try {
                FontManager.INSTANCE.LoadFlags(font_id.get(ctx), range_name.get(ctx));
                TextUtils.INSTANCE.fontRuntimeManager.broadcastTexturesModels();

            } catch (IOException | FontFormatException e) {
                ctx.sendMessage(Message.raw(e.getMessage()));
            }
        }
    }

    public static class CustomRangeCommand extends AbstractPlayerCommand {
        RequiredArg<String> font_id;
        RequiredArg<String> range_name;
        RequiredArg<Integer> start;
        RequiredArg<Integer> end;

        protected CustomRangeCommand() {
            super("customrange", "Generate a glyphs for the specified range and font if you are not sure use https://www.unicodepedia.com/groups/ or the simpler range subCommand");
            font_id = withRequiredArg("font_id", "Id of the desired font", ArgTypes.STRING);
            range_name = withRequiredArg("range_name", "Desired name for the range", ArgTypes.STRING);
            start = withRequiredArg("start", "Start of the Unicode range", ArgTypes.INTEGER);
            end = withRequiredArg("end", "End of the Unicode range", ArgTypes.INTEGER);
        }

        @Override
        protected void execute(@NonNullDecl CommandContext ctx, @NonNullDecl Store<EntityStore> store, @NonNullDecl Ref<EntityStore> ref, @NonNullDecl PlayerRef playerRef, @NonNullDecl World world) {
            if (!FontManager.INSTANCE.IsFontLoaded(font_id.get(ctx))) {
                ctx.sendMessage(Message.raw("Font not found"));
            }
            try {
                FontManager.INSTANCE.LoadRange(font_id.get(ctx), start.get(ctx), end.get(ctx), range_name.get(ctx));
            } catch (IOException | FontFormatException e) {
                ctx.sendMessage(Message.raw(e.getMessage()));
            }
        }
    }

    public static class RemoveCommand extends AbstractPlayerCommand {
        RequiredArg<String> font_id;

        protected RemoveCommand() {
            super("remove", "Remove the specified font if not in use");
            font_id = withRequiredArg("font_id", "Id of the desired font", ArgTypes.STRING);
        }

        @Override
        protected void execute(@NonNullDecl CommandContext ctx, @NonNullDecl Store<EntityStore> store, @NonNullDecl Ref<EntityStore> ref, @NonNullDecl PlayerRef playerRef, @NonNullDecl World world) {
            List<String> used_by = new ArrayList<>();
            var font = font_id.get(ctx);
            if (FontManager.INSTANCE.getFontSettings(font).loaded_by == FontConfig.SOURCE.MODS) {
                ctx.sendMessage(Message.raw("Can't remove font added through mods"));
                return;
            }

            for (var holoid : TextManager.text3dUtilsEntity.keySet()) {
                var entity = world.getEntityRef(TextManager.text3dUtilsEntity.get(holoid));
                if (entity == null)
                    continue;
                TextUtils3DTextComponent text = store.getComponent(entity, TextUtils3DTextComponent.getComponentType());
                if (text == null)
                    continue;
                if (Objects.equals(text.getFont_name(), font)) {
                    used_by.add(holoid);
                }
            }

            if (!used_by.isEmpty()) {
                ctx.sendMessage(Message.raw("Can't remove font %s it's used by %s".formatted(font, Arrays.toString(used_by.toArray()))));
                return;
            }

            try {
                FontManager.INSTANCE.RemoveFont(font);
            } catch (IOException e) {
                TextUtils.INSTANCE.getLogger().at(Level.WARNING).withCause(e).log("Failed to remove %s", font);
                ctx.sendMessage(Message.raw("Failed to remove %s".formatted(font)));
                return;
            }
            ctx.sendMessage(Message.raw("Font %s as been removed".formatted(font)));
        }
    }
}
