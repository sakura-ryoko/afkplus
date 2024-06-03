package com.github.sakuraryoko.afkplus;

import static com.github.sakuraryoko.afkplus.data.ModData.*;

import net.fabricmc.api.ClientModInitializer;

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
