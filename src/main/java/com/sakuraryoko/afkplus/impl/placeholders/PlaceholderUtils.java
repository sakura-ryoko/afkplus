/*
 * This file is part of the AfkPlus project, licensed under the
 * GNU Lesser General Public License v3.0
 *
 * Copyright (C) 2026  Sakura Ryoko and contributors
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

package com.sakuraryoko.afkplus.impl.placeholders;

import eu.pb4.placeholders.api.PlaceholderContext;
import eu.pb4.placeholders.api.Placeholders;
import org.jetbrains.annotations.ApiStatus;

import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;

//#if MC >= 26.1
//$$ import eu.pb4.placeholders.api.ServerPlaceholderContext;
//$$ import eu.pb4.placeholders.api.node.TextNode;
//$$ import com.sakuraryoko.morecolors.impl.text.TextUtils;
//#endif

@ApiStatus.Internal
public class PlaceholderUtils
{
	public static Component parseText(Component text, PlaceholderContext ctx)
	{
		//#if MC >= 26.1
		//$$ return Placeholders.SERVER_PLACEHOLDER_PARSER.parseComponent(TextNode.convert(text), ctx.asParserContext());
		//#else
		return Placeholders.parseText(text, ctx);
		//#endif
	}

	public static PlaceholderContext ofPlayer(ServerPlayer player)
	{
		//#if MC >= 26.1
		//$$ return ServerPlaceholderContext.of(player);
		//#else
		return PlaceholderContext.of(player);
		//#endif
	}

	public static PlaceholderContext ofEntity(Entity entity)
	{
		//#if MC >= 26.1
		//$$ return ServerPlaceholderContext.of(entity);
		//#else
		return PlaceholderContext.of(entity);
		//#endif
	}

	public static PlaceholderContext ofCmdSrc(CommandSourceStack cmdSrc)
	{
		//#if MC >= 26.1
		//$$ return ServerPlaceholderContext.of(cmdSrc);
		//#else
		return PlaceholderContext.of(cmdSrc);
		//#endif
	}

	public static PlaceholderContext ofServer(MinecraftServer server)
	{
		//#if MC >= 26.1
		//$$ return ServerPlaceholderContext.of(server);
		//#else
		return PlaceholderContext.of(server);
		//#endif
	}
}
