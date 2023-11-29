package io.github.sakuraryoko.afkplus;

import static io.github.sakuraryoko.afkplus.data.ModData.*;

import net.fabricmc.api.ClientModInitializer;

public class AfkPlusClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        if (!AFK_INIT) {
            // AFK_DEBUG = true;
            AFK_INIT = true;
            AfkPlusMod.init();
        }
    }
}
