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

//#if MC >= 12006
//$$ import eu.pb4.placeholders.api.ParserContext;
//#else
//#endif
import eu.pb4.placeholders.api.TextParserUtils;
import org.jetbrains.annotations.Nullable;

import net.minecraft.network.chat.Component;

//#if MC >= 12006
//$$ @SuppressWarnings("deprecation")
//#else
//#endif
public class TextUtils
{
    // TODO - Remove legacy support in the future
    public static final boolean LEGACY = true;

    //#if MC >= 12006
    //$$ public static Component formatText(String str, ParserContext ctx)
    //$$ {
        //$$ return TextParser.PARSE.parseText(str, ctx);
    //$$ }
    //#else
    //#endif

    public static Component formatText(String str)
    {
        if (LEGACY)
        {
            return TextParserUtils.formatText(str);
        }

        //#if MC >= 12006
        //$$ return TextParser.PARSE.parseNode(str).toText();
        //#else
        return TextParserUtils.formatText(str);
        //#endif
    }

    public static Component formatTextSafe(String str)
    {
        if (LEGACY)
        {
            return TextParserUtils.formatTextSafe(str);
        }

        //#if MC >= 12006
        //$$ return TextParser.PARSE.parseNode(str).toText();
        //#else
        return TextParserUtils.formatTextSafe(str);
        //#endif
    }

    public static Component of(String str)
    {
        return Component.literal(str);
    }

    public static Component translate(String translateStr, @Nullable String fallback)
    {
        //#if MC > 11903
        //$$ return Component.translatableWithFallback(translateStr, fallback);
        //#else
        return Component.translatable(translateStr);
        //#endif
    }
}
