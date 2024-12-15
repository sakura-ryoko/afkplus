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

package com.sakuraryoko.afkplus.config.data;

@Deprecated
public class TomlConfigData
{
    public AfkPlusOptions afkPlusOptions = new AfkPlusOptions();
    public PacketOptions packetOptions = new PacketOptions();
    public PlaceholderOptions PlaceholderOptions = new PlaceholderOptions();
    public PlayerListOptions playerListOptions = new PlayerListOptions();
    public MessageOptions messageOptions = new MessageOptions();

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

    public static class PlayerListOptions
    {
        public boolean enableListDisplay;
        public String afkPlayerName;
        public int updateInterval;
    }

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
