package io.github.flo_12344.textutils.utils;

import com.hypixel.hytale.codec.Codec;
import com.hypixel.hytale.codec.KeyedCodec;
import com.hypixel.hytale.codec.builder.BuilderCodec;
import com.hypixel.hytale.codec.codecs.map.MapCodec;
import io.github.flo_12344.textutils.TextUtils;
import io.github.flo_12344.textutils.runtime.FontRuntimeManager;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.lang.classfile.CodeBuilder;
import java.nio.file.Path;
import java.util.*;
import java.util.List;

public class FontManager {
    public static final BuilderCodec<FontManager> CODEC = BuilderCodec.<FontManager>builder(FontManager.class, FontManager::new)
            .append(new KeyedCodec<>("Loaded_Font", new MapCodec<>(FontConfig.CODEC, HashMap::new)),
                    (fontManager, s) -> fontManager.loaded_font.putAll(s),
                    fontManager -> fontManager.loaded_font).add()
            .afterDecode((fontManager, extraInfo) -> {
                fontManager.loaded_font.forEach((s, fontConfig) -> {
                    try {
                        FontManager.INSTANCE.InitFromSave(fontConfig.font_file, s, fontConfig.size, fontConfig.loaded);
                    } catch (IOException | FontFormatException e) {
                        throw new RuntimeException(e);
                    }
                });
            })
            .build();
    public static FontManager INSTANCE;

    private final HashMap<String, FontConfig> loaded_font = new HashMap<>();
    private static Path FONT_DIR;

    public HashMap<String, FontConfig> getLoaded_font() {
        return loaded_font;
    }

    private FontManager() {
        INSTANCE = this;
        FONT_DIR = TextUtils.INSTANCE.getDataDirectory().resolve("fonts");
    }

    public void InitFromSave(String font_name, String font_id, float size, EnumSet<FontConfig.LOADABLE_BLOCK> loaded) throws IOException, FontFormatException {
        Init(font_name, font_id, size);
        loaded_font.get(font_id).loaded = loaded;
    }

    public boolean Init(String font_name, String font_id, float size) throws IOException, FontFormatException {
        Font font;
        var file = FONT_DIR.resolve(font_name + ".ttf").toFile();
        if (file.exists()) {
            font = Font.createFont(Font.TRUETYPE_FONT, file)
                    .deriveFont(size);
        } else {
            file = FONT_DIR.resolve(font_name + ".otf").toFile();
            if (file.exists()) {
                font = Font.createFont(Font.TRUETYPE_FONT, file)
                        .deriveFont(size);
            } else return false;
        }

        String no_ext_font = font_name.replace(".ttf", "").replace(".otf", "");

        BufferedImage tempImage = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
        Graphics2D tempG2d = tempImage.createGraphics();
        tempG2d.setFont(font);
        FontMetrics fm = tempG2d.getFontMetrics();
        tempG2d.dispose();
        int potSize = 32; // Default minimum
        int maxWidth = Arrays.stream(fm.getWidths()).max().getAsInt();
        while (potSize < Math.max(maxWidth, fm.getHeight())) {
            potSize *= 2;
        }
        FontConfig fs = new FontConfig();
        fs.fm = fm;
        fs.max_height = fm.getHeight();
        fs.max_width = maxWidth;
        fs.texture_size = potSize;
        fs.font_file = font_name;
        fs.size = size;

        loaded_font.put(font_id, fs);
        String dir_path = FontRuntimeManager.resolveRuntimeBasePath()
                .resolve(FontRuntimeManager.RUNTIME_ASSETS_DIR)
                .resolve(FontRuntimeManager.RUNTIME_MODEL_DIR) + File.separator + no_ext_font;
        File f = new File(dir_path);
        f.mkdirs();
        dir_path = FontRuntimeManager.resolveRuntimeBasePath()
                .resolve(FontRuntimeManager.RUNTIME_ASSETS_DIR)
                .resolve("Common")
                .resolve(FontRuntimeManager.MODEL_TEXTURE_PATH) + File.separator + no_ext_font;
        f = new File(dir_path);
        f.mkdirs();
        ModelGenerator.genCharBlockyModel(dir_path, potSize);
        LoadCharacter(font_id, (char) 0);
        return true;
    }

    public void LoadRanges(String font_name, int start, int end) throws IOException, FontFormatException {
        Font font = Font.createFont(Font.TRUETYPE_FONT, new File(TextUtils.INSTANCE.getDataDirectory() + File.separator + "fonts" + File.separator + font_name + ".ttf")).deriveFont(32f);
        for (char c = (char) start; c < end; c++) {
            LoadCharacter(font_name, c, font);
        }
    }

    public void LoadFlags(String font_name, EnumSet<FontConfig.LOADABLE_BLOCK> flags) throws IOException, FontFormatException {
        for (var flag : flags) {
            switch (flag) {
                case BASIC_LATIN -> LoadRanges(font_name, 1, 127);
                case LATIN_EXT_1 -> LoadRanges(font_name, 128, 255);
                case LATIN_EXT_AB -> LoadRanges(font_name, 256, 591);
                case PUNCTUATION -> LoadRanges(font_name, 8192, 8303);
                case CURRENCY -> LoadRanges(font_name, 8352, 8377);
                case ARROW -> LoadRanges(font_name, 8592, 8703);
                case BOX -> LoadRanges(font_name, 9472, 9599);
            }
        }
        loaded_font.get(font_name).loaded.addAll(flags);
    }

    public void LoadFlags(String font_name, List<String> flags_str) throws IOException, FontFormatException {
        EnumSet<FontConfig.LOADABLE_BLOCK> flags = EnumSet.noneOf(FontConfig.LOADABLE_BLOCK.class);
        for (var flag : flags_str) {
            switch (flag) {
                case "latin" -> flags.add(FontConfig.LOADABLE_BLOCK.BASIC_LATIN);
                case "latin_1" -> flags.add(FontConfig.LOADABLE_BLOCK.LATIN_EXT_1);
                case "latin_ab" -> flags.add(FontConfig.LOADABLE_BLOCK.LATIN_EXT_AB);
                case "punct" -> flags.add(FontConfig.LOADABLE_BLOCK.PUNCTUATION);
                case "currency" -> flags.add(FontConfig.LOADABLE_BLOCK.CURRENCY);
                case "arrow" -> flags.add(FontConfig.LOADABLE_BLOCK.ARROW);
                case "box" -> flags.add(FontConfig.LOADABLE_BLOCK.BOX);
            }
        }
        LoadFlags(font_name, flags);
    }

    public void LoadCharacter(String font_name, char c) throws IOException, FontFormatException {
        Font font = Font.createFont(Font.TRUETYPE_FONT, new File(TextUtils.INSTANCE.getDataDirectory() + File.separator + "fonts" + File.separator + font_name + ".ttf")).deriveFont(32f);
        LoadCharacter(font_name, c, font);
    }

    private void LoadCharacter(String font_name, char c, Font font) throws IOException {
//        String dir_path = TextUtils.INSTANCE.getDataDirectory() + File.separator + "data" + File.separator + font_name;
        String dir_path = FontRuntimeManager.resolveRuntimeBasePath().resolve(FontRuntimeManager.RUNTIME_ASSETS_DIR).resolve("Common").resolve(FontRuntimeManager.MODEL_TEXTURE_PATH) + File.separator + font_name;
        var out = new File(dir_path + File.separator + "U" + String.format("%04X", (int) c) + ".png");
        if (out.exists())
            return;
        var fs = loaded_font.get(font_name);
        BufferedImage image = new BufferedImage(
                fs.texture_size, fs.texture_size, BufferedImage.TYPE_INT_ARGB
        );

        Graphics2D g2d = image.createGraphics();
        g2d.setFont(font);
        g2d.setColor(Color.WHITE);

        int charWidth = fs.fm.charWidth(c);
        int x = (fs.texture_size - charWidth) / 2;
        int y = (fs.texture_size + fs.fm.getAscent() - fs.fm.getDescent()) / 2;

        g2d.drawString(String.valueOf(c), x, y);
        g2d.dispose();
        ImageIO.write(image, "PNG", out);
        String model_path = FontRuntimeManager.resolveRuntimeBasePath().resolve(FontRuntimeManager.RUNTIME_ASSETS_DIR).resolve(FontRuntimeManager.RUNTIME_MODEL_DIR) + File.separator + font_name;
        ModelGenerator.genEntityModelAsset(model_path, c, font_name);
    }


    public boolean IsFontLoaded(String font_name, EnumSet<FontConfig.LOADABLE_BLOCK> flag) {
        if (!loaded_font.containsKey(font_name)) {
            return false;
        } else {
            return loaded_font.get(font_name).loaded.containsAll(flag);
        }
    }

    public boolean IsFontLoaded(String font_name) {
        return loaded_font.containsKey(font_name);
    }

    public FontConfig getFontSettings(String font_name) {
        return loaded_font.get(font_name);
    }

    public static String getCharFileAsString(char c, String font) {
        return font + "_U" + String.format("%04X", (int) c);
    }


}
