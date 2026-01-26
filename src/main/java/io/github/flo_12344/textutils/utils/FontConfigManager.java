package io.github.flo_12344.textutils.utils;

import javax.swing.*;
import java.util.HashMap;

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
    }

    private HashMap<String, FontSettings> loaded_font = new HashMap<>();

    public void Load(String font_name, float size) {
    }

    public void LoadRangesOf(String font_name, int start, int end) {
    }

    public void LoadFlags(String font_name, int flag) {
    }

    public void LoadFlags(String font_name, String flag) {
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
