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

package com.sakuraryoko.afkplus.impl.config.data.options;

import org.jetbrains.annotations.ApiStatus;

import com.sakuraryoko.corelib.api.config.IConfigOption;

@ApiStatus.Internal
public class AfkPlusOptions implements IConfigOption
{
    public boolean enableAfkCommand;
    public boolean enableNoAfkCommand;
    public boolean enableAfkInfoCommand;
    public boolean enableAfkMeCommand;
    public int afkCommandPermissions;
    public int noAfkCommandPermissions;
    public int afkInfoCommandPermissions;
    public int afkMeCommandPermissions;
    public int afkPlusCommandPermissions;
    public int afkCommandCooldown;
    public boolean debugMode;

    public AfkPlusOptions()
    {
        this.defaults();
    }

    public void defaults()
    {
        this.enableAfkCommand = true;
        this.enableNoAfkCommand = true;
        this.enableAfkInfoCommand = true;
        this.enableAfkMeCommand = true;
        this.afkCommandPermissions = 0;
        this.noAfkCommandPermissions = 0;
        this.afkInfoCommandPermissions = 2;
        this.afkMeCommandPermissions = 0;
        this.afkPlusCommandPermissions = 3;
        this.afkCommandCooldown = 5;
        this.debugMode = false;
    }

    @Override
    public AfkPlusOptions copy(IConfigOption opt)
    {
        AfkPlusOptions opts = (AfkPlusOptions) opt;

        this.enableAfkCommand = opts.enableAfkCommand;
        this.enableNoAfkCommand = opts.enableNoAfkCommand;
        this.enableAfkInfoCommand = opts.enableAfkInfoCommand;
        this.enableAfkMeCommand = opts.enableAfkMeCommand;
        this.afkCommandPermissions = opts.afkCommandPermissions;
        this.noAfkCommandPermissions = opts.noAfkCommandPermissions;
        this.afkInfoCommandPermissions = opts.afkInfoCommandPermissions;
        this.afkMeCommandPermissions = opts.afkMeCommandPermissions;
        this.afkPlusCommandPermissions = opts.afkPlusCommandPermissions;
        this.afkCommandCooldown = opts.afkCommandCooldown;
        this.debugMode = opts.debugMode;

        return this;
    }
}
