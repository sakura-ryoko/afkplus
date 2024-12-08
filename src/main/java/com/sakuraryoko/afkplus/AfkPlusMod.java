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
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;

import com.sakuraryoko.afkplus.commands.CommandManager;
import com.sakuraryoko.afkplus.config.ConfigManager;
import com.sakuraryoko.afkplus.events.ServerEvents;
import com.sakuraryoko.afkplus.text.nodes.NodeManager;
import com.sakuraryoko.afkplus.text.placeholders.PlaceholderManager;
import com.sakuraryoko.afkplus.util.AfkPlusConflicts;
import com.sakuraryoko.afkplus.util.AfkPlusInfo;
import com.sakuraryoko.afkplus.util.AfkLogger;

import static com.sakuraryoko.afkplus.AfkPlusReference.AFK_DEBUG;

public class AfkPlusMod
{
    public static void init()
    {
        AFK_DEBUG = false;

        AfkLogger.initLogger();
        AfkLogger.debug("Initializing Mod.");

        AfkPlusInfo.initModInfo();
        AfkPlusInfo.displayModInfo();

        if (AfkPlusInfo.isClient())
        {
            AfkLogger.info("MOD is running in a CLIENT Environment.");
        }
        if (!AfkPlusConflicts.checkMods())
        {
            AfkLogger.warn("Mod conflicts check has FAILED.");
        }

        AfkLogger.debug("Config Initializing.");
        ConfigManager.initConfig();
        AfkLogger.debug("Loading Config.");
        ConfigManager.loadConfig();
        AfkLogger.debug("Initializing nodes.");
        NodeManager.initNodes();
        AfkLogger.debug("Registering nodes.");
        NodeManager.registerNodes();
        AfkLogger.debug("Registering Placeholders.");
        PlaceholderManager.register();
        //#if MC >= 12006
        //$$ AfkLogger.debug("Building Text Parser.");
        //$$ TextParser.build();
        //#else
        //#endif
        AfkLogger.debug("Registering commands.");
        CommandManager.register();

        AfkLogger.debug("Registering Server Events.");
        ServerLifecycleEvents.SERVER_STARTING.register(ServerEvents::starting);
        ServerLifecycleEvents.SERVER_STARTED.register(ServerEvents::started);
        ServerLifecycleEvents.END_DATA_PACK_RELOAD.register((server, serverResourceManager, success) -> ServerEvents.dpReload(server));
        ServerLifecycleEvents.SERVER_STOPPING.register(ServerEvents::stopping);
        ServerLifecycleEvents.SERVER_STOPPED.register(ServerEvents::stopped);
        AfkLogger.debug("All Tasks Done.");
    }
}
