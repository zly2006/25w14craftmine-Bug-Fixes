package com.github.zly2006.craftminefixes;

import net.fabricmc.loader.api.entrypoint.PreLaunchEntrypoint;
import net.minecraft.SharedConstants;

public class PreLaunch implements PreLaunchEntrypoint {
    @Override
    public void onPreLaunch() {
        SharedConstants.IS_RUNNING_IN_IDE = true;
    }
}
