package io.github.flo_12344.textutils.utils;

import com.hypixel.hytale.codec.Codec;
import com.hypixel.hytale.codec.KeyedCodec;
import com.hypixel.hytale.codec.builder.BuilderCodec;
import io.sentry.util.Pair;

import javax.annotation.Nonnull;
import java.awt.*;
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
            .append(new KeyedCodec<>("Loaded_By", Codec.STRING),
                    (fontConfig, string) ->
                            fontConfig.loaded_by = SOURCE.valueOf(string),
                    fontConfig -> fontConfig.loaded_by.name()).add()
            .build();

    public enum LOADABLE_BLOCK {
        BASIC_LATIN, // 1 -> 127
        LATIN_EXT_1, // 128 -> 255
        LATIN_EXT_AB, // 256 -> 591
        PUNCTUATION, // 8192 -> 8303
        CURRENCY, // 8352 -> 8377
        ARROW, // 8592 -> 8703
        BOX,  // 9472 -> 9599

        CYRILLIC, // 1024 -> 1279
        CYRILLIC_SUP, // 1280 -> 1319
        CYRILLIC_A, // 11744 -> 11775
        CYRILLIC_B, // 42560 -> 42647

        GREEK, // 880 -> 1023
        GREEK_EXT, // 7936 -> 8190

        DEVANAGARI, // 2304 -> 2431
        DEVANAGARI_EXT, // 43232 -> 43256

        CJK, // 19968 -> 40907
        CJK_A, // 13312 -> 19893
        CJK_B, // 131072 -> 173782
        CJK_C, // 173824 -> 177972
        CJK_D, // 177984 -> 178205

        CJK_SYM_PUNC, // 12288 -> 12351

        HIRAGANA, // 12352 -> 12447
        KATAKANA, // 12448 -> 12543

        HANGUL, // 44032 -> 55215

        THAI, // 3585 -> 3675

        HEBREW, // 1425 -> 1524
        ARABIC, // 1536 -> 1791
        ARABIC_SUP, // 1872 -> 1919

        MISC, // 9728 -> 9983
        EMOTE, // 128513 -> 128591
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
    public int glyph_size = 0;

    public static String getLoadedRangesAsString(EnumSet<LOADABLE_BLOCK> loaded) {
        StringBuilder result = new StringBuilder();
        loaded.forEach(flag -> {
            var se = GetRange(flag);
            result.append(flag.toString())
                    .append("(")
                    .append(se.getFirst())
                    .append("-")
                    .append(se.getSecond())
                    .append("), ");
        });
        // Remove the trailing comma and space if the result is not empty
        if (!result.isEmpty()) {
            result.setLength(result.length() - 2);
        } else {
            result.append("No ranges loaded");
        }

        return result.toString();
    }


    public static Pair<Integer, Integer> GetRange(LOADABLE_BLOCK flag) {
        return switch (flag) {
            case BASIC_LATIN -> new Pair<>(1, 127);
            case LATIN_EXT_1 -> new Pair<>(128, 255);
            case LATIN_EXT_AB -> new Pair<>(256, 591);
            case PUNCTUATION -> new Pair<>(8192, 8303);
            case CURRENCY -> new Pair<>(8352, 8377);
            case ARROW -> new Pair<>(8592, 8703);
            case BOX -> new Pair<>(9472, 9599);
            case CYRILLIC -> new Pair<>(1024, 1279);
            case CYRILLIC_SUP -> new Pair<>(1280, 1319);
            case CYRILLIC_A -> new Pair<>(11744, 11775);
            case CYRILLIC_B -> new Pair<>(42560, 42647);
            case GREEK -> new Pair<>(880, 1023);
            case GREEK_EXT -> new Pair<>(7936, 8190);
            case DEVANAGARI -> new Pair<>(2304, 2431);
            case DEVANAGARI_EXT -> new Pair<>(43232, 43256);
            case CJK -> new Pair<>(19968, 40907);
            case CJK_A -> new Pair<>(13312, 19893);
            case CJK_B -> new Pair<>(131072, 173782);
            case CJK_C -> new Pair<>(173824, 177972);
            case CJK_D -> new Pair<>(177984, 178205);
            case CJK_SYM_PUNC -> new Pair<>(12288, 12351);
            case HIRAGANA -> new Pair<>(12352, 12447);
            case KATAKANA -> new Pair<>(12448, 12542);
            case HANGUL -> new Pair<>(44032, 55215);
            case THAI -> new Pair<>(3585, 3675);
            case HEBREW -> new Pair<>(1425, 1524);
            case ARABIC -> new Pair<>(1536, 1791);
            case ARABIC_SUP -> new Pair<>(1872, 1919);
            case MISC -> new Pair<>(9728, 9983);
            case EMOTE -> new Pair<>(128513, 128591);
            default -> new Pair<>(0, 0);
        };
    }

    public static LOADABLE_BLOCK GetFromStr(String flag) {
        return switch (flag) {
            case "latin" -> LOADABLE_BLOCK.BASIC_LATIN;
            case "latin_1" -> LOADABLE_BLOCK.LATIN_EXT_1;
            case "latin_ab" -> LOADABLE_BLOCK.LATIN_EXT_AB;

            case "punct" -> LOADABLE_BLOCK.PUNCTUATION;
            case "currency" -> LOADABLE_BLOCK.CURRENCY;
            case "arrow" -> LOADABLE_BLOCK.ARROW;
            case "box" -> LOADABLE_BLOCK.BOX;

            case "cyrilic" -> LOADABLE_BLOCK.CYRILLIC;
            case "cyrilic_sup" -> LOADABLE_BLOCK.CYRILLIC_SUP;
            case "cyrilic_a" -> LOADABLE_BLOCK.CYRILLIC_A;
            case "cyrilic_b" -> LOADABLE_BLOCK.CYRILLIC_B;

            case "greek" -> LOADABLE_BLOCK.GREEK;
            case "greek_ext" -> LOADABLE_BLOCK.GREEK_EXT;

            case "devanagari" -> LOADABLE_BLOCK.DEVANAGARI;
            case "devanagari_ext" -> LOADABLE_BLOCK.DEVANAGARI_EXT;

            case "cjk" -> LOADABLE_BLOCK.CJK;
            case "cjk_a" -> LOADABLE_BLOCK.CJK_A;
            case "cjk_b" -> LOADABLE_BLOCK.CJK_B;
            case "cjk_c" -> LOADABLE_BLOCK.CJK_C;
            case "cjk_d" -> LOADABLE_BLOCK.CJK_D;
            case "cjk_sym_punc" -> LOADABLE_BLOCK.CJK_SYM_PUNC;
            case "hiragana" -> LOADABLE_BLOCK.HIRAGANA;
            case "katakana" -> LOADABLE_BLOCK.KATAKANA;
            case "hangul" -> LOADABLE_BLOCK.HANGUL;
            case "thai" -> LOADABLE_BLOCK.THAI;

            case "hebrew" -> LOADABLE_BLOCK.HEBREW;
            case "arabic" -> LOADABLE_BLOCK.ARABIC;
            case "arabic_sup" -> LOADABLE_BLOCK.ARABIC_SUP;

            case "misc" -> LOADABLE_BLOCK.MISC;
            case "emote" -> LOADABLE_BLOCK.EMOTE;
            default -> throw new IllegalStateException("Unexpected value: " + flag);
        };
    }

    public static String getAllOptions() {
        return "latin|" +
                "latin_1|" +
                "latin_ab|" +
                "punct|" +
                "currency|" +
                "arrow|" +
                "box|" +
                "cyrilic|" +
                "cyrilic_sup|" +
                "cyrilic_a|" +
                "cyrilic_b|" +
                "greek|" +
                "greek_ext|" +
                "devanagari|" +
                "devanagari_ext|" +
                "cjk|" +
                "cjk_a|" +
                "cjk_b|" +
                "cjk_c|" +
                "cjk_d|" +
                "cjk_sym_punc|" +
                "hiragana|" +
                "katakana|" +
                "hangul|" +
                "thai|" +
                "hebrew|" +
                "arabic|" +
                "arabic_sup|" +
                "misc|" +
                "emote";
    }
}
