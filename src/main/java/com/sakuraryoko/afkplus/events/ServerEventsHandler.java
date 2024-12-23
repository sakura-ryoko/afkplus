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

import java.util.ArrayList;
import java.util.Collection;

import org.jetbrains.annotations.ApiStatus;

import net.minecraft.client.server.IntegratedServer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.GameType;

import com.sakuraryoko.afkplus.AfkPlusMod;
import com.sakuraryoko.afkplus.config.AfkConfigManager;
import com.sakuraryoko.afkplus.util.AfkPlusConflicts;

@ApiStatus.Internal
public class ServerEventsHandler
{
    private static final ServerEventsHandler INSTANCE = new ServerEventsHandler();
    public static ServerEventsHandler getInstance() { return INSTANCE; }
    private Collection<String> dpCollection;

    public ServerEventsHandler()
    {
        this.dpCollection = new ArrayList<>();
    }

    @ApiStatus.Internal
    public void onStarting(MinecraftServer server)
    {
        AfkPlusMod.debugLog("onStarting(): Server is starting, {}", server.getServerModName());
    }

    @ApiStatus.Internal
    public void onStarted(MinecraftServer server)
    {
        this.dpCollection = server.getPackRepository().getSelectedIds();

        if (!AfkPlusConflicts.checkDatapacks(this.dpCollection))
        {
            AfkPlusMod.LOGGER.warn("onStarted(): MOD Data Pack test has FAILED.");
        }

        AfkPlusMod.debugLog("onStarted(): Server has started, {}", server.getServerModName());
    }

    @ApiStatus.Internal
    public void onReloadComplete(MinecraftServer server, Collection<String> resources)
    {
        this.dpCollection = server.getPackRepository().getSelectedIds();

        if (!AfkPlusConflicts.checkDatapacks(this.dpCollection))
        {
            AfkPlusMod.LOGGER.warn("onReloadComplete(): MOD Data Pack test has FAILED.");
        }

        AfkPlusMod.debugLog("onReloadComplete(): Server has reloaded it's data packs, {}", server.getServerModName());
    }

    @ApiStatus.Internal
    public void onIntegratedStarted(IntegratedServer server)
    {
        AfkPlusMod.debugLog("onIntegratedStarted(): Integrated Server is starting, {}", server.getServerModName());
    }

    @ApiStatus.Internal
    public void onOpenToLan(IntegratedServer server, GameType mode)
    {
        AfkPlusMod.debugLog("onOpenToLan(): Server is open for LAN, {} [Game Mode: {}]", server.getServerModName(), mode.getName());
    }

    @ApiStatus.Internal
    public void onStopping(MinecraftServer server)
    {
        AfkPlusMod.debugLog("onStopping(): Server is stopping, {}", server.getServerModName());
        AfkConfigManager.getInstance().saveAllConfigs();
    }

    @ApiStatus.Internal
    public void onStopped(MinecraftServer server)
    {
        AfkPlusMod.debugLog("onStopped(): Server has stopped, {}", server.getServerModName());
    }
}
