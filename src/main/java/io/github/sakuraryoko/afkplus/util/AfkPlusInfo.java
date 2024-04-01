package io.github.sakuraryoko.afkplus.util;

import static io.github.sakuraryoko.afkplus.data.ModData.*;

import java.util.Iterator;

import eu.pb4.placeholders.TextParser;
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
    private static final ModContainer AFK_CONTAINER = AFK_INST.getModContainer(AFK_MOD_ID).get();

    public static void initModInfo()
    {
        AFK_MC_VERSION = MinecraftVersion.GAME_VERSION.getName();
        AFK_ENV = AFK_INST.getEnvironmentType();
        ModMetadata AFK_METADATA = AFK_CONTAINER.getMetadata();
        AFK_VERSION = AFK_METADATA.getVersion().getFriendlyString();
        AFK_NAME = AFK_METADATA.getName();
        AFK_DESC = AFK_METADATA.getDescription();
        AFK_AUTHOR = AFK_METADATA.getAuthors();
        AFK_CONTRIB = AFK_METADATA.getContributors();
        AFK_CONTACTS = AFK_METADATA.getContact();
        AFK_LICENSES = AFK_METADATA.getLicense();
        AFK_AUTHO_STRING = getAuthoString();
        AFK_CONTRIB_STRING = getContribString();
        AFK_LICENSES_STRING = getLicenseString();
        AFK_HOMEPAGE_STRING = getHomepageString();
        AFK_SOURCES_STRING = getSourcesString();
    }

    public static void displayModInfo()
    {
        AfkPlusLogger.info(AFK_NAME + "-" + AFK_MC_VERSION + "-" + AFK_VERSION);
        AfkPlusLogger.info("Author: " + AFK_AUTHO_STRING);
    }

    public static Text getModInfoText()
    {
        String modInfo = AFK_NAME + "-" + AFK_MC_VERSION + "-" + AFK_VERSION
        + "\nAuthor: <pink>" + AFK_AUTHO_STRING + "</pink>"
        + "\nLicense: <yellow>" + AFK_LICENSES_STRING + "</yellow>"
        + "\nHomepage: <cyan><url:'" + AFK_HOMEPAGE_STRING + "'>" + AFK_HOMEPAGE_STRING + "</url></cyan>"
        + "\nSource: <cyan><url:'" + AFK_SOURCES_STRING + "'>" + AFK_SOURCES_STRING + "</url></cyan>"
        + "\nDescription: <light_blue>" + AFK_DESC;
        Text info = TextParser.parse(modInfo);
        AfkPlusLogger.debug(modInfo);
        return info;
    }

    public static boolean isServer() {
        return AFK_ENV == EnvType.SERVER;
    }

    public static boolean isClient() {
        return AFK_ENV == EnvType.CLIENT;
    }

    private static String getAuthoString()
    {
        StringBuilder authoString = new StringBuilder();
        if (AFK_AUTHOR.isEmpty())
            return authoString.toString();
        else
        {
            final Iterator<Person> iterator = AFK_AUTHOR.iterator();
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
        if (AFK_CONTRIB.isEmpty())
            return contribString.toString();
        else
        {
            final Iterator<Person> iterator = AFK_CONTRIB.iterator();
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
        if (AFK_LICENSES.isEmpty())
            return licsenseString.toString();
        else
        {
            final Iterator<String> iterator = AFK_LICENSES.iterator();
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
        String homepageString = AFK_CONTACTS.asMap().get("homepage");
        if (homepageString.isEmpty())
            return "";
        else
            return homepageString;
    }

    private static String getSourcesString()
    {
        String sourcesString = AFK_CONTACTS.asMap().get("sources");
        if (sourcesString.isEmpty())
            return "";
        else
            return sourcesString;
    }
}
