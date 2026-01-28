package io.github.flo_12344.textutils.utils;

import com.hypixel.hytale.codec.Codec;
import com.hypixel.hytale.codec.KeyedCodec;
import com.hypixel.hytale.codec.builder.BuilderCodec;
import com.hypixel.hytale.codec.codecs.EnumCodec;
import com.hypixel.hytale.server.core.util.Config;
import org.checkerframework.checker.nullness.compatqual.NonNullDecl;

import java.awt.*;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.EnumSet;

public class FontConfig {
    public static final BuilderCodec<FontConfig> CODEC = BuilderCodec.<FontConfig>builder(
                    FontConfig.class, FontConfig::new)
            .append(new KeyedCodec<>("Font", Codec.STRING),
                    (fontConfig, s) -> fontConfig.font_file = s,
                    fontConfig -> fontConfig.font_file).add()
            .append(new KeyedCodec<>("Size", Codec.FLOAT),
                    (fontConfig, v) -> fontConfig.size = v,
                    fontConfig -> fontConfig.size).add()
            .append(new KeyedCodec<>("Loaded", Codec.STRING_ARRAY),
                    (fontConfig, strings) ->
                            fontConfig.loaded = Arrays.stream(strings).map(LOADABLE_BLOCK::valueOf).collect(() -> EnumSet.noneOf(LOADABLE_BLOCK.class), EnumSet::add, EnumSet::addAll),
                    fontConfig -> fontConfig.loaded.stream().map(Enum::name).toArray(String[]::new)).add()
//            .append(new KeyedCodec<>("Loaded_By", Codec.STRING),
//                    (fontConfig, string) ->
//                            fontConfig.loaded_by = SOURCE.valueOf(string),
//                    fontConfig -> fontConfig.loaded_by.name()).add()
            .build();

    public enum LOADABLE_BLOCK {
        BASIC_LATIN, // 1 -> 127
        LATIN_EXT_1, // 128 -> 255
        LATIN_EXT_AB, // 256 -> 591
        PUNCTUATION, // 8192 -> 8303
        CURRENCY, // 8352 -> 8377
        ARROW, // 8592 -> 8703
        BOX,  // 9472 -> 9599

        // added for later

//        CYRILIC, // 1024 -> 1279

//        GREEK , // 880 -> 1023

//        DEVANAGARI, //

//        CJK_FLAG, // 19968 -> 40907
//        CJK_A_FLAG, // 13312 -> 19893
//        CJK_B, // 131072 -> 173782
//        CJK_C, // 173824 -> 177972
//        CJK_D, // 177984 -> 178205

//        HIRAGANE, // 12352 -> 12447
//        KATAKANA, // 12448 -> 12543

//        HANGUL, // 44032 -> 55215

//        THAI, // 3585 -> 3675

//        HEBREW, // 1425 -> 1524
//        ARABIC, // 1536 -> 1791
    }

    public enum SOURCE {
        MODS, USER
    }

    public EnumSet<LOADABLE_BLOCK> loaded = EnumSet.noneOf(LOADABLE_BLOCK.class);
    public SOURCE loaded_by = SOURCE.USER;
    public float size = 0;
    public String font_file;

    public FontMetrics fm;
    public int max_width = 0;
    public int max_height = 0;
    public int texture_size = 0;

    public static String getLoadedRangesAsString(EnumSet<FontConfig.LOADABLE_BLOCK> loaded) {
        StringBuilder result = new StringBuilder();
        loaded.forEach(flags -> {
            switch (flags) {
                case BASIC_LATIN -> result.append("Basic Latin (0-127), ");
                case LATIN_EXT_1 -> result.append("Latin Extended-1 (128-255), ");
                case LATIN_EXT_AB -> result.append("Latin Extended-A/B (256-591), ");
                case PUNCTUATION -> result.append("Punctuation (8192-8303), ");
                case CURRENCY -> result.append("Currency (8352-8377), ");
                case BOX -> result.append("Arrows (8592-8703), ");
                case ARROW -> result.append("Box Drawing (9472-9599), ");
                default -> throw new IllegalStateException("Unexpected value: " + flags);
            }
        });
        // Remove the trailing comma and space if the result is not empty
        if (!result.isEmpty()) {
            result.setLength(result.length() - 2);
        } else {
            result.append("No ranges loaded");
        }

        return result.toString();
    }
}
