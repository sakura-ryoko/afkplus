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

        AfkPlusLogger.debug("Checking for conflicting mods.");

        // Check for svrutil --> /afk command primarily, the rest is ok
        modTarget = "svrutil";
        if (FabricLoader.getInstance().isModLoaded(modTarget))
        {
            modData = FabricLoader.getInstance().getModContainer(modTarget).get().getMetadata();
            modVer = modData.getVersion().getFriendlyString();
            modName = modData.getName();
            AfkPlusLogger.warn(modName + "-" + modVer
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
            AfkPlusLogger.warn(modName + "-" + modVer
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
            AfkPlusLogger.warn(modName + "-" + modVer
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
            AfkPlusLogger.warn(modName + "-" + modVer
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
            AfkPlusLogger.warn(modName + "-" + modVer
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
            AfkPlusLogger.warn(modName + "-" + modVer
                    + " has been found, please remove this mod to avoid AFK timeout/player list confusion.");
            modCheck = false;
        }

        return modCheck;
    }

    public static boolean checkDatapacks(Collection<String> dpCollection)
    {
        boolean dpCheck = true;
        // Check for any data packs matching with "afk"
        AfkPlusLogger.debug("Data pack reload detected.  Checking for conflicting data packs.");
        for (String dpString : dpCollection)
        {
            if (dpString.contains("afk") || dpString.contains("Afk") || dpString.contains("AFK"))
            {
                AfkPlusLogger.warn(
                        "Possible conflict found with data pack: " + dpString + " -- please remove/disable it.");
                dpCheck = false;
            }
        }
        return dpCheck;
    }
}
