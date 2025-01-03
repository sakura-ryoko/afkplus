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

import javax.annotation.Nonnull;
import eu.pb4.placeholders.api.PlaceholderContext;
import eu.pb4.placeholders.api.Placeholders;

import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.Component;

import com.sakuraryoko.afkplus.AfkPlus;
import com.sakuraryoko.afkplus.compat.morecolors.TextHandler;
import com.sakuraryoko.afkplus.config.ConfigWrap;

public class AfkPlayerInfo
{
    public static String getString(@Nonnull AfkPlayer afkPlayer)
    {
        StringBuilder afkStatus = new StringBuilder();

        if (afkPlayer.isAfk())
        {
            afkStatus.append("<bold><magenta>AFK Information:");
            afkStatus.append("<r>\nPlayer: ").append(afkPlayer.getName());
            afkStatus.append("<r>\nAfk Since: ").append(ConfigWrap.place().afkTimePlaceholderFormatting);
            afkStatus.append(afkPlayer.getAfkTimeString()).append("<r> (Format: ").append(afkPlayer.getAfkTimeFormat()).append(")");

            if (ConfigWrap.mess().displayDuration)
            {
                afkStatus.append("<r>\nDuration: ").append(ConfigWrap.place().afkDurationPlaceholderFormatting);
                afkStatus.append(afkPlayer.getAfkDurationString()).append("<r> (Format: ").append(afkPlayer.getAfkDurationFormat()).append(")");
            }
            else
            {
                afkStatus.append("<r>\nDuration: <copper>DISABLED").append("<r>");
            }

            if (afkPlayer.getPlayer().isCreative())
            {
                afkStatus.append("<r>\nDamage Status: <light_blue>CREATIVE");
            }
            else if (afkPlayer.getPlayer().isSpectator())
            {
                afkStatus.append("<r>\nDamage Status: <gray>SPECTATOR");
            }
            else if (afkPlayer.isDamageEnabled())
            {
                afkStatus.append("<r>\nDamage Status: <green>Enabled");
            }
            else
            {
                afkStatus.append("<r>\nDamage Status: <red>Disabled");
            }

            if (afkPlayer.isLockDamageEnabled())
            {
                afkStatus.append(" <red>[RESTRICTED]");
            }
            else
            {
                afkStatus.append(" <green>[ALLOWED]");
            }

            AfkPlus.debugLog("AkfStatus.getString(): {}", afkStatus.toString());
        }
        else if (afkPlayer.isNoAfkEnabled())
        {
            afkStatus.append("Player: ").append(afkPlayer.getName()).append("<r>\n<burnt_orange>Has toggled No Afk Mode. (No timeouts)");
        }

        return afkStatus.toString();
    }

    public static Component getReason(@Nonnull AfkPlayer afkPlayer, CommandSourceStack src)
    {
        StringBuilder reasonFormat = new StringBuilder();
        Component afkReason = Component.empty();

        if (afkPlayer.isAfk())
        {
            reasonFormat.append("<r>Reason: ").append(ConfigWrap.place().afkReasonPlaceholderFormatting);

            if (afkPlayer.getAfkReason().isEmpty())
            {
                reasonFormat.append(TextHandler.getInstance().formatTextSafe(reasonFormat + "none"));
            }
            else
            {
                afkReason = Placeholders.parseText(
                            TextHandler.getInstance().formatTextSafe(reasonFormat + afkPlayer.getAfkReason()),
                            PlaceholderContext.of(src));
            }

            AfkPlus.debugLog("AkfStatus.getReason(): {}", afkReason.toString());
        }
        else
        {
            afkReason = Component.empty();
        }

        return afkReason;
    }
}
