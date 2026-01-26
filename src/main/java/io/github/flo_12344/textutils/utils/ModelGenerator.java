package io.github.flo_12344.textutils.utils;

import com.google.gson.JsonObject;
import com.hypixel.hytale.server.core.universe.Universe;
import com.hypixel.hytale.server.core.util.io.FileUtil;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

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
}
