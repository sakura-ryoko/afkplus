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
        AfkPlusLogger.debug("Initializing Mod.");
        AfkPlusInfo.initModInfo();
        AfkPlusInfo.displayModInfo();
        if (AfkPlusInfo.isClient()) {
            AfkPlusLogger.info("MOD is running in a CLIENT Environment.");
        }
        if (!AfkPlusConflicts.checkMods())
            AfkPlusLogger.warn("Mod conflicts check has FAILED.");
        AfkPlusLogger.debug("Config Initializing.");
        ConfigManager.initConfig();
        AfkPlusLogger.debug("Loading Config.");
        ConfigManager.loadConfig();
        AfkPlusLogger.debug("Registering Placeholders.");
        PlaceholderManager.register();
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
