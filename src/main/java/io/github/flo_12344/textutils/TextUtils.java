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
//        var entities = config.get().getTextEntity();
//
//        this.getEventRegistry().register(EventPriority.LAST, AllWorldsLoadedEvent.class, allWorldsLoadedEvent -> {
//            for (Iterator<String> it = entities.keys().asIterator(); it.hasNext(); ) {
//                var val = it.next();
//                var text3dData = entities.get(val);
//                var world = Universe.get().getWorld(text3dData.getWorld());
//                TextManager.SpawnText(text3dData.getPosition(), text3dData.getRotation(), world, text3dData.getText(), val, true);
//            }
//        });

        List<String> folders = new ArrayList<>(List.of(new String[]{"data", "fonts"}));

        folders.forEach(s -> {
            String dir_path = getDataDirectory() + File.separator + s;
            File dir = new File(dir_path);
            if (dir.exists())
                return;

            boolean directoryCreated = dir.mkdirs();
            if (!directoryCreated)
                Universe.get().getLogger().atSevere().log("FAILED to create %s dir", s);
        });

//        FontManager.INSTANCE = new FontManager();

//        File fonts = new File(getDataDirectory() + File.separator + "fonts");
//        var list = fonts.list(new FilenameFilter() {
//            @Override
//            public boolean accept(File dir, String name) {
//                return name.endsWith(".ttf");
//            }
//        });

//        Arrays.stream(list).toList().forEach(s -> {
//            try {
//                var font_name = s.substring(0, s.lastIndexOf(".ttf"));
//                FontConfigManager.INSTANCE.Init(font_name);
//                FontConfigManager.INSTANCE.LoadFlags(font_name, FontConfigManager.FontSettings.BASIC_LATIN_FLAG);
//            } catch (IOException | FontFormatException e) {
//                throw new RuntimeException(e);
//            }
//        });
        fontRuntimeManager = new FontRuntimeManager();
        fontRuntimeManager.registerRuntimePack();

        Path cwd = Paths.get("").toAbsolutePath();
        Path cwdName = cwd.getFileName();
        if (cwdName != null && "run".equalsIgnoreCase(cwdName.toString())) {
            Universe.get().getLogger().atSevere().log("FAILED to create %s dir", cwd);
            return;
        }
        Path runDir = cwd.resolve("run");
        if (Files.isDirectory(runDir)) {
            Universe.get().getLogger().atSevere().log("FAILED to create %s dir", runDir.toAbsolutePath());
        }
        Universe.get().getLogger().atSevere().log("FAILED to create %s dir", cwd);
    }

    @Override
    protected void shutdown() {
        this.FONT_MANAGER.save();
        super.shutdown();
    }
}