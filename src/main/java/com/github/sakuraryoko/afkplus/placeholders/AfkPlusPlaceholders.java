package com.github.sakuraryoko.afkplus.placeholders;

import com.github.sakuraryoko.afkplus.config.ConfigManager;
import com.github.sakuraryoko.afkplus.data.IAfkPlayer;
import com.github.sakuraryoko.afkplus.data.ModData;
import eu.pb4.placeholders.api.PlaceholderResult;
import eu.pb4.placeholders.api.Placeholders;
import eu.pb4.placeholders.api.TextParserUtils;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;
import org.apache.commons.lang3.time.DurationFormatUtils;

public class AfkPlusPlaceholders {
    protected static void registerAfk() {
        Placeholders.register(Identifier.of(ModData.AFK_MOD_ID, "afk"), (ctx, arg) -> {
            if (!ctx.hasPlayer()) {
                return PlaceholderResult.invalid("No player!");
            }
            IAfkPlayer player = (IAfkPlayer) ctx.player();
            assert player != null;
            Text result = player.afkplus$isAfk()
                    ? Placeholders.parseText(TextParserUtils.formatTextSafe(ConfigManager.CONFIG.PlaceholderOptions.afkPlaceholder), ctx)
                    : Text.of("");
            return PlaceholderResult.value(result);
        });
    }
    protected static void registerDuration() {
        Placeholders.register(Identifier.of(ModData.AFK_MOD_ID, "duration"), (ctx, arg) -> {
            if (!ctx.hasPlayer()) {
                return PlaceholderResult.invalid("No player!");
            }
            IAfkPlayer player = (IAfkPlayer) ctx.player();
            assert player != null;
            Text result;
            if (ConfigManager.CONFIG.PlaceholderOptions.afkDurationPretty) {
                result = player.afkplus$isAfk()
                        ? TextParserUtils.formatTextSafe(ConfigManager.CONFIG.PlaceholderOptions.afkDurationPlaceholderFormatting + DurationFormatUtils.formatDurationWords(Util.getMeasuringTimeMs() - player.afkplus$getAfkTimeMs(), true, true) + "<r>")
                        : TextParserUtils.formatTextSafe("");
            } else {
                result = player.afkplus$isAfk()
                        ? TextParserUtils.formatTextSafe(ConfigManager.CONFIG.PlaceholderOptions.afkDurationPlaceholderFormatting + DurationFormatUtils.formatDurationHMS(Util.getMeasuringTimeMs() - player.afkplus$getAfkTimeMs()) + "<r>")
                        : TextParserUtils.formatTextSafe("");
            }
            return PlaceholderResult.value(result);
        });
    }
    protected static void registerDisplayName() {
        Placeholders.register(Identifier.of(ModData.AFK_MOD_ID, "name"), (ctx, arg) -> {
            if (!ctx.hasPlayer()) {
                return PlaceholderResult.invalid("No player!");
            }
            IAfkPlayer player = (IAfkPlayer) ctx.player();
            assert player != null;
            Text result = player.afkplus$isAfk()
                    ? Placeholders.parseText(TextParserUtils.formatTextSafe(ConfigManager.CONFIG.PlaceholderOptions.afkPlusNamePlaceholderAfk), ctx)
                    : Placeholders.parseText(TextParserUtils.formatTextSafe(ConfigManager.CONFIG.PlaceholderOptions.afkPlusNamePlaceholder), ctx);
            return PlaceholderResult.value(result);
        });
        Placeholders.register(Identifier.of(ModData.AFK_MOD_ID, "display_name"), (ctx, arg) -> {
            if (!ctx.hasPlayer()) {
                return PlaceholderResult.invalid("No player!");
            }
            IAfkPlayer player = (IAfkPlayer) ctx.player();
            assert player != null;
            Text result = player.afkplus$isAfk()
                    ? Placeholders.parseText(TextParserUtils.formatTextSafe(ConfigManager.CONFIG.PlaceholderOptions.afkPlusNamePlaceholderAfk), ctx)
                    : Placeholders.parseText(TextParserUtils.formatTextSafe(ConfigManager.CONFIG.PlaceholderOptions.afkPlusNamePlaceholder), ctx);
            return PlaceholderResult.value(result);
        });
    }
    protected static void registerReason() {
        Placeholders.register(Identifier.of(ModData.AFK_MOD_ID, "reason"), (ctx, arg) -> {
            if (!ctx.hasPlayer()) {
                return PlaceholderResult.invalid("No player!");
            }
            IAfkPlayer player = (IAfkPlayer) ctx.player();
            assert player != null;
            Text result = player.afkplus$isAfk()
                    ? TextParserUtils.formatTextSafe(ConfigManager.CONFIG.PlaceholderOptions.afkReasonPlaceholderFormatting + player.afkplus$getAfkReason() + "<r>")
                    : TextParserUtils.formatTextSafe("");
            return PlaceholderResult.value(result);
        });
    }
    protected static void registerTime() {
        Placeholders.register(Identifier.of(ModData.AFK_MOD_ID, "time"), (ctx, arg) -> {
            if (!ctx.hasPlayer()) {
                return PlaceholderResult.invalid("No player!");
            }
            IAfkPlayer player = (IAfkPlayer) ctx.player();
            assert player != null;
            Text result = player.afkplus$isAfk()
                    ? TextParserUtils.formatTextSafe(ConfigManager.CONFIG.PlaceholderOptions.afkTimePlaceholderFormatting + player.afkplus$getAfkTimeString() + "<r>")
                    : TextParserUtils.formatTextSafe("");
            return PlaceholderResult.value(result);
        });
    }
    protected static void registerInvulnerable() {
        Placeholders.register(Identifier.of(ModData.AFK_MOD_ID, "invulnerable"), (ctx, arg) -> {
            if (!ctx.hasPlayer()) {
                return PlaceholderResult.invalid("No player!");
            }
            IAfkPlayer player = (IAfkPlayer) ctx.player();
            assert player != null;
            Text result = player.afkplus$isDamageEnabled()
                    ? Text.of("")
                    : Placeholders.parseText(TextParserUtils.formatTextSafe(ConfigManager.CONFIG.PlaceholderOptions.afkInvulnerablePlaceholder + "<r>"), ctx);
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
