package io.github.sakuraryoko.afkplus;

import io.github.sakuraryoko.afkplus.commands.CommandManager;
import io.github.sakuraryoko.afkplus.config.ConfigManager;
import io.github.sakuraryoko.afkplus.events.ServerEvents;
import io.github.sakuraryoko.afkplus.placeholders.PlaceholderManager;
import io.github.sakuraryoko.afkplus.util.AfkPlusConflicts;
import io.github.sakuraryoko.afkplus.util.AfkPlusInfo;
import io.github.sakuraryoko.afkplus.util.AfkPlusLogger;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;

public class AfkPlusMod {

    // Generic Mod init
    public static void init() {
        AfkPlusLogger.initLogger();
        AfkPlusInfo.initModInfo();
        AfkPlusInfo.displayModInfo();
        if (AfkPlusInfo.isClient()) {
            AfkPlusLogger.info("MOD is running in a CLIENT Environment.");
        }
        AfkPlusConflicts.checkMods();
        AfkPlusLogger.debug("Config Initializing.");
        ConfigManager.initConfig();
        ConfigManager.loadConfig();
        AfkPlusLogger.debug("Config successful, registerring Placeholders.");
        PlaceholderManager.register();
        AfkPlusLogger.debug("All Placeholders registered, registerring commands.");
        CommandManager.register();
        AfkPlusLogger.debug("Done registerring commands.");
        ServerLifecycleEvents.SERVER_STARTING.register(server -> {
            ServerEvents.starting(server);
        });
        ServerLifecycleEvents.SERVER_STARTED.register(server -> {
            ServerEvents.started(server);
        });
        ServerLifecycleEvents.END_DATA_PACK_RELOAD.register((server, serverResourceManager, success) -> {
            ServerEvents.dpReload(server);
        });
        ServerLifecycleEvents.SERVER_STOPPING.register((server) -> {
            ServerEvents.stopping(server);
        });
        ServerLifecycleEvents.SERVER_STOPPED.register((server) -> {
            ServerEvents.stopped(server);
        });
    }

}
