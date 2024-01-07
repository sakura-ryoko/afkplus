package io.github.sakuraryoko.afkplus;

import static io.github.sakuraryoko.afkplus.data.ModData.*;

import net.fabricmc.api.DedicatedServerModInitializer;

public class AfkPlusServer implements DedicatedServerModInitializer {

    // We need this to preempt other mods registering the /afk command
    @Override
    public void onInitializeServer() {
        if (!AFK_INIT) {
            // AFK_DEBUG = true;
            AFK_INIT = true;
            AfkPlusMod.init();
        }
    }
}
