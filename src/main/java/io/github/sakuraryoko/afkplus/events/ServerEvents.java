package io.github.sakuraryoko.afkplus.events;

import java.util.Collection;

import io.github.sakuraryoko.afkplus.util.AfkPlusConflicts;
import io.github.sakuraryoko.afkplus.util.AfkPlusLogger;
import net.minecraft.server.MinecraftServer;

public class ServerEvents {
    static private Collection<String> dpCollection;

    public static void starting(MinecraftServer server) {
        AfkPlusLogger.debug("Server is starting. " + server.getName());
    }

    public static void started(MinecraftServer server) {
        dpCollection = server.getDataPackManager().getEnabledNames();
        if (!AfkPlusConflicts.checkDatapacks(dpCollection))
            AfkPlusLogger.warn("MOD Data Pack test has FAILED.");
        AfkPlusLogger.debug("Server has started. " + server.getName());
    }

    public static void dpReload(MinecraftServer server) {
        dpCollection = server.getDataPackManager().getEnabledNames();
        if (!AfkPlusConflicts.checkDatapacks(dpCollection))
            AfkPlusLogger.warn("MOD Data Pack test has FAILED.");
        AfkPlusLogger.debug("Server has reloaded it's data packs. " + server.getName());
    }

    public static void stopping(MinecraftServer server) {
        AfkPlusLogger.debug("Server is stopping. " + server.getName());
    }

    public static void stopped(MinecraftServer server) {
        AfkPlusLogger.debug("Server has stopped. " + server.getName());
    }
}
