package io.github.flo_12344.textutils.pages;

import com.hypixel.hytale.protocol.packets.interface_.CustomPageLifetime;
import com.hypixel.hytale.server.core.entity.entities.player.pages.BasicCustomUIPage;
import com.hypixel.hytale.server.core.ui.builder.UICommandBuilder;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import org.checkerframework.checker.nullness.compatqual.NonNullDecl;

public class TestPages extends BasicCustomUIPage {
    private String text;

    public TestPages(@NonNullDecl PlayerRef playerRef, String text) {
        super(playerRef, CustomPageLifetime.CanDismiss);
        this.text = text;
    }

    @Override
    public void build(UICommandBuilder uiCommandBuilder) {
        uiCommandBuilder.append("Pages/textutils.ui");
        uiCommandBuilder.appendInline("#Main", text);
    }

}
