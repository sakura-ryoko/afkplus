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

package com.sakuraryoko.afkplus;

//#if MC >= 12006
//$$ import com.sakuraryoko.afkplus.text.TextParser;
//#else
//#endif

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;

import com.sakuraryoko.afkplus.commands.CommandManager;
import com.sakuraryoko.afkplus.config.AfkConfigManager;
import com.sakuraryoko.afkplus.events.ServerEvents;
import com.sakuraryoko.afkplus.text.nodes.NodeManager;
import com.sakuraryoko.afkplus.text.placeholders.PlaceholderManager;
import com.sakuraryoko.afkplus.util.AfkPlusConflicts;
import com.sakuraryoko.afkplus.util.AfkPlusInfo;

public class AfkPlusMod
{
    public static Logger LOGGER = LogManager.getLogger(AfkPlusReference.MOD_ID);

    public static void init()
    {
        debugLog("Initializing Mod.");

        AfkPlusInfo.initModInfo();
        AfkPlusInfo.displayModInfo();

        if (AfkPlusInfo.isClient())
        {
            LOGGER.info("MOD is running in a CLIENT Environment.");
        }
        if (!AfkPlusConflicts.checkMods())
        {
            LOGGER.warn("Mod conflicts check has FAILED.");
        }

        debugLog("Config Initializing.");
        AfkConfigManager.getInstance().initAllConfigs();
        debugLog("Loading Config.");
        AfkConfigManager.getInstance().loadAllConfigs();
        debugLog("Initializing nodes.");
        NodeManager.initNodes();
        debugLog("Registering nodes.");
        NodeManager.registerNodes();
        debugLog("Registering Placeholders.");
        PlaceholderManager.register();
        //#if MC >= 12006
        //$$ debugLog("Building Text Parser.");
        //$$ TextParser.build();
        //#else
        //#endif
        debugLog("Registering commands.");
        CommandManager.register();

        debugLog("Registering Server Events.");
        ServerLifecycleEvents.SERVER_STARTING.register(ServerEvents::starting);
        ServerLifecycleEvents.SERVER_STARTED.register(ServerEvents::started);
        ServerLifecycleEvents.END_DATA_PACK_RELOAD.register((server, serverResourceManager, success) -> ServerEvents.dpReload(server));
        ServerLifecycleEvents.SERVER_STOPPING.register(ServerEvents::stopping);
        ServerLifecycleEvents.SERVER_STOPPED.register(ServerEvents::stopped);
        debugLog("All Tasks Done.");
    }
    
    public static void debugLog(String key, Object... args)
    {
        if (AfkPlusReference.AFK_DEBUG)
        {
            LOGGER.info(String.format("[DEBUG] %s", key), args);
        }
    }
}
