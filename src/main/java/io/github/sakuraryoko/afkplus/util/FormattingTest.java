package io.github.sakuraryoko.afkplus.util;

import eu.pb4.placeholders.api.TextParserUtils;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

public class FormattingTest {
    public static Text runBuiltInTest() {
        String testString = "<r>BUILT-IN:";
        Formatting test = Formatting.byName("black");
        if (test != null)
            testString = testString + "<r>\n<black>Black: " + test.getName();

        test = Formatting.byName("dark_blue");
        if (test != null)
            testString = testString + "<r>\n<dark_blue>Dark Blue: " + test.getName();

        test = Formatting.byName("dark_green");
        if (test != null)
            testString = testString + "<r>\n<dark_green>Dark Green: " + test.getName();

        test = Formatting.byName("dark_aqua");
        if (test != null)
            testString = testString + "<r>\n<dark_aqua>Dark Aqua: " + test.getName();

        test = Formatting.byName("dark_red");
        if (test != null)
            testString = testString + "<r>\n<dark_red>Dark Red: " + test.getName();

        test = Formatting.byName("dark_purple");
        if (test != null)
            testString = testString + "<r>\n<dark_purple>Dark Purple: " + test.getName();

        test = Formatting.byName("gold");
        if (test != null)
            testString = testString + "<r>\n<gold>Gold: " + test.getName();

        test = Formatting.byName("gray");
        if (test != null)
            testString = testString + "<r>\n<gray>Gray: " + test.getName();

        test = Formatting.byName("dark_gray");
        if (test != null)
            testString = testString + "<r>\n<dark_gray>Dark Gray: " + test.getName();

        test = Formatting.byName("blue");
        if (test != null)
            testString = testString + "<r>\n<blue>Blue: " + test.getName();

        test = Formatting.byName("green");
        if (test != null)
            testString = testString + "<r>\n<green>Green: " + test.getName();

        test = Formatting.byName("aqua");
        if (test != null)
            testString = testString + "<r>\n<aqua>Aqua: " + test.getName();

        test = Formatting.byName("red");
        if (test != null)
            testString = testString + "<r>\n<red>Red: " + test.getName();

        test = Formatting.byName("light_purple");
        if (test != null)
            testString = testString + "<r>\n<light_purple>Light Purple: " + test.getName();

        test = Formatting.byName("yellow");
        if (test != null)
            testString = testString + "<r>\n<yellow>Yellow: " + test.getName();

        test = Formatting.byName("white");
        if (test != null)
            testString = testString + "<r>\n<white>White: " + test.getName();

        test = Formatting.byName("obfuscated");
        if (test != null)
            testString = testString + "<r>\n<obfuscated>OBFUSCATED: " + test.getName();

        test = Formatting.byName("bold");
        if (test != null)
            testString = testString + "<r>\n<bold>Bold: " + test.getName();

        test = Formatting.byName("strikethrough");
        if (test != null)
            testString = testString + "<r>\n<strikethrough>Strikethrough: " + test.getName();

        test = Formatting.byName("underline");
        if (test != null)
            testString = testString + "<r>\n<underline>Underline: " + test.getName();

        test = Formatting.byName("italic");
        if (test != null)
            testString = testString + "<r>\n<italic>Italic: " + test.getName();

        AfkPlusLogger.debug("FormatTest.runBuiltInTest() --> testString: " + testString);
        return TextParserUtils.formatTextSafe(testString);
    }
    public static Text runAliasTest() {
        String testString = "<r>ALIASES:";
        testString = testString + "<r>\n<orange>Orange: orange";
        testString = testString + "<r>\n<grey>Grey: grey";
        testString = testString + "<r>\n<pink>Pink: pink";
        testString = testString + "<r>\n<dark_grey>Dark Grey: dark_grey";
        AfkPlusLogger.debug("FormatTest.runAliasTest() --> testString: " + testString);
        return TextParserUtils.formatTextSafe(testString);
    }
    public static Text runColorsTest() {
        String testString = "<r>COLORS:";
        Text out;
        testString = testString + "<r>\n<brown>Brown: brown";
        testString = testString + "<r>\n<cyan>Cyan: cyan";
        testString = testString + "<r>\n<dark_brown>Dark Brown: dark_brown";
        testString = testString + "<r>\n<dark_pink>Dark Pink: dark_pink";
        testString = testString + "<r>\n<light_blue>Light Blue: light_blue";
        testString = testString + "<r>\n<light_gray>Light Gray: light_gray";
        testString = testString + "<r>\n<light_pink>Light Pink: light_pink";
        testString = testString + "<r>\n<lime>Lime: lime";
        testString = testString + "<r>\n<magenta>Magenta: magenta";
        testString = testString + "<r>\n<orange>Orange: orange";
        testString = testString + "<r>\n<pink>Pink: pink";
        testString = testString + "<r>\n<purple>Purple: purple";
        testString = testString + "<r>\n<ugly>Some Ugly Random Color: ugly";
        AfkPlusLogger.debug("FormatTest.runColorsTest() --> testString: " + testString);
        out = TextParserUtils.formatTextSafe(testString);
        AfkPlusLogger.debug("FormatTest.runColorsTest() --> out: " + out.getString());
        return out;
    }
}
