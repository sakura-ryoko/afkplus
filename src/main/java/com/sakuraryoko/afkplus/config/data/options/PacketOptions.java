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
    public boolean disableDamage;
    public int disableDamageCooldown;
    public boolean bypassSleepCount;
    public boolean bypassInsomnia;
    public boolean afkKickEnabled;
    public boolean afkKickNonSurvival;
    public int afkKickTimer;
    public int afkKickSafePermissions;

    public PacketOptions()
    {
        this.defaults();
    }

    public void defaults()
    {
        this.resetOnLook = false;
        this.resetOnMovement = false;
        this.timeoutSeconds = 240;
        this.disableDamage = false;
        this.disableDamageCooldown = 15;
        this.bypassSleepCount = true;
        this.bypassInsomnia = true;
        this.afkKickEnabled = false;
        this.afkKickNonSurvival = false;
        this.afkKickTimer = 3000;
        this.afkKickSafePermissions = 3;
    }

    @Override
    public PacketOptions copy(IConfigOption opt)
    {
        PacketOptions opts = (PacketOptions) opt;

        this.resetOnLook = opts.resetOnLook;
        this.resetOnMovement = opts.resetOnMovement;
        this.timeoutSeconds = opts.timeoutSeconds;
        this.disableDamage = opts.disableDamage;
        this.disableDamageCooldown = opts.disableDamageCooldown;
        this.bypassSleepCount = opts.bypassSleepCount;
        this.bypassInsomnia = opts.bypassInsomnia;
        this.afkKickEnabled = opts.afkKickEnabled;
        this.afkKickNonSurvival = opts.afkKickNonSurvival;
        this.afkKickTimer = opts.afkKickTimer;
        this.afkKickSafePermissions = opts.afkKickSafePermissions;

        return this;
    }

    @SuppressWarnings("deprecation")
    public PacketOptions fromToml(TomlConfigData.PacketOptions opts)
    {
        this.resetOnLook = opts.resetOnLook;
        this.resetOnMovement = opts.resetOnMovement;
        this.timeoutSeconds = opts.timeoutSeconds;
        this.disableDamage = opts.disableDamage;
        this.disableDamageCooldown = opts.disableDamageCooldown;
        this.bypassSleepCount = opts.bypassSleepCount;
        this.bypassInsomnia = opts.bypassInsomnia;
        this.afkKickEnabled = opts.afkKickEnabled;
        this.afkKickNonSurvival = opts.afkKickNonSurvival;
        this.afkKickTimer = opts.afkKickTimer;
        this.afkKickSafePermissions = opts.afkKickSafePermissions;

        return this;
    }
}
