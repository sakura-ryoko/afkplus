package io.github.sakuraryoko.afkplus;

import java.util.Collection;

import io.github.sakuraryoko.afkplus.commands.CommandManagerServer;
import io.github.sakuraryoko.afkplus.config.ConfigManager;
import io.github.sakuraryoko.afkplus.placeholders.PlaceholderManager;
import io.github.sakuraryoko.afkplus.util.AfkPlusConflicts;
import io.github.sakuraryoko.afkplus.util.AfkPlusInfo;
import io.github.sakuraryoko.afkplus.util.AfkPlusLogger;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;

public class AfkPlusMod {
    static private Collection<String> dpCollection;

    // Dedicated Server
    public static void initServer() {
        AfkPlusLogger.initLogger();
        AfkPlusInfo.initModInfo();
        AfkPlusInfo.displayModInfo();
        AfkPlusConflicts.checkMods();
        AfkPlusLogger.debug("Config Initializing.");
        ConfigManager.initConfig();
        ConfigManager.loadConfig();
        AfkPlusLogger.debug("Config successful, registering commands.");
        CommandManagerServer.register();
        AfkPlusLogger.debug("Command registrations done, registering placeholders.");
        PlaceholderManager.register();
        AfkPlusLogger.debug("All Placeholders registered, checking datapacks.");
        ServerLifecycleEvents.SERVER_STARTED.register(server -> {
            dpCollection = server.getDataPackManager().getEnabledNames();
            AfkPlusConflicts.checkDatapacks(dpCollection);
        });
        ServerLifecycleEvents.END_DATA_PACK_RELOAD.register((server, serverResourceManager, success) -> {
            dpCollection = server.getDataPackManager().getEnabledNames();
            AfkPlusConflicts.checkDatapacks(dpCollection);
        });
    }

    // Generic Mod
    public static void initMain() {
        AfkPlusLogger.initLogger();
        AfkPlusInfo.initModInfo();
        AfkPlusInfo.displayModInfo();
        if (AfkPlusInfo.isServer()) {
            AfkPlusConflicts.checkMods();
            AfkPlusLogger.debug("Config Initializing.");
            ConfigManager.initConfig();
            ConfigManager.loadConfig();
            AfkPlusLogger.debug("Config successful, registering commands.");
            CommandManagerServer.register();
            AfkPlusLogger.debug("Command registrations done, registering placeholders.");
            PlaceholderManager.register();
            AfkPlusLogger.debug("All Placeholders registered, checking datapacks.");
            ServerLifecycleEvents.SERVER_STARTED.register(server -> {
                dpCollection = server.getDataPackManager().getEnabledNames();
                AfkPlusConflicts.checkDatapacks(dpCollection);
            });
            ServerLifecycleEvents.END_DATA_PACK_RELOAD.register((server, serverResourceManager, success) -> {
                dpCollection = server.getDataPackManager().getEnabledNames();
                AfkPlusConflicts.checkDatapacks(dpCollection);
            });
        } else {
            AfkPlusLogger.warn("MOD running in a CLIENT Environment.  Disabling.");
        }
    }

    // Integrated server launch
    public static void startServer() {
        AfkPlusLogger.debug("Integrated server launched.  Do something here.");
    }
}
