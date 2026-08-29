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

import net.fabricmc.loader.api.FabricLoader;

import com.sakuraryoko.afkplus.impl.events.PlayerEventsHandler;
import com.sakuraryoko.unplugged_afk.api.UnpluggedAfkEvents;

public class UnpluggedAfkEventsCompat
{
	private static final UnpluggedAfkEventsCompat INSTANCE = new UnpluggedAfkEventsCompat();
	public static UnpluggedAfkEventsCompat getInstance() { return INSTANCE; }
	private final boolean hasUnpluggedAfk;

	public UnpluggedAfkEventsCompat()
	{
		this.hasUnpluggedAfk = FabricLoader.getInstance().isModLoaded("unplugged_afk");
	}

	public boolean hasUnpluggedAfk()
	{
		return this.hasUnpluggedAfk;
	}

	public void registerEvents()
	{
		if (this.hasUnpluggedAfk())
		{
			UnpluggedAfkEvents.UNPLUGGED_START.register(PlayerEventsHandler.getInstance()::onUnpluggedStart);
			UnpluggedAfkEvents.UNPLUGGED_RESPAWN.register(PlayerEventsHandler.getInstance()::onUnpluggedRespawn);
			UnpluggedAfkEvents.UNPLUGGED_END.register(PlayerEventsHandler.getInstance()::onUnpluggedEnd);
		}
	}
}
