package com.sakuraryoko.afkplus.util;

import com.sakuraryoko.afkplus.compat.TextUtils;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

public class FormattingExample
{
    public static Text runBuiltInTest()
    {
        String testString = "<r><bold><i><dark_gray>*** BUILT-IN (Vanilla Formatters):";
        Formatting test = Formatting.byName("black");
        if (test != null)
        {
            testString = testString + "<r>\n<copy:'<black>'><black>" + test.getName();
        }

        test = Formatting.byName("dark_blue");
        if (test != null)
        {
            testString = testString + "<r> <copy:'<dark_blue>'><dark_blue>" + test.getName();
        }

        test = Formatting.byName("dark_green");
        if (test != null)
        {
            testString = testString + "<r> <copy:'<dark_green>'><dark_green>" + test.getName();
        }

        test = Formatting.byName("dark_aqua");
        if (test != null)
        {
            testString = testString + "<r> <copy:'<dark_aqua>'><dark_aqua>" + test.getName();
        }

        test = Formatting.byName("dark_red");
        if (test != null)
        {
            testString = testString + "<r> <copy:'<dark_red>'><dark_red>" + test.getName();
        }

        test = Formatting.byName("dark_purple");
        if (test != null)
        {
            testString = testString + "<r> <copy:'<dark_purple>'><dark_purple>" + test.getName();
        }

        test = Formatting.byName("gold");
        if (test != null)
        {
            testString = testString + "<r> <copy:'<gold>'><gold>" + test.getName();
        }

        test = Formatting.byName("gray");
        if (test != null)
        {
            testString = testString + "<r> <copy:'<gray>'><gray>" + test.getName();
        }

        test = Formatting.byName("dark_gray");
        if (test != null)
        {
            testString = testString + "<r> <copy:'<dark_gray>'><dark_gray>" + test.getName();
        }

        test = Formatting.byName("blue");
        if (test != null)
        {
            testString = testString + "<r> <copy:'<blue>'><blue>" + test.getName();
        }

        test = Formatting.byName("green");
        if (test != null)
        {
            testString = testString + "<r> <copy:'<green>'><green>" + test.getName();
        }

        test = Formatting.byName("aqua");
        if (test != null)
        {
            testString = testString + "<r> <copy:'<aqua>'><aqua>" + test.getName();
        }

        test = Formatting.byName("red");
        if (test != null)
        {
            testString = testString + "<r> <copy:'<red>'><red>" + test.getName();
        }

        test = Formatting.byName("light_purple");
        if (test != null)
        {
            testString = testString + "<r> <copy:'<light_purple>'><light_purple>" + test.getName();
        }

        test = Formatting.byName("yellow");
        if (test != null)
        {
            testString = testString + "<r> <copy:'<yellow>'><yellow>" + test.getName();
        }

        test = Formatting.byName("white");
        if (test != null)
        {
            testString = testString + "<r> <copy:'<white>'><white>" + test.getName();
        }

        test = Formatting.byName("obfuscated");
        if (test != null)
        {
            testString = testString + "<r>\n<u><copy:'<obfuscated>'>obfuscated:<r> <obfuscated>" + test.getName();
        }

        test = Formatting.byName("bold");
        if (test != null)
        {
            testString = testString + "<r> <copy:'<bold>'><bold>" + test.getName();
        }

        test = Formatting.byName("strikethrough");
        if (test != null)
        {
            testString = testString + "<r> <copy:'<strikethrough>'><strikethrough>" + test.getName();
        }

        test = Formatting.byName("underline");
        if (test != null)
        {
            testString = testString + "<r> <copy:'<underline>'><underline>" + test.getName();
        }

        test = Formatting.byName("italic");
        if (test != null)
        {
            testString = testString + "<r> <copy:'<italic>'><italic>" + test.getName();
        }

        AfkPlusLogger.debug("FormatTest.runBuiltInTest() --> testString: " + testString);
        return TextUtils.formatText(testString);
    }

    public static Text runAliasTest()
    {
        String testString = "<r><b><i><rainbow>*** PlaceholderAPI (Default) nodes:";
        testString = testString + "<r>\n<copy:'<orange>'><orange>orange (gold alias)";
        testString = testString + "<r> <copy:'<pink>'><pink>pink (light_purple alias)";
        testString = testString + "<r> <copy:'<rainbow>'><rainbow>rainbow";
        testString = testString + "<r> <copy:'<gradient:#76C610:#DE8BB4>'><gradient:#76C610:#DE8BB4>gradient:#76C610:#DE8BB4";
        testString = testString + "<r>\n<green><hover:show_text:'hover text'>hover:show_text:'hover text'";
        testString = testString + "<r> <u><aqua><url:'https://github.io'>url:'https://github.io'";

        AfkPlusLogger.debug("FormatTest.runAliasTest() --> testString: " + testString);
        return TextUtils.formatText(testString);
    }

    public static Text runColorsTest()
    {
        String testString = "<r><b><i><salmon>*** <u>AFKPLUS ONLY!</u>:";
        testString = testString + "<r>\n<copy:'<bluetiful>'><bluetiful>bluetiful";
        testString = testString + "<r> <copy:'<brown>'><brown>brown";
        testString = testString + "<r> <copy:'<burnt_orange>'><burnt_orange>burnt_orange";
        testString = testString + "<r> <copy:'<canary>'><canary>canary";
        testString = testString + "<r> <copy:'<cool_mint>'><cool_mint>cool_mint";
        testString = testString + "<r> <copy:'<copper>'><copper>copper";
        testString = testString + "<r> <copy:'<cyan>'><cyan>cyan";
        testString = testString + "<r> <copy:'<dark_brown>'><dark_brown>dark_brown";
        testString = testString + "<r> <copy:'<dark_pink>'><dark_pink>dark_pink";
        testString = testString + "<r> <copy:'<light_blue>'><light_blue>light_blue";
        testString = testString + "<r> <copy:'<light_brown>'><light_brown>light_brown";
        testString = testString + "<r> <copy:'<light_gray>'><light_gray>light_gray";
        testString = testString + "<r> <copy:'<light_pink>'><light_pink>light_pink";
        testString = testString + "<r> <copy:'<lime>'><lime>lime";
        testString = testString + "<r> <copy:'<magenta>'><magenta>magenta";
        testString = testString + "<r> <copy:'<powder_blue>'><powder_blue>powder_blue";
        testString = testString + "<r> <copy:'<purple>'><purple>purple";
        testString = testString + "<r> <copy:'<royal_purple>'><royal_purple>royal_purple";
        testString = testString + "<r> <copy:'<salmon>'><salmon>salmon";
        testString = testString + "<r> <copy:'<shamrock>'><shamrock>shamrock";
        testString = testString + "<r> <copy:'<tickle_me_pink>'><tickle_me_pink>tickle_me_pink";
        testString = testString + "<r> <copy:'<ultramarine_blue>'><ultramarine_blue>ultramarine_blue";
        testString = testString + "<r>\n<i><u><gray>* You can click on most of these options to copy the tag to your Clipboard ***";

        AfkPlusLogger.debug("FormatTest.runColorsTest() --> testString: " + testString);
        return TextUtils.formatText(testString);
    }
}
