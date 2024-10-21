package com.sakuraryoko.afkplus.nodes;

import java.util.ArrayList;
import java.util.List;
import org.jetbrains.annotations.Nullable;

import com.mojang.serialization.DataResult;
import net.minecraft.network.chat.TextColor;

import com.sakuraryoko.afkplus.util.AfkPlusLogger;

public class MoreColorNode
{
    private final String name;
    private final String hexCode;
    private List<String> aliases = new ArrayList<>();
    private TextColor color;

    protected MoreColorNode(String name, String hexCode)
    {
        DataResult<TextColor> dr;
        dr = TextColor.parseColor(hexCode);
        if (dr.error().isEmpty())
        {
            this.color = dr.result().orElse(null);
            if (this.color != null)
            {
                this.name = name;
                this.hexCode = hexCode;
            }
            else
            {
                AfkPlusLogger.warn("MoreColor(" + name + ") unhandled error (color is null)");
                this.name = "";
                this.hexCode = "";
            }
        }
        else
        {
            AfkPlusLogger.warn("MoreColor(" + name + ") is Invalid, error: " + dr.error().toString());
            this.name = "";
            this.hexCode = "";
        }
    }

    protected MoreColorNode(String name, String hexCode, @Nullable List<String> aliases)
    {
        DataResult<TextColor> dr;
        dr = TextColor.parseColor(hexCode);
        if (dr.error().isEmpty())
        {
            this.color = dr.result().orElse(null);
            if (this.color != null)
            {
                this.name = name;
                this.hexCode = hexCode;
                this.aliases = aliases;
            }
            else
            {
                AfkPlusLogger.warn("MoreColor(" + name + ") unhandled error (color is null)");
                this.name = "";
                this.hexCode = "";
            }
        }
        else
        {
            AfkPlusLogger.warn("MoreColor(" + name + ") is Invalid, error: " + dr.error().toString());
            this.name = "";
            this.hexCode = "";
        }
    }

    protected String getName()
    {
        return this.name;
    }

    protected String getHexCode()
    {
        return this.hexCode;
    }

    @Nullable
    protected List<String> getAliases()
    {
        return this.aliases;
    }

    protected TextColor getColor()
    {
        return this.color;
    }
}
