/*
 * This file is part of the AfkPlus project, licensed under the
 * GNU Lesser General Public License v3.0
 *
 * Copyright (C) 2024  Sakura Ryoko and contributors
 *
 * AfkPlus is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * AfkPlus is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with AfkPlus.  If not, see <https://www.gnu.org/licenses/>.
 */

package com.sakuraryoko.afkplus.placeholders;

import eu.pb4.placeholders.api.PlaceholderResult;
import eu.pb4.placeholders.api.Placeholders;
import org.apache.commons.lang3.time.DurationFormatUtils;

import net.minecraft.Util;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import com.sakuraryoko.afkplus.compat.TextUtils;
import com.sakuraryoko.afkplus.config.ConfigManager;
import com.sakuraryoko.afkplus.data.IAfkPlayer;
import com.sakuraryoko.afkplus.data.ModData;

public class AfkPlusPlaceholders
{
    protected static void registerAfk()
    {
        //#if MC >= 12101
        //$$ Placeholders.register(ResourceLocation.fromNamespaceAndPath(ModData.AFK_MOD_ID, "afk"), (ctx, arg) ->
        //#else
        Placeholders.register(new ResourceLocation(ModData.AFK_MOD_ID, "afk"), (ctx, arg) ->
        //#endif
        {
            if (!ctx.hasPlayer())
            {
                return PlaceholderResult.invalid("No player!");
            }
            IAfkPlayer player = (IAfkPlayer) ctx.player();
            assert player != null;
            Component result = player.afkplus$isAfk()
                               ? Placeholders.parseText(TextUtils.formatTextSafe(ConfigManager.CONFIG.PlaceholderOptions.afkPlaceholder), ctx)
                               : Component.empty();
            return PlaceholderResult.value(result);
        });
    }

    protected static void registerDuration()
    {
        //#if MC >= 12101
        //$$ Placeholders.register(ResourceLocation.fromNamespaceAndPath(ModData.AFK_MOD_ID, "duration"), (ctx, arg) ->
        //#else
        Placeholders.register(new ResourceLocation(ModData.AFK_MOD_ID, "duration"), (ctx, arg) ->
        //#endif
        {
            if (!ctx.hasPlayer())
            {
                return PlaceholderResult.invalid("No player!");
            }
            IAfkPlayer player = (IAfkPlayer) ctx.player();
            assert player != null;
            Component result;
            if (ConfigManager.CONFIG.PlaceholderOptions.afkDurationPretty)
            {
                result = player.afkplus$isAfk()
                         ? TextUtils.formatTextSafe(ConfigManager.CONFIG.PlaceholderOptions.afkDurationPlaceholderFormatting + DurationFormatUtils.formatDurationWords(Util.getMillis() - player.afkplus$getAfkTimeMs(), true, true) + "<r>")
                         : TextUtils.formatTextSafe("");
            }
            else
            {
                result = player.afkplus$isAfk()
                         ? TextUtils.formatTextSafe(ConfigManager.CONFIG.PlaceholderOptions.afkDurationPlaceholderFormatting + DurationFormatUtils.formatDurationHMS(Util.getMillis() - player.afkplus$getAfkTimeMs()) + "<r>")
                         : Component.empty();
            }
            return PlaceholderResult.value(result);
        });
    }

    protected static void registerDisplayName()
    {
        //#if MC >= 12101
        //$$ Placeholders.register(ResourceLocation.fromNamespaceAndPath(ModData.AFK_MOD_ID, "name"), (ctx, arg) ->
        //#else
        Placeholders.register(new ResourceLocation(ModData.AFK_MOD_ID, "name"), (ctx, arg) ->
        //#endif
        {
            if (!ctx.hasPlayer())
            {
                return PlaceholderResult.invalid("No player!");
            }
            IAfkPlayer player = (IAfkPlayer) ctx.player();
            assert player != null;
            Component result = player.afkplus$isAfk()
                               ? Placeholders.parseText(TextUtils.formatTextSafe(ConfigManager.CONFIG.PlaceholderOptions.afkPlusNamePlaceholderAfk), ctx)
                               : Placeholders.parseText(TextUtils.formatTextSafe(ConfigManager.CONFIG.PlaceholderOptions.afkPlusNamePlaceholder), ctx);
            return PlaceholderResult.value(result);
        });
        //#if MC >= 12101
        //$$ Placeholders.register(ResourceLocation.fromNamespaceAndPath(ModData.AFK_MOD_ID, "display_name"), (ctx, arg) ->
        //#else
        Placeholders.register(new ResourceLocation(ModData.AFK_MOD_ID, "display_name"), (ctx, arg) ->
        //#endif
        {
            if (!ctx.hasPlayer())
            {
                return PlaceholderResult.invalid("No player!");
            }
            IAfkPlayer player = (IAfkPlayer) ctx.player();
            assert player != null;
            Component result = player.afkplus$isAfk()
                               ? Placeholders.parseText(TextUtils.formatTextSafe(ConfigManager.CONFIG.PlaceholderOptions.afkPlusNamePlaceholderAfk), ctx)
                               : Placeholders.parseText(TextUtils.formatTextSafe(ConfigManager.CONFIG.PlaceholderOptions.afkPlusNamePlaceholder), ctx);
            return PlaceholderResult.value(result);
        });
    }

    protected static void registerReason()
    {
        //#if MC >= 12101
        //$$ Placeholders.register(ResourceLocation.fromNamespaceAndPath(ModData.AFK_MOD_ID, "reason"), (ctx, arg) ->
        //#else
        Placeholders.register(new ResourceLocation(ModData.AFK_MOD_ID, "reason"), (ctx, arg) ->
        //#endif
        {
            if (!ctx.hasPlayer())
            {
                return PlaceholderResult.invalid("No player!");
            }
            IAfkPlayer player = (IAfkPlayer) ctx.player();
            assert player != null;
            Component result = player.afkplus$isAfk()
                               ? TextUtils.formatTextSafe(ConfigManager.CONFIG.PlaceholderOptions.afkReasonPlaceholderFormatting + player.afkplus$getAfkReason() + "<r>")
                               : Component.empty();
            return PlaceholderResult.value(result);
        });
    }

    protected static void registerTime()
    {
        //#if MC >= 12101
        //$$ Placeholders.register(ResourceLocation.fromNamespaceAndPath(ModData.AFK_MOD_ID, "time"), (ctx, arg) ->
        //#else
        Placeholders.register(new ResourceLocation(ModData.AFK_MOD_ID, "time"), (ctx, arg) ->
        //#endif
        {
            if (!ctx.hasPlayer())
            {
                return PlaceholderResult.invalid("No player!");
            }
            IAfkPlayer player = (IAfkPlayer) ctx.player();
            assert player != null;
            Component result = player.afkplus$isAfk()
                               ? TextUtils.formatTextSafe(ConfigManager.CONFIG.PlaceholderOptions.afkTimePlaceholderFormatting + player.afkplus$getAfkTimeString() + "<r>")
                               : TextUtils.formatTextSafe("");
            return PlaceholderResult.value(result);
        });
    }

    protected static void registerInvulnerable()
    {
        //#if MC >= 12101
        //$$ Placeholders.register(ResourceLocation.fromNamespaceAndPath(ModData.AFK_MOD_ID, "invulnerable"), (ctx, arg) ->
        //#else
        Placeholders.register(new ResourceLocation(ModData.AFK_MOD_ID, "invulnerable"), (ctx, arg) ->
        //#endif
        {
            if (!ctx.hasPlayer())
            {
                return PlaceholderResult.invalid("No player!");
            }
            IAfkPlayer player = (IAfkPlayer) ctx.player();
            assert player != null;
            Component result = player.afkplus$isDamageEnabled()
                               ? Component.empty()
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
