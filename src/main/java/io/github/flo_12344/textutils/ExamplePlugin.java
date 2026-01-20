package io.github.flo_12344.textutils;

import com.hypixel.hytale.server.core.plugin.JavaPlugin;
import com.hypixel.hytale.server.core.plugin.JavaPluginInit;
import io.github.flo_12344.textutils.commands.List3dTextCommand;
import io.github.flo_12344.textutils.commands.Move3dTextCommand;
import io.github.flo_12344.textutils.commands.New3dTextCommand;
import io.github.flo_12344.textutils.commands.Remove3dTextCommand;
import io.github.flo_12344.textutils.component.MovingComponent;
import io.github.flo_12344.textutils.component.TextUtils3DTextComponent;
import io.github.flo_12344.textutils.system.Cleanup3dTextSystem;
import io.github.flo_12344.textutils.system.EditText3DSystem;
import io.github.flo_12344.textutils.system.Move3dTextUpdateSystem;
import io.github.flo_12344.textutils.system.SetupMove3dTextSystem;

import javax.annotation.Nonnull;

public class ExamplePlugin extends JavaPlugin {

    public ExamplePlugin(@Nonnull JavaPluginInit init) {
        super(init);
    }

    @Override
    protected void setup() {
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
}