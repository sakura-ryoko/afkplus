package io.github.sakuraryoko.afkplus;

import static io.github.sakuraryoko.afkplus.data.ModData.*;

import net.fabricmc.api.DedicatedServerModInitializer;

public class AfkPlusServer implements DedicatedServerModInitializer {

    @Override
    public void onInitializeServer() {
        AFK_DEBUG = false;
        AfkPlusMod.initServer();
    }
}
