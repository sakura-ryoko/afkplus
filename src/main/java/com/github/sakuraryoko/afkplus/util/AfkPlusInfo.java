package com.github.sakuraryoko.afkplus.util;

import java.util.Iterator;

import com.github.sakuraryoko.afkplus.data.ModData;
import eu.pb4.placeholders.api.TextParserUtils;
import net.fabricmc.api.EnvType;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import net.fabricmc.loader.api.metadata.ModMetadata;
import net.fabricmc.loader.api.metadata.Person;
import net.minecraft.MinecraftVersion;
import net.minecraft.text.Text;

public class AfkPlusInfo
{
    private static final FabricLoader AFK_INST = FabricLoader.getInstance();
    private static final ModContainer AFK_CONTAINER = AFK_INST.getModContainer(ModData.AFK_MOD_ID).get();

    public static void initModInfo()
    {
        ModData.AFK_MC_VERSION = MinecraftVersion.CURRENT.getName();
        ModData.AFK_ENV = AFK_INST.getEnvironmentType();
        ModMetadata AFK_METADATA = AFK_CONTAINER.getMetadata();
        ModData.AFK_VERSION = AFK_METADATA.getVersion().getFriendlyString();
        ModData.AFK_NAME = AFK_METADATA.getName();
        ModData.AFK_DESC = AFK_METADATA.getDescription();
        ModData.AFK_AUTHOR = AFK_METADATA.getAuthors();
        ModData.AFK_CONTRIB = AFK_METADATA.getContributors();
        ModData.AFK_CONTACTS = AFK_METADATA.getContact();
        ModData.AFK_LICENSES = AFK_METADATA.getLicense();
        ModData.AFK_AUTHO_STRING = getAuthoString();
        ModData.AFK_CONTRIB_STRING = getContribString();
        ModData.AFK_LICENSES_STRING = getLicenseString();
        ModData.AFK_HOMEPAGE_STRING = getHomepageString();
        ModData.AFK_SOURCES_STRING = getSourcesString();
    }

    public static void displayModInfo()
    {
        AfkPlusLogger.info(ModData.AFK_NAME + "-" + ModData.AFK_MC_VERSION + "-" + ModData.AFK_VERSION);
        AfkPlusLogger.info("Author: " + ModData.AFK_AUTHO_STRING);
    }

    public static Text getModInfoText()
    {
        String modInfo = ModData.AFK_NAME + "-" + ModData.AFK_MC_VERSION + "-" + ModData.AFK_VERSION
        + "\nAuthor: <pink>" + ModData.AFK_AUTHO_STRING + "</pink>"
        + "\nLicense: <yellow>" + ModData.AFK_LICENSES_STRING + "</yellow>"
        + "\nHomepage: <cyan><url:'" + ModData.AFK_HOMEPAGE_STRING + "'>" + ModData.AFK_HOMEPAGE_STRING + "</url></cyan>"
        + "\nSource: <cyan><url:'" + ModData.AFK_SOURCES_STRING + "'>" + ModData.AFK_SOURCES_STRING + "</url></cyan>"
        + "\nDescription: <light_blue>" + ModData.AFK_DESC;
        Text info = TextParserUtils.formatText(modInfo);
        AfkPlusLogger.debug(modInfo);
        return info;
    }

    public static boolean isServer() {
        return ModData.AFK_ENV == EnvType.SERVER;
    }

    public static boolean isClient() {
        return ModData.AFK_ENV == EnvType.CLIENT;
    }

    private static String getAuthoString()
    {
        StringBuilder authoString = new StringBuilder();
        if (ModData.AFK_AUTHOR.isEmpty())
            return authoString.toString();
        else
        {
            final Iterator<Person> iterator = ModData.AFK_AUTHOR.iterator();
            while (iterator.hasNext())
            {
                if (authoString.isEmpty())
                    authoString = new StringBuilder(iterator.next().getName());
                else
                    authoString.append(", ").append(iterator.next().getName());
            }
            return authoString.toString();
        }
    }

    private static String getContribString()
    {
        StringBuilder contribString = new StringBuilder();
        if (ModData.AFK_CONTRIB.isEmpty())
            return contribString.toString();
        else
        {
            final Iterator<Person> iterator = ModData.AFK_CONTRIB.iterator();
            while (iterator.hasNext())
            {
                if (contribString.isEmpty())
                    contribString = new StringBuilder(iterator.next().getName());
                else
                    contribString.append(", ").append(iterator.next().getName());
            }
            return contribString.toString();
        }
    }

    private static String getLicenseString()
    {
        StringBuilder licsenseString = new StringBuilder();
        if (ModData.AFK_LICENSES.isEmpty())
            return licsenseString.toString();
        else
        {
            final Iterator<String> iterator = ModData.AFK_LICENSES.iterator();
            while (iterator.hasNext())
            {
                if (licsenseString.isEmpty())
                    licsenseString = new StringBuilder(iterator.next());
                else
                    licsenseString.append(", ").append(iterator.next());
            }
            return licsenseString.toString();
        }
    }

    private static String getHomepageString()
    {
        String homepageString = ModData.AFK_CONTACTS.asMap().get("homepage");
        if (homepageString.isEmpty())
            return "";
        else
            return homepageString;
    }

    private static String getSourcesString()
    {
        String sourcesString = ModData.AFK_CONTACTS.asMap().get("sources");
        if (sourcesString.isEmpty())
            return "";
        else
            return sourcesString;
    }
}
