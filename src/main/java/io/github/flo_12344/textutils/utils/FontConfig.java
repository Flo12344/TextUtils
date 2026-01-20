package io.github.flo_12344.textutils.utils;

import com.hypixel.hytale.codec.builder.BuilderCodec;
import com.hypixel.hytale.server.core.util.Config;
import org.checkerframework.checker.nullness.compatqual.NonNullDecl;

import java.nio.file.Path;

public class FontConfig {
public static final BuilderCodec<FontConfig> CODEC = BuilderCodec.<FontConfig>builder(
        FontConfig.class, FontConfig::new).build();
}
