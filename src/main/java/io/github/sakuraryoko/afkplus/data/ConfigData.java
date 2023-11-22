package io.github.sakuraryoko.afkplus.data;

public class ConfigData {
    public AfkPlusOptions afkPlusOptions = new AfkPlusOptions();
    public PacketOptions packetOptions = new PacketOptions();
    public PlaceholderOptions PlaceholderOptions = new PlaceholderOptions();
    public PlayerListOptions playerListOptions = new PlayerListOptions();
    public MessageOptions messageOptions = new MessageOptions();

    public static class AfkPlusOptions {
        public boolean enableAfkCommand;
        public boolean enableAfkInfoCommand;
        public int afkCommandPermissions;
        public int afkInfoCommandPermissions;
        public int afkPlusCommandPermissions;
        public String afkTimeoutString;
    }

    public static class PacketOptions {
        public int timeoutSeconds;
        public Boolean resetOnMovement;
        public Boolean resetOnLook;
    }

    public static class PlaceholderOptions {
        public String afkPlaceholder;
        public String afkPlusNamePlaceholderAfk;
        public String afkPlusNamePlaceholder;
        public String afkDurationPlaceholderFormatting;
        public String afkTimePlaceholderFormatting;
        public String afkReasonPlaceholderFormatting;
        public boolean afkDurationPretty;
    }

    public static class PlayerListOptions {
        public boolean enableListDisplay;
        public String afkPlayerName;
    }

    public static class MessageOptions {
        public boolean enableMessages;
        public String whenAfk;
        public String whenReturn;
        public boolean prettyDuration;
        public String defaultReason;
    }
}
