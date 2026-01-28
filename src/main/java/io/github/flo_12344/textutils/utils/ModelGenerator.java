package io.github.flo_12344.textutils.utils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class ModelGenerator {
    public static void genCharBlockyModel(String out_dir, int size) throws IOException {
        FileWriter out = new FileWriter(out_dir + File.separator + "base_model.blockymodel");
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
        out.append("              \"x\": 0,\n");
        out.append("              \"y\": 0\n");
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

    public static void genEntityModelAsset(String out_dir, char c, String font_name) throws IOException {
        FileWriter out = new FileWriter(out_dir + File.separator + font_name + "_U" + String.format("%04X", (int) c) + ".json");
        out.append("{\n");
        if (c == 0)
            out.append("\"Model\": \"Items/Textutils/" + font_name + "/base_model.blockymodel\",\n");
        else
            out.append("\"Parent\": \"" + font_name + "_U0000\",");
        out.append("    \"Texture\": \"Items/Textutils/" + font_name + "/U" + String.format("%04X", (int) c) + ".png\",\n");
        out.append("    \"Id\": \"" + font_name + "_U" + String.format("%04X", (int) c) + "\"");
        if (c == 0) {
            out.append(",\n");
            out.append("    \"HitBox\": {\n");
            out.append("        \"Max\": {\n");
            out.append("            \"X\": 0.02,\n");
            out.append("            \"Y\": 0.05,\n");
            out.append("            \"Z\": 0.02\n");
            out.append("        },\n");
            out.append("        \"Min\": {\n");
            out.append("            \"X\": 0,\n");
            out.append("            \"Y\": 0,\n");
            out.append("            \"Z\": 0\n");
            out.append("        }\n");
            out.append("    },\n");
            out.append("    \"MinScale\": 0.25,\n");
            out.append("    \"MaxScale\": 3");
        }
        out.append("\n}");
        out.close();
    }
}
