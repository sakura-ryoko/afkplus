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

public class PacketOptions implements IConfigOption
{
    public int timeoutSeconds;
    public boolean resetOnMovement;
    public boolean resetOnLook;
    public boolean ignoreAttacks;
    public boolean bypassSleepCount;
    public boolean bypassInsomnia;
    public String afkTimeoutString;
    public String afkTimeoutIgnoreAttack;

    public PacketOptions()
    {
        this.defaults();
    }

    public void defaults()
    {
        this.timeoutSeconds = 240;
        this.resetOnMovement = false;
        this.resetOnLook = false;
        this.ignoreAttacks = false;
        this.bypassSleepCount = true;
        this.bypassInsomnia = true;
        this.afkTimeoutString = "<i><gray>timeout<r>";
        this.afkTimeoutIgnoreAttack = "<i><gray>only swinging their sword<r>";
    }

    @Override
    public PacketOptions copy(IConfigOption opt)
    {
        PacketOptions opts = (PacketOptions) opt;

        this.timeoutSeconds = opts.timeoutSeconds;
        this.resetOnMovement = opts.resetOnMovement;
        this.resetOnLook = opts.resetOnLook;
        this.ignoreAttacks = opts.ignoreAttacks;
        this.bypassSleepCount = opts.bypassSleepCount;
        this.bypassInsomnia = opts.bypassInsomnia;
        this.afkTimeoutString = opts.afkTimeoutString;
        this.afkTimeoutIgnoreAttack = opts.afkTimeoutIgnoreAttack;

        return this;
    }

    @SuppressWarnings("deprecation")
    public PacketOptions fromToml(TomlConfigData.PacketOptions opts, PacketOptions opt)
    {
        this.copy(opt);

        this.resetOnLook = opts.resetOnLook;
        this.resetOnMovement = opts.resetOnMovement;
        this.ignoreAttacks = false;
        this.timeoutSeconds = opts.timeoutSeconds;
        this.bypassSleepCount = opts.bypassSleepCount;
        this.bypassInsomnia = opts.bypassInsomnia;
        this.afkTimeoutString = "<i><gray>timeout<r>";
        this.afkTimeoutIgnoreAttack = "<i><gray>only swinging their sword<r>";

        return this;
    }
}
