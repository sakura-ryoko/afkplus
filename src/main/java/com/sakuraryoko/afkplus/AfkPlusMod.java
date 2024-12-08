package com.sakuraryoko.afkplus;

import com.sakuraryoko.afkplus.compat.TextParser;
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
        TextParser.build();
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
