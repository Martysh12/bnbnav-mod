package com.martysh12.bnbnav.gui;

import com.googlecode.jsonrpc4j.JsonRpcClientException;
import com.martysh12.bnbnav.BnbnavMod;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.LiteralText;

public class BnbnavOptionsScreen extends Screen {
    private final Screen parent;

    private ButtonWidget pingButton;
    private ButtonWidget checkNameButton;
    private TextFieldWidget responseField;

    public BnbnavOptionsScreen(Screen parent) {
        super(new LiteralText("Bnbnav Options"));
        this.parent = parent;
    }

    @Override
    protected void init() {
        this.pingButton = new ButtonWidget(10, 10, 90, 20, new LiteralText("Ping"), (button) -> {
            BnbnavMod.logger.info("Pinging bnbnav...");

            if (!BnbnavMod.connection.isConnected()) {
                return;
            }

            try {
                String response = BnbnavMod.connection.ping();
                BnbnavMod.logger.info("Received response: " + response);
                this.responseField.setText(response);
            } catch (Throwable e) {
                BnbnavMod.logger.error("Couldn't receive message. " + e.getMessage());
            }
        });
        this.addDrawableChild(this.pingButton);

        this.checkNameButton = new ButtonWidget(100, 10, 90, 20, new LiteralText("Check Name"), (button) -> {
            BnbnavMod.logger.info("Checking username...");

            if (!BnbnavMod.connection.isConnected()) {
                return;
            }

            try {
                String response = BnbnavMod.connection.getLoggedInUsername();
                BnbnavMod.logger.info("Received response: " + response);
                this.responseField.setText(response);
            } catch (JsonRpcClientException e) {
                BnbnavMod.logger.error("bnbnav error (code " + e.getCode() + "): " + e.getMessage());
                this.responseField.setText(e.getMessage());
            } catch (Throwable e) {
                BnbnavMod.logger.error("Couldn't receive message. " + e.getMessage());
            }
        });
        this.addDrawableChild(this.checkNameButton);

        this.responseField = new TextFieldWidget(this.textRenderer, 10, 40, 180, 20, new LiteralText("Response"));
        this.responseField.setEditable(false);
        this.addSelectableChild(this.responseField);

        // Back button
        this.addDrawableChild(new ButtonWidget(10, this.height - 30, 90, 20, new LiteralText("Back"), (button) -> {
            assert this.client != null;
            this.client.setScreen(this.parent);
        }));
    }

    private void updateButtons() {
        this.pingButton.active = BnbnavMod.connection.isConnected();
        this.checkNameButton.active = BnbnavMod.connection.isConnected();
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        this.updateButtons();

        this.renderBackground(matrices);

        this.responseField.render(matrices, mouseX, mouseY, delta);
        super.render(matrices, mouseX, mouseY, delta);
    }
}
