package com.github.sakuraryoko.afkplus.data;

import org.jetbrains.annotations.NotNull;

@NotNull
public class ConfigData
{
    public AfkPlusOptions afkPlusOptions = new AfkPlusOptions();
    public PacketOptions packetOptions = new PacketOptions();
    public PlaceholderOptions PlaceholderOptions = new PlaceholderOptions();
    public PlayerListOptions playerListOptions = new PlayerListOptions();
    public MessageOptions messageOptions = new MessageOptions();

    @NotNull
    public static class AfkPlusOptions
    {
        public boolean enableAfkCommand;
        public boolean enableNoAfkCommand;
        public boolean enableAfkInfoCommand;
        public boolean enableAfkExCommand;
        public int afkCommandPermissions;
        public int noAfkCommandPermissions;
        public int afkInfoCommandPermissions;
        public int afkExCommandPermissions;
        public int afkPlusCommandPermissions;
        public String afkTimeoutString;
    }

    @NotNull
    public static class PacketOptions
    {
        public int timeoutSeconds;
        public boolean resetOnMovement;
        public boolean resetOnLook;
        public boolean disableDamage;
        public int disableDamageCooldown;
        public boolean bypassSleepCount;
        public boolean bypassInsomnia;
        public boolean afkKickEnabled;
        public boolean afkKickNonSurvival;
        public int afkKickTimer;
        public int afkKickSafePermissions;
    }

    @NotNull
    public static class PlaceholderOptions
    {
        public String afkPlaceholder;
        public String afkPlusNamePlaceholderAfk;
        public String afkPlusNamePlaceholder;
        public String afkDurationPlaceholderFormatting;
        public String afkTimePlaceholderFormatting;
        public String afkReasonPlaceholderFormatting;
        public boolean afkDurationPretty;
        public String afkInvulnerablePlaceholder;
    }

    @NotNull
    public static class PlayerListOptions
    {
        public boolean enableListDisplay;
        public String afkPlayerName;
    }

    @NotNull
    public static class MessageOptions
    {
        public boolean enableMessages;
        public String whenAfk;
        public String whenReturn;
        public boolean prettyDuration;
        public String defaultReason;
        public String whenDamageDisabled;
        public String whenDamageEnabled;
        public boolean displayDuration;
        public String afkKickMessage;
        public String whenKicked;
    }
}
