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

package com.sakuraryoko.afkplus.impl.compat.unplugged_afk;

import java.util.UUID;

import javax.annotation.Nonnull;

import net.fabricmc.loader.api.FabricLoader;

import com.sakuraryoko.unplugged_afk.api.UnpluggedAfkAPI;

public class UnpluggedAfkAPICompat
{
	private static final UnpluggedAfkAPICompat INSTANCE = new UnpluggedAfkAPICompat();
	public static UnpluggedAfkAPICompat getInstance() { return INSTANCE; }
	private final boolean hasUnpluggedAfk;

	public UnpluggedAfkAPICompat()
	{
		this.hasUnpluggedAfk = FabricLoader.getInstance().isModLoaded("unplugged_afk");
	}

	public boolean hasUnpluggedAfk()
	{
		return this.hasUnpluggedAfk;
	}

	public boolean isUnplugged(@Nonnull UUID uuid)
	{
		if (this.hasUnpluggedAfk())
		{
			return UnpluggedAfkAPI.isUnplugged(uuid);
		}

		return false;
	}
}
