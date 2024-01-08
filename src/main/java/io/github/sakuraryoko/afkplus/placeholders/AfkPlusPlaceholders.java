package io.github.sakuraryoko.afkplus.placeholders;

import eu.pb4.placeholders.PlaceholderAPI;
import eu.pb4.placeholders.PlaceholderResult;
import eu.pb4.placeholders.TextParser;
import io.github.sakuraryoko.afkplus.data.IAfkPlayer;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;
import org.apache.commons.lang3.time.DurationFormatUtils;

import static io.github.sakuraryoko.afkplus.config.ConfigManager.CONFIG;
import static io.github.sakuraryoko.afkplus.data.ModData.AFK_MOD_ID;

public class AfkPlusPlaceholders {
    protected static void registerAfk() {
        PlaceholderAPI.register(new Identifier(AFK_MOD_ID, "afk"), (ctx) -> {
            if (!ctx.hasPlayer()) {
                return PlaceholderResult.invalid("No player!");
            }
            IAfkPlayer player = (IAfkPlayer) ctx.getPlayer();
            assert player != null;
            Text result = player.afkplus$isAfk()
                    ? PlaceholderAPI.parseText(TextParser.parse(CONFIG.PlaceholderOptions.afkPlaceholder), ctx.getPlayer())
                    : Text.of("");
            return PlaceholderResult.value(result);
        });
    }
    protected static void registerDuration() {
        PlaceholderAPI.register(new Identifier(AFK_MOD_ID, "duration"), (ctx) -> {
            if (!ctx.hasPlayer()) {
                return PlaceholderResult.invalid("No player!");
            }
            IAfkPlayer player = (IAfkPlayer) ctx.getPlayer();
            assert player != null;
            Text result;
            if (CONFIG.PlaceholderOptions.afkDurationPretty) {
                result = player.afkplus$isAfk()
                        ? TextParser.parse(CONFIG.PlaceholderOptions.afkDurationPlaceholderFormatting + DurationFormatUtils.formatDurationWords(Util.getMeasuringTimeMs() - player.afkplus$getAfkTimeMs(), true, true) + "<r>")
                        : TextParser.parse("");
            } else {
                result = player.afkplus$isAfk()
                        ? TextParser.parse(CONFIG.PlaceholderOptions.afkDurationPlaceholderFormatting + DurationFormatUtils.formatDurationHMS(Util.getMeasuringTimeMs() - player.afkplus$getAfkTimeMs()) + "<r>")
                        : TextParser.parse("");
            }
            return PlaceholderResult.value(result);
        });
    }
    protected static void registerDisplayName() {
        PlaceholderAPI.register(new Identifier(AFK_MOD_ID, "name"), (ctx) -> {
            if (!ctx.hasPlayer()) {
                return PlaceholderResult.invalid("No player!");
            }
            IAfkPlayer player = (IAfkPlayer) ctx.getPlayer();
            assert player != null;
            Text result = player.afkplus$isAfk()
                    ? PlaceholderAPI.parseText(TextParser.parse(CONFIG.PlaceholderOptions.afkPlusNamePlaceholderAfk), ctx.getPlayer())
                    : PlaceholderAPI.parseText(TextParser.parse(CONFIG.PlaceholderOptions.afkPlusNamePlaceholder), ctx.getPlayer());
            return PlaceholderResult.value(result);
        });
        PlaceholderAPI.register(new Identifier(AFK_MOD_ID, "display_name"), (ctx) -> {
            if (!ctx.hasPlayer()) {
                return PlaceholderResult.invalid("No player!");
            }
            IAfkPlayer player = (IAfkPlayer) ctx.getPlayer();
            assert player != null;
            Text result = player.afkplus$isAfk()
                    ? PlaceholderAPI.parseText(TextParser.parse(CONFIG.PlaceholderOptions.afkPlusNamePlaceholderAfk), ctx.getPlayer())
                    : PlaceholderAPI.parseText(TextParser.parse(CONFIG.PlaceholderOptions.afkPlusNamePlaceholder), ctx.getPlayer());
            return PlaceholderResult.value(result);
        });
    }
    protected static void registerReason() {
        PlaceholderAPI.register(new Identifier(AFK_MOD_ID, "reason"), (ctx) -> {
            if (!ctx.hasPlayer()) {
                return PlaceholderResult.invalid("No player!");
            }
            IAfkPlayer player = (IAfkPlayer) ctx.getPlayer();
            assert player != null;
            Text result = player.afkplus$isAfk()
                    ? TextParser.parse(CONFIG.PlaceholderOptions.afkReasonPlaceholderFormatting + player.afkplus$getAfkReason() + "<r>")
                    : TextParser.parse("");
            return PlaceholderResult.value(result);
        });
    }
    protected static void registerTime() {
        PlaceholderAPI.register(new Identifier(AFK_MOD_ID, "time"), (ctx) -> {
            if (!ctx.hasPlayer()) {
                return PlaceholderResult.invalid("No player!");
            }
            IAfkPlayer player = (IAfkPlayer) ctx.getPlayer();
            assert player != null;
            Text result = player.afkplus$isAfk()
                    ? TextParser.parse(CONFIG.PlaceholderOptions.afkTimePlaceholderFormatting + player.afkplus$getAfkTimeString() + "<r>")
                    : TextParser.parse("");
            return PlaceholderResult.value(result);
        });
    }
    protected static void registerInvulnerable() {
        PlaceholderAPI.register(new Identifier(AFK_MOD_ID, "invulnerable"), (ctx) -> {
            if (!ctx.hasPlayer()) {
                return PlaceholderResult.invalid("No player!");
            }
            IAfkPlayer player = (IAfkPlayer) ctx.getPlayer();
            assert player != null;
            Text result = player.afkplus$isDamageEnabled()
                    ? Text.of("")
                    : PlaceholderAPI.parseText(TextParser.parse(CONFIG.PlaceholderOptions.afkInvulnerablePlaceholder + "<r>"), ctx.getPlayer());
            return PlaceholderResult.value(result);
        });
    }

    protected static void register() {
        registerAfk();
        registerDisplayName();
        registerDuration();
        registerReason();
        registerTime();
        registerInvulnerable();
    }
}
