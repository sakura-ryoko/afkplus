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

package com.sakuraryoko.afkplus.text;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;

import com.sakuraryoko.afkplus.AfkPlusMod;
import com.sakuraryoko.afkplus.config.ConfigWrap;
import com.sakuraryoko.afkplus.text.nodes.type.MoreColorNode;

public class FormattingExample
{
    public static Component runBuiltInTest()
    {
        StringBuilder testString = new StringBuilder();
        testString.append("<r><bold><i><gray>*** BUILT-IN (Vanilla Formatters):\n");

        for (ChatFormatting fmt : ChatFormatting.values())
        {
            if (fmt.equals(ChatFormatting.OBFUSCATED))
            {
                //#if MC >= 11904
                //$$ testString.append("<r> <u><copy:'<").append(fmt.getName()).append(">'>").append(fmt.getName()).append(":<r> <").append(fmt.getName()).append(">").append(fmt.getName());
                //#else
                testString.append("<r> <underline><copy:'<").append(fmt.getName()).append(">'>").append(fmt.getName()).append(":<r> <").append(fmt.getName()).append(">").append(fmt.getName());
                //#endif
            }
            else if (!fmt.equals(ChatFormatting.RESET))
            {
                testString.append("<r> <copy:'<").append(fmt.getName()).append(">'><").append(fmt.getName()).append(">").append(fmt.getName());
            }
        }

        AfkPlusMod.debugLog("FormatTest.runBuiltInTest() --> testString: {}", testString.toString());
        return TextUtils.formatText(testString.toString());
    }

    public static Component runPlaceholderAPITest()
    {
        StringBuilder testString = new StringBuilder();
        testString.append("<r><b><i><rainbow>*** PlaceholderAPI (Default) nodes:\n");

        testString.append("<r> <copy:'<orange>'><orange>orange (gold alias)");
        testString.append("<r> <copy:'<pink>'><pink>pink (light_purple alias)");
        testString.append("<r> <copy:'<rainbow>'><rainbow>rainbow");
        testString.append("<r> <copy:'<gradient:#76C610:#DE8BB4>'><gradient:#76C610:#DE8BB4>gradient:#76C610:#DE8BB4");
        testString.append("<r> <green><hover:show_text:'hover text'>hover:show_text:'hover text'");
        //#if MC >= 11904
        //$$ testString.append("<r> <u><aqua><url:'https://github.io'>url:'https://github.io'");
        //#else
        testString.append("<r> <underline><aqua><url:'https://github.io'>url:'https://github.io'");
        //#endif

        AfkPlusMod.debugLog("FormatTest.runAliasTest() --> testString: {}", testString.toString());
        return TextUtils.formatText(testString.toString());
    }

    public static Component runMoreColorsTest()
    {
        StringBuilder testString = new StringBuilder();

        //#if MC >= 11904
        //$$ testString.append("<r><b><i><salmon>*** <u>USING AFKPLUS ONLY!</u>:\n");
        //#else
        testString.append("<r><b><i><salmon>*** <underline>USING AFKPLUS ONLY!</underline>:\n");
        //#endif

        for (MoreColorNode iNode : ConfigWrap.colors())
        {
            testString.append("<r> <copy:'<").append(iNode.getName()).append(">'><").append(iNode.getName()).append(">").append(iNode.getName());
        }

        //#if MC >= 11904
        //$$  testString.append("<r>\n<i><u><gray>* You can click on most of these options to copy the tag to your Clipboard ***");
        //#else
        testString.append("<r>\n<i><underline><gray>* You can click on most of these options to copy the tag to your Clipboard ***");
        //#endif

        AfkPlusMod.debugLog("FormatTest.runColorsTest() --> testString: {}", testString.toString());
        return TextUtils.formatText(testString.toString());
    }
}
