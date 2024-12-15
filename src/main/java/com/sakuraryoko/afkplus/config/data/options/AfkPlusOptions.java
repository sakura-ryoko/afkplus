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

public class AfkPlusOptions
{
    public boolean enableAfkCommand;
    public boolean enableNoAfkCommand;
    public boolean enableAfkInfoCommand;
    public boolean enableAfkExCommand;
    public int afkCommandPermissions;
    public int noAfkCommandPermissions;
    public int afkInfoCommandPermissions;
    public int afkExCommandPermissions;
    public int afkPlusCommandPermissions;
    public String afkTimeoutString;

    public AfkPlusOptions()
    {
        this.defaults();
    }

    public void defaults()
    {
        this.afkCommandPermissions = 0;
        this.afkExCommandPermissions = 0;
        this.noAfkCommandPermissions = 0;
        this.afkInfoCommandPermissions = 2;
        this.afkPlusCommandPermissions = 3;
        this.enableAfkCommand = true;
        this.enableAfkExCommand = true;
        this.enableNoAfkCommand = true;
        this.enableAfkInfoCommand = true;
        this.afkTimeoutString = "<i><gray>timeout<r>";
    }

    public AfkPlusOptions copy(AfkPlusOptions opts)
    {
        this.enableAfkCommand = opts.enableAfkCommand;
        this.enableNoAfkCommand = opts.enableNoAfkCommand;
        this.enableAfkInfoCommand = opts.enableAfkInfoCommand;
        this.enableAfkExCommand = opts.enableAfkExCommand;
        this.afkCommandPermissions = opts.afkCommandPermissions;
        this.noAfkCommandPermissions = opts.noAfkCommandPermissions;
        this.afkInfoCommandPermissions = opts.afkInfoCommandPermissions;
        this.afkExCommandPermissions = opts.afkExCommandPermissions;
        this.afkPlusCommandPermissions = opts.afkPlusCommandPermissions;
        this.afkTimeoutString = opts.afkTimeoutString;

        return this;
    }

    @SuppressWarnings("deprecation")
    public void fromToml(TomlConfigData.AfkPlusOptions opts)
    {
        this.enableAfkCommand = opts.enableAfkCommand;
        this.enableNoAfkCommand = opts.enableNoAfkCommand;
        this.enableAfkInfoCommand = opts.enableAfkInfoCommand;
        this.enableAfkExCommand = opts.enableAfkExCommand;
        this.afkCommandPermissions = opts.afkCommandPermissions;
        this.noAfkCommandPermissions = opts.noAfkCommandPermissions;
        this.afkInfoCommandPermissions = opts.afkInfoCommandPermissions;
        this.afkExCommandPermissions = opts.afkExCommandPermissions;
        this.afkPlusCommandPermissions = opts.afkPlusCommandPermissions;
        this.afkTimeoutString = opts.afkTimeoutString;
    }
}
