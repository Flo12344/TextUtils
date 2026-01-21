package io.github.flo_12344.textutils.component;

import com.hypixel.hytale.component.Component;
import com.hypixel.hytale.component.ComponentType;
import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.server.core.universe.world.World;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import org.checkerframework.checker.nullness.compatqual.NullableDecl;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.CopyOnWriteArrayList;

public class TextUtils3DTextComponent implements Component<EntityStore> {
    private static ComponentType<EntityStore, TextUtils3DTextComponent> TYPE;
    private boolean edited;
    private String font_name;
    private String text;
    private String id;
    private CopyOnWriteArrayList<UUID> text_entities = new CopyOnWriteArrayList<>();

    public static ComponentType<EntityStore, TextUtils3DTextComponent> getComponentType() {
        return TYPE;
    }

    public TextUtils3DTextComponent() {
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
        return new TextUtils3DTextComponent(font_name, text, UUID.randomUUID().toString());
    }
}
