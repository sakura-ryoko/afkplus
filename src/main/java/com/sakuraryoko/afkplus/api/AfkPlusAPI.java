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

package com.sakuraryoko.afkplus.api;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.minecraft.server.level.ServerPlayer;

import com.sakuraryoko.afkplus.player.AfkPlayer;
import com.sakuraryoko.afkplus.player.AfkPlayerList;

public interface AfkPlusAPI
{
    static boolean isPlayerAfk(@Nonnull ServerPlayer player)
    {
        AfkPlayer afkplayer = AfkPlayerList.getInstance().getPlayer(player);
        return afkplayer != null && afkplayer.isAfk();
    }

    static boolean isPlayerNoAfk(@Nonnull ServerPlayer player)
    {
        AfkPlayer afkplayer = AfkPlayerList.getInstance().getPlayer(player);
        return afkplayer != null && afkplayer.isNoAfkEnabled();
    }

    static boolean isDamageEnabled(@Nonnull ServerPlayer player)
    {
        AfkPlayer afkplayer = AfkPlayerList.getInstance().getPlayer(player);
        return afkplayer != null && afkplayer.isDamageEnabled();
    }

    static boolean isDamageLocked(@Nonnull ServerPlayer player)
    {
        AfkPlayer afkplayer = AfkPlayerList.getInstance().getPlayer(player);
        return afkplayer != null && afkplayer.isLockDamageEnabled();
    }

    static long getAfkTimeMs(@Nonnull ServerPlayer player)
    {
        AfkPlayer afkplayer = AfkPlayerList.getInstance().getPlayer(player);
        return afkplayer != null ? afkplayer.getAfkTimeMs() : 0L;
    }

    static String getAfkDurationString(@Nonnull ServerPlayer player)
    {
        AfkPlayer afkplayer = AfkPlayerList.getInstance().getPlayer(player);
        return afkplayer != null ? afkplayer.getAfkDurationString() : "";
    }

    static String getAfkTimeString(@Nonnull ServerPlayer player)
    {
        AfkPlayer afkplayer = AfkPlayerList.getInstance().getPlayer(player);
        return afkplayer != null ? afkplayer.getAfkTimeString() : "";
    }

    static String getAfkReason(@Nonnull ServerPlayer player)
    {
        AfkPlayer afkplayer = AfkPlayerList.getInstance().getPlayer(player);
        return afkplayer != null ? afkplayer.getAfkReason() : "";
    }

    static boolean toggleAfkStatus(@Nonnull ServerPlayer player, @Nullable String reason)
    {
        AfkPlayer afkplayer = AfkPlayerList.getInstance().addOrGetPlayer(player);

        if (afkplayer.isAfk())
        {
            afkplayer.getHandler().unregisterAfk();
            return false;
        }
        else
        {
            afkplayer.getHandler().registerAfk(reason);
            return true;
        }
    }
}
