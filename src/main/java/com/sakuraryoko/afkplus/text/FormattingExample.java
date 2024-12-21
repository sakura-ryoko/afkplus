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

public class FormattingExample
{
    public static Component runBuiltInTest()
    {
        String testString = "<r><bold><i><dark_gray>*** BUILT-IN (Vanilla Formatters):";
        ChatFormatting test = ChatFormatting.getByName("black");
        if (test != null)
        {
            testString = testString + "<r>\n<copy:'<black>'><black>" + test.getName();
        }

        test = ChatFormatting.getByName("dark_blue");
        if (test != null)
        {
            testString = testString + "<r> <copy:'<dark_blue>'><dark_blue>" + test.getName();
        }

        test = ChatFormatting.getByName("dark_green");
        if (test != null)
        {
            testString = testString + "<r> <copy:'<dark_green>'><dark_green>" + test.getName();
        }

        test = ChatFormatting.getByName("dark_aqua");
        if (test != null)
        {
            testString = testString + "<r> <copy:'<dark_aqua>'><dark_aqua>" + test.getName();
        }

        test = ChatFormatting.getByName("dark_red");
        if (test != null)
        {
            testString = testString + "<r> <copy:'<dark_red>'><dark_red>" + test.getName();
        }

        test = ChatFormatting.getByName("dark_purple");
        if (test != null)
        {
            testString = testString + "<r> <copy:'<dark_purple>'><dark_purple>" + test.getName();
        }

        test = ChatFormatting.getByName("gold");
        if (test != null)
        {
            testString = testString + "<r> <copy:'<gold>'><gold>" + test.getName();
        }

        test = ChatFormatting.getByName("gray");
        if (test != null)
        {
            testString = testString + "<r> <copy:'<gray>'><gray>" + test.getName();
        }

        test = ChatFormatting.getByName("dark_gray");
        if (test != null)
        {
            testString = testString + "<r> <copy:'<dark_gray>'><dark_gray>" + test.getName();
        }

        test = ChatFormatting.getByName("blue");
        if (test != null)
        {
            testString = testString + "<r> <copy:'<blue>'><blue>" + test.getName();
        }

        test = ChatFormatting.getByName("green");
        if (test != null)
        {
            testString = testString + "<r> <copy:'<green>'><green>" + test.getName();
        }

        test = ChatFormatting.getByName("aqua");
        if (test != null)
        {
            testString = testString + "<r> <copy:'<aqua>'><aqua>" + test.getName();
        }

        test = ChatFormatting.getByName("red");
        if (test != null)
        {
            testString = testString + "<r> <copy:'<red>'><red>" + test.getName();
        }

        test = ChatFormatting.getByName("light_purple");
        if (test != null)
        {
            testString = testString + "<r> <copy:'<light_purple>'><light_purple>" + test.getName();
        }

        test = ChatFormatting.getByName("yellow");
        if (test != null)
        {
            testString = testString + "<r> <copy:'<yellow>'><yellow>" + test.getName();
        }

        test = ChatFormatting.getByName("white");
        if (test != null)
        {
            testString = testString + "<r> <copy:'<white>'><white>" + test.getName();
        }

        test = ChatFormatting.getByName("obfuscated");
        if (test != null)
        {
            //#if MC >= 11904
            //$$ testString = testString + "<r>\n<u><copy:'<obfuscated>'>obfuscated:<r> <obfuscated>" + test.getName();
            //#else
            testString = testString + "<r>\n<underline><copy:'<obfuscated>'>obfuscated:<r> <obfuscated>" + test.getName();
            //#endif
        }

        test = ChatFormatting.getByName("bold");
        if (test != null)
        {
            testString = testString + "<r> <copy:'<bold>'><bold>" + test.getName();
        }

        test = ChatFormatting.getByName("strikethrough");
        if (test != null)
        {
            testString = testString + "<r> <copy:'<strikethrough>'><strikethrough>" + test.getName();
        }

        test = ChatFormatting.getByName("underline");
        if (test != null)
        {
            testString = testString + "<r> <copy:'<underline>'><underline>" + test.getName();
        }

        test = ChatFormatting.getByName("italic");
        if (test != null)
        {
            testString = testString + "<r> <copy:'<italic>'><italic>" + test.getName();
        }

        AfkPlusMod.debugLog("FormatTest.runBuiltInTest() --> testString: {}", testString);
        return TextUtils.formatText(testString);
    }

    public static Component runAliasTest()
    {
        String testString = "<r><b><i><rainbow>*** PlaceholderAPI (Default) nodes:";
        testString = testString + "<r>\n<copy:'<orange>'><orange>orange (gold alias)";
        testString = testString + "<r> <copy:'<pink>'><pink>pink (light_purple alias)";
        testString = testString + "<r> <copy:'<rainbow>'><rainbow>rainbow";
        testString = testString + "<r> <copy:'<gradient:#76C610:#DE8BB4>'><gradient:#76C610:#DE8BB4>gradient:#76C610:#DE8BB4";
        testString = testString + "<r>\n<green><hover:show_text:'hover text'>hover:show_text:'hover text'";
        //#if MC >= 11904
        //$$ testString = testString + "<r> <u><aqua><url:'https://github.io'>url:'https://github.io'";
        //#else
        testString = testString + "<r> <underline><aqua><url:'https://github.io'>url:'https://github.io'";
        //#endif

        AfkPlusMod.debugLog("FormatTest.runAliasTest() --> testString: {}", testString);
        return TextUtils.formatText(testString);
    }

    public static Component runColorsTest()
    {
        //#if MC >= 11904
        //$$ String testString = "<r><b><i><salmon>*** <u>AFKPLUS ONLY!</u>:";
        //#else
        String testString = "<r><b><i><salmon>*** <underline>AFKPLUS ONLY!</underline>:";
        //#endif
        testString = testString + "<r>\n<copy:'<aztec_gold>'><aztec_gold>aztec_gold";
        testString = testString + "<r> <copy:'<bluetiful>'><bluetiful>bluetiful";
        testString = testString + "<r> <copy:'<brown>'><brown>brown";
        testString = testString + "<r> <copy:'<burnt_orange>'><burnt_orange>burnt_orange";
        testString = testString + "<r> <copy:'<canary>'><canary>canary";
        testString = testString + "<r> <copy:'<cool_mint>'><cool_mint>cool_mint";
        testString = testString + "<r> <copy:'<copper>'><copper>copper";
        testString = testString + "<r> <copy:'<cotton_candy>'><cotton_candy>cotton_candy";
        testString = testString + "<r> <copy:'<cyan>'><cyan>cyan";
        testString = testString + "<r> <copy:'<dark_brown>'><dark_brown>dark_brown";
        testString = testString + "<r> <copy:'<dark_pink>'><dark_pink>dark_pink";
        testString = testString + "<r> <copy:'<lavender>'><lavender>lavender";
        testString = testString + "<r> <copy:'<light_blue>'><light_blue>light_blue";
        testString = testString + "<r> <copy:'<light_brown>'><light_brown>light_brown";
        testString = testString + "<r> <copy:'<light_gray>'><light_gray>light_gray";
        testString = testString + "<r> <copy:'<light_pink>'><light_pink>light_pink";
        testString = testString + "<r> <copy:'<lime>'><lime>lime";
        testString = testString + "<r> <copy:'<magenta>'><magenta>magenta";
        testString = testString + "<r> <copy:'<maroon>'><maroon>maroon";
        testString = testString + "<r> <copy:'<pacific_blue>'><pacific_blue>pacific_blue";
        testString = testString + "<r> <copy:'<peach>'><peach>peach";
        testString = testString + "<r> <copy:'<plum>'><plum>plum";
        testString = testString + "<r> <copy:'<powder_blue>'><powder_blue>powder_blue";
        testString = testString + "<r> <copy:'<purple>'><purple>purple";
        testString = testString + "<r> <copy:'<royal_purple>'><royal_purple>royal_purple";
        testString = testString + "<r> <copy:'<salmon>'><salmon>salmon";
        testString = testString + "<r> <copy:'<scarlet>'><scarlet>scarlet";
        testString = testString + "<r> <copy:'<sea_green>'><sea_green>sea_green";
        testString = testString + "<r> <copy:'<sepia>'><sepia>sepia";
        testString = testString + "<r> <copy:'<shamrock>'><shamrock>shamrock";
        testString = testString + "<r> <copy:'<sunset_orange>'><sunset_orange>sunset_orange";
        testString = testString + "<r> <copy:'<tickle_me_pink>'><tickle_me_pink>tickle_me_pink";
        testString = testString + "<r> <copy:'<timberwolf>'><timberwolf>timberwolf";
        testString = testString + "<r> <copy:'<ultramarine_blue>'><ultramarine_blue>ultramarine_blue";
        //#if MC >= 11904
        //$$ testString = testString + "<r>\n<i><u><gray>* You can click on most of these options to copy the tag to your Clipboard ***";
        //#else
        testString = testString + "<r>\n<i><underline><gray>* You can click on most of these options to copy the tag to your Clipboard ***";
        //#endif

        AfkPlusMod.debugLog("FormatTest.runColorsTest() --> testString: {}", testString);
        return TextUtils.formatText(testString);
    }
}
