/*
 * This file is part of the AfkPlus project, licensed under the
 * GNU Lesser General Public License v3.0
 *
 * Copyright (C) 2024  Sakura Ryoko and contributors
 *
 * AfkPlus is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * AfkPlus is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with AfkPlus.  If not, see <https://www.gnu.org/licenses/>.
 */

package com.sakuraryoko.afkplus.modinit;

import com.sakuraryoko.afkplus.AfkPlus;
import com.sakuraryoko.afkplus.Reference;
import com.sakuraryoko.afkplus.commands.CommandRegister;
import com.sakuraryoko.afkplus.compat.carpet.CarpetCompat;
import com.sakuraryoko.afkplus.compat.morecolors.TextHandler;
import com.sakuraryoko.afkplus.compat.styledplayerlist.StyledPlayerListCompat;
import com.sakuraryoko.afkplus.compat.vanish.VanishEventsCompat;
import com.sakuraryoko.afkplus.config.AfkConfigHandler;
import com.sakuraryoko.afkplus.config.ConfigWrap;
import com.sakuraryoko.afkplus.events.PlayerEventsHandler;
import com.sakuraryoko.afkplus.events.ServerEventsHandler;
import com.sakuraryoko.afkplus.placeholders.PlaceholderManager;
import com.sakuraryoko.corelib.api.modinit.IModInitDispatcher;
import com.sakuraryoko.corelib.api.modinit.ModInitData;
import com.sakuraryoko.corelib.api.text.ITextHandler;
import com.sakuraryoko.corelib.impl.config.ConfigManager;
import com.sakuraryoko.corelib.impl.events.players.PlayerEventsManager;
import com.sakuraryoko.corelib.impl.events.server.ServerEventsManager;

public class AfkPlusInit implements IModInitDispatcher
{
    private static final AfkPlusInit INSTANCE = new AfkPlusInit();
    public static AfkPlusInit getInstance() { return INSTANCE; }

    private final ModInitData MOD_DATA;
    private boolean INIT = false;

    public AfkPlusInit()
    {
        this.MOD_DATA = new ModInitData(Reference.MOD_ID);
        this.MOD_DATA.setTextHandler(this.getTextHandler());
    }

    @Override
    public ModInitData getModInit()
    {
        return this.MOD_DATA;
    }

    @Override
    public String getModId()
    {
        return Reference.MOD_ID;
    }

    @Override
    public ITextHandler getTextHandler()
    {
        return TextHandler.getInstance();
    }

    @Override
    public boolean isDebug()
    {
        return Reference.DEBUG || ConfigWrap.afk().debugMode;
    }

    @Override
    public boolean isInitComplete()
    {
        return this.INIT;
    }

    @Override
    public void reset()
    {
        // NO-OP
    }

    @Override
    public void onModInit()
    {
        AfkPlus.debugLog("Initializing Mod.");
        for (String s : this.getBasic(ModInitData.BASIC_INFO))
        {
            AfkPlus.LOGGER.info(s);
        }

        AfkPlus.debugLog("Config Initializing.");
        ConfigManager.getInstance().registerConfigDispatcher(AfkConfigHandler.getInstance());
        AfkPlus.debugLog("Registering Placeholders.");
        PlaceholderManager.register();
        AfkPlus.debugLog("Registering commands.");
        CommandRegister.register();
        AfkPlus.debugLog("Registering Handlers.");

        ServerEventsManager.getInstance().registerEventDispatcher(ServerEventsHandler.getInstance());
        PlayerEventsManager.getInstance().registerPlayerEvents(PlayerEventsHandler.getInstance());

        if (VanishEventsCompat.getInstance().hasVanish())
        {
            VanishEventsCompat.getInstance().registerEvents();
        }
        if (CarpetCompat.getInstance().hasCarpet())
        {
            CarpetCompat.getInstance().handleCarpet();
        }
        if (StyledPlayerListCompat.getInstance().hasStyledPlayerList())
        {
            StyledPlayerListCompat.getInstance().handleStyledPlayerList();
        }

        AfkPlus.debugLog("All Tasks Done.");
        this.INIT = true;
    }
}
