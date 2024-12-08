package com.sakuraryoko.afkplus.compat;

import eu.pb4.placeholders.api.ParserContext;
import eu.pb4.placeholders.api.TextParserUtils;
import org.jetbrains.annotations.Nullable;

import net.minecraft.network.chat.Component;

public class TextUtils
{
    // TODO - Remove legacy support in the future
    public static final boolean LEGACY = true;

    public static Component formatText(String str, ParserContext ctx)
    {
        return TextParser.PARSE.parseText(str, ctx);
    }

    public static Component formatText(String str)
    {
        if (LEGACY)
        {
            return TextParserUtils.formatText(str);
        }

        return TextParser.PARSE.parseNode(str).toText();
    }

    public static Component formatTextSafe(String str)
    {
        if (LEGACY)
        {
            return TextParserUtils.formatTextSafe(str);
        }

        return TextParser.PARSE.parseNode(str).toText();
    }

    public static Component of(String str)
    {
        return Component.literal(str);
    }

    public static Component translate(String translateStr, @Nullable String fallback)
    {
        return Component.translatableWithFallback(translateStr, fallback);
    }
}
