package io.github.flo_12344.textutils;

import com.hypixel.hytale.assetstore.AssetRegistry;
import com.hypixel.hytale.common.util.StringUtil;
import com.hypixel.hytale.server.core.asset.HytaleAssetStore;
import com.hypixel.hytale.server.core.modules.collision.WorldUtil;
import com.hypixel.hytale.server.core.plugin.JavaPlugin;
import com.hypixel.hytale.server.core.plugin.JavaPluginInit;
import com.hypixel.hytale.server.core.universe.Universe;
import com.hypixel.hytale.server.core.util.io.FileUtil;
import io.github.flo_12344.textutils.commands.*;
import io.github.flo_12344.textutils.component.MovingComponent;
import io.github.flo_12344.textutils.component.Text3dDeleterComponent;
import io.github.flo_12344.textutils.component.TextUtils3DTextComponent;
import io.github.flo_12344.textutils.system.*;
import io.github.flo_12344.textutils.utils.FontConfigManager;

import javax.annotation.Nonnull;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TextUtils extends JavaPlugin {
    public static TextUtils INSTANCE;
//    public final Config<TextConfig> config;

    public TextUtils(@Nonnull JavaPluginInit init) {
        super(init);
        INSTANCE = this;
//        this.config = this.withConfig("TextConfig", TextConfig.CODEC);
    }

    @Override
    protected void setup() {
//        this.config.save();


        this.getCommandRegistry().registerCommand(new New3dTextCommand());
        this.getCommandRegistry().registerCommand(new Move3dTextCommand());
        this.getCommandRegistry().registerCommand(new Remove3dTextCommand());
        this.getCommandRegistry().registerCommand(new List3dTextCommand());

//        this.getCommandRegistry().registerCommand(new Test2dCommand());
//        this.getEventRegistry().registerGlobal(PlayerReadyEvent.class, ExampleEvent::onPlayerReady);

        TextUtils3DTextComponent.init(this.getEntityStoreRegistry().registerComponent(TextUtils3DTextComponent.class, "TextUtils3DText", TextUtils3DTextComponent.CODEC));
        Text3dDeleterComponent.init(this.getEntityStoreRegistry().registerComponent(Text3dDeleterComponent.class, "Text3dDeleter", Text3dDeleterComponent.CODEC));
//        MovingComponent.init(this.getEntityStoreRegistry().registerComponent(MovingComponent.class, "Text3dMoving", MovingComponent.CODEC));
        this.getEntityStoreRegistry().registerSystem(new EditText3DSystem());
        this.getEntityStoreRegistry().registerSystem(new Text3dSpawned());
        this.getEntityStoreRegistry().registerSystem(new DeleterText3dSystem());
//        this.getEntityStoreRegistry().registerSystem(new SetupMove3dTextSystem());
//        this.getEntityStoreRegistry().registerSystem(new MoveText3dUpdateSystem());


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

        FontConfigManager.INSTANCE = new FontConfigManager();

        File fonts = new File(getDataDirectory() + File.separator + "fonts");
        var list = fonts.list(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                return name.endsWith(".ttf");
            }
        });

        Arrays.stream(list).toList().forEach(s -> {
            try {
                var font_name = s.substring(0, s.lastIndexOf(".ttf"));
                FontConfigManager.INSTANCE.Init(font_name);
                FontConfigManager.INSTANCE.LoadFlags(font_name, FontConfigManager.FontSettings.BASIC_LATIN_FLAG);
            } catch (IOException | FontFormatException e) {
                throw new RuntimeException(e);
            }
        });

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
//        this.config.save();
        super.shutdown();
    }
}