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

package com.sakuraryoko.afkplus.impl.config;

import java.nio.file.Files;
import java.nio.file.Path;

import org.jetbrains.annotations.ApiStatus;

import com.sakuraryoko.afkplus.impl.AfkPlus;
import com.sakuraryoko.afkplus.impl.Reference;
import com.sakuraryoko.afkplus.impl.config.data.AfkConfigData;
import com.sakuraryoko.afkplus.impl.config.data.options.*;
import com.sakuraryoko.afkplus.impl.modinit.AfkPlusInit;
import com.sakuraryoko.corelib.api.config.IConfigData;
import com.sakuraryoko.corelib.api.config.IConfigDispatch;
import com.sakuraryoko.corelib.api.time.TimeFormat;

@ApiStatus.Internal
public class AfkConfigHandler implements IConfigDispatch
{
    private static final AfkConfigHandler INSTANCE = new AfkConfigHandler();
    public static AfkConfigHandler getInstance() { return INSTANCE; }
    private AfkConfigData CONFIG = newConfig();
    private final String CONFIG_ROOT = ".";
    private final String CONFIG_NAME = Reference.MOD_ID;
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

    public DisableDamageOptions getDisableDamageOptions()
    {
        return CONFIG.DAMAGE;
    }

    public KickOptions getKickOptions()
    {
        return CONFIG.KICK;
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
    public void initConfig()
    {
        // Check for "configs/afkplus.json" -> Move to "configs/afkplus/afkplus.json"
        this.checkForRootConfig();
    }

    private void checkForRootConfig()
    {
        try
        {
            Path dir = Reference.CONFIG_DIR.resolve(Reference.MOD_ID);

            // json file found in the afkplus subfolder instead of the root
            if (Files.isDirectory(dir))
            {
                AfkPlus.LOGGER.warn("checkForRootConfig(): Found Sub Config dir [{}]", dir.getFileName().toString());

                Path oldFile = dir.resolve(this.getConfigName() + ".json");

                if (Files.exists(oldFile))
                {
                    Path newFile = Reference.CONFIG_DIR.resolve(Reference.MOD_ID +".json");
                    // Move the file
                    AfkPlus.LOGGER.warn("checkForRootConfig(): Moving Root Config file [{}/{}] to [{}] ...", dir.getFileName().toString(), oldFile.getFileName().toString(), newFile.getFileName().toString());
                    Files.move(oldFile, newFile);
                }

                Files.delete(dir);
            }
        }
        catch (Exception err)
        {
            AfkPlus.LOGGER.error("checkForRootConfig(): Error moving Root Config file // {}", err.getMessage());
        }
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
        AfkPlus.debugLog("AfkConfigHandler#defaults(): Setting default config.");

        // Set default values
        config.config_date = TimeFormat.RFC1123.formatNow(null);
        config.AFK_PLUS.defaults();
        config.MESSAGE.defaults();
        config.PACKET.defaults();
        config.DAMAGE.defaults();
        config.KICK.defaults();
        config.PLACEHOLDER.defaults();
        config.PLAYER_LIST.defaults();

        return config;
    }

    @Override
    public AfkConfigData update(IConfigData newConfig)
    {
        AfkConfigData newConf = (AfkConfigData) newConfig;
        AfkPlus.debugLog("AfkConfigHandler#update(): Refresh config.");

        // Refresh
        CONFIG.comment = AfkPlusInit.getInstance().getModVersionString() + " Config";
        CONFIG.config_date = TimeFormat.RFC1123.formatNow(null);
        AfkPlus.debugLog("AfkConfigHandler#update(): save_date: {} --> {}", newConf.config_date, CONFIG.config_date);

        // Upgrade defaulted configs to include "%afkplus:unplugged%" (August 2026)
        if (newConf.PLAYER_LIST.afkPlayerName.equals("<i><gray>[AFK%afkplus:invulnerable%] %player:displayname%<r>"))
        {
            newConf.PLAYER_LIST.afkPlayerName = "<i><gray>[AFK%afkplus:invulnerable%%afkplus:unplugged%] %player:displayname%<r>";
        }
        if (newConf.PLACEHOLDER.afkPlaceholder.equals("<i><gray>[AFK%afkplus:invulnerable%]<r>"))
        {
            newConf.PLACEHOLDER.afkPlaceholder = "<i><gray>[AFK%afkplus:invulnerable%%afkplus:unplugged%]<r>";
        }
        if (newConf.PLACEHOLDER.afkPlusNamePlaceholderAfk.equals("<i><gray>[AFK%afkplus:invulnerable%] %player:displayname_unformatted%<r>"))
        {
            newConf.PLACEHOLDER.afkPlusNamePlaceholderAfk = "<i><gray>[AFK%afkplus:invulnerable%%afkplus:unplugged%] %player:displayname_unformatted%<r";
        }

        // Upgrade (June 2026) defaulted configs to replace it with "%afkplus:unplugged%"
        if (newConf.PLAYER_LIST.afkPlayerName.equals("<i><gray>[AFK%afkplus:invulnerable%%afkplus:shadow%] %player:displayname%<r>"))
        {
            newConf.PLAYER_LIST.afkPlayerName = "<i><gray>[AFK%afkplus:invulnerable%%afkplus:unplugged%] %player:displayname%<r>";
        }
        if (newConf.PLACEHOLDER.afkPlaceholder.equals("<i><gray>[AFK%afkplus:invulnerable%%afkplus:shadow%]<r>"))
        {
            newConf.PLACEHOLDER.afkPlaceholder = "<i><gray>[AFK%afkplus:invulnerable%%afkplus:unplugged%]<r>";
        }
        if (newConf.PLACEHOLDER.afkPlusNamePlaceholderAfk.equals("<i><gray>[AFK%afkplus:invulnerable%%afkplus:shadow%] %player:displayname_unformatted%<r>"))
        {
            newConf.PLACEHOLDER.afkPlusNamePlaceholderAfk = "<i><gray>[AFK%afkplus:invulnerable%%afkplus:unplugged%] %player:displayname_unformatted%<r";
        }

        // Copy Incoming Config
        CONFIG.AFK_PLUS.copy(newConf.AFK_PLUS);
        CONFIG.MESSAGE.copy(newConf.MESSAGE);
        CONFIG.PACKET.copy(newConf.PACKET);
        CONFIG.DAMAGE.copy(newConf.DAMAGE);
        CONFIG.KICK.copy(newConf.KICK);
        CONFIG.PLACEHOLDER.copy(newConf.PLACEHOLDER);
        CONFIG.PLAYER_LIST.copy(newConf.PLAYER_LIST);

        return CONFIG;
    }

    @Override
    public void execute(boolean fromInit)
    {
        AfkPlus.debugLog("AfkConfigHandler#execute(): Execute config.");

        // Do this when the Config gets finalized.
        AfkPlus.debugLog("AfkConfigHandler#execute(): new config_date: {}", CONFIG.config_date);
    }
}
