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

package com.sakuraryoko.afkplus.text.nodes;

import java.util.Iterator;
import java.util.List;
import eu.pb4.placeholders.api.node.TextNode;
import eu.pb4.placeholders.api.node.parent.ColorNode;
import eu.pb4.placeholders.api.parsers.TextParserV1;
//#if MC >= 12006
//$$ import eu.pb4.placeholders.api.parsers.tag.TagRegistry;
//$$ import eu.pb4.placeholders.api.parsers.tag.TextTag;
//#else
//#endif
import eu.pb4.placeholders.impl.textparser.TextParserImpl;

import net.minecraft.network.chat.TextColor;

import com.sakuraryoko.afkplus.AfkPlusMod;
import com.sakuraryoko.afkplus.text.TextUtils;
import com.sakuraryoko.afkplus.text.nodes.type.MoreColorNode;

//#if MC >= 12006
//$$ @SuppressWarnings("deprecation")
//#else
//#endif
public class NodeManager
{
    private static void registerColors()
    {
        final Iterator<MoreColorNode> iterator = MoreColorNodes.COLORS.iterator();
        MoreColorNode iColorNode;
        while (iterator.hasNext())
        {
            iColorNode = iterator.next();
            // DataResult checked at initialization
            AfkPlusMod.debugLog("registerColors(): register ColorNode: {} // {}", iColorNode.getName(), iColorNode.getHexCode());
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
//#if MC >= 12006
                //$$ TagRegistry.registerDefault(
                    //$$ TextTag.enclosing(
                        //$$ iColorNode.getName(),
                        //$$ iColorNode.getAliases(),
                        //$$ "color",
                        //$$ true,
                        //$$ (nodes, data, parser) -> new ColorNode(nodes, finalIColorNode)
                    //$$ )
                //$$ );
//#else
//#endif
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
//#if MC >= 12006
                //$$ TagRegistry.registerDefault(
                    //$$ TextTag.enclosing(
                        //$$ iColorNode.getName(),
                        //$$ List.of(""),
                        //$$ "color",
                        //$$ true,
                        //$$ (nodes, data, parser) -> new ColorNode(nodes, finalIColorNode)
                    //$$ )
                //$$ );
//#else
//#endif
            }
        }
    }

    public static void initNodes()
    {
        MoreColorNodes.register();
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
