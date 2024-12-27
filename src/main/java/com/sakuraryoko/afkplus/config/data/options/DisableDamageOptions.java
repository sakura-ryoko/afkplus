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

public class DisableDamageOptions implements IConfigOption
{
    public boolean disableDamage;
    public int disableDamageCooldown;
    public String whenDamageDisabled;
    public String whenDamageEnabled;

    public DisableDamageOptions()
    {
        this.defaults();
    }

    @Override
    public void defaults()
    {
        this.disableDamage = false;
        this.disableDamageCooldown = 15;
        this.whenDamageDisabled = "%player:displayname% <yellow>is marked as <red>Invulnerable.<r>";
        this.whenDamageEnabled = "%player:displayname% <yellow>is no longer <red>Invulnerable.<r>";
    }

    @Override
    public DisableDamageOptions copy(IConfigOption opt)
    {
        DisableDamageOptions opts = (DisableDamageOptions) opt;

        this.disableDamage = opts.disableDamage;
        this.disableDamageCooldown = opts.disableDamageCooldown;
        this.whenDamageDisabled = opts.whenDamageDisabled;
        this.whenDamageEnabled = opts.whenDamageEnabled;

        return this;
    }

    @SuppressWarnings("deprecation")
    public DisableDamageOptions fromToml(TomlConfigData.PacketOptions opts, DisableDamageOptions opt)
    {
        this.copy(opt);

        this.disableDamage = opts.disableDamage;
        this.disableDamageCooldown = opts.disableDamageCooldown;

        return this;
    }

    @SuppressWarnings("deprecation")
    public DisableDamageOptions fromToml(TomlConfigData.MessageOptions opts, DisableDamageOptions opt)
    {
        this.copy(opt);

        this.whenDamageDisabled = opts.whenDamageDisabled;
        this.whenDamageEnabled = opts.whenDamageEnabled;

        return this;
    }
}
