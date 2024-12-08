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

package com.sakuraryoko.afkplus.util;

import java.util.Collection;

import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.metadata.ModMetadata;

public class AfkPlusConflicts
{
    public static boolean checkMods()
    {
        String modTarget;
        String modVer;
        String modName;
        ModMetadata modData;
        boolean modCheck = true;

        AfkLogger.debug("Checking for conflicting mods.");

        // Check for svrutil --> /afk command primarily, the rest is ok
        modTarget = "svrutil";
        if (FabricLoader.getInstance().isModLoaded(modTarget))
        {
            modData = FabricLoader.getInstance().getModContainer(modTarget).get().getMetadata();
            modVer = modData.getVersion().getFriendlyString();
            modName = modData.getName();
            AfkLogger.warn(modName + "-" + modVer
                    + " has been found, please verify that the /afk command is disabled under config/svrutil/commands.json.");
            modCheck = false;
        }

        // Check for antilogout --> /afk command, and changes timeout behavior's
        // (Remove)
        modTarget = "antilogout";
        if (FabricLoader.getInstance().isModLoaded(modTarget))
        {
            modData = FabricLoader.getInstance().getModContainer(modTarget).get().getMetadata();
            modVer = modData.getVersion().getFriendlyString();
            modName = modData.getName();
            AfkLogger.warn(modName + "-" + modVer
                    + " has been found, please remove this mod to avoid AFK timeout confusion.");
            modCheck = false;
        }

        // Check for autoafk --> afk timeout / damage disabling
        modTarget = "autoafk";
        if (FabricLoader.getInstance().isModLoaded(modTarget))
        {
            modData = FabricLoader.getInstance().getModContainer(modTarget).get().getMetadata();
            modVer = modData.getVersion().getFriendlyString();
            modName = modData.getName();
            AfkLogger.warn(modName + "-" + modVer
                    + " has been found, please remove this mod to avoid AFK timeout confusion.");
            modCheck = false;
        }

        // Check for sessility --> changes timeout behavior's (Remove)
        modTarget = "sessility";
        if (FabricLoader.getInstance().isModLoaded(modTarget))
        {
            modData = FabricLoader.getInstance().getModContainer(modTarget).get().getMetadata();
            modVer = modData.getVersion().getFriendlyString();
            modName = modData.getName();
            AfkLogger.warn(modName + "-" + modVer
                    + " has been found, please remove this mod to avoid AFK timeout confusion.");
            modCheck = false;
        }
        // Check for playtime-tracker --> changes timeout behavior's (Remove)
        modTarget = "playtime-tracker";
        if (FabricLoader.getInstance().isModLoaded(modTarget))
        {
            modData = FabricLoader.getInstance().getModContainer(modTarget).get().getMetadata();
            modVer = modData.getVersion().getFriendlyString();
            modName = modData.getName();
            AfkLogger.warn(modName + "-" + modVer
                    + " has been found, please remove this mod to avoid AFK timeout/player list confusion.");
            modCheck = false;
        }

        // Check for afkdisplay --> this is literary an outdated version of this afkplus
        modTarget = "afkdisplay";
        if (FabricLoader.getInstance().isModLoaded(modTarget))
        {
            modData = FabricLoader.getInstance().getModContainer(modTarget).get().getMetadata();
            modVer = modData.getVersion().getFriendlyString();
            modName = modData.getName();
            AfkLogger.warn(modName + "-" + modVer
                    + " has been found, please remove this mod to avoid AFK timeout/player list confusion.");
            modCheck = false;
        }

        return modCheck;
    }

    public static boolean checkDatapacks(Collection<String> dpCollection)
    {
        boolean dpCheck = true;
        // Check for any data packs matching with "afk"
        AfkLogger.debug("Data pack reload detected.  Checking for conflicting data packs.");
        for (String dpString : dpCollection)
        {
            if (dpString.contains("afk") || dpString.contains("Afk") || dpString.contains("AFK"))
            {
                AfkLogger.warn(
                        "Possible conflict found with data pack: " + dpString + " -- please remove/disable it.");
                dpCheck = false;
            }
        }
        return dpCheck;
    }
}
