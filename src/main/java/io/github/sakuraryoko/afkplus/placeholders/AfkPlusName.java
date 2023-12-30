package io.github.sakuraryoko.afkplus.placeholders;

import static io.github.sakuraryoko.afkplus.config.ConfigManager.*;
import static io.github.sakuraryoko.afkplus.data.ModData.*;

import eu.pb4.placeholders.api.PlaceholderResult;
import eu.pb4.placeholders.api.Placeholders;
import eu.pb4.placeholders.api.TextParserUtils;
import io.github.sakuraryoko.afkplus.data.AfkPlayerData;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class AfkPlusName {
    public static void register() {
        Placeholders.register(new Identifier(AFK_MOD_ID, "name"), (ctx, arg) -> {
            if (!ctx.hasPlayer()) {
                return PlaceholderResult.invalid("No player!");
            }
            AfkPlayerData player = (AfkPlayerData) ctx.player();
            assert player != null;
            Text result = player.afkplus$isAfk()
                    ? Placeholders.parseText(
                    TextParserUtils.formatTextSafe(CONFIG.PlaceholderOptions.afkPlusNamePlaceholderAfk), ctx)
                    : Placeholders.parseText(
                    TextParserUtils.formatTextSafe(CONFIG.PlaceholderOptions.afkPlusNamePlaceholder),
                    ctx);
            return PlaceholderResult.value(result);
        });
        Placeholders.register(new Identifier(AFK_MOD_ID, "display_name"), (ctx, arg) -> {
            if (!ctx.hasPlayer()) {
                return PlaceholderResult.invalid("No player!");
            }
            AfkPlayerData player = (AfkPlayerData) ctx.player();
            assert player != null;
            Text result = player.afkplus$isAfk()
                    ? Placeholders.parseText(
                    TextParserUtils.formatTextSafe(CONFIG.PlaceholderOptions.afkPlusNamePlaceholderAfk), ctx)
                    : Placeholders.parseText(
                    TextParserUtils.formatTextSafe(CONFIG.PlaceholderOptions.afkPlusNamePlaceholder),
                    ctx);
            return PlaceholderResult.value(result);
        });
    }
}
