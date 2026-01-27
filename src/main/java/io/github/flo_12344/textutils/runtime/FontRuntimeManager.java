package io.github.flo_12344.textutils.runtime;

import com.hypixel.hytale.assetstore.AssetUpdateQuery;
import com.hypixel.hytale.common.plugin.PluginManifest;
import com.hypixel.hytale.common.semver.Semver;
import com.hypixel.hytale.server.core.asset.AssetModule;
import com.hypixel.hytale.server.core.asset.common.CommonAssetModule;
import com.hypixel.hytale.server.core.asset.common.CommonAssetRegistry;
import com.hypixel.hytale.server.core.asset.common.asset.FileCommonAsset;
import com.hypixel.hytale.server.core.asset.type.model.config.ModelAsset;
import io.github.flo_12344.textutils.TextUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.logging.Level;

// Based on https://github.com/Jacobwasbeast/ImageFrames-Hytale
public class FontRuntimeManager {
    public static final String TILE_PREFIX = "TextUtils_Glyph_";
    public static final String RUNTIME_ASSETS_PACK = "TextUtilsRuntimeAssets";
    public static final String RUNTIME_ASSETS_DIR = "Textutils_Assets";
    public static final String RUNTIME_MODEL_DIR = "Server/Models/Textutils/";
    public static final String MODEL_TEXTURE_PATH = "Items/Textutils/";
    public static final AssetUpdateQuery UPDATE_QUERY = new AssetUpdateQuery(
            new AssetUpdateQuery.RebuildCache(false, true, true, false, false, false));

    private final Path runtimeAssetsPath;
    private final Path runtimeCommonModelPath;
    private final Path runtimeModelPath;
//    private volatile BufferedImage frameTextureCache;

    public FontRuntimeManager() {
        this.runtimeAssetsPath = resolveRuntimeBasePath().resolve(RUNTIME_ASSETS_DIR);
        this.runtimeCommonModelPath = runtimeAssetsPath.resolve("Common").resolve(MODEL_TEXTURE_PATH);
        this.runtimeModelPath = runtimeAssetsPath.resolve(RUNTIME_MODEL_DIR);
    }

    public void registerRuntimePack() {
        PluginManifest manifest = PluginManifest.CoreBuilder.corePlugin(TextUtils.class)
                .description("Runtime assets for Textutils").build();
        manifest.setName(RUNTIME_ASSETS_PACK);
        manifest.setVersion(Semver.fromString("1.0.0"));
        AssetModule.get().registerPack(RUNTIME_ASSETS_PACK, runtimeAssetsPath, manifest);

        CommonAssetModule commonAssetModule = CommonAssetModule.get();
        if (commonAssetModule == null || !Files.isDirectory(runtimeCommonModelPath)) {
            return;
        }

        try (var stream = Files.list(runtimeCommonModelPath)) {
            stream.filter(p -> p.getFileName().toString().toLowerCase().endsWith(".png") || p.getFileName().toString().endsWith(".blockymodel")).forEach(path -> {
                try (var sub = Files.list(path)) {
                    sub.forEach(path1 -> {
                        String fileName = path1.getFileName().toString();
                        String assetPath = "Models/Textutils/" + path.getFileName() + File.separator + fileName;
                        if (CommonAssetRegistry.hasCommonAsset(assetPath)) {
                            return;
                        }
                        try {
                            byte[] bytes = Files.readAllBytes(path1);
                            commonAssetModule.addCommonAsset(RUNTIME_ASSETS_PACK, new FileCommonAsset(path1, assetPath, bytes));
                        } catch (IOException e) {
                            TextUtils.INSTANCE.getLogger().at(Level.WARNING).withCause(e).log("Failed to register asset %s",
                                    assetPath);
                        }
                    });
                } catch (IOException e) {
                    TextUtils.INSTANCE.getLogger().at(Level.WARNING).withCause(e).log("Failed to scan assets");
                }

            });
        } catch (IOException e) {
            TextUtils.INSTANCE.getLogger().at(Level.WARNING).withCause(e).log("Failed to scan assets");
        }
    }

    public void reload() {
        var assetStore = ModelAsset.getAssetStore();
//        assetStore.loadAssetsFromPaths(RUNTIME_ASSETS_PACK, new ArrayList<>(), UPDATE_QUERY);
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


//    private void ensureCommonAssetsRegistered() {
//        CommonAssetModule commonAssetModule = CommonAssetModule.get();
//        if (commonAssetModule == null || !Files.isDirectory(runtimeCommonBlocksPath)) {
//            return;
//        }
//        try (var stream = Files.list(runtimeCommonBlocksPath)) {
//            stream.filter(p -> p.getFileName().toString().toLowerCase().endsWith(".png")).forEach(path -> {
//                String fileName = path.getFileName().toString();
//                String assetPath = "Blocks/ImageFrames/tiles/" + fileName;
//                if (CommonAssetRegistry.hasCommonAsset(assetPath)) {
//                    return;
//                }
//                try {
//                    byte[] bytes = Files.readAllBytes(path);
//                    commonAssetModule.addCommonAsset(RUNTIME_ASSETS_PACK, new FileCommonAsset(path, assetPath, bytes));
//                } catch (IOException e) {
//                    plugin.getLogger().at(Level.WARNING).withCause(e).log("Failed to register tile asset %s",
//                            assetPath);
//                }
//            });
//        } catch (IOException e) {
//            plugin.getLogger().at(Level.WARNING).withCause(e).log("Failed to scan tile assets");
//        }
//    }

    private void ensureBlockTypesRegistered() {
        if (!Files.isDirectory(runtimeModelPath)) {
            return;
        }
        try {
            Files.createDirectories(runtimeCommonModelPath);
        } catch (IOException e) {
            TextUtils.INSTANCE.getLogger().at(Level.WARNING).withCause(e).log("Failed to create runtime block type directory");
            return;
        }
        try (var parent = Files.list(runtimeModelPath)) {
            parent.forEach(parent_path -> {
                try (var stream = Files.list(parent_path)) {
                    stream.filter(p -> p.getFileName().toString().toLowerCase().endsWith(".png")).forEach(path -> {
                        String fileName = path.getFileName().toString();
                        String baseName = fileName.substring(0, fileName.length() - 4);
                        String tileKey = TILE_PREFIX + baseName;
                        String assetPath = MODEL_TEXTURE_PATH + fileName;
                        Path jsonPath = runtimeCommonModelPath.resolve(tileKey + ".json");
                    });
                } catch (IOException e) {
                    TextUtils.INSTANCE.getLogger().at(Level.WARNING).withCause(e).log("Failed to scan runtime tiles for block types");
                }
            });
        } catch (IOException e) {
            TextUtils.INSTANCE.getLogger().at(Level.WARNING).withCause(e).log("Failed to scan runtime tiles for block types");
        }

        List<Path> jsonPaths = listJsonFiles(runtimeCommonModelPath);
        loadBlockTypeAssets(jsonPaths);
    }

    private void loadBlockTypeAssets(List<Path> paths) {
        if (paths == null || paths.isEmpty()) {
            return;
        }
        try {
            @SuppressWarnings("unchecked")
            var assetStore = ModelAsset.getAssetStore();
            assetStore.loadAssetsFromPaths(RUNTIME_ASSETS_PACK, paths, UPDATE_QUERY, true);
        } catch (Exception e) {
            TextUtils.INSTANCE.getLogger().at(Level.WARNING).withCause(e).log("Failed to load ImageFrames block types");
        }
    }
}
