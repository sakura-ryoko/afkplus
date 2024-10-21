package com.sakuraryoko.afkplus.nodes;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import eu.pb4.placeholders.api.node.TextNode;
import eu.pb4.placeholders.api.node.parent.ColorNode;
import eu.pb4.placeholders.api.parsers.TextParserV1;
import eu.pb4.placeholders.api.parsers.tag.TagRegistry;
import eu.pb4.placeholders.api.parsers.tag.TextTag;
import eu.pb4.placeholders.impl.textparser.TextParserImpl;

import net.minecraft.network.chat.TextColor;

import com.sakuraryoko.afkplus.compat.TextUtils;
import com.sakuraryoko.afkplus.util.AfkPlusLogger;

public class NodeManager
{
    public static List<MoreColorNode> COLORS = new ArrayList<>();

    private static void initColors()
    {
        COLORS.add(new MoreColorNode("bluetiful", "#3C69E7", List.of("blue2")));
        COLORS.add(new MoreColorNode("brown", "#632C04"));
        COLORS.add(new MoreColorNode("burnt_orange", "#FF7034", List.of("orange2")));
        COLORS.add(new MoreColorNode("canary", "#FFFF99", List.of("yellow2")));
        COLORS.add(new MoreColorNode("cool_mint", "#DDEBEC"));
        COLORS.add(new MoreColorNode("copper", "#DA8A67"));
        COLORS.add(new MoreColorNode("cyan", "#2D7C9D"));
        COLORS.add(new MoreColorNode("dark_brown", "#421F05"));
        COLORS.add(new MoreColorNode("dark_pink", "#DE8BB4"));
        COLORS.add(new MoreColorNode("light_blue", "#82ACE7"));
        COLORS.add(new MoreColorNode("light_brown", "#7A4621"));
        COLORS.add(new MoreColorNode("light_gray", "#BABAC1", List.of("light_grey")));
        COLORS.add(new MoreColorNode("light_pink", "#F7B4D6"));
        COLORS.add(new MoreColorNode("lime", "#76C610"));
        COLORS.add(new MoreColorNode("magenta", "#CB69C5"));
        //COLORS.add(new MoreColorNode("orange","#E69E34"));
        //COLORS.add(new MoreColorNode("pink","#EDA7CB"));
        COLORS.add(new MoreColorNode("powder_blue", "#C0D5F0"));
        COLORS.add(new MoreColorNode("purple", "#A453CE"));
        COLORS.add(new MoreColorNode("royal_purple", "#6B3FA0"));
        COLORS.add(new MoreColorNode("salmon", "#FF91A4", List.of("pink_salmon")));
        COLORS.add(new MoreColorNode("shamrock", "#33CC99"));
        COLORS.add(new MoreColorNode("tickle_me_pink", "#FC80A5"));
        COLORS.add(new MoreColorNode("ultramarine_blue", "#3F26BF", List.of("ultramarine")));
    }

    private static void registerColors()
    {
        final Iterator<MoreColorNode> iterator = COLORS.iterator();
        MoreColorNode iColorNode;
        while (iterator.hasNext())
        {
            iColorNode = iterator.next();
            // DataResult checked at initialization
            AfkPlusLogger.debug("registerColors(): register ColorNode: " + iColorNode.getName() + " // " + iColorNode.getHexCode());
            TextColor finalIColorNode = iColorNode.getColor();
            if (iColorNode.getAliases() != null)
            {
                if (TextUtils.LEGACY)
                {
                    // Legacy Parser
                    TextParserV1.registerDefault(
                            TextParserV1.TextTag.of(
                                    iColorNode.getName(),
                                    iColorNode.getAliases(),
                                    "color",
                                    true,
                                    wrap((nodes, arg) -> new ColorNode(nodes, finalIColorNode))
                            )
                    );
                }
                // New Code
                TagRegistry.registerDefault(
                        TextTag.enclosing(
                                iColorNode.getName(),
                                iColorNode.getAliases(),
                                "color",
                                true,
                                (nodes, data, parser) -> new ColorNode(nodes, finalIColorNode)
                        )
                );
            }
            else
            {
                if (TextUtils.LEGACY)
                {
                    // Legacy Parser
                    TextParserV1.registerDefault(
                            TextParserV1.TextTag.of(
                                    iColorNode.getName(),
                                    List.of(""),
                                    "color",
                                    true,
                                    wrap((nodes, arg) -> new ColorNode(nodes, finalIColorNode))
                            )
                    );
                }
                // New Code
                TagRegistry.registerDefault(
                        TextTag.enclosing(
                                iColorNode.getName(),
                                List.of(""),
                                "color",
                                true,
                                (nodes, data, parser) -> new ColorNode(nodes, finalIColorNode)
                        )
                );
            }
        }
    }

    public static void initNodes()
    {
        initColors();
    }

    public static void registerNodes()
    {
        registerColors();
    }

    // Copied wrap() from TextTags.java
    private static TextParserV1.TagNodeBuilder wrap(Wrapper wrapper)
    {
        return (tag, data, input, handlers, endAt) ->
        {
            // Legacy Parser
            var out = TextParserImpl.recursiveParsing(input, handlers, endAt);
            return new TextParserV1.TagNodeValue(wrapper.wrap(out.nodes(), data), out.length());
        };
    }

    interface Wrapper
    {
        TextNode wrap(TextNode[] nodes, String arg);
    }
}
