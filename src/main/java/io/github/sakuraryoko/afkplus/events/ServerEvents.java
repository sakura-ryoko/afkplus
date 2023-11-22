package io.github.sakuraryoko.afkplus.events;

import java.util.Collection;

import io.github.sakuraryoko.afkplus.util.AfkPlusConflicts;
import io.github.sakuraryoko.afkplus.util.AfkPlusLogger;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.integrated.IntegratedServer;

public class ServerEvents {
    static private Collection<String> dpCollection;

    public static void starting(MinecraftServer server) {
        AfkPlusLogger.debug("Server is starting. " + server.getName());
    }

    public static void integratedServerStart(IntegratedServer server) {
        if (server != null) {
            dpCollection = server.getDataPackManager().getEnabledNames();
            AfkPlusConflicts.checkDatapacks(dpCollection);
            AfkPlusLogger.debug("Integrated Server has started. " + server.getName());
        } else {
            AfkPlusLogger.debug("Integrated Server has started. ");
        }
    }

    public static void started(MinecraftServer server) {
        dpCollection = server.getDataPackManager().getEnabledNames();
        AfkPlusConflicts.checkDatapacks(dpCollection);
        AfkPlusLogger.debug("Server has started. " + server.getName());
    }

    public static void dpReload(MinecraftServer server) {
        dpCollection = server.getDataPackManager().getEnabledNames();
        AfkPlusConflicts.checkDatapacks(dpCollection);
        AfkPlusLogger.debug("Server has reloaded it's datapacks. " + server.getName());
    }

    public static void stopping(MinecraftServer server) {
        AfkPlusLogger.debug("Server is stopping. " + server.getName());
    }

    public static void integratedServerStopping(IntegratedServer server) {
        if (server != null) {
            AfkPlusLogger.debug("Integrated Server is stopping. " + server.getName());
        } else {
            AfkPlusLogger.debug("Integrated Server is stopping. ");
        }
    }

    public static void stopped(MinecraftServer server) {
        AfkPlusLogger.debug("Server has stopped. " + server.getName());
    }
}
