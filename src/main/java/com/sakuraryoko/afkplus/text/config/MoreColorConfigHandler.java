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

package com.sakuraryoko.afkplus.text.config;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import com.sakuraryoko.afkplus.AfkPlusMod;
import com.sakuraryoko.afkplus.AfkPlusReference;
import com.sakuraryoko.afkplus.config.interfaces.IConfigData;
import com.sakuraryoko.afkplus.config.interfaces.IConfigDispatch;
import com.sakuraryoko.afkplus.text.nodes.type.MoreColorNode;

public class MoreColorConfigHandler implements IConfigDispatch
{
    private static final MoreColorConfigHandler INSTANCE = new MoreColorConfigHandler();
    public static MoreColorConfigHandler getInstance() { return INSTANCE; }
    private final MoreColorData CONFIG = newConfig();
    private final String CONFIG_ROOT = AfkPlusReference.MOD_ID;
    private final String CONFIG_NAME = "more_colors";
    private boolean loaded = false;

    @Override
    public String getConfigRoot()
    {
        return this.CONFIG_ROOT;
    }

    @Override
    public boolean useRootDir()
    {
        return false;
    }

    @Override
    public String getConfigName()
    {
        return this.CONFIG_NAME;
    }

    @Override
    public MoreColorData newConfig()
    {
        return new MoreColorData();
    }

    @Override
    public MoreColorData getConfig()
    {
        return CONFIG;
    }

    @Override
    public boolean isLoaded()
    {
        return this.loaded;
    }

    public List<MoreColorNode> getColors()
    {
        return CONFIG.COLORS;
    }

    @Override
    public void onPreLoadConfig()
    {
        this.loaded = false;
    }

    @Override
    public void onPostLoadConfig()
    {
        this.loaded = true;
    }

    @Override
    public void onPreSaveConfig()
    {
        this.loaded = false;
    }

    @Override
    public void onPostSaveConfig()
    {
        this.loaded = true;
    }
    
    @Override
    public MoreColorData defaults()
    {
        MoreColorData config = this.newConfig();
        AfkPlusMod.debugLog("MoreColorConfigHandler#defaults(): Setting default config.");

        // Set default values
        config.config_date = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH.mm.ss", Locale.ROOT).format(ZonedDateTime.now());
        config.COLORS = new ArrayList<>();

        config.COLORS.add(new MoreColorNode("aztec_gold", "#C39953"));
        config.COLORS.add(new MoreColorNode("bluetiful", "#3C69E7", List.of("blue2")));
        config.COLORS.add(new MoreColorNode("brown", "#632C04"));
        config.COLORS.add(new MoreColorNode("burnt_orange", "#FF7034", List.of("orange2")));
        config.COLORS.add(new MoreColorNode("canary", "#FFFF99", List.of("yellow2")));
        config.COLORS.add(new MoreColorNode("cool_mint", "#DDEBEC"));
        config.COLORS.add(new MoreColorNode("copper", "#DA8A67"));
        config.COLORS.add(new MoreColorNode("cotton_candy", "#FFB7D5"));
        config.COLORS.add(new MoreColorNode("cyan", "#2D7C9D"));
        config.COLORS.add(new MoreColorNode("dark_brown", "#421F05"));
        config.COLORS.add(new MoreColorNode("dark_pink", "#DE8BB4"));
        config.COLORS.add(new MoreColorNode("lavender", "#BF8FCC"));
        config.COLORS.add(new MoreColorNode("light_blue", "#82ACE7"));
        config.COLORS.add(new MoreColorNode("light_brown", "#7A4621"));
        config.COLORS.add(new MoreColorNode("light_gray", "#BABAC1", List.of("light_grey")));
        config.COLORS.add(new MoreColorNode("light_pink", "#F7B4D6"));
        config.COLORS.add(new MoreColorNode("lime", "#76C610"));
        config.COLORS.add(new MoreColorNode("magenta", "#CB69C5"));
        config.COLORS.add(new MoreColorNode("maroon", "#C32148"));
        //config.COLORS.add(new MoreColorNode("orange","#E69E34"));
        //config.COLORS.add(new MoreColorNode("pink","#EDA7CB"));
        config.COLORS.add(new MoreColorNode("pacific_blue", "#009DC4"));
        config.COLORS.add(new MoreColorNode("peach", "#FFCBA4"));
        config.COLORS.add(new MoreColorNode("plum", "#843179"));
        config.COLORS.add(new MoreColorNode("powder_blue", "#C0D5F0"));
        config.COLORS.add(new MoreColorNode("purple", "#A453CE"));
        config.COLORS.add(new MoreColorNode("royal_purple", "#6B3FA0"));
        config.COLORS.add(new MoreColorNode("salmon", "#FF91A4", List.of("pink_salmon")));
        config.COLORS.add(new MoreColorNode("scarlet", "#FD0E35"));
        config.COLORS.add(new MoreColorNode("sea_green", "#93DFB8"));
        config.COLORS.add(new MoreColorNode("sepia", "#9E5B40"));
        config.COLORS.add(new MoreColorNode("shamrock", "#33CC99"));
        config.COLORS.add(new MoreColorNode("sunset_orange", "#FE4C40"));
        config.COLORS.add(new MoreColorNode("tickle_me_pink", "#FC80A5"));
        config.COLORS.add(new MoreColorNode("timberwolf", "#D9D6CF"));
        config.COLORS.add(new MoreColorNode("ultramarine_blue", "#3F26BF", List.of("ultramarine")));
        
        return config;
    }

    @Override
    public MoreColorData update(IConfigData newConfig)
    {
        MoreColorData newConf = (MoreColorData) newConfig;
        AfkPlusMod.debugLog("MoreColorConfigHandler#update(): Refresh config.");

        // Refresh
        CONFIG.comment = "AFK Plus config " + AfkPlusReference.MC_VERSION + "-" + AfkPlusReference.AFK_VERSION + " --> (MORE_COLORS)";
        CONFIG.config_date = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH.mm.ss", Locale.ROOT).format(ZonedDateTime.now());
        AfkPlusMod.debugLog("MoreColorConfigHandler#update(): save_date: {} --> {}", newConf.config_date, CONFIG.config_date);

        // Copy Incoming Config
        CONFIG.COLORS.clear();
        CONFIG.COLORS.addAll(newConf.COLORS);
        
        return CONFIG;
    }

    @Override
    public void execute()
    {
        AfkPlusMod.debugLog("MoreColorConfigHandler#execute(): Execute config.");

        // Do this when the Config gets finalized.
        AfkPlusMod.debugLog("MoreColorConfigHandler#execute(): new config_date: {}", CONFIG.config_date);
    }
}
