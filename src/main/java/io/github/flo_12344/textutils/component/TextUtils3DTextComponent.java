package io.github.flo_12344.textutils.component;

import com.hypixel.hytale.codec.Codec;
import com.hypixel.hytale.codec.KeyedCodec;
import com.hypixel.hytale.codec.builder.BuilderCodec;
import com.hypixel.hytale.component.Component;
import com.hypixel.hytale.component.ComponentType;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import org.checkerframework.checker.nullness.compatqual.NullableDecl;

import java.util.UUID;
import java.util.concurrent.CopyOnWriteArrayList;

public class TextUtils3DTextComponent implements Component<EntityStore> {
    public static final BuilderCodec<TextUtils3DTextComponent> CODEC =
            BuilderCodec.builder(TextUtils3DTextComponent.class, TextUtils3DTextComponent::new)
                    .append(new KeyedCodec<>("FontName", Codec.STRING),
                            (c, v) -> c.font_name = v,
                            c -> c.font_name).add()
                    .append(new KeyedCodec<>("Text", Codec.STRING),
                            (c, v) -> c.text = v,
                            c -> c.text).add()
                    .append(new KeyedCodec<>("TextId", Codec.STRING),
                            (c, v) -> c.id = v,
                            c -> c.id).add()
                    .append(new KeyedCodec<>("Visible", Codec.BOOLEAN),
                            (c, v) -> c.visible = v,
                            c -> c.visible).add()
                    .build();

    private static ComponentType<EntityStore, TextUtils3DTextComponent> TYPE;
    private boolean edited;
    private String font_name;
    private String text;
    private String id;
    private boolean visible = true;
    private CopyOnWriteArrayList<UUID> text_entities = new CopyOnWriteArrayList<>();

    public static ComponentType<EntityStore, TextUtils3DTextComponent> getComponentType() {
        return TYPE;
    }

    public TextUtils3DTextComponent() {
        edited = true;
    }

    public static void init(ComponentType<EntityStore, TextUtils3DTextComponent> type) {
        TYPE = type;
    }

    public TextUtils3DTextComponent(String font_name, String text, String id) {
        this.font_name = font_name;
        this.text = text;
        this.id = id;
        edited = true;
    }

    public void setText(String text) {
        this.text = text;
        edited = true;
    }

    public String getText() {
        return text;
    }

    public void setEdited(boolean edited) {
        this.edited = edited;
    }

    public boolean isEdited() {
        return edited;
    }

    public void setFont_name(String font_name) {
        this.font_name = font_name;
        edited = true;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
        this.edited = true;
    }

    public String getId() {
        return id;
    }

    public String getFont_name() {
        return font_name;
    }

    public CopyOnWriteArrayList<UUID> getText_entities() {
        return text_entities;
    }

    public void setText_entities(CopyOnWriteArrayList<UUID> text_entities) {
        this.text_entities = text_entities;
    }

    @NullableDecl
    @Override
    public Component<EntityStore> clone() {
        return new TextUtils3DTextComponent(font_name, text, getId());
    }
}
