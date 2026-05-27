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

package com.sakuraryoko.afkplus.impl.commands.arguments;

import java.util.Arrays;
import java.util.Collection;
import java.util.concurrent.CompletableFuture;
import it.unimi.dsi.fastutil.objects.Object2LongMap;
import it.unimi.dsi.fastutil.objects.Object2LongOpenHashMap;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.network.chat.Component;

public class RealTimeArgument implements ArgumentType<Long>
{
	public static final Long DEFAULT = 3600000L;            // Default: 1 hour
	private static final Collection<String> EXAMPLES = Arrays.asList("0h", "0m", "0s");
	private static final SimpleCommandExceptionType ERROR_INVALID_UNIT = new SimpleCommandExceptionType(Component.translatable("argument.time.invalid_unit"));
	private static final Object2LongMap<String> UNITS = new Object2LongOpenHashMap<>();

	public static RealTimeArgument realTime()
	{
		return new RealTimeArgument();
	}

	@Override
	public Long parse(StringReader reader) throws CommandSyntaxException
	{
		float time = reader.readFloat();
		String string = reader.readUnquotedString();
		long unit = UNITS.getOrDefault(string, 1000L);

		if (unit < 1000L)
		{
			throw ERROR_INVALID_UNIT.create();
		}
		else
		{
			long result = Math.round(time * unit);

			if (result < 1000L)
			{
				throw ERROR_INVALID_UNIT.create();
			}
			else
			{
				return result;
			}
		}
	}

	@Override
	public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder)
	{
		StringReader stringReader = new StringReader(builder.getRemaining());

		try
		{
			stringReader.readFloat();
		}
		catch (CommandSyntaxException var5)
		{
			return builder.buildFuture();
		}

		return SharedSuggestionProvider.suggest(UNITS.keySet(), builder.createOffset(builder.getStart() + stringReader.getCursor()));
	}

	@Override
	public Collection<String> getExamples()
	{
		return EXAMPLES;
	}

	static
	{
		UNITS.put("d", 86400000L);
		UNITS.put("h", 3600000L);
		UNITS.put("m", 60000L);
		UNITS.put("s", 1000L);
	}
}
