package io.github.flo_12344.textutils.runtime;

import com.hypixel.hytale.assetstore.AssetUpdateQuery;
import com.hypixel.hytale.common.plugin.PluginManifest;
import com.hypixel.hytale.common.semver.Semver;
import com.hypixel.hytale.server.core.asset.AssetModule;
import com.hypixel.hytale.server.core.asset.common.CommonAsset;
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
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

// Based on https://github.com/Jacobwasbeast/ImageFrames-Hytale
public class FontRuntimeManager {
    public static final String RUNTIME_ASSETS_PACK = "TextUtilsRuntimeAssets";
    public static final String RUNTIME_ASSETS_DIR = "Textutils_Assets";
    public static final String RUNTIME_MODEL_DIR = "Server/Models/Textutils/";
    public static final String MODEL_TEXTURE_PATH = "Items/Textutils/";
    public static final String UI_TEXTURE_PATH = "UI/Custom/Textutils/";

    private final Path runtimeAssetsPath;
    private final Path runtimeCommonModelPath;

    public FontRuntimeManager() {
        this.runtimeAssetsPath = resolveRuntimeBasePath().resolve(RUNTIME_ASSETS_DIR);
        this.runtimeCommonModelPath = runtimeAssetsPath.resolve("Common").resolve(MODEL_TEXTURE_PATH);
    }

    public void registerRuntimePack() {
        PluginManifest manifest = PluginManifest.CoreBuilder.corePlugin(TextUtils.class)
                .description("Runtime assets for Textutils").build();
        manifest.setName(RUNTIME_ASSETS_PACK);
        manifest.setVersion(Semver.fromString("1.0.0"));
        AssetModule.get().registerPack(RUNTIME_ASSETS_PACK, runtimeAssetsPath, manifest);
        ensureCommonAssetsRegistered();
    }

    public void ensureCommonAssetsRegistered() {
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

    public void broadcastCommonAssets() {
        ensureCommonAssetsRegistered();
        CommonAssetModule commonAssetModule = CommonAssetModule.get();
        if (commonAssetModule == null) {
            return;
        }
        java.util.List<com.hypixel.hytale.server.core.asset.common.CommonAsset> assets = CommonAssetRegistry
                .getCommonAssetsStartingWith(RUNTIME_ASSETS_PACK, "Items/Textutils/");
        if (assets == null || assets.isEmpty()) {
            return;
        }
        commonAssetModule.sendAssets(assets, false);
        TextUtils.INSTANCE.getLogger().at(java.util.logging.Level.INFO).log("Broadcasted %d Font textures", assets.size());
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

}
