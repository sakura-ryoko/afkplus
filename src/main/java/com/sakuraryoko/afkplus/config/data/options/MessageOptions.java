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

public class MessageOptions
{
    public boolean enableMessages;
    public String whenAfk;
    public String whenReturn;
    public boolean prettyDuration;
    public String defaultReason;
    public String whenDamageDisabled;
    public String whenDamageEnabled;
    public boolean displayDuration;
    public String afkKickMessage;
    public String whenKicked;

    public MessageOptions()
    {
        this.defaults();
    }

    public void defaults()
    {
        this.enableMessages = true;
        this.whenAfk = "%player:displayname% <yellow>is now AFK<r>";
        this.whenReturn = "%player:displayname% <yellow>is no longer AFK<r>";
        this.prettyDuration = true;
        this.defaultReason = "<gray>poof!<r>";
        this.whenDamageDisabled = "%player:displayname% <yellow>is marked as <red>Invulnerable.<r>";
        this.whenDamageEnabled = "%player:displayname% <yellow>is no longer <red>Invulnerable.<r>";
        this.displayDuration = true;
        this.afkKickMessage = "<copper>AFK beyond the allowed time limit set by your Administrator.<r>";
        this.whenKicked = "%player:displayname% <copper>was kicked for being AFK.<r>";
    }

    public MessageOptions copy(MessageOptions opts)
    {
        this.enableMessages = opts.enableMessages;
        this.whenAfk = opts.whenAfk;
        this.whenReturn = opts.whenReturn;
        this.prettyDuration = opts.prettyDuration;
        this.defaultReason = opts.defaultReason;
        this.whenDamageDisabled = opts.whenDamageDisabled;
        this.whenDamageEnabled = opts.whenDamageEnabled;
        this.displayDuration = opts.displayDuration;
        this.afkKickMessage = opts.afkKickMessage;
        this.whenKicked = opts.whenKicked;

        return this;
    }

    @SuppressWarnings("deprecation")
    public void fromToml(TomlConfigData.MessageOptions opts)
    {
        this.enableMessages = opts.enableMessages;
        this.whenAfk = opts.whenAfk;
        this.whenReturn = opts.whenReturn;
        this.prettyDuration = opts.prettyDuration;
        this.defaultReason = opts.defaultReason;
        this.whenDamageDisabled = opts.whenDamageDisabled;
        this.whenDamageEnabled = opts.whenDamageEnabled;
        this.displayDuration = opts.displayDuration;
        this.afkKickMessage = opts.afkKickMessage;
        this.whenKicked = opts.whenKicked;
    }
}
