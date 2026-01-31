package io.github.flo_12344.textutils.commands;

import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.math.vector.Vector3d;
import com.hypixel.hytale.math.vector.Vector3f;
import com.hypixel.hytale.math.vector.Vector3i;
import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.command.system.CommandContext;
import com.hypixel.hytale.server.core.command.system.arguments.system.OptionalArg;
import com.hypixel.hytale.server.core.command.system.arguments.system.RequiredArg;
import com.hypixel.hytale.server.core.command.system.arguments.types.ArgTypes;
import com.hypixel.hytale.server.core.command.system.basecommands.AbstractPlayerCommand;
import com.hypixel.hytale.server.core.command.system.basecommands.AbstractTargetEntityCommand;
import com.hypixel.hytale.server.core.entity.UUIDComponent;
import com.hypixel.hytale.server.core.modules.entity.component.TransformComponent;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.world.World;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import io.github.flo_12344.textutils.component.Text3dDeleterComponent;
import io.github.flo_12344.textutils.component.Text3dTrackerComponent;
import io.github.flo_12344.textutils.component.TextUtils3DTextComponent;
import io.github.flo_12344.textutils.utils.FontManager;
import io.github.flo_12344.textutils.utils.TextManager;
import it.unimi.dsi.fastutil.objects.ObjectList;
import org.checkerframework.checker.nullness.compatqual.NonNullDecl;

import java.util.Objects;
import java.util.UUID;

public class Text3dCommand extends AbstractPlayerCommand {


    public Text3dCommand() {
        super("text3d", "");
        addSubCommand(new NewCommand());
        addSubCommand(new ListCommand());
        addSubCommand(new EditCommand());
        addSubCommand(new RemoveCommand());
        addSubCommand(new HideCommand());
        addSubCommand(new ShowCommand());
        addSubCommand(new MoveCommand());
        addSubCommand(new RotateCommand());
        addSubCommand(new ResizeCommand());
        addSubCommand(new TrackCommand());
    }

    @Override
    protected void execute(@NonNullDecl CommandContext commandContext, @NonNullDecl Store<EntityStore> store, @NonNullDecl Ref<EntityStore> ref, @NonNullDecl PlayerRef playerRef, @NonNullDecl World world) {

    }

    public static class ListCommand extends AbstractPlayerCommand {
        protected ListCommand() {
            super("list", "List all holograms");
        }

        @Override
        protected void execute(@NonNullDecl CommandContext commandContext, @NonNullDecl Store<EntityStore> store, @NonNullDecl Ref<EntityStore> ref, @NonNullDecl PlayerRef playerRef, @NonNullDecl World world) {
            TextManager.text3dUtilsEntity.keys().asIterator().forEachRemaining(s -> {
                commandContext.sendMessage(Message.raw(s));
            });
        }
    }

    public static class NewCommand extends AbstractPlayerCommand {
        RequiredArg<Vector3i> position;
        RequiredArg<String> text;
        OptionalArg<String> id;
        OptionalArg<String> font_id;
        OptionalArg<Vector3f> rotation;
        OptionalArg<Float> size;

        protected NewCommand() {
            super("new", "Create a new hologram");
            position = withRequiredArg("position", "Position of the hologram", ArgTypes.VECTOR3I);
            text = withRequiredArg("text", "Text to display color formatting works like that -> '{color_name}colored{\\\\color_name} not colored'", ArgTypes.STRING);
            id = withOptionalArg("id", "Id to access the hologram for edits if not set will get a random uuid", ArgTypes.STRING);
            font_id = withOptionalArg("font", "Id of the desired font", ArgTypes.STRING);
            rotation = withOptionalArg("rotation", "Rotation of the hologram in rad", ArgTypes.ROTATION);
            size = withOptionalArg("size", "Size of the hologram", ArgTypes.FLOAT);
        }

        @Override
        protected void execute(@NonNullDecl CommandContext ctx, @NonNullDecl Store<EntityStore> store, @NonNullDecl Ref<EntityStore> ref, @NonNullDecl PlayerRef playerRef, @NonNullDecl World world) {
            String content = text.get(ctx);
            if (content.startsWith("\""))
                content = content.substring(1, content.lastIndexOf("\""));
            final Vector3d pos = position.get(ctx).toVector3d();
            final Vector3f rot = ctx.getInput(rotation) == null ? new Vector3f() : rotation.get(ctx);
            final String _id = ctx.provided(id) ? id.get(ctx) : UUID.randomUUID().toString();
            String font = ctx.getInput(font_id) == null ? "" : font_id.get(ctx);
            final float tsize = ctx.getInput(size) == null ? 1.0f : size.get(ctx);
            if (font.isEmpty()) {
                if (FontManager.INSTANCE.getLoaded_font().isEmpty()) {
                    ctx.sendMessage(Message.raw("No Font loaded"));
                    return;
                } else {
                    font = FontManager.INSTANCE.getLoaded_font().keySet().stream().toList().getFirst();
                }
            }
            if (!FontManager.INSTANCE.IsFontLoaded(font)) {
                ctx.sendMessage(Message.raw(String.format("Font %s doesn't exist.", font)));
                return;
            }
            if (TextManager.text3dUtilsEntity.containsKey(_id)) {
                ctx.sendMessage(Message.raw(String.format("TextUtilsEntity with label %s already exist.", _id)));
                return;
            }

            TextManager.SpawnText3dEntity(pos, rot, world, content, _id, font, tsize);
        }
    }

    public static class EditCommand extends AbstractPlayerCommand {
        RequiredArg<String> label;
        RequiredArg<String> text;
        OptionalArg<String> font_id;

        public EditCommand() {
            super("edit", "Edit a specified hologram");
            label = withRequiredArg("label", "Id of the desired hologram", ArgTypes.STRING);
            text = withRequiredArg("text", "Text to use as a replacement", ArgTypes.STRING);
            font_id = withOptionalArg("font", "Id of the desired font", ArgTypes.STRING);
        }

        @Override
        protected void execute(@NonNullDecl CommandContext ctx, @NonNullDecl Store<EntityStore> store, @NonNullDecl Ref<EntityStore> ref, @NonNullDecl PlayerRef playerRef, @NonNullDecl World world) {
            var label_str = label.get(ctx);
            if (!TextManager.text3dUtilsEntity.containsKey(label_str)) {
                ctx.sendMessage(Message.raw(String.format("Unknown TextUtilsEntity label: %s", label)));
                return;
            }
            final String font = ctx.getInput(font_id) == null ? "" : font_id.get(ctx);
            if (!font.isEmpty() && !FontManager.INSTANCE.IsFontLoaded(font)) {
                ctx.sendMessage(Message.raw(String.format("Font %s doesn't exist.", font)));
                return;
            }
            String content = text.get(ctx);
            if (content.startsWith("\""))
                content = content.substring(1, content.lastIndexOf("\""));

            TextManager.EditText3dContent(label_str, world, store, content);
            if (!font.isEmpty()) {
                TextManager.ChangeText3dFont(label_str, world, store, font);
            }
        }
    }

    public static class MoveCommand extends AbstractPlayerCommand {
        RequiredArg<String> label;
        RequiredArg<Float> x, y, z;

        public MoveCommand() {
            super("move", "Move a desired hologram by precise value");
            label = withRequiredArg("label", "Id of the desired hologram", ArgTypes.STRING);
            x = withRequiredArg("x", "", ArgTypes.FLOAT);
            y = withRequiredArg("y", "", ArgTypes.FLOAT);
            z = withRequiredArg("z", "", ArgTypes.FLOAT);
        }

        @Override
        protected void execute(@NonNullDecl CommandContext ctx, @NonNullDecl Store<EntityStore> store, @NonNullDecl Ref<EntityStore> ref, @NonNullDecl PlayerRef playerRef, @NonNullDecl World world) {
            var label_str = label.get(ctx);
            if (!TextManager.text3dUtilsEntity.containsKey(label_str)) {
                ctx.sendMessage(Message.raw(String.format("Unknown TextUtilsEntity label: %s", label)));
                return;
            }
            TextManager.MoveText3dEntity(label_str, world, store, new Vector3d(x.get(ctx), y.get(ctx), z.get(ctx)));
        }

    }

    public static class RotateCommand extends AbstractPlayerCommand {
        RequiredArg<String> label;
        RequiredArg<Vector3f> rotation;

        public RotateCommand() {
            super("rotate", "Rotate the specified hologram");
            label = withRequiredArg("label", "Id of the desired hologram", ArgTypes.STRING);
            rotation = withRequiredArg("rotation", "", ArgTypes.ROTATION);
        }

        @Override
        protected void execute(@NonNullDecl CommandContext ctx, @NonNullDecl Store<EntityStore> store, @NonNullDecl Ref<EntityStore> ref, @NonNullDecl PlayerRef playerRef, @NonNullDecl World world) {
            var label_str = label.get(ctx);
            if (!TextManager.text3dUtilsEntity.containsKey(label_str)) {
                ctx.sendMessage(Message.raw(String.format("Unknown TextUtilsEntity label: %s", label)));
                return;
            }

            TextManager.RotateText3dEntity(label_str, world, store, rotation.get(ctx));
        }
    }

    public static class ResizeCommand extends AbstractPlayerCommand {
        RequiredArg<String> label;
        RequiredArg<Float> size;

        public ResizeCommand() {
            super("resize", "Change the size of the specified hologram");
            label = withRequiredArg("label", "Id of the desired hologram", ArgTypes.STRING);
            size = withRequiredArg("size", "", ArgTypes.FLOAT);
        }

        @Override
        protected void execute(@NonNullDecl CommandContext ctx, @NonNullDecl Store<EntityStore> store, @NonNullDecl Ref<EntityStore> ref, @NonNullDecl PlayerRef playerRef, @NonNullDecl World world) {
            var label_str = label.get(ctx);
            if (!TextManager.text3dUtilsEntity.containsKey(label_str)) {
                ctx.sendMessage(Message.raw(String.format("Unknown TextUtilsEntity label: %s", label)));
                return;
            }

            TextManager.ResizeText3dEntity(label_str, world, store, size.get(ctx));

        }
    }

    public static class RemoveCommand extends AbstractPlayerCommand {
        RequiredArg<String> label;

        public RemoveCommand() {
            super("remove", "Remove the specified hologram");
            label = withRequiredArg("label", "Id of the desired hologram", ArgTypes.STRING);
        }

        @Override
        protected void execute(@NonNullDecl CommandContext ctx, @NonNullDecl Store<EntityStore> store, @NonNullDecl Ref<EntityStore> ref, @NonNullDecl PlayerRef playerRef, @NonNullDecl World world) {
            var label_str = label.get(ctx);
            if (!TextManager.text3dUtilsEntity.containsKey(label_str)) {
                ctx.sendMessage(Message.raw(String.format("Unknown TextUtilsEntity label: %s", label)));
                return;
            }
            TextManager.RemoveText3dEntity(label_str, world, store);
        }
    }

    public static class HideCommand extends AbstractPlayerCommand {
        RequiredArg<String> label;

        public HideCommand() {
            super("hide", "Hide the specified hologram");
            label = withRequiredArg("id", "Id of the desired hologram", ArgTypes.STRING);
        }

        @Override
        protected void execute(@NonNullDecl CommandContext ctx, @NonNullDecl Store<EntityStore> store, @NonNullDecl Ref<EntityStore> ref, @NonNullDecl PlayerRef playerRef, @NonNullDecl World world) {
            var label_str = label.get(ctx);
            if (!TextManager.text3dUtilsEntity.containsKey(label_str)) {
                ctx.sendMessage(Message.raw(String.format("Unknown TextUtilsEntity label: %s", label)));
                return;
            }

            TextManager.SetText3dVisibility(label_str, world, store, false);
            ctx.sendMessage(Message.raw(String.format("TextUtilsEntity %s hidden", label_str)));
        }
    }

    public static class ShowCommand extends AbstractPlayerCommand {
        RequiredArg<String> label;

        public ShowCommand() {
            super("show", "Unhide the specified hologram");
            label = withRequiredArg("label", "", ArgTypes.STRING);
        }

        @Override
        protected void execute(@NonNullDecl CommandContext ctx, @NonNullDecl Store<EntityStore> store, @NonNullDecl Ref<EntityStore> ref, @NonNullDecl PlayerRef playerRef, @NonNullDecl World world) {
            var label_str = label.get(ctx);
            if (!TextManager.text3dUtilsEntity.containsKey(label_str)) {
                ctx.sendMessage(Message.raw(String.format("Unknown TextUtilsEntity label: %s", label)));
                return;
            }

            TextManager.SetText3dVisibility(label_str, world, store, true);
            ctx.sendMessage(Message.raw(String.format("TextUtilsEntity %s now visible", label_str)));
        }
    }

    public static class TrackCommand extends AbstractTargetEntityCommand {
        RequiredArg<String> label;
        OptionalArg<Vector3i> offset;

        public TrackCommand() {
            super("track", "Link the specified hologram to an entity");
            label = withRequiredArg("id", "Id of the desired hologram", ArgTypes.STRING);
            offset = withOptionalArg("offset", "Offset to the entity", ArgTypes.VECTOR3I);
        }

        @Override
        protected void execute(@NonNullDecl CommandContext ctx, @NonNullDecl ObjectList<Ref<EntityStore>> objectList, @NonNullDecl World world, @NonNullDecl Store<EntityStore> store) {
            var label_str = label.get(ctx);

            if (!TextManager.text3dUtilsEntity.containsKey(label_str)) {
                ctx.sendMessage(Message.raw(String.format("Unknown TextUtilsEntity label: %s", label_str)));
                return;
            }

            if (objectList.isEmpty())
                return;
            var uuid = store.getComponent(objectList.getFirst(), UUIDComponent.getComponentType());

            var text_entity = world.getEntityRef(TextManager.text3dUtilsEntity.get(label_str));
            store.addComponent(text_entity, Text3dTrackerComponent.getComponentType(), new Text3dTrackerComponent(uuid.getUuid(), offset.get(ctx)));
        }
    }

}
