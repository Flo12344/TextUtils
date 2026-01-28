package io.github.flo_12344.textutils.utils;

import com.hypixel.hytale.component.*;
import com.hypixel.hytale.math.vector.Vector3d;
import com.hypixel.hytale.math.vector.Vector3f;
import com.hypixel.hytale.server.core.asset.type.model.config.Model;
import com.hypixel.hytale.server.core.asset.type.model.config.ModelAsset;
import com.hypixel.hytale.server.core.entity.UUIDComponent;
import com.hypixel.hytale.server.core.modules.entity.component.*;
import com.hypixel.hytale.server.core.modules.entity.tracker.NetworkId;
import com.hypixel.hytale.server.core.universe.world.World;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import io.github.flo_12344.textutils.TextUtils;
import io.github.flo_12344.textutils.component.TextUtils3DTextComponent;

import javax.annotation.Nonnull;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TextManager {
    public static ConcurrentHashMap<String, UUID> textUtilsEntity = new ConcurrentHashMap<>();

    public static void SpawnText(Vector3d pos, Vector3f rot, @Nonnull World world, String text, String _label, String font, float size) {
        world.execute(() -> {
            Store<EntityStore> store = world.getEntityStore().getStore();
            Holder<EntityStore> holder = EntityStore.REGISTRY.newHolder();

            TransformComponent transform = new TransformComponent(pos, rot);

            holder.addComponent(TransformComponent.getComponentType(), transform);
            holder.addComponent(NetworkId.getComponentType(), new NetworkId(store.getExternalData().takeNextNetworkId()));
            holder.ensureComponent(UUIDComponent.getComponentType());
            holder.addComponent(Intangible.getComponentType(), Intangible.INSTANCE);
            store.addComponent(store.addEntity(holder, AddReason.SPAWN),
                    TextUtils3DTextComponent.getComponentType(),
                    new TextUtils3DTextComponent(font, text, _label, size));
        });
    }

    public static class ColoredText {
        private String text;
        private String color;

        public ColoredText(String text, String color) {
            this.text = text;
            this.color = color;
        }

        public String getText() {
            return text;
        }

        public String getColor() {
            return color;
        }
    }

    public static String getGradientId(String color) {
        return switch (color) {
            case "yellow" -> "Blond";
            case "light_blue" -> "Blue";
            case "blue" -> "Blue";
            case "dark_blue" -> "BlueDark";
            case "white" -> "White";
            case "aqua" -> "Turquoise";
            case "lime" -> "Green";
            case "red" -> "RedDark";
            case "orange" -> "Copper";
            case "purple" -> "Lavender";
            case "pink" -> "Pink";
            case "green" -> "Green";
            case "brown" -> "BrownDark";
            case "beige" -> "BlondCaramel";
            case "grey" -> "Black";
            case "black" -> "Black";
            default -> "White";
        };
    }

    public static String getGradientSet(String color) {
        return switch (color) {
            case "aqua", "pink", "grey", "beige", "brown", "green", "purple", "orange", "white", "dark_blue",
                 "light_blue", "yellow" -> "Hair";
            case "blue" -> "Fantasy_Cotton_Dark";
            case "red" -> "Eyes_Gradient";
            case "black" -> "Flashy_Synthetic";
            case "lime" -> "Colored_Cotton";
            default -> "Colored_Cotton";
        };
    }


    public static List<ColoredText> parseFormattedText(String input) {
        List<ColoredText> formattedTexts = new ArrayList<>();
        Pattern pattern = Pattern.compile("\\{(\\w+)\\}(.*?)\\{\\\\\\1\\}");
        Matcher matcher = pattern.matcher(input);

        int lastEnd = 0;
        while (matcher.find()) {
            if (matcher.start() > lastEnd) {
                formattedTexts.add(new ColoredText(input.substring(lastEnd, matcher.start()), "white"));
            }
            String color = matcher.group(1);
            String text = matcher.group(2);
            formattedTexts.add(new ColoredText(text, color.toLowerCase()));

            lastEnd = matcher.end();
        }

        if (lastEnd < input.length()) {
            formattedTexts.add(new ColoredText(input.substring(lastEnd), "white"));
        }

        return formattedTexts;
    }
}
