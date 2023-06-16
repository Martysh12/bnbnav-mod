package com.martysh12.bnbnav.gui;

import com.martysh12.bnbnav.BnbnavMod;
import com.martysh12.bnbnav.net.BnbnavClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.LiteralText;

public class BnbnavOptionsScreen extends Screen {
    private final Screen parent;

    private ButtonWidget startButton;
    private ButtonWidget pingButton;
    private TextFieldWidget responseField;

    public BnbnavOptionsScreen(Screen parent) {
        super(new LiteralText("Bnbnav Options"));
        this.parent = parent;
    }

    @Override
    protected void init() {
        this.startButton = new ButtonWidget(10, 10, 90, 20, new LiteralText("Connect"), (button) -> {
            BnbnavMod.logger.info("Connecting to bnbnav...");
            try {
                BnbnavMod.connection = new BnbnavClient();
            } catch (Throwable e) {
                BnbnavMod.logger.error("Unable to connect. " + e.getMessage());
            }

            this.updatePingButton();
        });
        this.addDrawableChild(this.startButton);

        this.pingButton = new ButtonWidget(100, 10, 90, 20, new LiteralText("Ping"), (button) -> {
            BnbnavMod.logger.info("Pinging bnbnav...");
            try {
                String response = BnbnavMod.connection.ping();
                BnbnavMod.logger.info("Received response: " + response);
                this.responseField.setText(response);
            } catch (Throwable e) {
                BnbnavMod.logger.error("Couldn't receive message. " + e.getMessage());
            }
        });
        this.addDrawableChild(this.pingButton);

        this.responseField = new TextFieldWidget(this.textRenderer, 10, 40, 180, 20, new LiteralText("Response"));
        this.responseField.setEditable(false);
        this.addSelectableChild(this.responseField);

        // Back button
        this.addDrawableChild(new ButtonWidget(10, this.height - 30, 90, 20, new LiteralText("Back"), (button) -> this.client.setScreen(this.parent)));

        this.updatePingButton();
    }

    private void updatePingButton() {
        this.pingButton.active = BnbnavMod.connection != null;
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        this.renderBackground(matrices);

        this.responseField.render(matrices, mouseX, mouseY, delta);
        super.render(matrices, mouseX, mouseY, delta);
    }
}
