package io.github.flo_12344.textutils.utils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;

public class ModelGenerator {
    public static void genCharBlockyModel(String out_dir, int size, int x, int y) throws IOException {
        FileWriter out = new FileWriter(out_dir + ".blockymodel");
        out.append("{\n");
        out.append("  \"nodes\": [\n");
        out.append("    {\n");
        out.append("      \"id\": \"1\",\n");
        out.append("      \"name\": \"c\",\n");
        out.append("      \"position\": {\n");
        out.append("        \"x\": 0,\n");
        out.append("        \"y\": 0,\n");
        out.append("        \"z\": 0\n");
        out.append("      },\n");
        out.append("      \"orientation\": {\n");
        out.append("        \"x\": 0,\n");
        out.append("        \"y\": 1,\n");
        out.append("        \"z\": 0,\n");
        out.append("        \"w\": 0\n");
        out.append("      },\n");
        out.append("      \"shape\": {\n");
        out.append("        \"type\": \"quad\",\n");
        out.append("        \"offset\": {\n");
        out.append("          \"x\": 0,\n");
        out.append("          \"y\": 0,\n");
        out.append("          \"z\": 0\n");
        out.append("        },\n");
        out.append("        \"stretch\": {\n");
        out.append("          \"x\": 1,\n");
        out.append("          \"y\": 1,\n");
        out.append("          \"z\": 1\n");
        out.append("        },\n");
        out.append("        \"settings\": {\n");
        out.append("          \"isPiece\": false,\n");
        out.append("          \"size\": {\n");
        out.append("            \"x\": %s,\n".formatted(size));
        out.append("            \"y\": %s\n".formatted(size));
        out.append("          },\n");
        out.append("          \"normal\": \"+Z\",\n");
        out.append("          \"isStaticBox\": true\n");
        out.append("        },\n");
        out.append("        \"textureLayout\": {\n");
        out.append("          \"front\": {\n");
        out.append("            \"offset\": {\n");
        out.append("              \"x\": %s,\n".formatted(x));
        out.append("              \"y\": %s\n".formatted(y));
        out.append("            },\n");
        out.append("            \"mirror\": {\n");
        out.append("              \"x\": false,\n");
        out.append("              \"y\": false\n");
        out.append("            },\n");
        out.append("            \"angle\": 0\n");
        out.append("          }\n");
        out.append("        },\n");
        out.append("        \"unwrapMode\": \"custom\",\n");
        out.append("        \"visible\": true,\n");
        out.append("        \"doubleSided\": true,\n");
        out.append("        \"shadingMode\": \"flat\"\n");
        out.append("      }\n");
        out.append("    }\n");
        out.append("  ],\n");
        out.append("  \"format\": \"prop\",\n");
        out.append("  \"lod\": \"auto\"\n");
        out.append("}");
        out.close();
    }

    public static void genEntityModelAsset(String out_dir, char c, String font_name, String file) throws IOException {
        FileWriter out = new FileWriter(out_dir + File.separator + font_name + "_U" + String.format("%04X", (int) c) + ".json");
        out.append("{\n");
        out.append("\"Model\": \"Items/Textutils/" + font_name + "/U" + String.format("%04X", (int) c) + ".blockymodel\",\n");
        out.append("    \"Texture\": \"Items/Textutils/" + font_name + "/" + file + ".png\",\n");
        out.append("\"HitBox\": {\n" +
                "    \"Max\": {\n" +
                "      \"X\": 0.02,\n" +
                "      \"Y\": 0.05,\n" +
                "      \"Z\": 0.02\n" +
                "    },\n" +
                "    \"Min\": {\n" +
                "      \"X\": 0,\n" +
                "      \"Y\": 0,\n" +
                "      \"Z\": 0\n" +
                "    }\n" +
                "  },\n" +
                "  \"MinScale\": 0.25,\n" +
                "  \"MaxScale\": 3");
        out.append("\n}");
        out.close();
    }
}
