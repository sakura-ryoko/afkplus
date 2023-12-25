package io.github.sakuraryoko.afkplus.placeholders;

import static io.github.sakuraryoko.afkplus.config.ConfigManager.*;
import static io.github.sakuraryoko.afkplus.data.ModData.*;

import eu.pb4.placeholders.api.PlaceholderResult;
import eu.pb4.placeholders.api.Placeholders;
import eu.pb4.placeholders.api.TextParserUtils;
import io.github.sakuraryoko.afkplus.data.AfkPlayerData;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class AfkPlusAfk {
    public static void register() {
        Placeholders.register(new Identifier(AFK_MOD_ID, "afk"), (ctx, arg) -> {
            if (!ctx.hasPlayer()) {
                return PlaceholderResult.invalid("No player!");
            }
            AfkPlayerData player = (AfkPlayerData) ctx.player();
            assert player != null;
            Text result = player.isAfk()
                    ? Placeholders.parseText(TextParserUtils.formatTextSafe(CONFIG.PlaceholderOptions.afkPlaceholder),
                    ctx)
                    : Text.of("");
            return PlaceholderResult.value(result);
        });
    }

    ;

}
