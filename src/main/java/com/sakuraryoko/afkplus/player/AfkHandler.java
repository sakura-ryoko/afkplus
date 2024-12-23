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

package com.sakuraryoko.afkplus.player;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import eu.pb4.placeholders.api.PlaceholderContext;
import eu.pb4.placeholders.api.Placeholders;
import me.lucko.fabric.api.permissions.v0.Permissions;
import org.apache.commons.lang3.time.DurationFormatUtils;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

import net.minecraft.Util;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundPlayerInfoPacket;
import net.minecraft.server.level.ServerPlayer;

import com.sakuraryoko.afkplus.AfkPlusMod;
import com.sakuraryoko.afkplus.api.AfkPlusEvents;
import com.sakuraryoko.afkplus.config.ConfigWrap;
import com.sakuraryoko.afkplus.player.interfaces.IPlayerInvoker;
import com.sakuraryoko.afkplus.text.TextUtils;

@ApiStatus.Internal
public class AfkHandler
{
    private AfkPlayer player;

    public AfkHandler(@Nonnull AfkPlayer afkPlayer)
    {
        this.player = afkPlayer;
    }

    @ApiStatus.Internal
    public void registerAfk(@Nullable String reason)
    {
        if (this.player.isAfk())
        {
            return;
        }

        this.player.setAfkTimeMs(Util.getMillis());
        this.player.setAfkTimeString(Util.getFilenameFormattedDateTime());

        if (reason == null && ConfigWrap.mess().defaultReason == null)
        {
            this.player.setAfkReason("<red>none");
        }
        else if (reason == null || reason.isEmpty())
        {
            this.player.setAfkReason("<red>none");
            Component mess = Placeholders.parseText(TextUtils.formatTextSafe(ConfigWrap.mess().whenAfk),
                                                    PlaceholderContext.of(this.player.getPlayer()));

            //AfkPlusLogger.debug("registerafk-mess().toString(): " + mess.toString());
            this.sendAfkMessage(mess);
        }
        else
        {
            this.player.setAfkReason(reason);
            String mess1 = ConfigWrap.mess().whenAfk + "<yellow>,<r> " + reason;
            Component mess2 = Placeholders.parseText(TextUtils.formatTextSafe(mess1), PlaceholderContext.of(this.player.getPlayer()));
            this.sendAfkMessage(mess2);
        }

        AfkPlusEvents.AFK_EVENT.invoker().onAfk(this.invoker().afkplus$player(),
                                                Placeholders.parseText(TextUtils.formatTextSafe(this.player.getAfkReason()), PlaceholderContext.of(this.player.getPlayer())));

        if (ConfigWrap.pack().disableDamage && ConfigWrap.pack().disableDamageCooldown < 1)
        {
            this.disableDamage();
        }

        this.updatePlayerList();
        this.player.setAfk(true);
    }

    @ApiStatus.Internal
    public void unregisterAfkSilently()
    {
        AfkPlusEvents.AFK_RETURN_EVENT.invoker().onReturn(this.invoker().afkplus$player(), Util.getMillis() - this.player.getAfkTimeMs());
        this.player.setAfk(false);
        this.player.setAfkTimeMs(0L);
        this.player.setAfkTimeString("");
        this.player.setAfkReason("");
    }

    @ApiStatus.Internal
    public void unregisterAfk()
    {
        if (!this.player.isAfk())
        {
            // Maybe it was called by PlayerManagerMixin?
            this.player.setAfk(false);
            this.player.setAfkTimeMs(0L);
            this.player.setAfkTimeString("");
            this.player.setAfkReason("");

            return;
        }

        long duration = Util.getMillis() - (this.player.getAfkTimeMs());

        if (ConfigWrap.mess().prettyDuration && ConfigWrap.mess().displayDuration)
        {
            String ret = ConfigWrap.mess().whenReturn + " <gray>(Gone for: <green>"
                    + DurationFormatUtils.formatDurationWords(duration, true, true) + "<gray>)<r>";

            Component mess1 = TextUtils.formatTextSafe(ret);
            Component mess2 = Placeholders.parseText(mess1, PlaceholderContext.of(this.player.getPlayer()));
            this.sendAfkMessage(mess2);
        }
        else if (ConfigWrap.mess().displayDuration)
        {
            String ret = ConfigWrap.mess().whenReturn + " <gray>(Gone for: <green>"
                    + DurationFormatUtils.formatDurationHMS(duration) + "<gray>)<r>";

            Component mess1 = TextUtils.formatTextSafe(ret);
            Component mess2 = Placeholders.parseText(mess1, PlaceholderContext.of(this.player.getPlayer()));
            this.sendAfkMessage(mess2);
        }
        else
        {
            String ret = ConfigWrap.mess().whenReturn + "<r>";
            Component mess1 = TextUtils.formatTextSafe(ret);
            Component mess2 = Placeholders.parseText(mess1, PlaceholderContext.of(this.player.getPlayer()));
            this.sendAfkMessage(mess2);
        }

        AfkPlusEvents.AFK_RETURN_EVENT.invoker().onReturn(this.invoker().afkplus$player(), duration);
        this.player.setAfk(false);
        this.player.setAfkTimeMs(0L);
        this.player.setAfkTimeString("");
        this.player.setAfkReason("");

        if (!this.player.isDamageEnabled())
        {
            this.enableDamage();
        }

        this.updatePlayerList();
    }

    @ApiStatus.Internal
    public void enableDamage()
    {
        // Doesn't matter if they are marked as AFK --> make them not Invulnerable.
        AfkPlusMod.debugLog("enableDamage() has been invoked for: {}", this.player.getName());

        if (this.player.getPlayer().isCreative() || this.player.getPlayer().isSpectator())
        {
            return;
        }

        // They don't need to be AFK, and 'public' makes it so /afkplus damage [Player] works
        if (!this.player.isDamageEnabled())
        {
            this.player.setDamageEnabled(true);

            if (this.invoker().afkplus$player().isInvulnerable())
            {
                this.toggleInvulnerable(false);
                AfkPlusMod.LOGGER.info("Damage Enabled for player: {}", this.player.getName());
            }

            // Send announcement
            if (!ConfigWrap.mess().whenDamageEnabled.isEmpty())
            {
                Component mess1 = TextUtils.formatTextSafe(ConfigWrap.mess().whenDamageEnabled);
                Component mess2 = Placeholders.parseText(mess1, PlaceholderContext.of(this.player.getPlayer()));
                this.sendAfkMessage(mess2);
            }

            AfkPlusEvents.AFK_ENABLE_DAMAGE.invoker().onDamageEnabled(this.invoker().afkplus$player());
        }

        this.updatePlayerList();
    }

    @ApiStatus.Internal
    public void disableDamage()
    {
        AfkPlusMod.debugLog("disableDamage() has been invoked for: {}", this.player.getName());

        if (this.player.getPlayer().isCreative() || this.player.getPlayer().isSpectator() || !ConfigWrap.pack().disableDamage)
        {
            return;
        }

        if (this.player.isLockDamageEnabled())
        {
            AfkPlusMod.LOGGER.info("Disable Damage is locked from player: {}", this.player.getName());
            return;
        }

        if (this.player.isAfk())
        {
            if (this.player.isDamageEnabled())
            {
                this.player.setDamageEnabled(false);

                if (!this.invoker().afkplus$player().isInvulnerable())
                {
                    this.toggleInvulnerable(true);
                    AfkPlusMod.LOGGER.info("Damage Disabled for player: {}", this.player.getName());
                }

                // Send announcement
                if (!ConfigWrap.mess().whenDamageDisabled.isEmpty())
                {
                    Component mess1 = TextUtils.formatTextSafe(ConfigWrap.mess().whenDamageDisabled);
                    Component mess2 = Placeholders.parseText(mess1, PlaceholderContext.of(this.player.getPlayer()));
                    this.sendAfkMessage(mess2);
                }

                AfkPlusEvents.AFK_DISABLE_DAMAGE.invoker().onDamageDisabled(this.invoker().afkplus$player());
            }

            this.updatePlayerList();
        }
    }

    @ApiStatus.Internal
    public void lockDamageEnabled()
    {
        this.player.setLockDamageEnabled(true);

        if (!this.player.isDamageEnabled())
        {
            this.enableDamage();
        }
    }

    @ApiStatus.Internal
    public void unlockDamageEnabled()
    {
        this.player.setLockDamageEnabled(false);
        this.player.tickPlayer(Util.getMillis());
    }

    @ApiStatus.Internal
    public void afkKick()
    {
        if (this.player.isAfk() && ConfigWrap.pack().afkKickEnabled)
        {
            if ((this.player.getPlayer().isCreative() || this.player.getPlayer().isSpectator()) && !ConfigWrap.pack().afkKickNonSurvival)
            {
                return;
            }
            else if (Permissions.check(this.player.getPlayer(), "afkplus.kick.safe", ConfigWrap.pack().afkKickSafePermissions))
            {
                return;
            }
            else if (Permissions.check(this.player.getPlayer(), "afkplus.afkplus", ConfigWrap.afk().afkPlusCommandPermissions))
            {
                return;
            }
            else
            {
                String kickReasonString;
                String kickMessageString;
                Component kickReason;
                Component kickMessage;

                if (ConfigWrap.mess().whenKicked.isEmpty())
                {
                    kickMessageString = "";
                }
                else
                {
                    kickMessageString = ConfigWrap.mess().whenKicked;
                }

                AfkPlusMod.LOGGER.warn("Configured timeout has been reached for player {} --> removing from server.", this.player.getAfkTimeString());

                if (!ConfigWrap.mess().afkKickMessage.isEmpty())
                {
                    if (ConfigWrap.mess().displayDuration)
                    {
                        long afkDuration = Util.getMillis() - (this.player.getPlayer().getLastActionTime());

                        if (ConfigWrap.mess().prettyDuration)
                        {
                            kickReasonString = ConfigWrap.mess().afkKickMessage + "\n <gray>(%player:displayname% was gone for: <green>"
                                    + DurationFormatUtils.formatDurationWords(afkDuration, true, true) + "<gray>)<r>";

                            if (!kickMessageString.isEmpty())
                            {
                                kickMessageString = kickMessageString + " <gray>(Gone for: <green>"
                                        + DurationFormatUtils.formatDurationWords(afkDuration, true, true) + "<gray>)<r>";
                            }
                        }
                        else
                        {
                            kickReasonString = ConfigWrap.mess().afkKickMessage + "\n <gray>(%player:displayname% was gone for: <green>"
                                    + DurationFormatUtils.formatDurationHMS(afkDuration) + "<gray>)<r>";

                            if (!kickMessageString.isEmpty())
                            {
                                kickMessageString = kickMessageString + " <gray>(Gone for: <green>"
                                        + DurationFormatUtils.formatDurationHMS(afkDuration) + "<gray>)<r>";
                            }
                        }
                    }
                    else
                    {
                        kickReasonString = ConfigWrap.mess().afkKickMessage;
                    }

                    kickReason = TextUtils.formatTextSafe(kickReasonString);
                    kickReason = Placeholders.parseText(kickReason, PlaceholderContext.of(this.player.getPlayer()));

                    this.player.setAfk(false);
                    this.player.setAfkTimeMs(0L);
                    this.player.setAfkTimeString("");
                    this.player.setAfkReason("");

                    if (!this.player.isDamageEnabled())
                    {
                        this.enableDamage();
                    }

                    this.invoker().afkplus$connection().disconnect(kickReason);

                    if (!kickMessageString.isEmpty())
                    {
                        kickMessage = TextUtils.formatTextSafe(kickMessageString);
                        kickMessage = Placeholders.parseText(kickMessage, PlaceholderContext.of(this.player.getPlayer()));

                        this.sendAfkMessage(kickMessage);
                    }
                }
                else
                {
                    kickReasonString = "<copper>AFK timeout<r>";

                    kickReason = TextUtils.formatTextSafe(kickReasonString);
                    kickReason = Placeholders.parseText(kickReason, PlaceholderContext.of(this.player.getPlayer()));

                    this.player.setAfk(false);
                    this.player.setAfkTimeMs(0L);
                    this.player.setAfkTimeString("");
                    this.player.setAfkReason("");

                    if (!this.player.isDamageEnabled())
                    {
                        this.enableDamage();
                    }

                    this.invoker().afkplus$connection().disconnect(kickReason);

                    if (!kickMessageString.isEmpty())
                    {
                        kickMessage = TextUtils.formatTextSafe(kickMessageString);
                        kickMessage = Placeholders.parseText(kickMessage, PlaceholderContext.of(this.player.getPlayer()));

                        this.sendAfkMessage(kickMessage);
                    }
                }
            }
        }
    }

    @ApiStatus.Internal
    public void updatePlayerList()
    {
        //#if MC >= 11903
        //$$ this.invoker().afkplus$server().getPlayerList().broadcastAll(new ClientboundPlayerInfoUpdatePacket(ClientboundPlayerInfoUpdatePacket.Action.UPDATE_DISPLAY_NAME, this.player.getPlayer()));
        //#else
        this.invoker().afkplus$server().getPlayerList().broadcastAll(new ClientboundPlayerInfoPacket(ClientboundPlayerInfoPacket.Action.UPDATE_DISPLAY_NAME, this.player.getPlayer()));
        //#endif
        AfkPlusMod.debugLog("sending player list update for {}", this.player.getName());
    }

    private void sendAfkMessage(Component message)
    {
        if (!ConfigWrap.mess().enableMessages || message.getString().trim().isEmpty())
        {
            return;
        }

        this.invoker().afkplus$server().sendSystemMessage(message);     // Server Log

        for (ServerPlayer player : this.invoker().afkplus$server().getPlayerList().getPlayers())
        {
            player.sendSystemMessage(message);
        }
    }

    private void toggleInvulnerable(boolean toggle)
    {
        this.invoker().afkplus$setInvulnerable(toggle);
    }

    @ApiStatus.Internal
    public void tickPlayer(@NotNull ServerPlayer player)
    {
        this.player = this.player.setPlayer(player);
        this.player.tickPlayer(Util.getMillis());
    }

    @ApiStatus.Internal
    public void reset()
    {
        if (this.invoker().afkplus$player().isInvulnerable())
        {
            this.toggleInvulnerable(false);
        }
    }

    private IPlayerInvoker invoker()
    {
        return (IPlayerInvoker) this.player.getPlayer();
    }
}
