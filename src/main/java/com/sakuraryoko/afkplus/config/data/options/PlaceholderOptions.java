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

public class PlaceholderOptions implements IConfigOption
{
    public String afkPlaceholder;
    public String afkPlusNamePlaceholderAfk;
    public String afkPlusNamePlaceholder;
    public String afkDurationPlaceholderFormatting;
    public String afkTimePlaceholderFormatting;
    public String afkReasonPlaceholderFormatting;
    public String afkInvulnerablePlaceholder;
    public DurationOption duration;
    public TimeDateOption timeDate;

    public PlaceholderOptions()
    {
        this.defaults();
    }

    public void defaults()
    {
        this.afkPlaceholder = "<i><gray>[AFK%afkplus:invulnerable%]<r>";
        this.afkPlusNamePlaceholder = "%player:displayname%";
        this.afkPlusNamePlaceholderAfk = "<i><gray>[AFK%afkplus:invulnerable%] %player:displayname_unformatted%<r>";
        this.afkDurationPlaceholderFormatting = "<green>";
        this.afkTimePlaceholderFormatting = "<green>";
        this.afkReasonPlaceholderFormatting = "";
        this.afkInvulnerablePlaceholder = ":<red>I<r>";
        this.duration = new DurationOption();
        this.timeDate = new TimeDateOption();
    }

    @Override
    public PlaceholderOptions copy(IConfigOption opt)
    {
        PlaceholderOptions opts = (PlaceholderOptions) opt;

        this.afkPlaceholder = opts.afkPlaceholder;
        this.afkPlusNamePlaceholder = opts.afkPlusNamePlaceholder;
        this.afkPlusNamePlaceholderAfk = opts.afkPlusNamePlaceholderAfk;
        this.afkDurationPlaceholderFormatting = opts.afkDurationPlaceholderFormatting;
        this.afkTimePlaceholderFormatting = opts.afkTimePlaceholderFormatting;
        this.afkReasonPlaceholderFormatting = opts.afkReasonPlaceholderFormatting;
        this.afkInvulnerablePlaceholder = opts.afkInvulnerablePlaceholder;
        this.duration.copy(opts.duration);
        this.timeDate.copy(opts.timeDate);

        return this;
    }

    @SuppressWarnings("deprecation")
    public PlaceholderOptions fromToml(TomlConfigData.PlaceholderOptions opts, PlaceholderOptions opt)
    {
        this.copy(opt);

        this.afkPlaceholder = opts.afkPlaceholder;
        this.afkPlusNamePlaceholder = opts.afkPlusNamePlaceholder;
        this.afkPlusNamePlaceholderAfk = opts.afkPlusNamePlaceholderAfk;
        this.afkDurationPlaceholderFormatting = opts.afkDurationPlaceholderFormatting;
        this.afkTimePlaceholderFormatting = opts.afkTimePlaceholderFormatting;
        this.afkReasonPlaceholderFormatting = opts.afkReasonPlaceholderFormatting;
        this.afkInvulnerablePlaceholder = opts.afkInvulnerablePlaceholder;
        this.duration = new DurationOption();
        this.timeDate = new TimeDateOption();

        if (opts.afkDurationPretty)
        {
            this.duration.option = DurationFormat.PRETTY;
        }

        return this;
    }
}
