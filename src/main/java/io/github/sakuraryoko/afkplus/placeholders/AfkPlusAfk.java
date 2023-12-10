package io.github.sakuraryoko.afkplus.placeholders;

import static io.github.sakuraryoko.afkplus.config.ConfigManager.*;
import static io.github.sakuraryoko.afkplus.data.ModData.*;

import eu.pb4.placeholders.PlaceholderAPI;
import eu.pb4.placeholders.PlaceholderResult;
import eu.pb4.placeholders.TextParser;
//import eu.pb4.placeholders.api.Placeholders;
import io.github.sakuraryoko.afkplus.data.AfkPlayerData;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class AfkPlusAfk {
    public static void register() {
        PlaceholderAPI.register(new Identifier(AFK_MOD_ID, "afk"), (ctx) -> {
            if (!ctx.hasPlayer()) {
                return PlaceholderResult.invalid("No player!");
            }
            AfkPlayerData player = (AfkPlayerData) ctx.getPlayer();
            assert player != null;
            Text result = player.isAfk()
                    ? PlaceholderAPI.parseText(TextParser.parse(CONFIG.PlaceholderOptions.afkPlaceholder),
                            ctx.getPlayer())
                    : Text.of("");
            return PlaceholderResult.value(result);
        });
    };

}
