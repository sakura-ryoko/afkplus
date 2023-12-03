package io.github.sakuraryoko.afkplus.util;

import static io.github.sakuraryoko.afkplus.config.ConfigManager.*;

import org.apache.commons.lang3.time.DurationFormatUtils;

import eu.pb4.placeholders.api.PlaceholderContext;
import eu.pb4.placeholders.api.Placeholders;
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
                if (afkPlayer.getAfkReason() == "") {
                    AfkStatus = "<bold><light_purple>AFK Information:"
                            + "<r>\nPlayer: " + target.getLiteralString()
                            + "<r>\nAfk Since: " + CONFIG.PlaceholderOptions.afkTimePlaceholderFormatting
                            + afkPlayer.getAfkTimeString() + "<r> (Format:yyyy-MM-dd_HH.mm.ss)"
                            + "<r>\nDuration: " + CONFIG.PlaceholderOptions.afkDurationPlaceholderFormatting
                            + DurationFormatUtils.formatDurationWords(duration, true, true)
                            + "<r>\nReason: none";
                } else {
                    AfkStatus = "<bold><light_purple>AFK Information:"
                            + "<r>\nPlayer: " + target.getLiteralString()
                            + "<r>\nAfk Since: " + CONFIG.PlaceholderOptions.afkTimePlaceholderFormatting
                            + afkPlayer.getAfkTimeString() + "<r> (Format:yyyy-MM-dd_HH.mm.ss)"
                            + "<r>\nDuration: " + CONFIG.PlaceholderOptions.afkDurationPlaceholderFormatting
                            + DurationFormatUtils.formatDurationWords(duration, true, true)
                            + "<r>\nReason: " + CONFIG.PlaceholderOptions.afkReasonPlaceholderFormatting
                            + Placeholders.parseText(Text.of(afkPlayer.getAfkReason()), PlaceholderContext.of(src));
                }
            } else {
                if (afkPlayer.getAfkReason() == "") {
                    AfkStatus = "<bold><light_purple>AFK Information:"
                            + "<r>\nPlayer: " + target.getLiteralString()
                            + "<r>\nAfk Since: " + CONFIG.PlaceholderOptions.afkTimePlaceholderFormatting
                            + afkPlayer.getAfkTimeString() + "<r> (Format:yyyy-MM-dd_HH.mm.ss)"
                            + "<r>\nDuration: " + CONFIG.PlaceholderOptions.afkDurationPlaceholderFormatting
                            + DurationFormatUtils.formatDurationHMS(duration)
                            + "<r>ms (Format:HH:mm:ss)"
                            + "<r>\nReason: none";
                } else {
                    AfkStatus = "<bold><light_purple>AFK Information:"
                            + "<r>\nPlayer: " + target.getLiteralString()
                            + "<r>\nAfk Since: " + CONFIG.PlaceholderOptions.afkTimePlaceholderFormatting
                            + afkPlayer.getAfkTimeString() + "<r> (Format:yyyy-MM-dd_HH.mm.ss)"
                            + "<r>\nDuration: " + CONFIG.PlaceholderOptions.afkDurationPlaceholderFormatting
                            + DurationFormatUtils.formatDurationHMS(duration)
                            + "<r>ms (Format:HH:mm:ss)"
                            + "<r>\nReason: " + CONFIG.PlaceholderOptions.afkReasonPlaceholderFormatting
                            + Placeholders.parseText(Text.of(afkPlayer.getAfkReason()), PlaceholderContext.of(src));
                }
            }
            AfkPlusLogger.debug("AkfStatus.getString(): " + AfkStatus);
        } else {
            AfkStatus = "Player: " + target.getLiteralString() + "<r>\n ... is not marked as AFK.";
        }
        return AfkStatus;
    }
}
