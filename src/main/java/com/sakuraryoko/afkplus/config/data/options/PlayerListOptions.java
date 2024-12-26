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

package com.sakuraryoko.afkplus.config.data.options;

import com.sakuraryoko.afkplus.config.data.TomlConfigData;
import com.sakuraryoko.corelib.api.config.IConfigOption;

public class PlayerListOptions implements IConfigOption
{
    public boolean enableListDisplay;
    public String afkPlayerName;
    public int updateInterval;

    public PlayerListOptions()
    {
        this.defaults();
    }

    public void defaults()
    {
        this.afkPlayerName = "<i><gray>[AFK%afkplus:invulnerable%] %player:displayname%<r>";
        this.enableListDisplay = true;
        this.updateInterval = 15;
    }

    @Override
    public PlayerListOptions copy(IConfigOption opt)
    {
        PlayerListOptions opts = (PlayerListOptions) opt;

        this.afkPlayerName = opts.afkPlayerName;
        this.enableListDisplay = opts.enableListDisplay;
        this.updateInterval = opts.updateInterval;

        return this;
    }

    @SuppressWarnings("deprecation")
    public PlayerListOptions fromToml(TomlConfigData.PlayerListOptions opts)
    {
        this.afkPlayerName = opts.afkPlayerName;
        this.enableListDisplay = opts.enableListDisplay;
        this.updateInterval = opts.updateInterval;

        return this;
    }
}
