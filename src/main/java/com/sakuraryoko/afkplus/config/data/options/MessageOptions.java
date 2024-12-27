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
import com.sakuraryoko.corelib.api.time.DurationFormat;
import com.sakuraryoko.corelib.api.time.DurationOption;
import com.sakuraryoko.corelib.api.time.TimeDateOption;

public class MessageOptions implements IConfigOption
{
    public boolean enableMessages;
    public String whenAfk;
    public String whenAfkPunctuation;
    public String whenReturn;
    public String whenReturnDurationPrefix;
    public String whenReturnDurationSuffix;
    public String defaultReason;
    public boolean displayDuration;
    public DurationOption duration;
    public TimeDateOption timeDate;

    public MessageOptions()
    {
        this.defaults();
    }

    public void defaults()
    {
        this.enableMessages = true;
        this.whenAfk = "%player:displayname% <yellow>is now AFK<r>";
        this.whenAfkPunctuation = "<yellow>,<r> ";
        this.whenReturn = "%player:displayname% <yellow>is no longer AFK<r>";
        this.whenReturnDurationPrefix = " <gray>(Gone for: <green>";
        this.whenReturnDurationSuffix = "<gray>)";
        this.defaultReason = "<gray>poof!<r>";
        this.displayDuration = true;
        this.duration = new DurationOption();
        this.duration.option = DurationFormat.PRETTY;
        this.timeDate = new TimeDateOption();
    }

    @Override
    public MessageOptions copy(IConfigOption opt)
    {
        MessageOptions opts = (MessageOptions) opt;

        this.enableMessages = opts.enableMessages;
        this.whenAfk = opts.whenAfk;
        this.whenAfkPunctuation = !opts.whenAfkPunctuation.isEmpty() ? opts.whenAfkPunctuation : "<yellow>,<r> ";
        this.whenReturn = opts.whenReturn;
        this.whenReturnDurationPrefix = !opts.whenReturnDurationPrefix.isEmpty() ? opts.whenReturnDurationPrefix : " <gray>(Gone for: <green>";
        this.whenReturnDurationSuffix = !opts.whenReturnDurationSuffix.isEmpty() ? opts.whenReturnDurationSuffix : "<gray>)";
        this.defaultReason = opts.defaultReason;
        this.displayDuration = opts.displayDuration;
        this.duration.copy(opts.duration);
        this.timeDate.copy(opts.timeDate);

        return this;
    }

    @SuppressWarnings("deprecation")
    public MessageOptions fromToml(TomlConfigData.MessageOptions opts, MessageOptions opt)
    {
        this.copy(opt);

        this.enableMessages = opts.enableMessages;
        this.whenAfk = opts.whenAfk;
        this.whenAfkPunctuation = "<yellow>,<r> ";
        this.whenReturn = opts.whenReturn;
        this.whenReturnDurationPrefix = " <gray>(Gone for: <green>";
        this.whenReturnDurationSuffix = "<gray>)";
        this.defaultReason = opts.defaultReason;
        this.displayDuration = opts.displayDuration;
        this.duration = new DurationOption();
        this.timeDate = new TimeDateOption();

        if (opts.prettyDuration)
        {
            this.duration.option = DurationFormat.PRETTY;
        }

        return this;
    }
}
