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

package com.sakuraryoko.afkplus.text.nodes;

import java.util.ArrayList;
import java.util.List;

import com.sakuraryoko.afkplus.text.nodes.type.MoreColorNode;

public class MoreColorNodes
{
    public static List<MoreColorNode> COLORS = new ArrayList<>();

    static void register()
    {
        COLORS.add(new MoreColorNode("aztec_gold", "#C39953"));
        COLORS.add(new MoreColorNode("bluetiful", "#3C69E7", List.of("blue2")));
        COLORS.add(new MoreColorNode("brown", "#632C04"));
        COLORS.add(new MoreColorNode("burnt_orange", "#FF7034", List.of("orange2")));
        COLORS.add(new MoreColorNode("canary", "#FFFF99", List.of("yellow2")));
        COLORS.add(new MoreColorNode("cool_mint", "#DDEBEC"));
        COLORS.add(new MoreColorNode("copper", "#DA8A67"));
        COLORS.add(new MoreColorNode("cotton_candy", "#FFB7D5"));
        COLORS.add(new MoreColorNode("cyan", "#2D7C9D"));
        COLORS.add(new MoreColorNode("dark_brown", "#421F05"));
        COLORS.add(new MoreColorNode("dark_pink", "#DE8BB4"));
        COLORS.add(new MoreColorNode("lavender", "#BF8FCC"));
        COLORS.add(new MoreColorNode("light_blue", "#82ACE7"));
        COLORS.add(new MoreColorNode("light_brown", "#7A4621"));
        COLORS.add(new MoreColorNode("light_gray", "#BABAC1", List.of("light_grey")));
        COLORS.add(new MoreColorNode("light_pink", "#F7B4D6"));
        COLORS.add(new MoreColorNode("lime", "#76C610"));
        COLORS.add(new MoreColorNode("magenta", "#CB69C5"));
        COLORS.add(new MoreColorNode("maroon", "#C32148"));
        //COLORS.add(new MoreColorNode("orange","#E69E34"));
        //COLORS.add(new MoreColorNode("pink","#EDA7CB"));
        COLORS.add(new MoreColorNode("pacific_blue", "#009DC4"));
        COLORS.add(new MoreColorNode("peach", "#FFCBA4"));
        COLORS.add(new MoreColorNode("plum", "#843179"));
        COLORS.add(new MoreColorNode("powder_blue", "#C0D5F0"));
        COLORS.add(new MoreColorNode("purple", "#A453CE"));
        COLORS.add(new MoreColorNode("royal_purple", "#6B3FA0"));
        COLORS.add(new MoreColorNode("salmon", "#FF91A4", List.of("pink_salmon")));
        COLORS.add(new MoreColorNode("scarlet", "#FD0E35"));
        COLORS.add(new MoreColorNode("sea_green", "#93DFB8"));
        COLORS.add(new MoreColorNode("sepia", "#9E5B40"));
        COLORS.add(new MoreColorNode("shamrock", "#33CC99"));
        COLORS.add(new MoreColorNode("sunset_orange", "#FE4C40"));
        COLORS.add(new MoreColorNode("tickle_me_pink", "#FC80A5"));
        COLORS.add(new MoreColorNode("timberwolf", "#D9D6CF"));
        COLORS.add(new MoreColorNode("ultramarine_blue", "#3F26BF", List.of("ultramarine")));
    }
}
