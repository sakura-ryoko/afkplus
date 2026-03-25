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

package com.sakuraryoko.afkplus.impl.placeholders.options;

import eu.pb4.placeholders.api.PlaceholderResult;
import eu.pb4.placeholders.api.Placeholders;
import org.jetbrains.annotations.ApiStatus;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import com.sakuraryoko.afkplus.Reference;
import com.sakuraryoko.afkplus.impl.compat.morecolors.TextHandler;
import com.sakuraryoko.afkplus.impl.config.ConfigWrap;
import com.sakuraryoko.afkplus.impl.player.AfkPlayer;
import com.sakuraryoko.afkplus.impl.player.AfkPlayerList;

@ApiStatus.Internal
public class TimePlaceholder
{
    public static void register()
    {
        //#if MC >= 26.1
        //$$ Placeholders.registerServer(Identifier.fromNamespaceAndPath(Reference.MOD_ID, "time"), (ctx, arg) ->
        //#elseif MC >= 1.21.1
        //$$ Placeholders.register(ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, "time"), (ctx, arg) ->
        //#else
        Placeholders.register(new ResourceLocation(Reference.MOD_ID, "time"), (ctx, arg) ->
        //#endif
        {
            //#if MC >= 26.1
            //$$ if (!ctx.hasServerPlayer() || ctx.serverPlayer() == null)
            //#else
            if (!ctx.hasPlayer() || ctx.player() == null)
            //#endif
            {
                return PlaceholderResult.invalid("No player!");
            }

            //#if MC >= 26.1
            //$$ AfkPlayer afkPlayer = AfkPlayerList.getInstance().addOrGetPlayer(ctx.serverPlayer());
            //#else
            AfkPlayer afkPlayer = AfkPlayerList.getInstance().addOrGetPlayer(ctx.player());
            //#endif
            Component result = afkPlayer.isAfk()
                               ? TextHandler.getInstance().formatTextSafe(ConfigWrap.place().afkTimePlaceholderFormatting
                                                                                  + afkPlayer.getAfkTimeStringForPlaceholder() + "<r>")
                               : TextHandler.getInstance().formatTextSafe("");

            return PlaceholderResult.value(result);
        });
    }
}
