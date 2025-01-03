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

package com.sakuraryoko.afkplus.compat.carpet;

import net.fabricmc.loader.api.FabricLoader;

import com.sakuraryoko.afkplus.AfkPlus;
import com.sakuraryoko.afkplus.config.ConfigWrap;

public class CarpetCompat
{
    private static final CarpetCompat INSTANCE = new CarpetCompat();
    public static CarpetCompat getInstance() { return INSTANCE; }
    private boolean hasCarpet;

    public CarpetCompat()
    {
        this.hasCarpet = FabricLoader.getInstance().isModLoaded("carpet");
        // styledplayerlist
    }

    public boolean hasCarpet()
    {
        return this.hasCarpet;
    }

    public void setHasCarpet(boolean toggle)
    {
        if (!this.hasCarpet && toggle)
        {
            this.handleCarpet();
        }

        this.hasCarpet = toggle;
    }

    // Check Config so that the Carpet Logger's
    // don't interfere with the List Display too much
    public void handleCarpet()
    {
        if (this.hasCarpet())
        {
            AfkPlus.debugLog("handleCarpet(): checking that List display config is enabled");

            if (!ConfigWrap.list().enableListDisplay)
            {
                ConfigWrap.list().enableListDisplay = true;
            }

            if (ConfigWrap.list().updateInterval < 1 || ConfigWrap.list().updateInterval > 10)
            {
                ConfigWrap.list().updateInterval = 10;
            }
        }
    }
}
