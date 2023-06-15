package com.martysh12.bnbnav;

import com.martysh12.bnbnav.net.BnbnavClient;
import net.fabricmc.api.ClientModInitializer;
import org.lwjgl.system.CallbackI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BnbnavMod implements ClientModInitializer {
    /**
     * Runs the mod initializer on the client environment.
     */
    public static final String MOD_ID = "bnbnav";
    public static final Logger logger = LoggerFactory.getLogger(MOD_ID);
    public static BnbnavClient connection;

    @Override
    public void onInitializeClient() {

    }
}
