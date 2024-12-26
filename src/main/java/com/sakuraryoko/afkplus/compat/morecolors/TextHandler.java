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

package com.sakuraryoko.afkplus.compat.morecolors;

import javax.annotation.Nonnull;

import net.minecraft.network.chat.Component;

import com.sakuraryoko.corelib.api.text.ITextHandler;
import com.sakuraryoko.morecolors.api.MoreColorsAPI;

public class TextHandler implements ITextHandler
{
    private static final TextHandler INSTANCE = new TextHandler();
    public static TextHandler getInstance() { return INSTANCE; }

    public Component formatTextSafe(@Nonnull String str)
    {
        return MoreColorsAPI.formatTextSafe(str);
    }

    public Component formatText(@Nonnull String str)
    {
        return MoreColorsAPI.formatText(str);
    }

    public Component of(@Nonnull String str)
    {
        return MoreColorsAPI.of(str);
    }
}