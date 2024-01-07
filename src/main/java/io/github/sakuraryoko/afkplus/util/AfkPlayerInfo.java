package io.github.sakuraryoko.afkplus.util;

import static io.github.sakuraryoko.afkplus.config.ConfigManager.*;

import io.github.sakuraryoko.afkplus.data.IAfkPlayer;
import org.apache.commons.lang3.time.DurationFormatUtils;

import eu.pb4.placeholders.api.PlaceholderContext;
import eu.pb4.placeholders.api.Placeholders;
import eu.pb4.placeholders.api.TextParserUtils;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;
import net.minecraft.util.Util;

public class AfkPlayerInfo {
    public static String getString(IAfkPlayer afkPlayer) {
        String AfkStatus;
        long duration;
        if (afkPlayer.afkplus$isAfk()) {
            duration = Util.getMeasuringTimeMs() - afkPlayer.afkplus$getAfkTimeMs();
            AfkStatus = "<bold><magenta>AFK Information:"
                    + "<r>\nPlayer: " + afkPlayer.afkplus$getName()
                    + "<r>\nAfk Since: " + CONFIG.PlaceholderOptions.afkTimePlaceholderFormatting
                    + afkPlayer.afkplus$getAfkTimeString() + "<r> (Format:yyyy-MM-dd_HH.mm.ss)"
                    + "<r>\nDuration: " + CONFIG.PlaceholderOptions.afkDurationPlaceholderFormatting;
            if (CONFIG.messageOptions.prettyDuration)
                AfkStatus = AfkStatus + DurationFormatUtils.formatDurationWords(duration, true, true);
            else
                AfkStatus = AfkStatus + DurationFormatUtils.formatDurationHMS(duration) + "<r>ms (Format:HH:mm:ss)";
            if (afkPlayer.afkplus$isCreative())
                AfkStatus = AfkStatus + "<r>\nDamage Status: <light_blue>CREATIVE";
            else if (afkPlayer.afkplus$isSpectator())
                AfkStatus = AfkStatus + "<r>\nDamage Status: <gray>SPECTATOR";
            else if (afkPlayer.afkplus$isDamageEnabled())
                AfkStatus = AfkStatus + "<r>\nDamage Status: <green>Enabled";
            else
                AfkStatus = AfkStatus + "<r>\nDamage Status: <red>Disabled";
            if (afkPlayer.afkplus$isLockDamageDisabled())
                AfkStatus = AfkStatus + " <red>[RESTRICTED]";
            else
                AfkStatus = AfkStatus + " <green>[ALLOWED]";
            AfkPlusLogger.debug("AkfStatus.getString(): " + AfkStatus);
        } else
            AfkStatus = "";
        return AfkStatus;
    }

    public static Text getReason(IAfkPlayer afkPlayer, ServerCommandSource src) {
        String reasonFormat;
        Text afkReason;
        if (afkPlayer.afkplus$isAfk()) {
            reasonFormat = "<r>Reason: " + CONFIG.PlaceholderOptions.afkReasonPlaceholderFormatting;
            if (afkPlayer.afkplus$getAfkReason().isEmpty()) {
                afkReason = TextParserUtils.formatTextSafe(reasonFormat + "none");
            } else {
                afkReason = Placeholders.parseText(
                        TextParserUtils.formatTextSafe(reasonFormat + afkPlayer.afkplus$getAfkReason()),
                        PlaceholderContext.of(src));
            }
            AfkPlusLogger.debug("AkfStatus.getReason(): " + afkReason.toString());
        } else {
            afkReason = Text.of("");
        }
        return afkReason;
    }
}
