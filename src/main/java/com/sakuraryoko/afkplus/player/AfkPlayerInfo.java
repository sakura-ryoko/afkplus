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
        String AfkStatus;

        if (afkPlayer.isAfk())
        {
            AfkStatus = "<bold><magenta>AFK Information:"
                    + "<r>\nPlayer: " + afkPlayer.getName()
                    + "<r>\nAfk Since: " + ConfigWrap.place().afkTimePlaceholderFormatting
                    + afkPlayer.getAfkTimeString() + "<r> (Format: "+afkPlayer.getAfkTimeFormat()+")";

            if (ConfigWrap.mess().displayDuration)
            {
                AfkStatus = AfkStatus + "<r>\nDuration: " + ConfigWrap.place().afkDurationPlaceholderFormatting;
                AfkStatus = AfkStatus + afkPlayer.getAfkDurationString() + "<r> (Format: "+afkPlayer.getAfkDurationFormat()+")";
            }
            else
            {
                AfkStatus = AfkStatus + "<r>\nDuration: <copper>DISABLED";
                AfkStatus = AfkStatus + "<r>";
            }

            if (afkPlayer.getPlayer().isCreative())
            {
                AfkStatus = AfkStatus + "<r>\nDamage Status: <light_blue>CREATIVE";
            }
            else if (afkPlayer.getPlayer().isSpectator())
            {
                AfkStatus = AfkStatus + "<r>\nDamage Status: <gray>SPECTATOR";
            }
            else if (afkPlayer.isDamageEnabled())
            {
                AfkStatus = AfkStatus + "<r>\nDamage Status: <green>Enabled";
            }
            else
            {
                AfkStatus = AfkStatus + "<r>\nDamage Status: <red>Disabled";
            }

            if (afkPlayer.isLockDamageEnabled())
            {
                AfkStatus = AfkStatus + " <red>[RESTRICTED]";
            }
            else
            {
                AfkStatus = AfkStatus + " <green>[ALLOWED]";
            }

            AfkPlus.debugLog("AkfStatus.getString(): {}", AfkStatus);
        }
        else if (afkPlayer.isNoAfkEnabled())
        {
            AfkStatus = "Player: " + afkPlayer.getName() + "<r>\n<burnt_orange>Has toggled No Afk Mode. (No timeouts)";
        }
        else
        {
            AfkStatus = "";
        }

        return AfkStatus;
    }

    public static Component getReason(@Nonnull AfkPlayer afkPlayer, CommandSourceStack src)
    {
        String reasonFormat;
        Component afkReason;

        if (afkPlayer.isAfk())
        {
            reasonFormat = "<r>Reason: " + ConfigWrap.place().afkReasonPlaceholderFormatting;

            if (afkPlayer.getAfkReason().isEmpty())
            {
                afkReason = TextHandler.getInstance().formatTextSafe(reasonFormat + "none");
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
