package com.martysh12.bnbnav.gui;

import com.martysh12.bnbnav.BnbnavMod;
import com.martysh12.bnbnav.net.BnbnavClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.LiteralText;

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
            BnbnavMod.logger.info("Attempting to connect to bnbnav.");

            try {
                BnbnavMod.connection = new BnbnavClient();
            } catch (Throwable e) {
                BnbnavMod.logger.error("Unable to connect. " + e.getMessage());
            }

            try {
                BnbnavMod.logger.debug("Received response: " + BnbnavMod.connection.ping());
            } catch (Throwable e) {
                BnbnavMod.logger.error("Couldn't receive message. " + e.getMessage());
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
