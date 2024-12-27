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

public class KickOptions implements IConfigOption
{
    public boolean afkKickEnabled;
    public boolean afkKickNonSurvival;
    public int afkKickTimer;
    public int afkKickSafePermissions;
    public String afkKickMessage;
    public String whenKicked;
    public String whenKickedDefaultReason;
    public String whenKickedDurationDefaultPrefix;
    public String whenKickedDurationNamedPrefix;
    public String whenKickedDurationSuffix;

    public KickOptions()
    {
        this.defaults();
    }

    @Override
    public void defaults()
    {
        this.afkKickEnabled = false;
        this.afkKickNonSurvival = false;
        this.afkKickTimer = 3000;
        this.afkKickSafePermissions = 3;
        this.afkKickMessage = "<copper>AFK beyond the allowed time limit set by your Administrator.<r>";
        this.whenKicked = "%player:displayname% <copper>was kicked for being AFK.<r>";
        this.whenKickedDefaultReason = "<copper>AFK timeout";
        this.whenKickedDurationDefaultPrefix = " <gray>(Gone for: <green>";
        this.whenKickedDurationNamedPrefix = " <gray>(%player:displayname% was gone for: <green>";
        this.whenKickedDurationSuffix = "<gray>)";
    }

    @Override
    public KickOptions copy(IConfigOption opt)
    {
        KickOptions opts = (KickOptions) opt;

        this.afkKickEnabled = opts.afkKickEnabled;
        this.afkKickNonSurvival = opts.afkKickNonSurvival;
        this.afkKickTimer = opts.afkKickTimer;
        this.afkKickSafePermissions = opts.afkKickSafePermissions;
        this.afkKickMessage = opts.afkKickMessage;
        this.whenKicked = opts.whenKicked;
        this.whenKickedDefaultReason = !opts.whenKickedDefaultReason.isEmpty() ? opts.whenKickedDefaultReason : "<copper>AFK timeout";
        this.whenKickedDurationDefaultPrefix = !opts.whenKickedDurationDefaultPrefix.isEmpty() ? opts.whenKickedDurationDefaultPrefix : " <gray>(Gone for: <green>";
        this.whenKickedDurationNamedPrefix = !opts.whenKickedDurationNamedPrefix.isEmpty() ? opts.whenKickedDurationNamedPrefix : " <gray>(%player:displayname% was gone for: <green>";
        this.whenKickedDurationSuffix = !opts.whenKickedDurationSuffix.isEmpty() ? opts.whenKickedDurationSuffix : "<gray>)";

        return this;
    }

    @SuppressWarnings("deprecation")
    public KickOptions fromToml(TomlConfigData.PacketOptions opts, KickOptions opt)
    {
        this.copy(opt);

        this.afkKickEnabled = opts.afkKickEnabled;
        this.afkKickNonSurvival = opts.afkKickNonSurvival;
        this.afkKickTimer = opts.afkKickTimer;
        this.afkKickSafePermissions = opts.afkKickSafePermissions;

        return this;
    }

    @SuppressWarnings("deprecation")
    public KickOptions fromToml(TomlConfigData.MessageOptions opts, KickOptions opt)
    {
        this.copy(opt);

        this.afkKickMessage = opts.afkKickMessage;
        this.whenKicked = opts.whenKicked;
        this.whenKickedDefaultReason = "<copper>AFK timeout";
        this.whenKickedDurationDefaultPrefix = " <gray>(Gone for: <green>";
        this.whenKickedDurationNamedPrefix = " <gray>(%player:displayname% was gone for: <green>";
        this.whenKickedDurationSuffix = "<gray>)";

        return this;
    }
}
