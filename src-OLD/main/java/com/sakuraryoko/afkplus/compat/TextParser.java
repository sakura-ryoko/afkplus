package com.sakuraryoko.afkplus.compat;

import eu.pb4.placeholders.api.parsers.NodeParser;

public class TextParser
{
    public static NodeParser LEGACY;
    public static NodeParser SIMP;
    public static NodeParser QUICK;
    public static NodeParser PLACEHOLDER;
    public static NodeParser PARSE;

    public static void build()
    {
        LEGACY = NodeParser.builder().legacyAll().build();
        SIMP = NodeParser.builder().simplifiedTextFormat().build();
        QUICK = NodeParser.builder().quickText().build();
        PLACEHOLDER = NodeParser.builder().globalPlaceholders().build();

        PARSE = NodeParser.builder().add(LEGACY).add(SIMP).add(QUICK).build();
    }
}
