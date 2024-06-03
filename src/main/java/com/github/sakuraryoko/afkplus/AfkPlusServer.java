package com.github.sakuraryoko.afkplus;

import static com.github.sakuraryoko.afkplus.data.ModData.*;

import net.fabricmc.api.DedicatedServerModInitializer;

public class AfkPlusServer implements DedicatedServerModInitializer
{
    @Override
    public void onInitializeServer()
    {
        if (!AFK_INIT)
        {
            AFK_INIT = true;
            AfkPlusMod.init();
        }
    }
}
