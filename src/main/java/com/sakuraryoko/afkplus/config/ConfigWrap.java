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

package com.sakuraryoko.afkplus.config;

import java.util.List;

import com.sakuraryoko.afkplus.config.data.options.*;
import com.sakuraryoko.afkplus.text.config.MoreColorConfigHandler;
import com.sakuraryoko.afkplus.text.nodes.type.MoreColorNode;

public class ConfigWrap
{
    public static AfkPlusOptions afk()
    {
        return AfkConfigHandler.getInstance().getAfkPlusOptions();
    }

    public static MessageOptions mess()
    {
        return AfkConfigHandler.getInstance().getMessageOptions();
    }

    public static PacketOptions pack()
    {
        return AfkConfigHandler.getInstance().getPacketOptions();
    }

    public static PlaceholderOptions place()
    {
        return AfkConfigHandler.getInstance().getPlaceholderOptions();
    }

    public static PlayerListOptions list()
    {
        return AfkConfigHandler.getInstance().getPlayerListOptions();
    }

    public static List<MoreColorNode> colors()
    {
        return MoreColorConfigHandler.getInstance().getColors();
    }
}
