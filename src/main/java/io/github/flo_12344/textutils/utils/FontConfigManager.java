package io.github.flo_12344.textutils.utils;

import io.github.flo_12344.textutils.TextUtils;
import io.github.flo_12344.textutils.runtime.FontRuntimeManager;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Objects;

public class FontConfigManager {
    public static FontConfigManager INSTANCE;

    public class FontSettings {
        public static final int BASIC_LATIN_FLAG = 1; // 0 -> 127
        public static final int LATIN_EXT_1_FLAG = 2; // 128 -> 255
        public static final int LATIN_EXT_AB_FLAG = 4; // 256 -> 591
        public static final int PUNCTUATION_FLAG = 8; // 8192 -> 8303
        public static final int CURRENCY_FLAG = 16; // 8352 -> 8377
        public static final int ARROW_FLAG = 32; // 8592 -> 8703
        public static final int BOX_FLAG = 64; // 9472 -> 9599

        // added for later

//        public static final int CYRILIC_FLAG = 128; // 1024 -> 1279

//        public static final int GREEK_FLAG = 256; // 880 -> 1023

//        public static final int DEVANAGARI_FLAG = 512; //

//        public static final int CJK_FLAG = 1024; // 19968 -> 40907
//        public static final int CJK_A_FLAG = 2048; // 13312 -> 19893
//        public static final int CJK_B_FLAG = 4096; // 131072 -> 173782
//        public static final int CJK_C_FLAG = 8192; // 173824 -> 177972
//        public static final int CJK_D_FLAG = 16384; // 177984 -> 178205

//        public static final int HIRAGANE_FLAG = 32768; // 12352 -> 12447
//        public static final int KATAKANA_FLAG = 65536; // 12448 -> 12543

//        public static final int HANGUL_FLAG = 131072; // 44032 -> 55215

//        public static final int THAI_FLAG = 262144; // 3585 -> 3675

//        public static final int HEBREW_FLAG = 524288; // 1425 -> 1524
//        public static final int ARABIC_FLAG = 1048576; // 1536 -> 1791

        public int loaded = 0;
        public FontMetrics fm;
        public int max_width = 0;
        public int max_height = 0;
        public int texture_size = 0;
    }

    private HashMap<String, FontSettings> loaded_font = new HashMap<>();

    public void Init(String font_name) throws IOException, FontFormatException {
        String dir_path = TextUtils.INSTANCE.getDataDirectory() + File.separator + "data" + File.separator + font_name;
        File dir = new File(dir_path);
        if (!dir.exists())
            dir.mkdirs();
        Font font =
                Font.createFont(Font.TRUETYPE_FONT, new File(TextUtils.INSTANCE.getDataDirectory() + File.separator + "fonts" + File.separator + font_name + ".ttf"))
                        .deriveFont(32f);
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
        FontSettings fs = new FontSettings();
        fs.fm = fm;
        fs.max_height = fm.getHeight();
        fs.max_width = maxWidth;
        fs.texture_size = potSize;

        loaded_font.put(font_name, fs);
        dir_path = FontRuntimeManager.resolveRuntimeBasePath().resolve(FontRuntimeManager.RUNTIME_ASSETS_DIR).resolve(FontRuntimeManager.RUNTIME_MODEL_DIR) + File.separator + font_name;
        File f = new File(dir_path);
        f.mkdirs();
        dir_path = FontRuntimeManager.resolveRuntimeBasePath().resolve(FontRuntimeManager.RUNTIME_ASSETS_DIR).resolve("Common").resolve(FontRuntimeManager.MODEL_TEXTURE_PATH) + File.separator + font_name;
        f = new File(dir_path);
        f.mkdirs();
        ModelGenerator.genCharBlockyModel(dir_path, potSize);

    }

    public void LoadRanges(String font_name, int start, int end) throws IOException, FontFormatException {
        Font font = Font.createFont(Font.TRUETYPE_FONT, new File(TextUtils.INSTANCE.getDataDirectory() + File.separator + "fonts" + File.separator + font_name + ".ttf")).deriveFont(32f);
        for (char c = (char) start; c < end; c++) {
            LoadCharacter(font_name, c, font);
        }
    }

    public void LoadFlags(String font_name, int flag) throws IOException, FontFormatException {
        if ((flag & FontSettings.BASIC_LATIN_FLAG) == FontSettings.BASIC_LATIN_FLAG) {
            LoadRanges(font_name, 0, 127);
        }
        if ((flag & FontSettings.LATIN_EXT_1_FLAG) == FontSettings.LATIN_EXT_1_FLAG) {
            LoadRanges(font_name, 128, 255);
        }
        if ((flag & FontSettings.LATIN_EXT_AB_FLAG) == FontSettings.LATIN_EXT_AB_FLAG) {
            LoadRanges(font_name, 256, 591);
        }
        if ((flag & FontSettings.PUNCTUATION_FLAG) == FontSettings.PUNCTUATION_FLAG) {
            LoadRanges(font_name, 8192, 8303);
        }
        if ((flag & FontSettings.CURRENCY_FLAG) == FontSettings.CURRENCY_FLAG) {
            LoadRanges(font_name, 8352, 8377);
        }
        if ((flag & FontSettings.ARROW_FLAG) == FontSettings.ARROW_FLAG) {
            LoadRanges(font_name, 8592, 8703);
        }
        if ((flag & FontSettings.BOX_FLAG) == FontSettings.BOX_FLAG) {
            LoadRanges(font_name, 9472, 9599);
        }
    }

    public void LoadFlags(String font_name, String flag) throws IOException, FontFormatException {
        if (Objects.equals(flag.toLowerCase(), "latin")) {
            LoadFlags(font_name, FontSettings.BASIC_LATIN_FLAG);
        }
        if (Objects.equals(flag.toLowerCase(), "latin_1")) {
            LoadFlags(font_name, FontSettings.LATIN_EXT_1_FLAG);
        }
        if (Objects.equals(flag.toLowerCase(), "latin_ab")) {
            LoadFlags(font_name, FontSettings.LATIN_EXT_AB_FLAG);
        }
        if (Objects.equals(flag.toLowerCase(), "punct")) {
            LoadFlags(font_name, FontSettings.PUNCTUATION_FLAG);
        }
        if (Objects.equals(flag.toLowerCase(), "currency")) {
            LoadFlags(font_name, FontSettings.CURRENCY_FLAG);
        }
        if (Objects.equals(flag.toLowerCase(), "arrow")) {
            LoadFlags(font_name, FontSettings.ARROW_FLAG);
        }
        if (Objects.equals(flag.toLowerCase(), "box")) {
            LoadFlags(font_name, FontSettings.BOX_FLAG);
        }
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

    public boolean IsFontLoaded(String font_name, int flag) {
        if (!loaded_font.containsKey(font_name)) {
            return false;
        } else {
            if (flag == 0)
                return true;
            return (loaded_font.get(font_name).loaded & flag) == flag;
        }
    }


}
