package com.github.mahmudindev.mcmod.modifiedbiomesourceterrablender.fabric;

import com.github.mahmudindev.mcmod.modifiedbiomesourceterrablender.ModifiedBiomeSourceTerraBlender;
import net.fabricmc.api.ModInitializer;

public final class ModifiedBiomeSourceTerraBlenderFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        // This code runs as soon as Minecraft is in a mod-load-ready state.
        // However, some things (like resources) may still be uninitialized.
        // Proceed with mild caution.

        // Run our common setup.
        ModifiedBiomeSourceTerraBlender.init();
    }
}
