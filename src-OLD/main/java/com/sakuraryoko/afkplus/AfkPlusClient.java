package com.sakuraryoko.afkplus;

import net.fabricmc.api.ClientModInitializer;

import static com.sakuraryoko.afkplus.data.ModData.AFK_INIT;

public class AfkPlusClient implements ClientModInitializer
{
    @Override
    public void onInitializeClient()
    {
        if (!AFK_INIT)
        {
            AFK_INIT = true;
            AfkPlusMod.init();
        }
    }
}
