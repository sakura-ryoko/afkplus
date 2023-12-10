package io.github.sakuraryoko.afkplus.util;

import static io.github.sakuraryoko.afkplus.config.ConfigManager.*;

import org.apache.commons.lang3.time.DurationFormatUtils;

import eu.pb4.placeholders.PlaceholderAPI;
import eu.pb4.placeholders.TextParser;
import io.github.sakuraryoko.afkplus.data.AfkPlayerData;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;
import net.minecraft.util.Util;

public class AfkPlayerInfo {
    public static String getString(AfkPlayerData afkPlayer, Text target,
            ServerCommandSource src) {
        String AfkStatus;
        long duration;
        if (afkPlayer.isAfk()) {
            duration = Util.getMeasuringTimeMs() - afkPlayer.getAfkTimeMs();
            if (CONFIG.messageOptions.prettyDuration) {
                AfkStatus = "<bold><light_purple>AFK Information:"
                        + "<r>\nPlayer: " + target.getString()
                        + "<r>\nAfk Since: " + CONFIG.PlaceholderOptions.afkTimePlaceholderFormatting
                        + afkPlayer.getAfkTimeString() + "<r> (Format:yyyy-MM-dd_HH.mm.ss)"
                        + "<r>\nDuration: " + CONFIG.PlaceholderOptions.afkDurationPlaceholderFormatting
                        + DurationFormatUtils.formatDurationWords(duration, true, true);
            } else {
                AfkStatus = "<bold><light_purple>AFK Information:"
                        + "<r>\nPlayer: " + target.getString()
                        + "<r>\nAfk Since: " + CONFIG.PlaceholderOptions.afkTimePlaceholderFormatting
                        + afkPlayer.getAfkTimeString() + "<r> (Format:yyyy-MM-dd_HH.mm.ss)"
                        + "<r>\nDuration: " + CONFIG.PlaceholderOptions.afkDurationPlaceholderFormatting
                        + DurationFormatUtils.formatDurationHMS(duration)
                        + "<r>ms (Format:HH:mm:ss)";
            }
            AfkPlusLogger.debug("AkfStatus.getString(): " + AfkStatus);
        } else {
            AfkStatus = "";
        }
        return AfkStatus;
    }

    public static Text getReason(AfkPlayerData afkPlayer, Text target,
            ServerCommandSource src) {
        String reasonFormat;
        Text afkReason;
        if (afkPlayer.isAfk()) {
            reasonFormat = "<r>Reason: " + CONFIG.PlaceholderOptions.afkReasonPlaceholderFormatting;
            if (afkPlayer.getAfkReason() == "") {
                afkReason = TextParser.parse(reasonFormat + "none");
            } else {
                afkReason = PlaceholderAPI.parseText(
                        TextParser.parse(reasonFormat + afkPlayer.getAfkReason()),
                        src.getServer());
            }
            AfkPlusLogger.debug("AkfStatus.getReason(): " + afkReason.toString());
        } else {
            afkReason = Text.of("");
        }
        return afkReason;
    }

}
