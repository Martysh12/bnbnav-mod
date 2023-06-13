package com.martysh12.bnbnav;

import net.fabricmc.api.ClientModInitializer;
import org.freedesktop.dbus.connections.impl.DirectConnection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BnbnavMod implements ClientModInitializer {
    /**
     * Runs the mod initializer on the client environment.
     */
    public static final String MOD_ID = "bnbnav";
    public static Logger logger = LoggerFactory.getLogger(MOD_ID);

    public static DirectConnection dc;

    @Override
    public void onInitializeClient() {

    }
}
