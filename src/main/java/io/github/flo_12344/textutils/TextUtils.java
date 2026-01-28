package io.github.flo_12344.textutils;

import com.hypixel.hytale.server.core.plugin.JavaPlugin;
import com.hypixel.hytale.server.core.plugin.JavaPluginInit;
import com.hypixel.hytale.server.core.universe.Universe;
import com.hypixel.hytale.server.core.util.Config;
import io.github.flo_12344.textutils.commands.*;
import io.github.flo_12344.textutils.component.Text3dDeleterComponent;
import io.github.flo_12344.textutils.component.TextUtils3DTextComponent;
import io.github.flo_12344.textutils.runtime.FontRuntimeManager;
import io.github.flo_12344.textutils.system.*;
import io.github.flo_12344.textutils.utils.FontManager;

import javax.annotation.Nonnull;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class TextUtils extends JavaPlugin {
    public static TextUtils INSTANCE;
    public final Config<FontManager> FONT_MANAGER;
    public FontRuntimeManager fontRuntimeManager;

    public TextUtils(@Nonnull JavaPluginInit init) {
        super(init);
        INSTANCE = this;
        this.FONT_MANAGER = this.withConfig("TextConfig", FontManager.CODEC);
    }

    @Override
    protected void setup() {
        this.FONT_MANAGER.save();


        this.getCommandRegistry().registerCommand(new FontCommand());
        this.getCommandRegistry().registerCommand(new Text3dCommand());

        this.getCommandRegistry().registerCommand(new Test2dCommand());

        TextUtils3DTextComponent.init(this.getEntityStoreRegistry().registerComponent(TextUtils3DTextComponent.class, "TextUtils3DText", TextUtils3DTextComponent.CODEC));
        Text3dDeleterComponent.init(this.getEntityStoreRegistry().registerComponent(Text3dDeleterComponent.class, "Text3dDeleter", Text3dDeleterComponent.CODEC));
        this.getEntityStoreRegistry().registerSystem(new Text3dSystem.EditText3DSystem());
        this.getEntityStoreRegistry().registerSystem(new Text3dSystem.Text3dSpawned());
        this.getEntityStoreRegistry().registerSystem(new Text3dSystem.DeleterText3dSystem());


    }

    @Override
    protected void start() {
        super.start();

        String dir_path = getDataDirectory() + File.separator + "fonts";
        File dir = new File(dir_path);
        if (dir.exists())
            return;

        boolean directoryCreated = dir.mkdirs();
        if (!directoryCreated)
            Universe.get().getLogger().atSevere().log("FAILED to create fonts dir");

        fontRuntimeManager = new FontRuntimeManager();
        fontRuntimeManager.registerRuntimePack();
    }

    @Override
    protected void shutdown() {
        this.FONT_MANAGER.save();
        super.shutdown();
    }
}