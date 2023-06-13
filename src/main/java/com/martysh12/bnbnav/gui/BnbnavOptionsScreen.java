package com.martysh12.bnbnav.gui;

import com.martysh12.bnbnav.BnbnavMod;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import org.freedesktop.dbus.connections.impl.DirectConnection;
import org.freedesktop.dbus.exceptions.DBusException;

public class BnbnavOptionsScreen extends Screen {
    private final Screen parent;

    private ButtonWidget startButton;
    private ButtonWidget pingButton;

    public BnbnavOptionsScreen(Screen parent) {
        super(new LiteralText("Bnbnav Options"));
        this.parent = parent;
    }

    @Override
    protected void init() {
        this.initWidgets();
    }

    private void initWidgets() {
        this.startButton = new ButtonWidget(10, 10, this.width / 2 - 15, 20, new LiteralText("Connect to bnbnav"), (button) -> {
            String bnbnavPath = System.getenv("XDG_RUNTIME_DIR") + "/bnbnav";

            BnbnavMod.logger.debug("Attempting to connect to bnbnav at " + bnbnavPath);

            try {
                BnbnavMod.dc = new DirectConnection("unix:path=" + bnbnavPath);
            } catch (DBusException e) {
                BnbnavMod.logger.error("Unable to connect to bnbnav: " + e.getMessage());
            }
        });

        this.addDrawableChild(this.startButton);

        // Back button
        this.addDrawableChild(new ButtonWidget(10, this.height - 30, 90, 20, new LiteralText("Back"), (button) -> this.client.setScreen(this.parent)));
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        this.renderBackground(matrices);

        super.render(matrices, mouseX, mouseY, delta);
    }
}
