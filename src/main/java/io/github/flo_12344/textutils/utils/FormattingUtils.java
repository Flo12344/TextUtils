package io.github.flo_12344.textutils.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FormattingUtils {
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

    public static String getUIColor(String color) {
        return
                switch (color) {
                    case "yellow" -> "#ffff00";
                    case "light_blue" -> "#add8e6";
                    case "blue" -> "#0000ff";
                    case "dark_blue" -> "#00008b";
                    case "white" -> "#ffffff";
                    case "aqua" -> "#00ffff";
                    case "lime" -> "#00ff00";
                    case "red" -> "#ff0000";
                    case "orange" -> "#ffA500";
                    case "purple" -> "#800080";
                    case "pink" -> "#ffc0cb";
                    case "green" -> "#008000";
                    case "brown" -> "#a52a2a";
                    case "beige" -> "#f5f5dc";
                    case "grey" -> "#808080";
                    case "black" -> "#000000";
                    default -> "#ffffff";
                } + "\n";
    }

    public static List<List<ColoredText>> parseFormattedText(String input) {
        List<List<ColoredText>> formattedTexts = new ArrayList<>();
        var to_process = input.split("\\\\n");

        int pos = 0;
        for (var line : to_process) {
            formattedTexts.add(new ArrayList<>());
            if (line.isEmpty()) {
                pos++;
                continue;
            }
            Pattern pattern = Pattern.compile("\\{(\\w+)\\}(.*?)\\{\\\\\\1\\}");
            Matcher matcher = pattern.matcher(line);

            int lastEnd = 0;
            while (matcher.find()) {
                if (matcher.start() > lastEnd) {
                    formattedTexts.get(pos).add(new ColoredText(line.substring(lastEnd, matcher.start()), "white"));
                }
                String color = matcher.group(1);
                String text = matcher.group(2);
                formattedTexts.get(pos).add(new ColoredText(text, color.toLowerCase()));

                lastEnd = matcher.end();
            }

            if (lastEnd < line.length()) {
                formattedTexts.get(pos).add(new ColoredText(line.substring(lastEnd), "white"));
            }
            pos++;
        }

        return formattedTexts;
    }
}
