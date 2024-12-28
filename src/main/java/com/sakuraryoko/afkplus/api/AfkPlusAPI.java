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

import eu.pb4.placeholders.api.PlaceholderContext;
import eu.pb4.placeholders.api.Placeholders;

import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;

import com.sakuraryoko.afkplus.compat.morecolors.TextHandler;
import com.sakuraryoko.afkplus.config.ConfigWrap;
import com.sakuraryoko.afkplus.player.AfkPlayer;
import com.sakuraryoko.afkplus.player.AfkPlayerList;

/**
 * AfkPlus API
 */
public interface AfkPlusAPI
{
    /**
     * Returns if a player is marked as AFK
     * @param player ()
     * @return (True|False)
     */
    static boolean isPlayerAfk(@Nonnull ServerPlayer player)
    {
        AfkPlayer afkPlayer = AfkPlayerList.getInstance().getPlayer(player);
        return afkPlayer != null && afkPlayer.isAfk();
    }

    /**
     * Returns if a player has their 'NoAFK' mode enabled via manually using the `/noafk` command
     * @param player ()
     * @return (True|False)
     */
    static boolean isPlayerNoAfk(@Nonnull ServerPlayer player)
    {
        AfkPlayer afkPlayer = AfkPlayerList.getInstance().getPlayer(player);
        return afkPlayer != null && afkPlayer.isNoAfkEnabled();
    }

    /**
     * Returns if an AFK player can be damaged (Damage Enabled)
     * @param player ()
     * @return (True|False)
     */
    static boolean isDamageEnabled(@Nonnull ServerPlayer player)
    {
        AfkPlayer afkPlayer = AfkPlayerList.getInstance().getPlayer(player);
        return afkPlayer != null && afkPlayer.isDamageEnabled();
    }

    /**
     * Return if an AFK player has been locked from using Disable Damage
     * @param player ()
     * @return (True|False)
     */
    static boolean isDamageLocked(@Nonnull ServerPlayer player)
    {
        AfkPlayer afkPlayer = AfkPlayerList.getInstance().getPlayer(player);
        return afkPlayer != null && afkPlayer.isLockDamageEnabled();
    }

    /**
     * Return the time duration in ms since the player went AFK
     * @param player ()
     * @return (ms or 0L)
     */
    static long getAfkTimeMs(@Nonnull ServerPlayer player)
    {
        AfkPlayer afkPlayer = AfkPlayerList.getInstance().getPlayer(player);
        return afkPlayer != null ? afkPlayer.getAfkTimeMs() : 0L;
    }

    /**
     * Return an AFK players' unformatted duration since they went AFK
     * @param player ()
     * @return (The Duration or "")
     */
    static String getAfkDurationString(@Nonnull ServerPlayer player)
    {
        AfkPlayer afkPlayer = AfkPlayerList.getInstance().getPlayer(player);
        return afkPlayer != null ? afkPlayer.getAfkDurationString() : "";
    }

    /**
     * Return an AFK players' unformatted time when they went AFK
     * @param player ()
     * @return (The Date/Time or "")
     */
    static String getAfkTimeString(@Nonnull ServerPlayer player)
    {
        AfkPlayer afkPlayer = AfkPlayerList.getInstance().getPlayer(player);
        return afkPlayer != null ? afkPlayer.getAfkTimeString() : "";
    }

    /**
     * Return an AFK players' Reason for being AFK.
     * @param player ()
     * @return (The Reason or "")
     */
    static String getAfkReason(@Nonnull ServerPlayer player)
    {
        AfkPlayer afkPlayer = AfkPlayerList.getInstance().getPlayer(player);
        return afkPlayer != null ? afkPlayer.getAfkReason() : "";
    }

    /**
     * Toggle a Player's AFK status, with an optional Reason
     * @param player ()
     * @param reason (Reason for AFK)
     * @return (if AFK)
     */
    static boolean toggleAfkStatus(@Nonnull ServerPlayer player, @Nullable String reason)
    {
        AfkPlayer afkPlayer = AfkPlayerList.getInstance().addOrGetPlayer(player);

        if (afkPlayer.isAfk())
        {
            afkPlayer.getHandler().unregisterAfk();
            return false;
        }
        else
        {
            afkPlayer.getHandler().registerAfk(reason);
            return true;
        }
    }

    /**
     * Returns the formatted %afkplus:display_name% value
     * @param player ()
     * @return (DisplayName)
     */
    static Component getAfkDisplayName(@Nonnull ServerPlayer player)
    {
        AfkPlayer afkPlayer = AfkPlayerList.getInstance().addOrGetPlayer(player);

        if (afkPlayer.isAfk())
        {
            return Placeholders.parseText(
                    TextHandler.getInstance().formatTextSafe(ConfigWrap.place().afkPlusNamePlaceholderAfk),
                    PlaceholderContext.of(player)
            );
        }
        else
        {
            return Placeholders.parseText(
                    TextHandler.getInstance().formatTextSafe(ConfigWrap.place().afkPlusNamePlaceholder),
                    PlaceholderContext.of(player)
            );
        }
    }
}
