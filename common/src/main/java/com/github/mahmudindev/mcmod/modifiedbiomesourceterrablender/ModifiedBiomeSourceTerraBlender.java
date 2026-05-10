package com.github.mahmudindev.mcmod.modifiedbiomesourceterrablender;

import com.github.mahmudindev.mcmod.modifiedbiomesourceterrablender.platform.Services;
import com.github.mahmudindev.mcmod.modifiedbiomesourceterrablender.platform.services.IPlatformHelper;
import com.mojang.logging.LogUtils;
import org.slf4j.Logger;

public final class ModifiedBiomeSourceTerraBlender {
    public static final String MOD_ID = "modifiedbiomesourceterrablender";
    public static final Logger LOGGER = LogUtils.getLogger();
    public static final IPlatformHelper PLATFORM = Services.PLATFORM;

    public static void init() {
        // Write common init code here.
    }
}
