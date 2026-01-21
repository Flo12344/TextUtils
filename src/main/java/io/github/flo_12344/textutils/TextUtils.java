package io.github.flo_12344.textutils;

import com.hypixel.hytale.event.EventPriority;
import com.hypixel.hytale.server.core.event.events.player.PlayerReadyEvent;
import com.hypixel.hytale.server.core.plugin.JavaPlugin;
import com.hypixel.hytale.server.core.plugin.JavaPluginInit;
import com.hypixel.hytale.server.core.universe.Universe;
import com.hypixel.hytale.server.core.universe.world.events.AllWorldsLoadedEvent;
import com.hypixel.hytale.server.core.util.Config;
import io.github.flo_12344.textutils.commands.List3dTextCommand;
import io.github.flo_12344.textutils.commands.Move3dTextCommand;
import io.github.flo_12344.textutils.commands.New3dTextCommand;
import io.github.flo_12344.textutils.commands.Remove3dTextCommand;
import io.github.flo_12344.textutils.component.MovingComponent;
import io.github.flo_12344.textutils.component.TextUtils3DTextComponent;
import io.github.flo_12344.textutils.data.TextConfig;
import io.github.flo_12344.textutils.system.*;
import io.github.flo_12344.textutils.utils.TextManager;

import javax.annotation.Nonnull;
import java.util.Iterator;

public class TextUtils extends JavaPlugin {
    public static TextUtils INSTANCE;
    public final Config<TextConfig> config;

    public TextUtils(@Nonnull JavaPluginInit init) {
        super(init);
        INSTANCE = this;
        this.config = this.withConfig("TextConfig", TextConfig.CODEC);
    }

    @Override
    protected void setup() {
        this.config.save();


        this.getCommandRegistry().registerCommand(new New3dTextCommand());
        this.getCommandRegistry().registerCommand(new Move3dTextCommand());
        this.getCommandRegistry().registerCommand(new Remove3dTextCommand());
        this.getCommandRegistry().registerCommand(new List3dTextCommand());
//        this.getEventRegistry().registerGlobal(PlayerReadyEvent.class, ExampleEvent::onPlayerReady);

        TextUtils3DTextComponent.init(this.getEntityStoreRegistry().registerComponent(TextUtils3DTextComponent.class, TextUtils3DTextComponent::new));
        MovingComponent.init(this.getEntityStoreRegistry().registerComponent(MovingComponent.class, MovingComponent::new));
        this.getEntityStoreRegistry().registerSystem(new EditText3DSystem());
        this.getEntityStoreRegistry().registerSystem(new SetupMove3dTextSystem());
        this.getEntityStoreRegistry().registerSystem(new Move3dTextUpdateSystem());
        this.getEntityStoreRegistry().registerSystem(new Cleanup3dTextSystem());
//        this.getEntityStoreRegistry().registerSystem(new EditText3DSystem());


    }

    @Override
    protected void start() {
        super.start();
        var entities = config.get().getTextEntity();

        this.getEventRegistry().register(EventPriority.LAST, AllWorldsLoadedEvent.class, allWorldsLoadedEvent -> {
            for (Iterator<String> it = entities.keys().asIterator(); it.hasNext(); ) {
                var val = it.next();
                var text3dData = entities.get(val);
                var world = Universe.get().getWorld(text3dData.getWorld());
                TextManager.SpawnText(text3dData.getPosition(), text3dData.getRotation(), world, text3dData.getText(), val, true);
            }
        });
    }

    @Override
    protected void shutdown() {
        this.config.save();
        super.shutdown();
    }
}