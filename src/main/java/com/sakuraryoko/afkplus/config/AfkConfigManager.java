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

import java.nio.file.Files;
import java.nio.file.Path;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParser;

import com.sakuraryoko.afkplus.AfkPlusMod;
import com.sakuraryoko.afkplus.AfkPlusReference;
import com.sakuraryoko.afkplus.config.interfaces.IConfigData;
import com.sakuraryoko.afkplus.config.interfaces.IConfigDispatch;

public class AfkConfigManager
{
    private static final AfkConfigManager INSTANCE = new AfkConfigManager();
    public static AfkConfigManager getInstance() { return INSTANCE; }
    private final Gson GSON = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();

    public void initAllConfigs()
    {
        this.checkForTomlFile();
    }

    public void loadAllConfigs()
    {
        AfkConfigHandler.getInstance().onPreLoadConfig();
        this.loadEach(AfkConfigHandler.getInstance());
        AfkConfigHandler.getInstance().onPostLoadConfig();
    }

    public void saveAllConfigs()
    {
        if (AfkConfigHandler.getInstance().isLoaded())
        {
            AfkConfigHandler.getInstance().onPreSaveConfig();
            this.saveEach(AfkConfigHandler.getInstance());
            AfkConfigHandler.getInstance().onPostSaveConfig();
        }
        else
        {
            // Also saves the file
            AfkConfigHandler.getInstance().onPreLoadConfig();
            this.loadEach(AfkConfigHandler.getInstance());
            AfkConfigHandler.getInstance().onPostLoadConfig();
        }
    }

    public void reloadAllConfigs()
    {
        this.loadEach(AfkConfigHandler.getInstance());
    }

    @SuppressWarnings("deprecation")
    private void checkForTomlFile()
    {
        try
        {
            var file = AfkPlusReference.CONFIG_DIR.resolve(AfkPlusReference.MOD_ID +".toml");

            if (Files.exists(file))
            {
                AfkPlusMod.LOGGER.warn("checkForTomlFile(): Found legacy TOML file [{}.toml]; importing ...", file.toString());
                AfkConfigHandler.getInstance().defaults();

                // Load TOML Config (Without saving it)
                TomlConfigManager.initConfig();
                TomlConfigManager.loadConfig();

                // Copy
                ConfigWrap.afk().fromToml(TomlConfigManager.CONFIG.afkPlusOptions);
                ConfigWrap.mess().fromToml(TomlConfigManager.CONFIG.messageOptions);
                ConfigWrap.pack().fromToml(TomlConfigManager.CONFIG.packetOptions);
                ConfigWrap.place().fromToml(TomlConfigManager.CONFIG.PlaceholderOptions);
                ConfigWrap.list().fromToml(TomlConfigManager.CONFIG.playerListOptions);

                // Save As Json
                AfkConfigHandler.getInstance().onPreSaveConfig();
                this.saveEach(AfkConfigHandler.getInstance());
                AfkConfigHandler.getInstance().execute();
                AfkConfigHandler.getInstance().onPostSaveConfig();

                // Delete it, never to be seen again :)
                AfkPlusMod.LOGGER.info("checkForTomlFile(): Deleting legacy TOML file [{}.toml]", file.toString());
                Files.delete(file);
            }
        }
        catch (Exception err)
        {
            AfkPlusMod.LOGGER.error("checkForTomlFile(): Error converting legacy TOML file // {}", err.getMessage());
        }
    }

    private void loadEach(IConfigDispatch config)
    {
        IConfigData conf = config.newConfig();

        AfkPlusMod.debugLog("loadEach(): --> [{}.json]", config.getConfigName());

        try
        {
            Path dir;

            if (config.useRootDir())
            {
                dir = AfkPlusReference.CONFIG_DIR;
            }
            else
            {
                dir = AfkPlusReference.CONFIG_DIR.resolve(config.getConfigRoot());
            }

            if (!Files.isDirectory(dir))
            {
                Files.createDirectory(dir);
            }

            var file = dir.resolve(config.getConfigName() + ".json");

            if (Files.exists(file))
            {
                var data = JsonParser.parseString(Files.readString(file));
                conf = GSON.fromJson(data, conf.getClass());
                AfkPlusMod.LOGGER.info("loadEach(): Read config for [{}.json]", config.getConfigName());
            }
            else
            {
                conf = config.defaults();
                AfkPlusMod.LOGGER.info("loadEach(): Config defaults for [{}.json]", config.getConfigName());
            }

            conf = config.update(conf);
            Files.writeString(file, GSON.toJson(conf));
            config.execute();
        }
        catch (Exception e)
        {
            AfkPlusMod.LOGGER.error("loadEach(): Error reading config [{}.json] // {}", config.getConfigName(), e.getMessage());
        }
    }

    private void saveEach(IConfigDispatch config)
    {
        IConfigData conf;

        AfkPlusMod.debugLog("saveEach(): --> [{}.json]", config.getConfigName());

        try
        {
            Path dir;

            if (config.useRootDir())
            {
                dir = AfkPlusReference.CONFIG_DIR;
            }
            else
            {
                dir = AfkPlusReference.CONFIG_DIR.resolve(config.getConfigRoot());
            }

            if (!Files.isDirectory(dir))
            {
                Files.createDirectory(dir);
            }

            var file = dir.resolve(config.getConfigName() + ".json");

            if (Files.exists(file))
            {
                AfkPlusMod.LOGGER.info("saveEach(): Deleting existing config file: [{}]", file.toString());
                Files.delete(file);
            }

            conf = config.getConfig();

            if (conf != null)
            {
                Files.writeString(file, GSON.toJson(conf));
                AfkPlusMod.LOGGER.info("saveEach(): Wrote config for [{}.json]", config.getConfigName());
            }
            else
            {
                AfkPlusMod.LOGGER.error("saveEach(): Error saving config file [{}.json] // config is empty!", config.getConfigName());
            }
        }
        catch (Exception e)
        {
            AfkPlusMod.LOGGER.error("saveEach(): Error saving config file [{}.json] // {}", config.getConfigName(), e.getMessage());
        }
    }
}
