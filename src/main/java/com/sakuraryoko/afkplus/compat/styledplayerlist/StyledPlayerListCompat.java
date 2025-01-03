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

package com.sakuraryoko.afkplus.compat.styledplayerlist;

import net.fabricmc.loader.api.FabricLoader;

import com.sakuraryoko.afkplus.AfkPlus;
import com.sakuraryoko.afkplus.config.ConfigWrap;

public class StyledPlayerListCompat
{
    //private static final AnsiLogger LOGGER = new AnsiLogger(StyledPlayerListCompat.class, true);
    private static final StyledPlayerListCompat INSTANCE = new StyledPlayerListCompat();
    public static StyledPlayerListCompat getInstance() { return INSTANCE; }
    private boolean hasStyledPlayerList;

    public StyledPlayerListCompat()
    {
        this.hasStyledPlayerList = FabricLoader.getInstance().isModLoaded("styledplayerlist");
    }

    public boolean hasStyledPlayerList()
    {
        return this.hasStyledPlayerList;
    }

    public void setStyledPlayerList(boolean toggle)
    {
        if (!this.hasStyledPlayerList && toggle)
        {
            this.handleStyledPlayerList();
        }

        this.hasStyledPlayerList = toggle;
    }

    // Check Config so that the Styled Player List updates
    // don't interfere with the List Display too much; and
    // potentially fix some of its quirks.
    public void handleStyledPlayerList()
    {
        if (this.hasStyledPlayerList())
        {
            this.checkListConfig();
        }
    }

    private void checkListConfig()
    {
        if (this.hasStyledPlayerList())
        {
            AfkPlus.debugLog("handleStyledPlayerList(): checking that List display config is enabled");

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
