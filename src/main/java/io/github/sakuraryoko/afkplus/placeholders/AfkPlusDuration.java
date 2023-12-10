package io.github.sakuraryoko.afkplus.placeholders;

import static io.github.sakuraryoko.afkplus.config.ConfigManager.*;
import static io.github.sakuraryoko.afkplus.data.ModData.*;

import org.apache.commons.lang3.time.DurationFormatUtils;

import eu.pb4.placeholders.PlaceholderAPI;
import eu.pb4.placeholders.PlaceholderResult;
import eu.pb4.placeholders.TextParser;
import io.github.sakuraryoko.afkplus.data.AfkPlayerData;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;

public class AfkPlusDuration {
    public static void register() {
        PlaceholderAPI.register(new Identifier(AFK_MOD_ID, "duration"), (ctx) -> {
            if (!ctx.hasPlayer()) {
                return PlaceholderResult.invalid("No player!");
            }
            AfkPlayerData player = (AfkPlayerData) ctx.getPlayer();
            assert player != null;
            if (CONFIG.PlaceholderOptions.afkDurationPretty) {
                Text result = player.isAfk()
                        ? TextParser.parse(
                                CONFIG.PlaceholderOptions.afkDurationPlaceholderFormatting
                                        + DurationFormatUtils.formatDurationWords(Util.getMeasuringTimeMs() -
                                                player.getAfkTimeMs(), true, true)
                                        + "<r>")
                        : TextParser.parse("");
                return PlaceholderResult.value(result);
            } else {
                Text result = player.isAfk()
                        ? TextParser.parse(CONFIG.PlaceholderOptions.afkDurationPlaceholderFormatting
                                + DurationFormatUtils.formatDurationHMS(Util.getMeasuringTimeMs() -
                                        player.getAfkTimeMs())
                                + "<r>")
                        : TextParser.parse("");
                return PlaceholderResult.value(result);
            }
        });
    };

}
