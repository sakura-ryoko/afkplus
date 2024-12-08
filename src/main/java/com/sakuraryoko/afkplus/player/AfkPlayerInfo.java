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

import eu.pb4.placeholders.api.PlaceholderContext;
import eu.pb4.placeholders.api.Placeholders;
import org.apache.commons.lang3.time.DurationFormatUtils;

import net.minecraft.Util;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.Component;

import com.sakuraryoko.afkplus.text.TextUtils;
import com.sakuraryoko.afkplus.util.AfkLogger;

import static com.sakuraryoko.afkplus.config.ConfigManager.CONFIG;

public class AfkPlayerInfo
{
    public static String getString(IAfkPlayer afkPlayer)
    {
        String AfkStatus;
        long duration;
        if (afkPlayer.afkplus$isAfk())
        {
            duration = Util.getMillis() - afkPlayer.afkplus$getAfkTimeMs();
            AfkStatus = "<bold><magenta>AFK Information:"
                    + "<r>\nPlayer: " + afkPlayer.afkplus$getName()
                    + "<r>\nAfk Since: " + CONFIG.PlaceholderOptions.afkTimePlaceholderFormatting
                    + afkPlayer.afkplus$getAfkTimeString() + "<r> (Format:yyyy-MM-dd_HH.mm.ss)";
            if (CONFIG.messageOptions.prettyDuration && CONFIG.messageOptions.displayDuration)
            {
                AfkStatus = AfkStatus + "<r>\nDuration: " + CONFIG.PlaceholderOptions.afkDurationPlaceholderFormatting;
                AfkStatus = AfkStatus + DurationFormatUtils.formatDurationWords(duration, true, true);
            }
            else if (CONFIG.messageOptions.displayDuration)
            {
                AfkStatus = AfkStatus + "<r>\nDuration: " + CONFIG.PlaceholderOptions.afkDurationPlaceholderFormatting;
                AfkStatus = AfkStatus + DurationFormatUtils.formatDurationHMS(duration) + "<r>ms (Format:HH:mm:ss)";
            }
            else
            {
                AfkStatus = AfkStatus + "<r>\nDuration: <copper>DISABLED";
                AfkStatus = AfkStatus + "<r>";
            }
            if (afkPlayer.afkplus$isCreative())
            {
                AfkStatus = AfkStatus + "<r>\nDamage Status: <light_blue>CREATIVE";
            }
            else if (afkPlayer.afkplus$isSpectator())
            {
                AfkStatus = AfkStatus + "<r>\nDamage Status: <gray>SPECTATOR";
            }
            else if (afkPlayer.afkplus$isDamageEnabled())
            {
                AfkStatus = AfkStatus + "<r>\nDamage Status: <green>Enabled";
            }
            else
            {
                AfkStatus = AfkStatus + "<r>\nDamage Status: <red>Disabled";
            }
            if (afkPlayer.afkplus$isLockDamageDisabled())
            {
                AfkStatus = AfkStatus + " <red>[RESTRICTED]";
            }
            else
            {
                AfkStatus = AfkStatus + " <green>[ALLOWED]";
            }
            AfkLogger.debug("AkfStatus.getString(): " + AfkStatus);
        }
        else if (afkPlayer.afkplus$isNoAfkEnabled())
        {
            AfkStatus = "Player: " + afkPlayer.afkplus$getName()
                    + "<r>\n<burnt_orange>Has toggled No Afk Mode. (No timeouts)";
        }
        else
        {
            AfkStatus = "";
        }
        return AfkStatus;
    }

    public static Component getReason(IAfkPlayer afkPlayer, CommandSourceStack src)
    {
        String reasonFormat;
        Component afkReason;
        if (afkPlayer.afkplus$isAfk())
        {
            reasonFormat = "<r>Reason: " + CONFIG.PlaceholderOptions.afkReasonPlaceholderFormatting;
            if (afkPlayer.afkplus$getAfkReason().isEmpty())
            {
                afkReason = TextUtils.formatTextSafe(reasonFormat + "none");
            }
            else
            {
                afkReason = Placeholders.parseText(
                        TextUtils.formatTextSafe(reasonFormat + afkPlayer.afkplus$getAfkReason()),
                        PlaceholderContext.of(src));
            }
            AfkLogger.debug("AkfStatus.getReason(): " + afkReason.toString());
        }
        else
        {
            afkReason = Component.empty();
        }
        return afkReason;
    }
}
