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
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import com.sakuraryoko.afkplus.AfkPlusMod;
import com.sakuraryoko.afkplus.AfkPlusReference;
import com.sakuraryoko.afkplus.config.interfaces.IConfigData;
import com.sakuraryoko.afkplus.config.interfaces.IConfigDispatch;
import com.sakuraryoko.afkplus.text.config.MoreColorConfigHandler;

public class AfkConfigManager
{
    private static final AfkConfigManager INSTANCE = new AfkConfigManager();
    public static AfkConfigManager getInstance() { return INSTANCE; }
    private final Gson GSON = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();

    public void initAllConfigs()
    {
        // Check for "configs/afkplus.toml"
        this.checkForTomlFile();
        // Check for "configs/afkplus.json" -> Move to "configs/afkplus/afkplus.json"
        this.checkForRootConfig();
    }

    public void loadAllConfigs()
    {
        // Main Config
        AfkConfigHandler.getInstance().onPreLoadConfig();
        this.loadEach(AfkConfigHandler.getInstance());
        AfkConfigHandler.getInstance().onPostLoadConfig();

        // More Colors
        MoreColorConfigHandler.getInstance().onPreLoadConfig();
        this.loadEach(MoreColorConfigHandler.getInstance());
        MoreColorConfigHandler.getInstance().onPostLoadConfig();
    }

    public void saveAllConfigs()
    {
        // Main Config
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

        // More Colors
        if (MoreColorConfigHandler.getInstance().isLoaded())
        {
            MoreColorConfigHandler.getInstance().onPreSaveConfig();
            this.saveEach(MoreColorConfigHandler.getInstance());
            MoreColorConfigHandler.getInstance().onPostSaveConfig();
        }
        else
        {
            // Also saves the file
            MoreColorConfigHandler.getInstance().onPreLoadConfig();
            this.loadEach(MoreColorConfigHandler.getInstance());
            MoreColorConfigHandler.getInstance().onPostLoadConfig();
        }
    }

    public void reloadAllConfigs()
    {
        this.loadEach(AfkConfigHandler.getInstance());
        this.loadEach(MoreColorConfigHandler.getInstance());
    }

    @SuppressWarnings("deprecation")
    private void checkForTomlFile()
    {
        try
        {
            Path tomlFile = AfkPlusReference.CONFIG_DIR.resolve(AfkPlusReference.MOD_ID +".toml");

            if (Files.exists(tomlFile))
            {
                AfkPlusMod.LOGGER.warn("checkForTomlFile(): Found legacy TOML file [{}]; importing ...", tomlFile.getFileName().toString());
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
                AfkPlusMod.LOGGER.info("checkForTomlFile(): Deleting legacy TOML file [{}]", tomlFile.getFileName().toString());
                Files.delete(tomlFile);
            }
        }
        catch (Exception err)
        {
            AfkPlusMod.LOGGER.error("checkForTomlFile(): Error converting legacy TOML file // {}", err.getMessage());
        }
    }

    private void checkForRootConfig()
    {
        try
        {
            Path oldFile = AfkPlusReference.CONFIG_DIR.resolve(AfkPlusReference.MOD_ID +".json");

            // json file found in the root instead of thr afkplus subfolder.
            if (Files.exists(oldFile))
            {
                AfkPlusMod.LOGGER.warn("checkForRootConfig(): Found Root Config file [{}]", oldFile.getFileName().toString());
                Path newDir = AfkPlusReference.CONFIG_DIR.resolve(AfkConfigHandler.getInstance().getConfigRoot());

                if (!Files.isDirectory(newDir))
                {
                    Files.createDirectory(newDir);
                }

                Path newFile = newDir.resolve(AfkConfigHandler.getInstance().getConfigName() + ".json");

                if (Files.exists(newFile))
                {
                    // A new config exists, just delete the old
                    AfkPlusMod.LOGGER.warn("checkForRootConfig(): New config exists [{}/{}], deleting Root Config file [{}] ...", newFile.getParent().getFileName().toString(), newFile.getFileName().toString(), oldFile.getFileName().toString());
                    Files.delete(oldFile);
                }
                else
                {
                    // Move the file
                    AfkPlusMod.LOGGER.warn("checkForRootConfig(): Moving Root Config file [{}] to [{}/{}] ...", oldFile.getFileName().toString(), newFile.getParent().getFileName().toString(), newFile.getFileName().toString());
                    Files.move(oldFile, newFile);
                }
            }
        }
        catch (Exception err)
        {
            AfkPlusMod.LOGGER.error("checkForRootConfig(): Error moving Root Config file // {}", err.getMessage());
        }
    }

    private void loadEach(IConfigDispatch config)
    {
        IConfigData conf = config.newConfig();
        AfkPlusMod.debugLog("loadEach(): --> [{}/{}.json]", config.getConfigRoot(), config.getConfigName());

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

            Path file = dir.resolve(config.getConfigName() + ".json");

            if (Files.exists(file))
            {
                JsonElement data = JsonParser.parseString(Files.readString(file));
                conf = GSON.fromJson(data, conf.getClass());
                AfkPlusMod.LOGGER.info("loadEach(): Read config for [{}/{}]", dir.getFileName().toString(), file.getFileName().toString());
            }
            else
            {
                conf = config.defaults();
                AfkPlusMod.LOGGER.info("loadEach(): Config defaults for [{}/{}.json]", config.getConfigRoot(), config.getConfigName());
            }

            conf = config.update(conf);
            Files.writeString(file, GSON.toJson(conf));
            config.execute();
        }
        catch (Exception e)
        {
            AfkPlusMod.LOGGER.error("loadEach(): Error reading config [{}/{}.json] // {}", config.getConfigRoot(), config.getConfigName(), e.getMessage());
        }
    }

    private void saveEach(IConfigDispatch config)
    {
        IConfigData conf;

        AfkPlusMod.debugLog("saveEach(): --> [{}/{}.json]", config.getConfigRoot(), config.getConfigName());

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

            Path file = dir.resolve(config.getConfigName() + ".json");

            if (Files.exists(file))
            {
                AfkPlusMod.LOGGER.info("saveEach(): Deleting existing config file: [{}/{}]", dir.getFileName().toString(), file.getFileName().toString());
                Files.delete(file);
            }

            conf = config.getConfig();

            if (conf != null)
            {
                Files.writeString(file, GSON.toJson(conf));
                AfkPlusMod.LOGGER.info("saveEach(): Wrote config for [{}/{}.json]", config.getConfigRoot(), config.getConfigName());
            }
            else
            {
                AfkPlusMod.LOGGER.error("saveEach(): Error saving config file [{}/{}.json] // config is empty!", config.getConfigRoot(), config.getConfigName());
            }
        }
        catch (Exception e)
        {
            AfkPlusMod.LOGGER.error("saveEach(): Error saving config file [{}/{}.json] // {}", config.getConfigRoot(), config.getConfigName(), e.getMessage());
        }
    }
}
