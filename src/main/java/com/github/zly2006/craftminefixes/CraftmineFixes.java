package com.github.zly2006.craftminefixes;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.Version;

public class CraftmineFixes implements ModInitializer {
    public static final String MOD_ID = "craftmine-fixes";
    public static final Version MOD_VERSION = FabricLoader.getInstance()
            .getModContainer(MOD_ID)
            .orElseThrow(() -> new IllegalStateException("Mod " + MOD_ID + " not found"))
            .getMetadata()
            .getVersion();

    @Override
    public void onInitialize() {
    }
}
