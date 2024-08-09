package com.sakuraryoko.afkplus.placeholders;

import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;

import eu.pb4.placeholders.api.PlaceholderResult;
import eu.pb4.placeholders.api.Placeholders;
import org.apache.commons.lang3.time.DurationFormatUtils;

import com.sakuraryoko.afkplus.compat.TextUtils;
import com.sakuraryoko.afkplus.config.ConfigManager;
import com.sakuraryoko.afkplus.data.IAfkPlayer;
import com.sakuraryoko.afkplus.data.ModData;

public class AfkPlusPlaceholders
{
    protected static void registerAfk()
    {
        Placeholders.register(Identifier.of(ModData.AFK_MOD_ID, "afk"), (ctx, arg) ->
        {
            if (!ctx.hasPlayer())
            {
                return PlaceholderResult.invalid("No player!");
            }
            IAfkPlayer player = (IAfkPlayer) ctx.player();
            assert player != null;
            Text result = player.afkplus$isAfk()
                    ? Placeholders.parseText(TextUtils.formatTextSafe(ConfigManager.CONFIG.PlaceholderOptions.afkPlaceholder), ctx)
                    : Text.of("");
            return PlaceholderResult.value(result);
        });
    }

    protected static void registerDuration()
    {
        Placeholders.register(Identifier.of(ModData.AFK_MOD_ID, "duration"), (ctx, arg) ->
        {
            if (!ctx.hasPlayer())
            {
                return PlaceholderResult.invalid("No player!");
            }
            IAfkPlayer player = (IAfkPlayer) ctx.player();
            assert player != null;
            Text result;
            if (ConfigManager.CONFIG.PlaceholderOptions.afkDurationPretty)
            {
                result = player.afkplus$isAfk()
                        ? TextUtils.formatTextSafe(ConfigManager.CONFIG.PlaceholderOptions.afkDurationPlaceholderFormatting + DurationFormatUtils.formatDurationWords(Util.getMeasuringTimeMs() - player.afkplus$getAfkTimeMs(), true, true) + "<r>")
                        : TextUtils.formatTextSafe("");
            }
            else
            {
                result = player.afkplus$isAfk()
                        ? TextUtils.formatTextSafe(ConfigManager.CONFIG.PlaceholderOptions.afkDurationPlaceholderFormatting + DurationFormatUtils.formatDurationHMS(Util.getMeasuringTimeMs() - player.afkplus$getAfkTimeMs()) + "<r>")
                        : TextUtils.formatTextSafe("");
            }
            return PlaceholderResult.value(result);
        });
    }

    protected static void registerDisplayName()
    {
        Placeholders.register(Identifier.of(ModData.AFK_MOD_ID, "name"), (ctx, arg) ->
        {
            if (!ctx.hasPlayer())
            {
                return PlaceholderResult.invalid("No player!");
            }
            IAfkPlayer player = (IAfkPlayer) ctx.player();
            assert player != null;
            Text result = player.afkplus$isAfk()
                    ? Placeholders.parseText(TextUtils.formatTextSafe(ConfigManager.CONFIG.PlaceholderOptions.afkPlusNamePlaceholderAfk), ctx)
                    : Placeholders.parseText(TextUtils.formatTextSafe(ConfigManager.CONFIG.PlaceholderOptions.afkPlusNamePlaceholder), ctx);
            return PlaceholderResult.value(result);
        });
        Placeholders.register(Identifier.of(ModData.AFK_MOD_ID, "display_name"), (ctx, arg) ->
        {
            if (!ctx.hasPlayer())
            {
                return PlaceholderResult.invalid("No player!");
            }
            IAfkPlayer player = (IAfkPlayer) ctx.player();
            assert player != null;
            Text result = player.afkplus$isAfk()
                    ? Placeholders.parseText(TextUtils.formatTextSafe(ConfigManager.CONFIG.PlaceholderOptions.afkPlusNamePlaceholderAfk), ctx)
                    : Placeholders.parseText(TextUtils.formatTextSafe(ConfigManager.CONFIG.PlaceholderOptions.afkPlusNamePlaceholder), ctx);
            return PlaceholderResult.value(result);
        });
    }

    protected static void registerReason()
    {
        Placeholders.register(Identifier.of(ModData.AFK_MOD_ID, "reason"), (ctx, arg) ->
        {
            if (!ctx.hasPlayer())
            {
                return PlaceholderResult.invalid("No player!");
            }
            IAfkPlayer player = (IAfkPlayer) ctx.player();
            assert player != null;
            Text result = player.afkplus$isAfk()
                    ? TextUtils.formatTextSafe(ConfigManager.CONFIG.PlaceholderOptions.afkReasonPlaceholderFormatting + player.afkplus$getAfkReason() + "<r>")
                    : TextUtils.formatTextSafe("");
            return PlaceholderResult.value(result);
        });
    }

    protected static void registerTime()
    {
        Placeholders.register(Identifier.of(ModData.AFK_MOD_ID, "time"), (ctx, arg) ->
        {
            if (!ctx.hasPlayer())
            {
                return PlaceholderResult.invalid("No player!");
            }
            IAfkPlayer player = (IAfkPlayer) ctx.player();
            assert player != null;
            Text result = player.afkplus$isAfk()
                    ? TextUtils.formatTextSafe(ConfigManager.CONFIG.PlaceholderOptions.afkTimePlaceholderFormatting + player.afkplus$getAfkTimeString() + "<r>")
                    : TextUtils.formatTextSafe("");
            return PlaceholderResult.value(result);
        });
    }

    protected static void registerInvulnerable()
    {
        Placeholders.register(Identifier.of(ModData.AFK_MOD_ID, "invulnerable"), (ctx, arg) ->
        {
            if (!ctx.hasPlayer())
            {
                return PlaceholderResult.invalid("No player!");
            }
            IAfkPlayer player = (IAfkPlayer) ctx.player();
            assert player != null;
            Text result = player.afkplus$isDamageEnabled()
                    ? Text.of("")
                    : Placeholders.parseText(TextUtils.formatTextSafe(ConfigManager.CONFIG.PlaceholderOptions.afkInvulnerablePlaceholder + "<r>"), ctx);
            return PlaceholderResult.value(result);
        });
    }

    protected static void register()
    {
        registerAfk();
        registerDisplayName();
        registerDuration();
        registerReason();
        registerTime();
        registerInvulnerable();
    }
}
