package io.github.sakuraryoko.afkplus.nodes;

import java.util.ArrayList;
import java.util.List;

import io.github.sakuraryoko.afkplus.util.AfkPlusLogger;
import org.jetbrains.annotations.Nullable;

import net.minecraft.text.TextColor;

public class MoreColorNode {
    private final String name;
    private final String hexCode;
    private List<String> aliases = new ArrayList<>();
    private final TextColor color;

    protected MoreColorNode(String name, String hexCode)
    {
        this.color = TextColor.parse(hexCode);
        if (this.color != null)
        {
            this.name = name;
            this.hexCode = hexCode;
        }
        else {
            AfkPlusLogger.warn("MoreColor("+ name +") unhandled error (color is null)");
            this.name = "";
            this.hexCode = "";
        }
    }
    protected MoreColorNode(String name, String hexCode, @Nullable List<String> aliases)
    {
        this.color = TextColor.parse(hexCode);
        if (this.color != null)
        {
            this.name = name;
            this.hexCode = hexCode;
            this.aliases = aliases;
        }
        else {
            AfkPlusLogger.warn("MoreColor("+ name +") unhandled error (color is null)");
            this.name = "";
            this.hexCode = "";
        }
    }

    protected String getName() { return this.name; }
    protected String getHexCode() { return this.hexCode; }
    @Nullable
    protected List<String> getAliases() { return this.aliases; }
    protected TextColor getColor() { return this.color; }
}
