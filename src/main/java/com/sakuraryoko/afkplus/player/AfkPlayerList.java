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

public class AfkPlayerList
{
    private static final AfkPlayerList INSTANCE = new AfkPlayerList();
    public static AfkPlayerList getInstance() { return INSTANCE; }

    private List<AfkPlayer> afkPlayers;

    public AfkPlayerList()
    {
        this.afkPlayers = new ArrayList<>();
    }

    public @Nullable AfkPlayer addPlayer(@Nonnull IAfkPlayer player)
    {
        for (AfkPlayer entry : this.afkPlayers)
        {
            if (entry.matches(player))
            {
                return null;
            }
        }

        AfkPlayer newEntry = new AfkPlayer(player);
        this.afkPlayers.add(newEntry);
        return newEntry;
    }

    public @Nullable AfkPlayer getPlayer(IAfkPlayer player)
    {
        for (AfkPlayer entry : this.afkPlayers)
        {
            if (entry.matches(player))
            {
                return entry;
            }
        }

        return this.addPlayer(player);
    }

    public void removePlayer(IAfkPlayer player)
    {
        for (AfkPlayer entry : this.afkPlayers)
        {
            if (entry.matches(player))
            {
                this.afkPlayers.remove(entry);
                break;
            }
        }
    }

    public void tickPlayers()
    {
        this.afkPlayers.forEach(this::tickEach);
    }

    public boolean tickEachByPlayer(@Nonnull IAfkPlayer player)
    {
        AfkPlayer afkPlayer = this.getPlayer(player);

        if (afkPlayer != null)
        {
            this.tickEach(afkPlayer);
            return true;
        }

        return false;
    }

    private void tickEach(AfkPlayer player)
    {
        // Do Something
    }

    public void clear()
    {
        this.afkPlayers.clear();
    }
}
