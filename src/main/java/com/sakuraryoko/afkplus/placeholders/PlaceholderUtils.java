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

package com.sakuraryoko.afkplus.placeholders;

import eu.pb4.placeholders.api.PlaceholderContext;
import eu.pb4.placeholders.api.Placeholders;

import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;

public class PlaceholderUtils
{
	public static Component parseText(Component text, PlaceholderContext context)
	{
		return Placeholders.parseText(text, context);
	}

	public static PlaceholderContext ofPlayer(ServerPlayer player)
	{
		return PlaceholderContext.of(player);
	}

	public static PlaceholderContext ofCmdSrc(CommandSourceStack cmdSrc)
	{
		return PlaceholderContext.of(cmdSrc);
	}

	public static PlaceholderContext ofServer(MinecraftServer server)
	{
		return PlaceholderContext.of(server);
	}
}
