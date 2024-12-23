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

package com.sakuraryoko.afkplus.text.config;

import java.util.ArrayList;
import java.util.List;
import com.google.gson.annotations.SerializedName;

import com.sakuraryoko.afkplus.config.interfaces.IConfigData;
import com.sakuraryoko.afkplus.text.nodes.type.MoreColorNode;

public class MoreColorData implements IConfigData
{
    @SerializedName("___comment")
    public String comment = "AFK Plus (More Color Nodes) Config";

    @SerializedName("config_date")
    public String config_date;

    @SerializedName("more_colors")
    public List<MoreColorNode> COLORS = new ArrayList<>();
}
