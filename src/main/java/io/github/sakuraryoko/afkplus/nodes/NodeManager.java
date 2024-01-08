package io.github.sakuraryoko.afkplus.nodes;

import eu.pb4.placeholders.TextParser;
import eu.pb4.placeholders.util.TextParserUtils;
import net.minecraft.text.Style;
import net.minecraft.text.TextColor;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class NodeManager {
    public static List<MoreColorNode> COLORS = new ArrayList<>();

    private static void initColors() {
        COLORS.add(new MoreColorNode("brown", "#632C04"));
        COLORS.add(new MoreColorNode("cyan","#2D7C9D"));
        COLORS.add(new MoreColorNode("dark_brown","#421F05"));
        COLORS.add(new MoreColorNode("dark_pink","#DE8BB4"));
        COLORS.add(new MoreColorNode("light_blue","#82ACE7"));
        COLORS.add(new MoreColorNode("light_brown","#7A4621"));
        COLORS.add(new MoreColorNode("light_gray","#BABAC1", List.of("light_grey")));
        COLORS.add(new MoreColorNode("light_pink","#F7B4D6"));
        COLORS.add(new MoreColorNode("lime","#76C610"));
        COLORS.add(new MoreColorNode("magenta","#CB69C5"));
        //COLORS.add(new MoreColorNode("orange","#E69E34"));
        COLORS.add(new MoreColorNode("pink","#EDA7CB"));
        COLORS.add(new MoreColorNode("purple","#A453CE"));
        COLORS.add(new MoreColorNode("salmon", "#FF91A4", List.of("pink_salmon")));
    }
    private static void registerColors() {
        final Iterator<MoreColorNode> iterator = COLORS.iterator();
        MoreColorNode iColorNode;
        while (iterator.hasNext()) {
            iColorNode = iterator.next();
            // DataResult checked at initialization
            TextColor finalIColorNode = iColorNode.getColor();
            if (iColorNode.getAliases() != null) {
                TextParser.TextFormatterHandler iColor = (tag, data, input, handlers, endAt) -> {
                    eu.pb4.placeholders.util.GeneralUtils.TextLengthPair out = TextParserUtils.recursiveParsing(input, handlers, endAt);
                    out.text().fillStyle(Style.EMPTY.withColor(finalIColorNode));
                    return out;
                };
                TextParser.register(iColorNode.getName(), iColor);
                List<String> iAliases = iColorNode.getAliases();
                for (String iAlias : iAliases) TextParser.register(iAlias, iColor);
            } else {
                TextParser.register(iColorNode.getName(), (tag, data, input, handlers, endAt) -> {
                    eu.pb4.placeholders.util.GeneralUtils.TextLengthPair out = TextParserUtils.recursiveParsing(input, handlers, endAt);
                    out.text().fillStyle(Style.EMPTY.withColor(finalIColorNode));
                    return out;
                });
            }
        }
    }
    public static void initNodes() {
        initColors();
    }
    public static void registerNodes() {
        registerColors();
    }

    // Copied wrap() from PlaceholderAPI 2.X - TextTags.java
    /*
    private static TextParserV1.TagNodeBuilder wrap(Wrapper wrapper) {
        return (tag, data, input, handlers, endAt) -> {
            var out = TextParserImpl.recursiveParsing(input, handlers, endAt);
            return new TextParserV1.TagNodeValue(wrapper.wrap(out.nodes(), data), out.length());
        };
    }
    interface Wrapper {
        TextNode wrap(TextNode[] nodes, String arg);
    }*/
}
