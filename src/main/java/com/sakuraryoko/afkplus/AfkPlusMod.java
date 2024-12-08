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
//$$ import com.sakuraryoko.afkplus.compat.TextParser;
//#else
//#endif
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;

import com.sakuraryoko.afkplus.commands.CommandManager;
import com.sakuraryoko.afkplus.config.ConfigManager;
import com.sakuraryoko.afkplus.events.ServerEvents;
import com.sakuraryoko.afkplus.nodes.NodeManager;
import com.sakuraryoko.afkplus.placeholders.PlaceholderManager;
import com.sakuraryoko.afkplus.util.AfkPlusConflicts;
import com.sakuraryoko.afkplus.util.AfkPlusInfo;
import com.sakuraryoko.afkplus.util.AfkPlusLogger;

import static com.sakuraryoko.afkplus.data.ModData.AFK_DEBUG;

public class AfkPlusMod
{
    public static void init()
    {
        AFK_DEBUG = false;

        AfkPlusLogger.initLogger();
        AfkPlusLogger.debug("Initializing Mod.");

        AfkPlusInfo.initModInfo();
        AfkPlusInfo.displayModInfo();

        if (AfkPlusInfo.isClient())
        {
            AfkPlusLogger.info("MOD is running in a CLIENT Environment.");
        }
        if (!AfkPlusConflicts.checkMods())
        {
            AfkPlusLogger.warn("Mod conflicts check has FAILED.");
        }

        AfkPlusLogger.debug("Config Initializing.");
        ConfigManager.initConfig();
        AfkPlusLogger.debug("Loading Config.");
        ConfigManager.loadConfig();
        AfkPlusLogger.debug("Initializing nodes.");
        NodeManager.initNodes();
        AfkPlusLogger.debug("Registering nodes.");
        NodeManager.registerNodes();
        AfkPlusLogger.debug("Registering Placeholders.");
        PlaceholderManager.register();
        AfkPlusLogger.debug("Building Text Parser.");
        //#if MC >= 12006
        //$$ TextParser.build();
        //#else
        //#endif
        AfkPlusLogger.debug("Registering commands.");
        CommandManager.register();

        AfkPlusLogger.debug("Registering Server Events.");
        ServerLifecycleEvents.SERVER_STARTING.register(ServerEvents::starting);
        ServerLifecycleEvents.SERVER_STARTED.register(ServerEvents::started);
        ServerLifecycleEvents.END_DATA_PACK_RELOAD.register((server, serverResourceManager, success) -> ServerEvents.dpReload(server));
        ServerLifecycleEvents.SERVER_STOPPING.register(ServerEvents::stopping);
        ServerLifecycleEvents.SERVER_STOPPED.register(ServerEvents::stopped);
        AfkPlusLogger.debug("All Tasks Done.");
    }
}
