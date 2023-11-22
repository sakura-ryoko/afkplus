package io.github.sakuraryoko.afkplus;

//import static io.github.sakuraryoko.afkplus.data.ModData.*;

import net.fabricmc.api.ModInitializer;

public class AfkPlusMain implements ModInitializer {

    @Override
    public void onInitialize() {
        // AFK_DEBUG = true;
        AfkPlusMod.initMain();
    }
}
