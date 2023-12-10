package io.github.sakuraryoko.afkplus.placeholders;

import static io.github.sakuraryoko.afkplus.config.ConfigManager.*;
import static io.github.sakuraryoko.afkplus.data.ModData.*;

import eu.pb4.placeholders.PlaceholderAPI;
import eu.pb4.placeholders.PlaceholderResult;
import eu.pb4.placeholders.TextParser;
import io.github.sakuraryoko.afkplus.data.AfkPlayerData;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class AfkPlusReason {
    public static void register() {
        PlaceholderAPI.register(new Identifier(AFK_MOD_ID, "reason"), (ctx) -> {
            if (!ctx.hasPlayer()) {
                return PlaceholderResult.invalid("No player!");
            }
            AfkPlayerData player = (AfkPlayerData) ctx.getPlayer();
            assert player != null;
            Text result = player.isAfk()
                    ? TextParser.parse(
                            CONFIG.PlaceholderOptions.afkReasonPlaceholderFormatting + player.getAfkReason()
                                    + "<r>")
                    : TextParser.parse("");
            return PlaceholderResult.value(result);
        });
    };
}
