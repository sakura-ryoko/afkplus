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
import net.minecraft.server.dedicated.DedicatedServer;
import net.minecraft.world.level.GameType;

import com.sakuraryoko.afkplus.AfkPlus;
import com.sakuraryoko.afkplus.util.AfkPlusConflicts;
import com.sakuraryoko.corelib.api.events.IServerEventsDispatch;

@ApiStatus.Internal
public class ServerEventsHandler implements IServerEventsDispatch
{
    private static final ServerEventsHandler INSTANCE = new ServerEventsHandler();
    public static ServerEventsHandler getInstance() { return INSTANCE; }
    private Collection<String> dpCollection;

    public ServerEventsHandler()
    {
        this.dpCollection = new ArrayList<>();
    }

    @Override
    @ApiStatus.Internal
    public void onStarting(MinecraftServer server)
    {
        AfkPlus.debugLog("onStarting(): Server is starting, {}", server.getServerModName());
    }

    @Override
    @ApiStatus.Internal
    public void onStarted(MinecraftServer server)
    {
        this.dpCollection = server.getPackRepository().getSelectedIds();

        if (!AfkPlusConflicts.checkDatapacks(this.dpCollection))
        {
            AfkPlus.LOGGER.warn("onStarted(): MOD Data Pack test has FAILED.");
        }

        AfkPlus.debugLog("onStarted(): Server has started, {}", server.getServerModName());
    }

    @Override
    @ApiStatus.Internal
    public void onReloadComplete(MinecraftServer server, Collection<String> resources)
    {
        this.dpCollection = server.getPackRepository().getSelectedIds();

        if (!AfkPlusConflicts.checkDatapacks(this.dpCollection))
        {
            AfkPlus.LOGGER.warn("onReloadComplete(): MOD Data Pack test has FAILED.");
        }

        AfkPlus.debugLog("onReloadComplete(): Server has reloaded it's data packs, {}", server.getServerModName());
    }

    @Override
    @ApiStatus.Internal
    public void onDedicatedStarted(DedicatedServer server)
    {
        AfkPlus.debugLog("onDedicatedStarted(): Dedicated Server is starting, {}", server.getServerModName());
    }

    @Override
    @ApiStatus.Internal
    public void onIntegratedStarted(IntegratedServer server)
    {
        AfkPlus.debugLog("onIntegratedStarted(): Integrated Server is starting, {}", server.getServerModName());
    }

    @Override
    @ApiStatus.Internal
    public void onOpenToLan(IntegratedServer server, GameType mode)
    {
        AfkPlus.debugLog("onOpenToLan(): Server is open for LAN, {} [Game Mode: {}]", server.getServerModName(), mode.getName());
    }

    @Override
    @ApiStatus.Internal
    public void onDedicatedStopping(DedicatedServer server)
    {
        AfkPlus.debugLog("onDedicatedStopping(): Server is stopping, {}", server.getServerModName());
    }

    @Override
    @ApiStatus.Internal
    public void onStopping(MinecraftServer server)
    {
        AfkPlus.debugLog("onStopping(): Server is stopping, {}", server.getServerModName());
    }

    @Override
    @ApiStatus.Internal
    public void onStopped(MinecraftServer server)
    {
        AfkPlus.debugLog("onStopped(): Server has stopped, {}", server.getServerModName());
    }
}
