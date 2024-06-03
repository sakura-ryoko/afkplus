package com.github.sakuraryoko.afkplus.util;

import com.github.sakuraryoko.afkplus.data.ModData;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class AfkPlusLogger {
    private static Logger LOGGER;
    private static boolean log;

    public static void initLogger() {
        LOGGER = LogManager.getLogger(ModData.AFK_MOD_ID);
        log = true;
        LOGGER.debug("[{}] Logger initalized.", ModData.AFK_MOD_ID);
    }

    public static void debug(String msg) {
        if (log) {
            if (ModData.AFK_DEBUG)
                LOGGER.info("[{}:DEBUG] " + msg, ModData.AFK_MOD_ID);
            else
                LOGGER.debug("[{}] " + msg, ModData.AFK_MOD_ID);
        }
    }

    public static void info(String msg) {
        if (log)
            LOGGER.info("[{}] " + msg, ModData.AFK_MOD_ID);
    }

    public static void warn(String msg) {
        if (log)
            LOGGER.warn("[{}] " + msg, ModData.AFK_MOD_ID);
    }

    public static void error(String msg) {
        if (log)
            LOGGER.error("[{}] " + msg, ModData.AFK_MOD_ID);
    }

    public static void fatal(String msg) {
        if (log)
            LOGGER.fatal("[{}] " + msg, ModData.AFK_MOD_ID);
    }
}
