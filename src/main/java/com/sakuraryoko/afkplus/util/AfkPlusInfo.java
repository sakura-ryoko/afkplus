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

package com.sakuraryoko.afkplus.util;

import java.util.Iterator;

import net.minecraft.DetectedVersion;
import net.minecraft.network.chat.Component;
import net.fabricmc.api.EnvType;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import net.fabricmc.loader.api.metadata.ModMetadata;
import net.fabricmc.loader.api.metadata.Person;

import com.sakuraryoko.afkplus.AfkPlusMod;
import com.sakuraryoko.afkplus.text.TextUtils;
import com.sakuraryoko.afkplus.AfkPlusReference;

public class AfkPlusInfo
{
    private static final FabricLoader AFK_INST = FabricLoader.getInstance();
    private static final ModContainer AFK_CONTAINER = AFK_INST.getModContainer(AfkPlusReference.MOD_ID).get();

    public static void initModInfo()
    {
        AfkPlusReference.MC_VERSION = DetectedVersion.BUILT_IN.getName();
        AfkPlusReference.MOD_ENV = AFK_INST.getEnvironmentType();
        ModMetadata AFK_METADATA = AFK_CONTAINER.getMetadata();
        AfkPlusReference.AFK_VERSION = AFK_METADATA.getVersion().getFriendlyString();
        AfkPlusReference.MOD_NAME = AFK_METADATA.getName();
        AfkPlusReference.MOD_DESC = AFK_METADATA.getDescription();
        AfkPlusReference.MOD_AUTHOR = AFK_METADATA.getAuthors();
        AfkPlusReference.MOD_CONTRIB = AFK_METADATA.getContributors();
        AfkPlusReference.MOD_CONTACTS = AFK_METADATA.getContact();
        AfkPlusReference.MOD_LICENSES = AFK_METADATA.getLicense();
        AfkPlusReference.MOD_AUTHO_STRING = getAuthoString();
        AfkPlusReference.MOD_CONTRIB_STRING = getContribString();
        AfkPlusReference.MOD_LICENSES_STRING = getLicenseString();
        AfkPlusReference.MOD_HOMEPAGE_STRING = getHomepageString();
        AfkPlusReference.MOD_SOURCES_STRING = getSourcesString();
    }

    public static void displayModInfo()
    {
        AfkPlusMod.LOGGER.info("{}-{}-{}", AfkPlusReference.MOD_NAME, AfkPlusReference.MC_VERSION, AfkPlusReference.AFK_VERSION);
        AfkPlusMod.LOGGER.info("Author: {}", AfkPlusReference.MOD_AUTHO_STRING);
    }

    public static Component getModInfoText()
    {
        String modInfo = AfkPlusReference.MOD_NAME + "-" + AfkPlusReference.MC_VERSION + "-" + AfkPlusReference.AFK_VERSION
                + "\nAuthor: <pink>" + AfkPlusReference.MOD_AUTHO_STRING + "</pink>"
                + "\nLicense: <yellow>" + AfkPlusReference.MOD_LICENSES_STRING + "</yellow>"
                + "\nHomepage: <cyan><url:'" + AfkPlusReference.MOD_HOMEPAGE_STRING + "'>" + AfkPlusReference.MOD_HOMEPAGE_STRING + "</url></cyan>"
                + "\nSource: <cyan><url:'" + AfkPlusReference.MOD_SOURCES_STRING + "'>" + AfkPlusReference.MOD_SOURCES_STRING + "</url></cyan>"
                + "\nDescription: <light_blue>" + AfkPlusReference.MOD_DESC;
        Component info = TextUtils.formatText(modInfo);
        AfkPlusMod.debugLog(modInfo);
        return info;
    }

    public static boolean isServer()
    {
        return AfkPlusReference.MOD_ENV == EnvType.SERVER;
    }

    public static boolean isClient()
    {
        return AfkPlusReference.MOD_ENV == EnvType.CLIENT;
    }

    private static String getAuthoString()
    {
        StringBuilder authoString = new StringBuilder();
        if (AfkPlusReference.MOD_AUTHOR.isEmpty())
        {
            return authoString.toString();
        }
        else
        {
            final Iterator<Person> iterator = AfkPlusReference.MOD_AUTHOR.iterator();
            while (iterator.hasNext())
            {
                if (authoString.isEmpty())
                {
                    authoString = new StringBuilder(iterator.next().getName());
                }
                else
                {
                    authoString.append(", ").append(iterator.next().getName());
                }
            }
            return authoString.toString();
        }
    }

    private static String getContribString()
    {
        StringBuilder contribString = new StringBuilder();
        if (AfkPlusReference.MOD_CONTRIB.isEmpty())
        {
            return contribString.toString();
        }
        else
        {
            final Iterator<Person> iterator = AfkPlusReference.MOD_CONTRIB.iterator();
            while (iterator.hasNext())
            {
                if (contribString.isEmpty())
                {
                    contribString = new StringBuilder(iterator.next().getName());
                }
                else
                {
                    contribString.append(", ").append(iterator.next().getName());
                }
            }
            return contribString.toString();
        }
    }

    private static String getLicenseString()
    {
        StringBuilder licsenseString = new StringBuilder();
        if (AfkPlusReference.MOD_LICENSES.isEmpty())
        {
            return licsenseString.toString();
        }
        else
        {
            final Iterator<String> iterator = AfkPlusReference.MOD_LICENSES.iterator();
            while (iterator.hasNext())
            {
                if (licsenseString.isEmpty())
                {
                    licsenseString = new StringBuilder(iterator.next());
                }
                else
                {
                    licsenseString.append(", ").append(iterator.next());
                }
            }
            return licsenseString.toString();
        }
    }

    private static String getHomepageString()
    {
        String homepageString = AfkPlusReference.MOD_CONTACTS.asMap().get("homepage");
        if (homepageString.isEmpty())
        {
            return "";
        }
        else
        {
            return homepageString;
        }
    }

    private static String getSourcesString()
    {
        String sourcesString = AfkPlusReference.MOD_CONTACTS.asMap().get("sources");
        if (sourcesString.isEmpty())
        {
            return "";
        }
        else
        {
            return sourcesString;
        }
    }
}
