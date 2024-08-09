package com.sakuraryoko.afkplus.compat;

import net.minecraft.text.Text;

import eu.pb4.placeholders.api.TextParserUtils;
import org.jetbrains.annotations.Nullable;

public class TextUtils
{
    //private static final TagParser SAFE = TagParser.QUICK_TEXT_WITH_STF_SAFE;
    //private static final TagParser DEFAULT = TagParser.QUICK_TEXT_WITH_STF;

    public static Text formatText(String str)
    {
        return TextParserUtils.formatText(str);
        //return DEFAULT.parseText(str, ParserContext.of());
    }

    public static Text formatTextSafe(String str)
    {
        return TextParserUtils.formatTextSafe(str);
        //return SAFE.parseText(str, ParserContext.of());
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
