package io.github.sakuraryoko.afkplus.placeholders;

import static io.github.sakuraryoko.afkplus.config.ConfigManager.*;
import static io.github.sakuraryoko.afkplus.data.ModData.*;

import org.apache.commons.lang3.time.DurationFormatUtils;

import eu.pb4.placeholders.api.PlaceholderResult;
import eu.pb4.placeholders.api.Placeholders;
import eu.pb4.placeholders.api.TextParserUtils;
import io.github.sakuraryoko.afkplus.data.AfkPlayerData;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;

public class AfkPlusDuration {
    public static void register() {
        Placeholders.register(new Identifier(AFK_MOD_ID, "duration"), (ctx, arg) -> {
            if (!ctx.hasPlayer()) {
                return PlaceholderResult.invalid("No player!");
            } else {
                AfkPlayerData player = (AfkPlayerData) ctx.player();
                assert player != null;
                if (CONFIG.PlaceholderOptions.afkDurationPretty) {
                    Text result = player.isAfk()
                            ? TextParserUtils.formatTextSafe(
                                    CONFIG.PlaceholderOptions.afkDurationPlaceholderFormatting
                                            + DurationFormatUtils.formatDurationWords(Util.getMeasuringTimeMs() -
                                                    player.getAfkTimeMs(), true, true)
                                            + "<r>")
                            : TextParserUtils.formatTextSafe("");
                    return PlaceholderResult.value(result);
                } else {
                    Text result = player.isAfk()
                            ? TextParserUtils.formatTextSafe(CONFIG.PlaceholderOptions.afkDurationPlaceholderFormatting
                                    + DurationFormatUtils.formatDurationHMS(Util.getMeasuringTimeMs() -
                                            player.getAfkTimeMs())
                                    + "<r>")
                            : TextParserUtils.formatTextSafe("");
                    return PlaceholderResult.value(result);
                }
            }
        });
    };

}
