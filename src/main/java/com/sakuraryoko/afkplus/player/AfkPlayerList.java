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

package com.sakuraryoko.afkplus.player;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.minecraft.Util;
import net.minecraft.server.level.ServerPlayer;

import com.sakuraryoko.afkplus.AfkPlusMod;

public class AfkPlayerList
{
    private static final AfkPlayerList INSTANCE = new AfkPlayerList();
    public static AfkPlayerList getInstance() { return INSTANCE; }

    private final List<AfkPlayer> afkPlayers;

    public AfkPlayerList()
    {
        this.afkPlayers = new ArrayList<>();
    }

    public @Nullable AfkPlayer getPlayer(@Nonnull ServerPlayer player)
    {
        for (AfkPlayer entry : this.afkPlayers)
        {
            if (entry.matches(player))
            {
                return entry;
            }
        }

        return null;
    }

    public AfkPlayer addOrGetPlayer(@Nonnull ServerPlayer player)
    {
        AfkPlayer afkPlayer = this.getPlayer(player);

        if (afkPlayer == null)
        {
            afkPlayer = AfkPlayer.init(player);
            this.afkPlayers.add(afkPlayer);
            AfkPlusMod.debugLog("AfkPlayerList(): addOrGetPlayer({}) --> ADD", afkPlayer.getName());
        }

        return afkPlayer;
    }

    public void removePlayer(@Nonnull ServerPlayer player)
    {
        for (AfkPlayer entry : this.afkPlayers)
        {
            if (entry.matches(player))
            {
                AfkPlusMod.debugLog("AfkPlayerList(): removePlayer({}) --> REMOVE", entry.getName());
                entry.reset();
                this.afkPlayers.remove(entry);
                break;
            }
        }
    }

    public void removeAllPlayers()
    {
        AfkPlusMod.debugLog("AfkPlayerList(): removeAllPlayers()");

        for (AfkPlayer entry : this.afkPlayers)
        {
            entry.reset();
        }

        this.clear();
    }

    public void tickPlayers()
    {
        this.afkPlayers.forEach((afkPlayer) -> afkPlayer.tickPlayer(Util.getMillis()));
    }

    public boolean tickEachByPlayer(@Nonnull ServerPlayer player)
    {
        AfkPlayer afkPlayer = this.getPlayer(player);

        if (afkPlayer != null)
        {
            afkPlayer.getHandler().tickPlayer(player);
            return true;
        }

        return false;
    }

    public void clear()
    {
        this.afkPlayers.clear();
    }
}
