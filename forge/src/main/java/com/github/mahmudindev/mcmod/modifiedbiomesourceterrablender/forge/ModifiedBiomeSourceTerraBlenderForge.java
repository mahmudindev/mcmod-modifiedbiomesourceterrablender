package com.github.mahmudindev.mcmod.modifiedbiomesourceterrablender.forge;

import com.github.mahmudindev.mcmod.modifiedbiomesourceterrablender.ModifiedBiomeSourceTerraBlender;
import net.minecraftforge.fml.common.Mod;

@Mod(ModifiedBiomeSourceTerraBlender.MOD_ID)
public final class ModifiedBiomeSourceTerraBlenderForge {
    public ModifiedBiomeSourceTerraBlenderForge() {
        // Run our common setup.
        ModifiedBiomeSourceTerraBlender.init();
    }
}
