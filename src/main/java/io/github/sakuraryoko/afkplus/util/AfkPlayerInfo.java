package io.github.sakuraryoko.afkplus.util;

import static io.github.sakuraryoko.afkplus.config.ConfigManager.*;

import org.apache.commons.lang3.time.DurationFormatUtils;

import io.github.sakuraryoko.afkplus.data.AfkPlayerData;
import net.minecraft.util.Util;

public class AfkPlayerInfo {
    public static String getString(AfkPlayerData afkPlayer, String user, String target) {
        String AfkStatus;
        long duration;
        if (afkPlayer.isAfk()) {
            duration = Util.getMeasuringTimeMs() - afkPlayer.getAfkTimeMs();
            if (CONFIG.messageOptions.prettyDuration) {
                if (afkPlayer.getAfkReason() == "") {
                    AfkStatus = "<bold><light_purple>AFK Information:"
                            + "<r>\nPlayer: " + target
                            + "<r>\nAfk Since: " + CONFIG.PlaceholderOptions.afkTimePlaceholderFormatting
                            + afkPlayer.getAfkTimeString() + "<r> (Format:yyyy-MM-dd_HH.mm.ss)"
                            + "<r>\nDuration: " + CONFIG.PlaceholderOptions.afkDurationPlaceholderFormatting
                            + DurationFormatUtils.formatDurationWords(duration, true, true)
                            + "<r>\nReason: none";
                } else {
                    AfkStatus = "<bold><light_purple>AFK Information:"
                            + "<r>\nPlayer: " + target
                            + "<r>\nAfk Since: " + CONFIG.PlaceholderOptions.afkTimePlaceholderFormatting
                            + afkPlayer.getAfkTimeString() + "<r> (Format:yyyy-MM-dd_HH.mm.ss)"
                            + "<r>\nDuration: " + CONFIG.PlaceholderOptions.afkDurationPlaceholderFormatting
                            + DurationFormatUtils.formatDurationWords(duration, true, true)
                            + "<r>\nReason: " + CONFIG.PlaceholderOptions.afkReasonPlaceholderFormatting
                            + afkPlayer.getAfkReason();
                }
            } else {
                if (afkPlayer.getAfkReason() == "") {
                    AfkStatus = "<bold><light_purple>AFK Information:"
                            + "<r>\nPlayer: " + target
                            + "<r>\nAfk Since: " + CONFIG.PlaceholderOptions.afkTimePlaceholderFormatting
                            + afkPlayer.getAfkTimeString() + "<r> (Format:yyyy-MM-dd_HH.mm.ss)"
                            + "<r>\nDuration: " + CONFIG.PlaceholderOptions.afkDurationPlaceholderFormatting
                            + DurationFormatUtils.formatDurationHMS(duration)
                            + "<r>ms (Format:HH:mm:ss)"
                            + "<r>\nReason: none";
                } else {
                    AfkStatus = "<bold><light_purple>AFK Information:"
                            + "<r>\nPlayer: " + target
                            + "<r>\nAfk Since: " + CONFIG.PlaceholderOptions.afkTimePlaceholderFormatting
                            + afkPlayer.getAfkTimeString() + "<r> (Format:yyyy-MM-dd_HH.mm.ss)"
                            + "<r>\nDuration: " + CONFIG.PlaceholderOptions.afkDurationPlaceholderFormatting
                            + DurationFormatUtils.formatDurationHMS(duration)
                            + "<r>ms (Format:HH:mm:ss)"
                            + "<r>\nReason: " + CONFIG.PlaceholderOptions.afkReasonPlaceholderFormatting
                            + afkPlayer.getAfkReason();
                }
            }
            AfkPlusLogger.debug("AkfStatus: " + AfkStatus);
            AfkPlusLogger.info(user + " displayed " + target + "'s AFK info.");
        } else {
            AfkStatus = "Player: " + target + "<r>\n ... is not marked as AFK.";
            AfkPlusLogger.info(user + " attempted to display " + target
                    + "'s AFK time/duration, but they wern't AFK.");
        }
        return AfkStatus;
    }
}
