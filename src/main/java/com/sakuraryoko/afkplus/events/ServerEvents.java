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

package com.sakuraryoko.afkplus.events;

import java.util.Collection;

import net.minecraft.server.MinecraftServer;

import com.sakuraryoko.afkplus.AfkPlusMod;
import com.sakuraryoko.afkplus.config.AfkConfigManager;
import com.sakuraryoko.afkplus.util.AfkPlusConflicts;

public class ServerEvents
{
    static private Collection<String> dpCollection;

    public static void starting(MinecraftServer server)
    {
        AfkPlusMod.debugLog("Server is starting, {}", server.getServerModName());
    }

    public static void started(MinecraftServer server)
    {
        dpCollection = server.getPackRepository().getSelectedIds();

        if (!AfkPlusConflicts.checkDatapacks(dpCollection))
        {
            AfkPlusMod.LOGGER.warn("MOD Data Pack test has FAILED.");
        }

        AfkPlusMod.debugLog("Server has started, {}", server.getServerModName());
    }

    public static void dpReload(MinecraftServer server)
    {
        dpCollection = server.getPackRepository().getSelectedIds();

        if (!AfkPlusConflicts.checkDatapacks(dpCollection))
        {
            AfkPlusMod.LOGGER.warn("MOD Data Pack test has FAILED.");
        }

        AfkPlusMod.debugLog("Server has reloaded it's data packs, {}", server.getServerModName());
    }

    public static void stopping(MinecraftServer server)
    {
        AfkPlusMod.debugLog("Server is stopping, {}", server.getServerModName());
        AfkConfigManager.getInstance().saveAllConfigs();
    }

    public static void stopped(MinecraftServer server)
    {
        AfkPlusMod.debugLog("Server has stopped, {}", server.getServerModName());
    }
}
