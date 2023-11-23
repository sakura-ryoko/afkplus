package io.github.sakuraryoko.afkplus;

import static io.github.sakuraryoko.afkplus.data.ModData.*;

import net.fabricmc.api.ModInitializer;

public class AfkPlusClient implements ModInitializer {

    @Override
    public void onInitialize() {
        if (!AFK_INIT) {
            // AFK_DEBUG = true;
            AFK_INIT = true;
            AfkPlusMod.init();
        }
    }
}
