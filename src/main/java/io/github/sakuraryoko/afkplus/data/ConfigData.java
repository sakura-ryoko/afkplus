package io.github.sakuraryoko.afkplus.data;

import org.jetbrains.annotations.NotNull;

@NotNull
public class ConfigData {
    public AfkPlusOptions afkPlusOptions = new AfkPlusOptions();
    public PacketOptions packetOptions = new PacketOptions();
    public PlaceholderOptions PlaceholderOptions = new PlaceholderOptions();
    public PlayerListOptions playerListOptions = new PlayerListOptions();
    public MessageOptions messageOptions = new MessageOptions();

    @NotNull
    public static class AfkPlusOptions {
        public boolean enableAfkCommand;
        public boolean enableAfkInfoCommand;
        public int afkCommandPermissions;
        public int afkInfoCommandPermissions;
        public int afkPlusCommandPermissions;
        public String afkTimeoutString;
    }

    @NotNull
    public static class PacketOptions {
        public int timeoutSeconds;
        public Boolean resetOnMovement;
        public Boolean resetOnLook;
        public Boolean cancelDamage;
    }

    @NotNull
    public static class PlaceholderOptions {
        public String afkPlaceholder;
        public String afkPlusNamePlaceholderAfk;
        public String afkPlusNamePlaceholder;
        public String afkDurationPlaceholderFormatting;
        public String afkTimePlaceholderFormatting;
        public String afkReasonPlaceholderFormatting;
        public boolean afkDurationPretty;
    }

    @NotNull
    public static class PlayerListOptions {
        public boolean enableListDisplay;
        public String afkPlayerName;
    }

    @NotNull
    public static class MessageOptions {
        public boolean enableMessages;
        public String whenAfk;
        public String whenReturn;
        public boolean prettyDuration;
        public String defaultReason;
    }
}
