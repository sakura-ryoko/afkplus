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

package com.sakuraryoko.afkplus.text.placeholders;

import eu.pb4.placeholders.api.PlaceholderResult;
import eu.pb4.placeholders.api.Placeholders;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import com.sakuraryoko.afkplus.AfkPlusReference;
import com.sakuraryoko.afkplus.config.ConfigWrap;
import com.sakuraryoko.afkplus.player.AfkPlayer;
import com.sakuraryoko.afkplus.player.AfkPlayerList;
import com.sakuraryoko.afkplus.text.TextUtils;

public class TimePlaceholder
{
    protected static void register()
    {
        //#if MC >= 12101
        //$$ Placeholders.register(ResourceLocation.fromNamespaceAndPath(AfkPlusReference.MOD_ID, "time"), (ctx, arg) ->
        //#else
        Placeholders.register(new ResourceLocation(AfkPlusReference.MOD_ID, "time"), (ctx, arg) ->
        //#endif
        {
            if (!ctx.hasPlayer() || ctx.player() == null)
            {
                return PlaceholderResult.invalid("No player!");
            }

            AfkPlayer afkPlayer = AfkPlayerList.getInstance().addOrGetPlayer(ctx.player());
            Component result = afkPlayer.isAfk()
                               ? TextUtils.formatTextSafe(ConfigWrap.place().afkTimePlaceholderFormatting + afkPlayer.getAfkTimeString() + "<r>")
                               : TextUtils.formatTextSafe("");

            return PlaceholderResult.value(result);
        });
    }
}
