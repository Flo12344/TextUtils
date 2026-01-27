package io.github.flo_12344.textutils.utils;

import com.google.gson.JsonObject;
import com.hypixel.hytale.server.core.universe.Universe;
import com.hypixel.hytale.server.core.util.io.FileUtil;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class ModelGenerator {
    public static void genBaseCharacterModel(String out_dir, int size) throws IOException {
        String json = "{\n" +
                "  \"nodes\": [\n" +
                "    {\n" +
                "      \"id\": \"1\",\n" +
                "      \"name\": \"c\",\n" +
                "      \"position\": {\n" +
                "        \"x\": 0,\n" +
                "        \"y\": 0,\n" +
                "        \"z\": 0\n" +
                "      },\n" +
                "      \"orientation\": {\n" +
                "        \"x\": 0,\n" +
                "        \"y\": 1,\n" +
                "        \"z\": 0,\n" +
                "        \"w\": 0\n" +
                "      },\n" +
                "      \"shape\": {\n" +
                "        \"type\": \"quad\",\n" +
                "        \"offset\": {\n" +
                "          \"x\": 0,\n" +
                "          \"y\": 0,\n" +
                "          \"z\": 0\n" +
                "        },\n" +
                "        \"stretch\": {\n" +
                "          \"x\": 1,\n" +
                "          \"y\": 1,\n" +
                "          \"z\": 1\n" +
                "        },\n" +
                "        \"settings\": {\n" +
                "          \"isPiece\": false,\n" +
                "          \"size\": {\n" +
                "            \"x\": %s,\n".formatted(size) +
                "            \"y\": %s\n".formatted(size) +
                "          },\n" +
                "          \"normal\": \"+Z\",\n" +
                "          \"isStaticBox\": true\n" +
                "        },\n" +
                "        \"textureLayout\": {\n" +
                "          \"front\": {\n" +
                "            \"offset\": {\n" +
                "              \"x\": 0,\n" +
                "              \"y\": 0\n" +
                "            },\n" +
                "            \"mirror\": {\n" +
                "              \"x\": false,\n" +
                "              \"y\": false\n" +
                "            },\n" +
                "            \"angle\": 0\n" +
                "          }\n" +
                "        },\n" +
                "        \"unwrapMode\": \"custom\",\n" +
                "        \"visible\": true,\n" +
                "        \"doubleSided\": true,\n" +
                "        \"shadingMode\": \"flat\"\n" +
                "      }\n" +
                "    }\n" +
                "  ],\n" +
                "  \"format\": \"prop\",\n" +
                "  \"lod\": \"auto\"\n" +
                "}";

        FileWriter out = new FileWriter(out_dir + File.separator + "base_character.blockymodel");
        out.write(json);
        out.close();
    }

    public static void genEntityModel(String out_dir, char c, String font_name) throws IOException {
        String json = "{\n" +
                "\"Model\": \"Items/" + font_name + "/base_model.blockymodel\",\n" +
                "    \"Texture\": \"Items/" + font_name + "/U" + String.format("%04X", (int) c) + ".png\n" +
                "    \"HitBox\": {\n" +
                "        \"Max\": {\n" +
                "            \"X\": 0.02,\n" +
                "            \"Y\": 0.05,\n" +
                "            \"Z\": 0.02\n" +
                "        },\n" +
                "        \"Min\": {\n" +
                "            \"X\": 0,\n" +
                "            \"Y\": 0,\n" +
                "            \"Z\": 0\n" +
                "        }\n" +
                "    },\n" +
                "    \"MinScale\": 0.25,\n" +
                "    \"MaxScale\": 3" +
                "}";

        FileWriter out = new FileWriter(out_dir + File.separator + font_name + "_U" + String.format("%04X", (int) c) + ".json");
        out.write(json);
        out.close();
    }
}
