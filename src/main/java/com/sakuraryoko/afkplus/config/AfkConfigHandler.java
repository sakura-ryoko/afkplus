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

package com.sakuraryoko.afkplus.config;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

import com.sakuraryoko.afkplus.AfkPlusMod;
import com.sakuraryoko.afkplus.AfkPlusReference;
import com.sakuraryoko.afkplus.config.data.AfkConfigData;
import com.sakuraryoko.afkplus.config.data.options.*;
import com.sakuraryoko.afkplus.config.interfaces.IConfigData;
import com.sakuraryoko.afkplus.config.interfaces.IConfigDispatch;

public class AfkConfigHandler implements IConfigDispatch
{
    private static final AfkConfigHandler INSTANCE = new AfkConfigHandler();
    public static AfkConfigHandler getInstance() { return INSTANCE; }
    private final AfkConfigData CONFIG = newConfig();
    private final String CONFIG_ROOT = ".";
    private final String CONFIG_NAME = AfkPlusReference.MOD_ID;
    private boolean loaded = false;

    @Override
    public String getConfigRoot()
    {
        return this.CONFIG_ROOT;
    }

    @Override
    public boolean useRootDir()
    {
        return true;
    }

    @Override
    public String getConfigName()
    {
        return this.CONFIG_NAME;
    }

    @Override
    public AfkConfigData newConfig()
    {
        return new AfkConfigData();
    }

    @Override
    public AfkConfigData getConfig()
    {
        return CONFIG;
    }

    public AfkPlusOptions getAfkPlusOptions()
    {
        return CONFIG.AFK_PLUS;
    }

    public MessageOptions getMessageOptions()
    {
        return CONFIG.MESSAGE;
    }

    public PacketOptions getPacketOptions()
    {
        return CONFIG.PACKET;
    }

    public PlaceholderOptions getPlaceholderOptions()
    {
        return CONFIG.PLACEHOLDER;
    }

    public PlayerListOptions getPlayerListOptions()
    {
        return CONFIG.PLAYER_LIST;
    }

    @Override
    public boolean isLoaded()
    {
        return this.loaded;
    }

    @Override
    public void onPreLoadConfig()
    {
        this.loaded = false;
    }

    @Override
    public void onPostLoadConfig()
    {
        this.loaded = true;
    }

    @Override
    public void onPreSaveConfig()
    {
        this.loaded = false;
    }

    @Override
    public void onPostSaveConfig()
    {
        this.loaded = true;
    }

    @Override
    public AfkConfigData defaults()
    {
        AfkConfigData config = this.newConfig();
        AfkPlusMod.debugLog("AfkConfigHandler#defaults(): Setting default config.");

        // Set default values
        config.config_date = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH.mm.ss", Locale.ROOT).format(ZonedDateTime.now());
        config.AFK_PLUS.defaults();
        config.MESSAGE.defaults();
        config.PACKET.defaults();
        config.PLACEHOLDER.defaults();
        config.PLAYER_LIST.defaults();

        return config;
    }

    @Override
    public AfkConfigData update(IConfigData newConfig)
    {
        AfkConfigData newConf = (AfkConfigData) newConfig;
        AfkPlusMod.debugLog("AfkConfigHandler#update(): Refresh config.");

        // Refresh
        CONFIG.comment = "AFK Plus config " + AfkPlusReference.MC_VERSION + "-" + AfkPlusReference.AFK_VERSION;
        CONFIG.config_date = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH.mm.ss", Locale.ROOT).format(ZonedDateTime.now());
        AfkPlusMod.debugLog("AfkConfigHandler#update(): save_date: {} --> {}", newConf.config_date, CONFIG.config_date);

        // Copy Incoming Config
        CONFIG.AFK_PLUS.copy(newConf.AFK_PLUS);
        CONFIG.MESSAGE.copy(newConf.MESSAGE);
        CONFIG.PACKET.copy(newConf.PACKET);
        CONFIG.PLACEHOLDER.copy(newConf.PLACEHOLDER);
        CONFIG.PLAYER_LIST.copy(newConf.PLAYER_LIST);

        return CONFIG;
    }

    @Override
    public void execute()
    {
        AfkPlusMod.debugLog("AfkConfigHandler#execute(): Execute config.");

        // Do this when the Config gets finalized.
        AfkPlusMod.debugLog("AfkConfigHandler#execute(): new config_date: {}", CONFIG.config_date);
    }
}
