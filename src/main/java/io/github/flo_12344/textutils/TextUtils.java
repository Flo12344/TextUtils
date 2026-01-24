package io.github.flo_12344.textutils;

import com.hypixel.hytale.server.core.plugin.JavaPlugin;
import com.hypixel.hytale.server.core.plugin.JavaPluginInit;
import io.github.flo_12344.textutils.commands.*;
import io.github.flo_12344.textutils.component.MovingComponent;
import io.github.flo_12344.textutils.component.Text3dDeleterComponent;
import io.github.flo_12344.textutils.component.TextUtils3DTextComponent;
import io.github.flo_12344.textutils.system.*;

import javax.annotation.Nonnull;

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

        this.getCommandRegistry().registerCommand(new Test2dCommand());
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
    }

    @Override
    protected void shutdown() {
//        this.config.save();
        super.shutdown();
    }
}