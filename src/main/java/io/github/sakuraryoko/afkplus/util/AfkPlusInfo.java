package io.github.sakuraryoko.afkplus.util;

import static io.github.sakuraryoko.afkplus.data.ModData.*;

import java.util.Iterator;

import eu.pb4.placeholders.api.TextParserUtils;
import net.fabricmc.api.EnvType;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import net.fabricmc.loader.api.metadata.ModMetadata;
import net.fabricmc.loader.api.metadata.Person;
import net.minecraft.text.Text;

public class AfkPlusInfo {
    private static final FabricLoader AFK_INST = FabricLoader.getInstance();
    private static final ModContainer AFK_CONTAINER = AFK_INST.getModContainer(AFK_MOD_ID).get();
    private static ModMetadata AFK_METADATA;

    public static void initModInfo() {
        AFK_MC_VERSION = FabricLoader.getInstance().getModContainer("minecraft").get().getMetadata().getVersion()
                .getFriendlyString();
        AFK_ENV = AFK_INST.getEnvironmentType();
        AFK_METADATA = AFK_CONTAINER.getMetadata();
        AFK_VERSION = AFK_METADATA.getVersion().getFriendlyString();
        AFK_NAME = AFK_METADATA.getName();
        AFK_DESC = AFK_METADATA.getDescription();
        AFK_AUTHO = AFK_METADATA.getAuthors();
        AFK_CONTRIB = AFK_METADATA.getContributors();
        AFK_CONTACTS = AFK_METADATA.getContact();
        AFK_LICENSES = AFK_METADATA.getLicense();
        AFK_AUTHO_STRING = getAuthoString();
        AFK_CONTRIB_STRING = getContribString();
        AFK_LICENSES_STRING = getLicenseString();
        AFK_HOMEPAGE_STRING = getHomepageString();
        AFK_SOURCES_STRING = getSourcesString();
    }

    public static void displayModInfo() {
        AfkPlusLogger.info(AFK_NAME + "-" + AFK_MC_VERSION + "-" + AFK_VERSION);
        AfkPlusLogger.info("Author: " + AFK_AUTHO_STRING);
    }

    public static Text getModInfoText() {
        String modInfo1 = AFK_NAME + "-" + AFK_MC_VERSION + "-" + AFK_VERSION;
        String modInfo2 = "Author: <light_purple>" + AFK_AUTHO_STRING + "</light_purple>";
        String modInfo3 = "License: <yellow>" + AFK_LICENSES_STRING + "</yellow>";
        String modInfo4 = "Homepage: <aqua><url:'" + AFK_HOMEPAGE_STRING + "'>" + AFK_HOMEPAGE_STRING + "</url></aqua>";
        String modInfo5 = "Source: <aqua><url:'" + AFK_SOURCES_STRING + "'>" + AFK_SOURCES_STRING + "</url></aqua>";
        String modInfo6 = "Description: " + AFK_DESC;

        Text info = TextParserUtils
                .formatText(modInfo1 + "\n" + modInfo2 + "\n" + modInfo3 + "\n" + modInfo4 + "\n" + modInfo5 + "\n"
                        + modInfo6);
        return info;

    }

    public static boolean isServer() {
        if (AFK_ENV == EnvType.SERVER) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean isClient() {
        if (AFK_ENV == EnvType.CLIENT) {
            return true;
        } else {
            return false;
        }
    }

    private static String getAuthoString() {
        String authoString = "";
        if (AFK_AUTHO.isEmpty()) {
            return authoString;
        } else {
            final Iterator<Person> iterator = AFK_AUTHO.iterator();
            for (; iterator.hasNext();) {
                if (authoString == "") {
                    authoString = iterator.next().getName();
                } else {
                    authoString = authoString + ", " + iterator.next().getName();
                }
            }
            return authoString;
        }
    }

    private static String getContribString() {
        String contribString = "";
        if (AFK_CONTRIB.isEmpty()) {
            return contribString;
        } else {
            final Iterator<Person> iterator = AFK_CONTRIB.iterator();
            for (; iterator.hasNext();) {
                if (contribString == "") {
                    contribString = iterator.next().getName();
                } else {
                    contribString = contribString + ", " + iterator.next().getName();
                }
            }
            return contribString;
        }
    }

    private static String getLicenseString() {
        String licsenseString = "";
        if (AFK_LICENSES.isEmpty()) {
            return licsenseString;
        } else {
            final Iterator<String> iterator = AFK_LICENSES.iterator();
            for (; iterator.hasNext();) {
                if (licsenseString == "") {
                    licsenseString = iterator.next();
                } else {
                    licsenseString = licsenseString + ", " + iterator.next();
                }
            }
            return licsenseString;
        }
    }

    private static String getHomepageString() {
        String homepageString = AFK_CONTACTS.asMap().get("homepage");
        if (homepageString == "" || homepageString == null) {
            return "";
        } else {
            return homepageString;
        }
    }

    private static String getSourcesString() {
        String sourcesString = AFK_CONTACTS.asMap().get("sources");
        if (sourcesString == "" || sourcesString == null) {
            return "";
        } else {
            return sourcesString;
        }
    }
}
