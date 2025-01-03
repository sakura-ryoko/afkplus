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

package com.sakuraryoko.afkplus.compat.vanish;

import me.drex.vanish.api.VanishEvents;

import net.fabricmc.loader.api.FabricLoader;

import com.sakuraryoko.afkplus.events.PlayerEventsHandler;

public class VanishEventsCompat
{
    private static final VanishEventsCompat INSTANCE = new VanishEventsCompat();
    public static VanishEventsCompat getInstance() { return INSTANCE; }
    private final boolean hasVanish;

    public VanishEventsCompat()
    {
        this.hasVanish = FabricLoader.getInstance().isModLoaded("melius-vanish");
    }

    public boolean hasVanish()
    {
        return this.hasVanish;
    }

    public void registerEvents()
    {
        if (this.hasVanish())
        {
            // Register
            VanishEvents.VANISH_EVENT.register(PlayerEventsHandler.getInstance()::onVanish);
            /*
            VanishEvents.VANISH_MESSAGE_EVENT.register(PlayerEventsHandler.getInstance()::getVanishMessage);
            VanishEvents.UN_VANISH_MESSAGE_EVENT.register(PlayerEventsHandler.getInstance()::getUnVanishMessage);
             */
        }
    }
}
