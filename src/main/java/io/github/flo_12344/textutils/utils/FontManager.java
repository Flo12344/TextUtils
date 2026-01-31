package io.github.flo_12344.textutils.utils;

import com.hypixel.hytale.codec.KeyedCodec;
import com.hypixel.hytale.codec.builder.BuilderCodec;
import com.hypixel.hytale.codec.codecs.map.MapCodec;
import com.hypixel.hytale.server.core.asset.AssetModule;
import com.hypixel.hytale.server.core.universe.Universe;
import com.hypixel.hytale.server.core.util.io.FileUtil;
import io.github.flo_12344.textutils.TextUtils;
import io.github.flo_12344.textutils.runtime.FontRuntimeManager;
import io.sentry.util.Pair;

import javax.annotation.Nonnull;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
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
                        FontManager.INSTANCE.InitFromSave(fontConfig.font_file, s, fontConfig.size, fontConfig.loaded, fontConfig.loaded_by);
                    } catch (IOException | FontFormatException e) {
                        throw new RuntimeException(e);
                    }
                });
            })
            .build();
    public static FontManager INSTANCE;

    private final HashMap<String, FontConfig> loaded_font = new HashMap<>();
    public static Path FONT_DIR;

    public HashMap<String, FontConfig> getLoaded_font() {
        return loaded_font;
    }

    private FontManager() {
        INSTANCE = this;
        FONT_DIR = TextUtils.INSTANCE.getDataDirectory().resolve("fonts");
    }

    private void InitFromSave(String font_name, String font_id, float size, EnumSet<FontConfig.LOADABLE_BLOCK> loaded, FontConfig.SOURCE source) throws IOException, FontFormatException {
        Init(font_name, font_id, size, source);
        LoadFlags(font_id, loaded);
    }

    /**
     * Font added through this methods will not be deletable through user commands
     */
    public boolean InitFromMod(String font_name, String font_id, float size) throws IOException, FontFormatException {
        return Init(font_name, font_id, size, FontConfig.SOURCE.MODS);
    }


    /**
     * Font added through this methods will be deletable through user commands
     */
    public boolean InitFromUserCommands(String font_name, String font_id, float size) throws IOException, FontFormatException {
        return Init(font_name, font_id, size, FontConfig.SOURCE.USER);
    }

    private boolean Init(String font_name, String font_id, float size, FontConfig.SOURCE source) throws IOException, FontFormatException {
        Font font;
        var file = new File(font_name + ".ttf");
        if (file.exists()) {
            font = Font.createFont(Font.TRUETYPE_FONT, file)
                    .deriveFont(size);
        } else {
            file = new File(font_name + ".otf");
            if (file.exists()) {
                font = Font.createFont(Font.TRUETYPE_FONT, file)
                        .deriveFont(size);
            } else return false;
        }

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
        fs.glyph_size = potSize;
        fs.font_file = font_name;
        fs.size = size;

        loaded_font.put(font_id, fs);
        String dir_path = FontRuntimeManager.resolveRuntimeBasePath()
                .resolve(FontRuntimeManager.RUNTIME_ASSETS_DIR)
                .resolve(FontRuntimeManager.RUNTIME_MODEL_DIR) + File.separator + font_id;
        File f = new File(dir_path);
        f.mkdirs();

        dir_path = FontRuntimeManager.resolveRuntimeBasePath()
                .resolve(FontRuntimeManager.RUNTIME_ASSETS_DIR).resolve("Common")
                .resolve(FontRuntimeManager.UI_TEXTURE_PATH) + File.separator + font_id;
        f = new File(dir_path);
        f.mkdirs();
        dir_path = FontRuntimeManager.resolveRuntimeBasePath()
                .resolve(FontRuntimeManager.RUNTIME_ASSETS_DIR)
                .resolve("Common")
                .resolve(FontRuntimeManager.MODEL_TEXTURE_PATH) + File.separator + font_id;
        f = new File(dir_path);
        f.mkdirs();
        if (AssetModule.get().getAssetPack(FontRuntimeManager.RUNTIME_ASSETS_PACK) == null && TextUtils.INSTANCE.fontRuntimeManager != null) {
            TextUtils.INSTANCE.fontRuntimeManager.registerRuntimePack();
        }
        return true;
    }

    public void RenderAtlases(String font_name, int start, int end, String atlas, Font font) throws IOException {
        var fs = loaded_font.get(font_name);
        int total = end - start + 1;

        int totalGlyphs = end - start + 1;
        int padding = 2;
        int cellSize = fs.glyph_size + padding * 2;
        int cols = (int) Math.ceil(Math.sqrt(totalGlyphs));
        int rows = (int) Math.ceil((double) totalGlyphs / cols);
        int rawSize = Math.max(cols, rows) * cellSize;

        int atlasSize;
        int pageCount = 0;

        if (rawSize <= 512) {
            atlasSize = 512;
        } else if (rawSize <= 1024) {
            atlasSize = 1024;
        } else if (rawSize <= 2048) {
            atlasSize = 2048;
        } else {
            atlasSize = 2048; // page size
            int glyphsPerRow = atlasSize / cellSize;
            int glyphsPerPage = glyphsPerRow * glyphsPerRow;
            pageCount = (int) Math.ceil(
                    (double) totalGlyphs / glyphsPerPage
            );
        }
        List<BufferedImage> pages = new ArrayList<>();

        pages.add(new BufferedImage(
                atlasSize, atlasSize, BufferedImage.TYPE_INT_ARGB
        ));

        Graphics2D g2d = pages.getLast().createGraphics();
        g2d.setFont(font);
        g2d.setColor(Color.WHITE);
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
                RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);

        String visual_out_dir = FontRuntimeManager.resolveRuntimeBasePath()
                .resolve(FontRuntimeManager.RUNTIME_ASSETS_DIR)
                .resolve("Common")
                .resolve(FontRuntimeManager.MODEL_TEXTURE_PATH)
                .resolve(font_name).toString();

        String asset_out_dir = FontRuntimeManager.resolveRuntimeBasePath()
                .resolve(FontRuntimeManager.RUNTIME_ASSETS_DIR)
                .resolve(FontRuntimeManager.RUNTIME_MODEL_DIR)
                .resolve(font_name).toString();

        int xpos = 0, ypos = 0;
        int currentPage = 0;
        for (var i = 0; i < total; i++) {
            char ch = (char) (start + i);
            //center to glyph bounds
            int charWidth = fs.fm.charWidth(ch);
            int cellX = xpos * cellSize + padding;
            int cellY = ypos * cellSize + padding;

            int x = cellX + (fs.glyph_size - charWidth) / 2;
            int y = cellY + (fs.glyph_size - (fs.fm.getAscent() + fs.fm.getDescent())) / 2 + fs.fm.getAscent();
            g2d.drawString(String.valueOf(ch), x, y);
            ModelGenerator.genCharBlockyModel(visual_out_dir + File.separator + "U" + String.format("%04X", (int) ch), fs.glyph_size, cellX, cellY);
            ModelGenerator.genEntityModelAsset(asset_out_dir, ch, font_name, atlas + (pageCount != 0 ? ("_" + currentPage) : ""));
            xpos++;
            if (xpos >= cols) {
                xpos = 0;
                ypos++;
                if (ypos * cellSize + padding >= atlasSize && pageCount != 0) {
                    currentPage++;
                    ypos = 0;
                    pages.add(new BufferedImage(
                            atlasSize, atlasSize, BufferedImage.TYPE_INT_ARGB
                    ));
                    g2d.dispose();
                    g2d = pages.getLast().createGraphics();
                    g2d.setFont(font);
                    g2d.setColor(Color.WHITE);
                    g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
                            RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
                    g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                            RenderingHints.VALUE_ANTIALIAS_ON);
                }
            }
        }
        g2d.dispose();
        currentPage = 0;
        for (var bufferedImage : pages) {
            ImageIO.write(bufferedImage, "PNG", new File(visual_out_dir + File.separator + atlas + (pageCount != 0 ? ("_" + currentPage) : "") + ".png"));
            currentPage++;
        }
    }

    public void LoadRange(String font_name, int start, int end, String block) throws IOException, FontFormatException {
        Font font = Font.createFont(Font.TRUETYPE_FONT, new File(TextUtils.INSTANCE.getDataDirectory() + File.separator + "fonts" + File.separator + font_name + ".ttf")).deriveFont(32f);
        for (char c = (char) start; c < end; c++) {
            LoadCharacter(font_name, c, font);
        }
        RenderAtlases(font_name, start, end, block, font);
    }

    private void LoadRange(String font_name, Pair<Integer, Integer> se, String block) throws IOException, FontFormatException {
        LoadRange(font_name, se.getFirst(), se.getSecond(), block);
    }

    public void LoadFlags(String font_name, EnumSet<FontConfig.LOADABLE_BLOCK> flags) throws IOException, FontFormatException {
        for (var flag : flags) {
            LoadRange(font_name, FontConfig.GetRange(flag), flag.toString());
        }
        loaded_font.get(font_name).loaded.addAll(flags);
    }

    public void LoadFlags(String font_name, List<String> flags_str) throws IOException, FontFormatException {
        EnumSet<FontConfig.LOADABLE_BLOCK> flags = EnumSet.noneOf(FontConfig.LOADABLE_BLOCK.class);
        for (var flag : flags_str) {
            flags.add(FontConfig.GetFromStr(flag));
        }
        LoadFlags(font_name, flags);
    }

    private void LoadCharacter(String font_name, char c, Font font) throws IOException {
        String ui_tex_dir_path = FontRuntimeManager.resolveRuntimeBasePath().resolve(FontRuntimeManager.RUNTIME_ASSETS_DIR).resolve("Common").resolve(FontRuntimeManager.UI_TEXTURE_PATH) + File.separator + font_name;
        var out_ui = new File(ui_tex_dir_path + File.separator + "U" + String.format("%04X", (int) c) + ".png");
        if (out_ui.exists())
            return;
        var fs = loaded_font.get(font_name);
        BufferedImage image = new BufferedImage(
                fs.glyph_size, fs.glyph_size, BufferedImage.TYPE_INT_ARGB
        );

        Graphics2D g2d = image.createGraphics();
        g2d.setFont(font);
        g2d.setColor(Color.WHITE);

        int charWidth = fs.fm.charWidth(c);
        int x = (fs.glyph_size - charWidth) / 2;
        int y = (fs.glyph_size + fs.fm.getAscent() - fs.fm.getDescent()) / 2;

        g2d.drawString(String.valueOf(c), x, y);
        g2d.dispose();
        ImageIO.write(image, "PNG", out_ui);
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

    public void RemoveFont(String font_name) throws IOException {
        FileUtil.deleteDirectory(FontRuntimeManager.resolveRuntimeBasePath()
                .resolve(FontRuntimeManager.RUNTIME_ASSETS_DIR)
                .resolve(FontRuntimeManager.RUNTIME_MODEL_DIR)
                .resolve(font_name));
        FileUtil.deleteDirectory(FontRuntimeManager.resolveRuntimeBasePath()
                .resolve(FontRuntimeManager.RUNTIME_ASSETS_DIR)
                .resolve("Common")
                .resolve(FontRuntimeManager.UI_TEXTURE_PATH).resolve(font_name));
        FileUtil.deleteDirectory(FontRuntimeManager.resolveRuntimeBasePath()
                .resolve(FontRuntimeManager.RUNTIME_ASSETS_DIR)
                .resolve("Common")
                .resolve(FontRuntimeManager.MODEL_TEXTURE_PATH).resolve(font_name));
        loaded_font.remove(font_name);
    }

}
