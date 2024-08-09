package com.sakuraryoko.afkplus;

import net.fabricmc.api.DedicatedServerModInitializer;

import static com.sakuraryoko.afkplus.data.ModData.AFK_INIT;

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
