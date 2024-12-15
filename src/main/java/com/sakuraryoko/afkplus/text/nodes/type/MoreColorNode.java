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

package com.sakuraryoko.afkplus.text.nodes.type;

import java.util.ArrayList;
import java.util.List;
import org.jetbrains.annotations.Nullable;

//#if MC >= 12004
//$$ import com.mojang.serialization.DataResult;
//#else
//#endif
import net.minecraft.network.chat.TextColor;

import com.sakuraryoko.afkplus.AfkPlusMod;

public class MoreColorNode
{
    private final String name;
    private final String hexCode;
    private List<String> aliases = new ArrayList<>();
    private final TextColor color;

    //#if MC >= 12004
    //$$ public MoreColorNode(String name, String hexCode)
    //$$ {
        //$$ DataResult<TextColor> dr;
        //$$ dr = TextColor.parseColor(hexCode);
        //$$ if (dr.error().isEmpty())
        //$$ {
            //$$ this.color = dr.result().orElse(null);
            //$$ if (this.color != null)
            //$$ {
                //$$ this.name = name;
                //$$ this.hexCode = hexCode;
            //$$ }
            //$$ else
            //$$ {
                //$$ AfkPlusMod.LOGGER.warn("MoreColor({}) unhandled error (color is null)", name);
                //$$ this.name = "";
                //$$ this.hexCode = "";
            //$$ }
        //$$ }
        //$$ else
        //$$ {
            //$$ AfkPlusMod.LOGGER.warn("MoreColor({}) is Invalid, error: {}", name, dr.error().toString());
            //$$ this.name = "";
            //$$ this.hexCode = "";
            //$$ this.color = null;
        //$$ }
    //$$ }

    //$$ public MoreColorNode(String name, String hexCode, @Nullable List<String> aliases)
    //$$ {
        //$$ DataResult<TextColor> dr;
        //$$ dr = TextColor.parseColor(hexCode);
        //$$ if (dr.error().isEmpty())
        //$$ {
            //$$ this.color = dr.result().orElse(null);
            //$$ if (this.color != null)
            //$$ {
                //$$ this.name = name;
                //$$ this.hexCode = hexCode;
                //$$ this.aliases = aliases;
            //$$ }
            //$$ else
            //$$ {
                //$$ AfkPlusMod.LOGGER.warn("MoreColor({}) unhandled error (color is null)", name);
                //$$ this.name = "";
                //$$ this.hexCode = "";
            //$$ }
        //$$ }
        //$$ else
        //$$ {
            //$$ AfkPlusMod.LOGGER.warn("MoreColor({}) is Invalid, error: {}", name, dr.error().toString());
            //$$ this.name = "";
            //$$ this.hexCode = "";
            //$$ this.color = null;
        //$$ }
    //$$ }
    //#else
    public MoreColorNode(String name, String hexCode)
    {
        this.color = TextColor.parseColor(hexCode);
        if (this.color != null)
        {
            this.name = name;
            this.hexCode = hexCode;
        }
        else {
            AfkPlusMod.LOGGER.warn("MoreColor({}) unhandled error (color is null)", name);
            this.name = "";
            this.hexCode = "";
        }
    }

    public MoreColorNode(String name, String hexCode, @Nullable List<String> aliases)
    {
        this.color = TextColor.parseColor(hexCode);
        if (this.color != null)
        {
            this.name = name;
            this.hexCode = hexCode;
            this.aliases = aliases;
        }
        else {
            AfkPlusMod.LOGGER.warn("MoreColor({}) unhandled error (color is null)", name);
            this.name = "";
            this.hexCode = "";
        }
    }
    //#endif

    public String getName()
    {
        return this.name;
    }

    public String getHexCode()
    {
        return this.hexCode;
    }

    @Nullable
    public List<String> getAliases()
    {
        return this.aliases;
    }

    public TextColor getColor()
    {
        return this.color;
    }
}
