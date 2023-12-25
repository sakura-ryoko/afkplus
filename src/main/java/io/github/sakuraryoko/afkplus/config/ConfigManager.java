package io.github.sakuraryoko.afkplus.config;

import static io.github.sakuraryoko.afkplus.data.ModData.*;

import java.io.File;

import com.moandjiezana.toml.Toml;
import com.moandjiezana.toml.TomlWriter;

import io.github.sakuraryoko.afkplus.data.ConfigData;
import io.github.sakuraryoko.afkplus.util.AfkPlusLogger;
import net.fabricmc.loader.api.FabricLoader;

public class ConfigManager {
    public static ConfigData CONFIG = new ConfigData();

    public static void initConfig() {
        CONFIG.afkPlusOptions.afkPlusCommandPermissions = 3;
        CONFIG.afkPlusOptions.enableAfkCommand = true;
        CONFIG.afkPlusOptions.enableAfkInfoCommand = true;
        CONFIG.afkPlusOptions.afkCommandPermissions = 0;
        CONFIG.afkPlusOptions.afkInfoCommandPermissions = 2;
        CONFIG.afkPlusOptions.afkTimeoutString = "<i><gray>timeout<r>";
        CONFIG.packetOptions.resetOnLook = false;
        CONFIG.packetOptions.resetOnMovement = false;
        CONFIG.packetOptions.timeoutSeconds = 180;
        CONFIG.PlaceholderOptions.afkPlaceholder = "<i><gray>[AFK]<r>";
        CONFIG.PlaceholderOptions.afkPlusNamePlaceholder = "%player:displayname%";
        CONFIG.PlaceholderOptions.afkPlusNamePlaceholderAfk = "<i><gray>[AFK] %player:displayname_unformatted%<r>";
        CONFIG.PlaceholderOptions.afkDurationPlaceholderFormatting = "<green>";
        CONFIG.PlaceholderOptions.afkTimePlaceholderFormatting = "<green>";
        CONFIG.PlaceholderOptions.afkReasonPlaceholderFormatting = "";
        CONFIG.PlaceholderOptions.afkDurationPretty = false;
        CONFIG.playerListOptions.afkPlayerName = "<i><gray>[AFK] %player:displayname%<r>";
        CONFIG.playerListOptions.enableListDisplay = true;
        CONFIG.messageOptions.enableMessages = true;
        CONFIG.messageOptions.whenAfk = "%player:displayname% <yellow>is now AFK<r>";
        CONFIG.messageOptions.whenReturn = "%player:displayname% <yellow>is no longer AFK<r>";
        CONFIG.messageOptions.prettyDuration = true;
        CONFIG.messageOptions.defaultReason = "<gray>poof!<r>";
        AfkPlusLogger.debug("Default config initalized.");
    }

    public static void loadConfig() {
        File conf = FabricLoader.getInstance().getConfigDir().resolve(AFK_MOD_ID + ".toml").toFile();
        try {
            if (conf.exists()) {
                CONFIG = new Toml().read(conf).to(ConfigData.class);
            } else {
                AfkPlusLogger.info("Config " + AFK_MOD_ID + ".toml not found, creating new file.");
                initConfig();
                try {
                    if (!conf.createNewFile()) {
                        AfkPlusLogger.error("Failed to create a new " + AFK_MOD_ID + ".toml file.");
                    }
                } catch (Exception ignored) {

                }
            }
            new TomlWriter().write(CONFIG, conf);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    public static void reloadConfig() {
        AfkPlusLogger.info("Reloading Config.");
        loadConfig();
    }
}
