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

import java.io.File;

import net.fabricmc.loader.api.FabricLoader;

import com.moandjiezana.toml.Toml;

import com.sakuraryoko.afkplus.AfkPlus;
import com.sakuraryoko.afkplus.Reference;
import com.sakuraryoko.afkplus.config.data.TomlConfigData;

@Deprecated
public class TomlConfigManager
{
    public static TomlConfigData CONFIG = new TomlConfigData();

    public static void initConfig()
    {
        CONFIG.afkPlusOptions.afkPlusCommandPermissions = 3;
        CONFIG.afkPlusOptions.enableAfkCommand = true;
        CONFIG.afkPlusOptions.enableNoAfkCommand = true;
        CONFIG.afkPlusOptions.enableAfkInfoCommand = true;
        CONFIG.afkPlusOptions.enableAfkExCommand = true;
        CONFIG.afkPlusOptions.afkCommandPermissions = 0;
        CONFIG.afkPlusOptions.noAfkCommandPermissions = 0;
        CONFIG.afkPlusOptions.afkExCommandPermissions = 0;
        CONFIG.afkPlusOptions.afkInfoCommandPermissions = 2;
        CONFIG.afkPlusOptions.afkTimeoutString = "<i><gray>timeout<r>";
        CONFIG.packetOptions.resetOnLook = false;
        CONFIG.packetOptions.resetOnMovement = false;
        CONFIG.packetOptions.timeoutSeconds = 240;
        CONFIG.packetOptions.disableDamage = false;
        CONFIG.packetOptions.disableDamageCooldown = 15;
        CONFIG.packetOptions.bypassSleepCount = true;
        CONFIG.packetOptions.bypassInsomnia = true;
        CONFIG.packetOptions.afkKickEnabled = false;
        CONFIG.packetOptions.afkKickNonSurvival = false;
        CONFIG.packetOptions.afkKickTimer = 3600;
        CONFIG.packetOptions.afkKickSafePermissions = 3;
        CONFIG.PlaceholderOptions.afkPlaceholder = "<i><gray>[AFK%afkplus:invulnerable%]<r>";
        CONFIG.PlaceholderOptions.afkPlusNamePlaceholder = "%player:displayname%";
        CONFIG.PlaceholderOptions.afkPlusNamePlaceholderAfk = "<i><gray>[AFK%afkplus:invulnerable%] %player:displayname_unformatted%<r>";
        CONFIG.PlaceholderOptions.afkDurationPlaceholderFormatting = "<green>";
        CONFIG.PlaceholderOptions.afkTimePlaceholderFormatting = "<green>";
        CONFIG.PlaceholderOptions.afkReasonPlaceholderFormatting = "";
        CONFIG.PlaceholderOptions.afkDurationPretty = false;
        CONFIG.PlaceholderOptions.afkInvulnerablePlaceholder = ":<red>I<r>";
        CONFIG.playerListOptions.afkPlayerName = "<i><gray>[AFK%afkplus:invulnerable%] %player:displayname%<r>";
        CONFIG.playerListOptions.enableListDisplay = true;
        CONFIG.playerListOptions.updateInterval = 15;
        CONFIG.messageOptions.enableMessages = true;
        CONFIG.messageOptions.whenAfk = "%player:displayname% <yellow>is now AFK<r>";
        CONFIG.messageOptions.whenReturn = "%player:displayname% <yellow>is no longer AFK<r>";
        CONFIG.messageOptions.prettyDuration = true;
        CONFIG.messageOptions.defaultReason = "<gray>poof!<r>";
        CONFIG.messageOptions.whenDamageDisabled = "%player:displayname% <yellow>is marked as <red>Invulnerable.<r>";
        CONFIG.messageOptions.whenDamageEnabled = "%player:displayname% <yellow>is no longer <red>Invulnerable.<r>";
        CONFIG.messageOptions.displayDuration = true;
        CONFIG.messageOptions.afkKickMessage = "<copper>AFK beyond the allowed time limit set by your Administrator.<r>";
        CONFIG.messageOptions.whenKicked = "%player:displayname% <copper>was kicked for being AFK.<r>";
        AfkPlus.debugLog("Default config initialized.");
    }

    public static void testConfig()
    {
        // Checks for invalid values
        if (CONFIG.afkPlusOptions.afkPlusCommandPermissions < 0 || CONFIG.afkPlusOptions.afkPlusCommandPermissions > 4)
        {
            CONFIG.afkPlusOptions.afkPlusCommandPermissions = 3;
        }
        //CONFIG.afkPlusOptions.enableAfkCommand = true;
        //CONFIG.afkPlusOptions.enableNoAfkCommand = true;
        //CONFIG.afkPlusOptions.enableAfkInfoCommand = true;
        //CONFIG.afkPlusOptions.enableAfkExCommand = true;
        if (CONFIG.afkPlusOptions.afkCommandPermissions < 0 || CONFIG.afkPlusOptions.afkCommandPermissions > 4)
        {
            CONFIG.afkPlusOptions.afkCommandPermissions = 0;
        }
        if (CONFIG.afkPlusOptions.noAfkCommandPermissions < 0 || CONFIG.afkPlusOptions.noAfkCommandPermissions > 4)
        {
            CONFIG.afkPlusOptions.noAfkCommandPermissions = 0;
        }
        if (CONFIG.afkPlusOptions.afkInfoCommandPermissions < 0 || CONFIG.afkPlusOptions.afkInfoCommandPermissions > 4)
        {
            CONFIG.afkPlusOptions.afkInfoCommandPermissions = 2;
        }
        if (CONFIG.afkPlusOptions.afkExCommandPermissions < 0 || CONFIG.afkPlusOptions.afkExCommandPermissions > 4)
        {
            CONFIG.afkPlusOptions.afkExCommandPermissions = 0;
        }
        if (CONFIG.afkPlusOptions.afkTimeoutString == null)
        {
            CONFIG.afkPlusOptions.afkTimeoutString = "<i><gray>timeout<r>";
        }
        //CONFIG.packetOptions.resetOnLook = false;
        //CONFIG.packetOptions.resetOnMovement = true;
        if (CONFIG.packetOptions.timeoutSeconds < -1 || CONFIG.packetOptions.timeoutSeconds > 3600)
        {
            CONFIG.packetOptions.timeoutSeconds = 240;
        }
        //CONFIG.packetOptions.disableDamage = false;
        if (CONFIG.packetOptions.disableDamageCooldown < -1 || CONFIG.packetOptions.disableDamageCooldown > 3600)
        {
            CONFIG.packetOptions.disableDamageCooldown = 15;
        }
        //CONFIG.packetOptions.bypassSleepCount = true;
        //CONFIG.packetOptions.bypassInsomnia = true;
        //CONFIG.packetOptions.afkKickEnabled = false;
        //CONFIG.packetOptions.afkKickNonSurvival = false;
        if (CONFIG.packetOptions.afkKickTimer < -1 || CONFIG.packetOptions.afkKickTimer > 14400)
        {
            CONFIG.packetOptions.afkKickTimer = 3600;
        }
        if (CONFIG.packetOptions.afkKickSafePermissions < 0 || CONFIG.packetOptions.afkKickSafePermissions > 4)
        {
            CONFIG.packetOptions.afkKickSafePermissions = 3;
        }
        if (CONFIG.PlaceholderOptions.afkPlaceholder == null)
        {
            CONFIG.PlaceholderOptions.afkPlaceholder = "<i><gray>[AFK%afkplus:invulnerable%]<r>";
        }
        if (CONFIG.PlaceholderOptions.afkPlusNamePlaceholder == null)
        {
            CONFIG.PlaceholderOptions.afkPlusNamePlaceholder = "%player:displayname%";
        }
        if (CONFIG.PlaceholderOptions.afkPlusNamePlaceholderAfk == null)
        {
            CONFIG.PlaceholderOptions.afkPlusNamePlaceholderAfk = "<i><gray>[AFK%afkplus:invulnerable%] %player:displayname_unformatted%<r>";
        }
        if (CONFIG.PlaceholderOptions.afkDurationPlaceholderFormatting == null)
        {
            CONFIG.PlaceholderOptions.afkDurationPlaceholderFormatting = "<green>";
        }
        if (CONFIG.PlaceholderOptions.afkTimePlaceholderFormatting == null)
        {
            CONFIG.PlaceholderOptions.afkTimePlaceholderFormatting = "<green>";
        }
        if (CONFIG.PlaceholderOptions.afkReasonPlaceholderFormatting == null)
        {
            CONFIG.PlaceholderOptions.afkReasonPlaceholderFormatting = "";
        }
        //CONFIG.PlaceholderOptions.afkDurationPretty = false;
        if (CONFIG.PlaceholderOptions.afkInvulnerablePlaceholder == null)
        {
            CONFIG.PlaceholderOptions.afkInvulnerablePlaceholder = ":<red>I<r>";
        }
        if (CONFIG.playerListOptions.afkPlayerName == null)
        {
            CONFIG.playerListOptions.afkPlayerName = "<i><gray>[AFK%afkplus:invulnerable%] %player:displayname%<r>";
        }
        //CONFIG.playerListOptions.enableListDisplay = true;
        if (CONFIG.playerListOptions.updateInterval < -1 || CONFIG.playerListOptions.updateInterval > 600)
        {
            CONFIG.playerListOptions.updateInterval = 15;
        }
        //CONFIG.messageOptions.enableMessages = true;
        if (CONFIG.messageOptions.whenAfk == null)
        {
            CONFIG.messageOptions.whenAfk = "%player:displayname% <yellow>is now AFK<r>";
        }
        if (CONFIG.messageOptions.whenReturn == null)
        {
            CONFIG.messageOptions.whenReturn = "%player:displayname% <yellow>is no longer AFK<r>";
        }
        //CONFIG.messageOptions.prettyDuration = true;
        if (CONFIG.messageOptions.defaultReason == null)
        {
            CONFIG.messageOptions.defaultReason = "<gray>poof!<r>";
        }
        if (CONFIG.messageOptions.whenDamageDisabled == null)
        {
            CONFIG.messageOptions.whenDamageDisabled = "%player:displayname% <yellow>is marked as <red>Invulnerable.<r>";
        }
        if (CONFIG.messageOptions.whenDamageEnabled == null)
        {
            CONFIG.messageOptions.whenDamageEnabled = "%player:displayname% <yellow>is no longer <red>Invulnerable.<r>";
        }
        //CONFIG.messageOptions.displayDuration = true;
        if (CONFIG.messageOptions.afkKickMessage == null)
        {
            CONFIG.messageOptions.afkKickMessage = "<copper>AFK beyond the allowed time limit set by your Administrator.<r>";
        }
        if (CONFIG.messageOptions.whenKicked == null)
        {
            CONFIG.messageOptions.whenKicked = "%player:displayname% <copper>was kicked for being AFK.<r>";
        }
        AfkPlus.debugLog("Config checked for null values.");
    }

    public static void loadConfig()
    {
        File conf = Reference.CONFIG_DIR.resolve(Reference.MOD_ID + ".toml").toFile();
        try
        {
            if (conf.exists())
            {
                CONFIG = new Toml().read(conf).to(TomlConfigData.class);
            }
            else
            {
                AfkPlus.LOGGER.info("Config {}.toml not found, creating new file.", Reference.MOD_ID);
                //initConfig();
                try
                {
                    if (!conf.createNewFile())
                    {
                        AfkPlus.LOGGER.error("Error creating config file {}.toml .", Reference.MOD_ID);
                    }
                }
                catch (Exception ignored)
                {
                }
            }
            testConfig();
            //new TomlWriter().write(CONFIG, conf);
        }
        catch (Exception ex)
        {
            throw new RuntimeException(ex);
        }
    }

    public static void reloadConfig()
    {
        AfkPlus.LOGGER.info("Reloading Config.");
        loadConfig();
    }
}
