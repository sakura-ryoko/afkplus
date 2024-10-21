package com.sakuraryoko.afkplus.util;

import eu.pb4.placeholders.api.PlaceholderContext;
import eu.pb4.placeholders.api.Placeholders;
import org.apache.commons.lang3.time.DurationFormatUtils;

import net.minecraft.Util;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.Component;

import com.sakuraryoko.afkplus.compat.TextUtils;
import com.sakuraryoko.afkplus.data.IAfkPlayer;

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
            AfkPlusLogger.debug("AkfStatus.getString(): " + AfkStatus);
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
            AfkPlusLogger.debug("AkfStatus.getReason(): " + afkReason.toString());
        }
        else
        {
            afkReason = Component.empty();
        }
        return afkReason;
    }
}
