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

package com.sakuraryoko.afkplus.config.data;

import com.google.gson.annotations.SerializedName;

import com.sakuraryoko.afkplus.config.data.options.*;
import com.sakuraryoko.afkplus.config.interfaces.IConfigData;

public class AfkConfigData implements IConfigData
{
    @SerializedName("___comment")
    public String comment = "AFK Plus Config";

    @SerializedName("config_date")
    public String config_date;

    @SerializedName("afk_plus")
    public AfkPlusOptions AFK_PLUS = new AfkPlusOptions();

    @SerializedName("packet")
    public PacketOptions PACKET = new PacketOptions();

    @SerializedName("player_list")
    public PlayerListOptions PLAYER_LIST = new PlayerListOptions();

    @SerializedName("place_holder")
    public PlaceholderOptions PLACEHOLDER = new PlaceholderOptions();

    @SerializedName("message")
    public MessageOptions MESSAGE = new MessageOptions();
}
