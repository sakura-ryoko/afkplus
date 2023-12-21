package io.github.sakuraryoko.afkplus.util;

import eu.pb4.placeholders.api.TextParserUtils;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

public class FormattingTest {
    public static Text runTest() {
        String testString;
        Formatting test = Formatting.byName("black");
        testString = "<black>Black: " + test.getName();

        test = Formatting.byName("dark_blue");
        testString = testString + "<r>\n<dark_blue>Dark Blue: " + test.getName();

        test = Formatting.byName("dark_green");
        testString = testString + "<r>\n<dark_green>Dark Green: " + test.getName();

        test = Formatting.byName("dark_aqua");
        testString = testString + "<r>\n<dark_aqua>Dark Aqua: " + test.getName();

        test = Formatting.byName("dark_red");
        testString = testString + "<r>\n<dark_red>Dark Red: " + test.getName();

        test = Formatting.byName("dark_purple");
        testString = testString + "<r>\n<dark_purple>Dark Purple: " + test.getName();

        test = Formatting.byName("gold");
        testString = testString + "<r>\n<gold>Gold: " + test.getName();

        test = Formatting.byName("gray");
        testString = testString + "<r>\n<gray>Gray: " + test.getName();

        test = Formatting.byName("dark_gray");
        testString = testString + "<r>\n<dark_gray>Dark Gray: " + test.getName();

        test = Formatting.byName("blue");
        testString = testString + "<r>\n<blue>Blue: " + test.getName();

        test = Formatting.byName("green");
        testString = testString + "<r>\n<green>Green: " + test.getName();

        test = Formatting.byName("aqua");
        testString = testString + "<r>\n<aqua>Aqua: " + test.getName();

        test = Formatting.byName("red");
        testString = testString + "<r>\n<red>Red: " + test.getName();

        test = Formatting.byName("light_purple");
        testString = testString + "<r>\n<light_purple>Light Purple: " + test.getName();

        test = Formatting.byName("yellow");
        testString = testString + "<r>\n<yellow>Yellow: " + test.getName();

        test = Formatting.byName("white");
        testString = testString + "<r>\n<white>White: " + test.getName();

        test = Formatting.byName("obfuscated");
        testString = testString + "<r>\n<obfuscated>OBFUSCATED: " + test.getName();

        test = Formatting.byName("bold");
        testString = testString + "<r>\n<bold>Bold: " + test.getName();

        test = Formatting.byName("strikethrough");
        testString = testString + "<r>\n<strikethrough>Strikethrough: " + test.getName();

        test = Formatting.byName("underline");
        testString = testString + "<r>\n<underline>Underline: " + test.getName();

        test = Formatting.byName("italic");
        testString = testString + "<r>\n<italic>Italic: " + test.getName();

        return TextParserUtils.formatTextSafe(testString);
    }
}
