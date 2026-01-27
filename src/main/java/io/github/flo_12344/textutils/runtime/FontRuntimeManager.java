package io.github.flo_12344.textutils.runtime;

import com.hypixel.hytale.assetstore.AssetUpdateQuery;
import com.hypixel.hytale.server.core.asset.type.model.config.Model;
import com.hypixel.hytale.server.core.asset.type.model.config.ModelAsset;
import io.github.flo_12344.textutils.TextUtils;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

// Based on https://github.com/Jacobwasbeast/ImageFrames-Hytale
public class FontRuntimeManager {

    public static final String RUNTIME_ASSETS_PACK = "mods/TextUtilsRuntimeAssets";
    public static final String RUNTIME_ASSETS_DIR = "textutils_assets";
    public static final String RUNTIME_MODEL_DIR = "Server/Models/Textutils/";
    public static final String MODEL_TEXTURE_PATH = "Items/Textutils/";
    public static final AssetUpdateQuery UPDATE_QUERY = new AssetUpdateQuery(
            new AssetUpdateQuery.RebuildCache(false, true, true, false, false, false));

    private final Path runtimeAssetsPath;
    private final Path runtimeCommonModelPath;
//    private volatile BufferedImage frameTextureCache;

    public FontRuntimeManager() {
        this.runtimeAssetsPath = resolveRuntimeBasePath();
        this.runtimeCommonModelPath = runtimeAssetsPath.resolve(MODEL_TEXTURE_PATH);
    }

    public void reload() {
        var assetStore = ModelAsset.getAssetStore();
        assetStore.loadAssetsFromPaths(RUNTIME_ASSETS_PACK, new ArrayList<>(), UPDATE_QUERY);
//        assetStore.load
    }

    public static Path resolveRuntimeBasePath() {
        Path cwd = Paths.get("").toAbsolutePath();
        Path cwdName = cwd.getFileName();
        if (cwdName != null && "run".equalsIgnoreCase(cwdName.toString())) {
            return cwd;
        }
        Path runDir = cwd.resolve("run");
        if (Files.isDirectory(runDir)) {
            return runDir.toAbsolutePath();
        }
        return cwd;
    }

    private List<Path> listJsonFiles(Path dir) {
        if (dir == null || !Files.isDirectory(dir)) {
            return java.util.Collections.emptyList();
        }
        try (var stream = Files.list(dir)) {
            return stream.filter(p -> p.getFileName().toString().toLowerCase().endsWith(".json"))
                    .collect(java.util.stream.Collectors.toList());
        } catch (IOException e) {
            TextUtils.INSTANCE.getLogger().at(Level.WARNING).withCause(e).log("Failed to scan jsons");
            return java.util.Collections.emptyList();
        }
    }
}
