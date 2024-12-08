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

package com.sakuraryoko.afkplus.util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.sakuraryoko.afkplus.AfkPlusReference;

public class AfkLogger
{
    private static Logger LOGGER;
    private static boolean log;

    public static void initLogger()
    {
        LOGGER = LogManager.getLogger(AfkPlusReference.AFK_MOD_ID);
        log = true;
        LOGGER.debug("[{}] Logger initialized.", AfkPlusReference.AFK_MOD_ID);
    }

    public static void debug(String msg)
    {
        if (log)
        {
            if (AfkPlusReference.AFK_DEBUG)
            {
                LOGGER.info("[{}:DEBUG] " + msg, AfkPlusReference.AFK_MOD_ID);
            }
            else
            {
                LOGGER.debug("[{}] " + msg, AfkPlusReference.AFK_MOD_ID);
            }
        }
    }

    public static void info(String msg)
    {
        if (log)
        {
            LOGGER.info("[{}] " + msg, AfkPlusReference.AFK_MOD_ID);
        }
    }

    public static void warn(String msg)
    {
        if (log)
        {
            LOGGER.warn("[{}] " + msg, AfkPlusReference.AFK_MOD_ID);
        }
    }

    public static void error(String msg)
    {
        if (log)
        {
            LOGGER.error("[{}] " + msg, AfkPlusReference.AFK_MOD_ID);
        }
    }

    public static void fatal(String msg)
    {
        if (log)
        {
            LOGGER.fatal("[{}] " + msg, AfkPlusReference.AFK_MOD_ID);
        }
    }
}
