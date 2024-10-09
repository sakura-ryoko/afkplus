package com.sakuraryoko.afkplus.compat;

import eu.pb4.placeholders.api.ParserContext;
import net.minecraft.text.Text;

import eu.pb4.placeholders.api.TextParserUtils;
import org.jetbrains.annotations.Nullable;

public class TextUtils
{
    // TODO - Remove legacy support in the future
    public static final boolean LEGACY = false;

    public static Text formatText(String str, ParserContext ctx)
    {
        return TextParser.PARSE.parseText(str, ctx);
    }

    public static Text formatText(String str)
    {
        if (LEGACY)
        {
            return TextParserUtils.formatText(str);
        }

        return TextParser.PARSE.parseNode(str).toText();
    }

    public static Text formatTextSafe(String str)
    {
        if (LEGACY)
        {
            return TextParserUtils.formatTextSafe(str);
        }

        return TextParser.PARSE.parseNode(str).toText();
    }

    public static Text of(String str)
    {
        return Text.of(str);
    }

    public static Text translate(String translateStr, @Nullable String fallback)
    {
        return Text.translatableWithFallback(translateStr, fallback);
    }
}
