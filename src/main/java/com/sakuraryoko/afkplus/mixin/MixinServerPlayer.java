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

package com.sakuraryoko.afkplus.mixin;

import eu.pb4.placeholders.api.PlaceholderContext;
import eu.pb4.placeholders.api.Placeholders;
import me.lucko.fabric.api.permissions.v0.Permissions;
import org.apache.commons.lang3.time.DurationFormatUtils;

import net.minecraft.Util;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundPlayerInfoUpdatePacket;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.sakuraryoko.afkplus.compat.TextUtils;
import com.sakuraryoko.afkplus.data.IAfkPlayer;
import com.sakuraryoko.afkplus.util.AfkPlusLogger;

import static com.sakuraryoko.afkplus.config.ConfigManager.CONFIG;

@Mixin(ServerPlayer.class)
public abstract class MixinServerPlayer extends Entity implements IAfkPlayer
{
    @Shadow
    @Final
    public MinecraftServer server;

    @Shadow
    public abstract boolean isCreative();

    @Shadow
    public abstract boolean isSpectator();

    @Shadow
    public ServerGamePacketListenerImpl connection;
    @Unique
    public ServerPlayer player = (ServerPlayer) (Object) this;
    @Unique
    private boolean isAfk = false;
    @Unique
    private long afkTimeMs = 0;
    @Unique
    private String afkTimeString = "";
    @Unique
    private String afkReason = "";
    @Unique
    private boolean isDamageEnabled = true;
    @Unique
    private boolean isLockDamageDisabled = false;
    @Unique
    private boolean noAfkEnabled = false;
    @Unique
    private long lastPlayerListTick = 0;

    public MixinServerPlayer(EntityType<?> type, Level world)
    {
        super(type, world);
    }

    @Unique
    public boolean afkplus$isAfk()
    {
        return this.isAfk;
    }

    @Unique
    public long afkplus$getAfkTimeMs()
    {
        return this.afkTimeMs;
    }

    @Unique
    public String afkplus$getAfkTimeString()
    {
        return this.afkTimeString;
    }

    @Unique
    public String afkplus$getAfkReason()
    {
        return this.afkReason;
    }

    @Unique
    public boolean afkplus$isDamageEnabled()
    {
        return this.isDamageEnabled;
    }

    @Unique
    public boolean afkplus$isLockDamageDisabled()
    {
        return this.isLockDamageDisabled;
    }

    @Unique
    public long afkplus$getLastPlayerListTick()
    {
        return this.lastPlayerListTick;
    }

    @Unique
    public void afkplus$registerAfk(String reason)
    {
        if (afkplus$isAfk())
        {
            return;
        }
        setAfkTime();
        if (reason == null && CONFIG.messageOptions.defaultReason == null)
        {
            setAfkReason("<red>none");
        }
        else if (reason == null || reason.isEmpty())
        {
            setAfkReason("<red>none");
            Component mess = Placeholders.parseText(TextUtils.formatTextSafe(CONFIG.messageOptions.whenAfk),
                                                    PlaceholderContext.of(this));

            //AfkPlusLogger.debug("registerafk-mess().toString(): " + mess.toString());
            sendAfkMessage(mess);
        }
        else
        {
            setAfkReason(reason);
            String mess1 = CONFIG.messageOptions.whenAfk + "<yellow>,<r> " + reason;
            Component mess2 = Placeholders.parseText(TextUtils.formatTextSafe(mess1), PlaceholderContext.of(player));
            sendAfkMessage(mess2);
        }
        if (CONFIG.packetOptions.disableDamage && CONFIG.packetOptions.disableDamageCooldown < 1)
        {
            afkplus$disableDamage();
        }
        afkplus$updatePlayerList();
        setAfk(true);
    }

    @Unique
    public void afkplus$unregisterAfk()
    {
        if (!afkplus$isAfk())
        {
            // Maybe it was called by PlayerManagerMixin?
            setAfk(false);
            clearAfkTime();
            clearAfkReason();
            return;
        }
        if (CONFIG.messageOptions.prettyDuration && CONFIG.messageOptions.displayDuration)
        {
            long duration = Util.getMillis() - (this.afkTimeMs);
            String ret = CONFIG.messageOptions.whenReturn + " <gray>(Gone for: <green>"
                    + DurationFormatUtils.formatDurationWords(duration, true, true) + "<gray>)<r>";

            Component mess1 = TextUtils.formatTextSafe(ret);
            Component mess2 = Placeholders.parseText(mess1, PlaceholderContext.of(this));
            sendAfkMessage(mess2);
        }
        else if (CONFIG.messageOptions.displayDuration)
        {
            long duration = Util.getMillis() - (this.afkTimeMs);
            String ret = CONFIG.messageOptions.whenReturn + " <gray>(Gone for: <green>"
                    + DurationFormatUtils.formatDurationHMS(duration) + "<gray>)<r>";

            Component mess1 = TextUtils.formatTextSafe(ret);
            Component mess2 = Placeholders.parseText(mess1, PlaceholderContext.of(player));
            sendAfkMessage(mess2);
        }
        else
        {
            String ret = CONFIG.messageOptions.whenReturn + "<r>";

            Component mess1 = TextUtils.formatTextSafe(ret);
            Component mess2 = Placeholders.parseText(mess1, PlaceholderContext.of(player));
            sendAfkMessage(mess2);
        }
        setAfk(false);
        clearAfkTime();
        clearAfkReason();
        if (!afkplus$isDamageEnabled())
        {
            afkplus$enableDamage();
        }
        afkplus$updatePlayerList();
    }

    @Unique
    public void afkplus$updatePlayerList()
    {
        this.server.getPlayerList().broadcastAll(new ClientboundPlayerInfoUpdatePacket(ClientboundPlayerInfoUpdatePacket.Action.UPDATE_DISPLAY_NAME, player));
        AfkPlusLogger.debug("sending player list update for " + afkplus$getName());
        this.lastPlayerListTick = Util.getMillis();
    }

    @Unique
    public String afkplus$getName()
    {
        return player.getName().getString();
    }

    @Unique
    private void sendAfkMessage(Component text)
    {
        if (!CONFIG.messageOptions.enableMessages || text.getString().trim().isEmpty())
        {
            return;
        }
        server.sendSystemMessage(text);
        for (ServerPlayer player : this.server.getPlayerList().getPlayers())
        {
            player.sendSystemMessage(text);
        }
    }

    @Unique
    private void setAfk(boolean isAfk)
    {
        this.isAfk = isAfk;
    }

    @Unique
    private void setAfkTime()
    {
        this.afkTimeMs = Util.getMillis();
        this.afkTimeString = Util.getFilenameFormattedDateTime();
    }

    @Unique
    private void clearAfkTime()
    {
        this.afkTimeMs = 0;
        this.afkTimeString = "";
    }

    @Unique
    private void setAfkReason(String reason)
    {
        if (reason == null || reason.isEmpty())
        {
            this.afkReason = "";
        }
        else
        {
            this.afkReason = reason;
        }
    }

    @Unique
    private void clearAfkReason()
    {
        this.afkReason = "";
    }

    @Unique
    public void afkplus$disableDamage()
    {
        AfkPlusLogger.debug("disableDamage() has been invoked for: " + afkplus$getName());
        if (player.isCreative())
        {
            return;
        }
        if (player.isSpectator())
        {
            return;
        }
        if (!CONFIG.packetOptions.disableDamage)
        {
            return;
        }
        if (afkplus$isLockDamageDisabled())
        {
            AfkPlusLogger.info("Disable Damage is locked from player: " + afkplus$getName());
            return;
        }
        if (afkplus$isAfk())
        {
            if (afkplus$isDamageEnabled())
            {
                this.isDamageEnabled = false;
                if (!player.isInvulnerable())
                {
                    player.setInvulnerable(true);
                    AfkPlusLogger.info("Damage Disabled for player: " + afkplus$getName());
                }
                // Send announcement
                if (!CONFIG.messageOptions.whenDamageDisabled.isEmpty())
                {
                    Component mess1 = TextUtils.formatTextSafe(CONFIG.messageOptions.whenDamageDisabled);
                    Component mess2 = Placeholders.parseText(mess1, PlaceholderContext.of(player));
                    sendAfkMessage(mess2);
                }
            }
            afkplus$updatePlayerList();
        }
    }

    @Unique
    public void afkplus$enableDamage()
    {
        // Doesn't matter if they are marked as AFK --> make them not Invulnerable.
        AfkPlusLogger.debug("enableDamage() has been invoked for: " + afkplus$getName());
        if (player.isCreative())
        {
            return;
        }
        if (player.isSpectator())
        {
            return;
        }
        // They don't need to be AFK, and 'public' makes it so /afkplus damage [Player] works
        if (!afkplus$isDamageEnabled())
        {
            this.isDamageEnabled = true;
            if (player.isInvulnerable())
            {
                player.setInvulnerable(false);
                AfkPlusLogger.info("Damage Enabled for player: " + afkplus$getName());
            }
            // Send announcement
            if (!CONFIG.messageOptions.whenDamageEnabled.isEmpty())
            {
                Component mess1 = TextUtils.formatTextSafe(CONFIG.messageOptions.whenDamageEnabled);
                Component mess2 = Placeholders.parseText(mess1, PlaceholderContext.of(player));
                sendAfkMessage(mess2);
            }
        }
        afkplus$updatePlayerList();
    }

    @Unique
    public void afkplus$afkKick()
    {
        if (afkplus$isAfk() && CONFIG.packetOptions.afkKickEnabled)
        {
            if ((player.isCreative() || player.isSpectator()) && !CONFIG.packetOptions.afkKickNonSurvival)
            {
                return;
            }
            else if (Permissions.check(player, "afkplus.kick.safe", CONFIG.packetOptions.afkKickSafePermissions))
            {
                return;
            }
            else if (Permissions.check(player, "afkplus.afkplus", CONFIG.afkPlusOptions.afkPlusCommandPermissions))
            {
                return;
            }
            else
            {
                String kickReasonString;
                String kickMessageString;
                Component kickReason;
                Component kickMessage;

                if (CONFIG.messageOptions.whenKicked.isEmpty())
                {
                    kickMessageString = "";
                }
                else
                {
                    kickMessageString = CONFIG.messageOptions.whenKicked;
                }

                AfkPlusLogger.warn("Configured timeout has been reached for player " + afkplus$getName() + " --> removing from server.");

                if (!CONFIG.messageOptions.afkKickMessage.isEmpty())
                {
                    if (CONFIG.messageOptions.displayDuration)
                    {
                        long afkDuration = Util.getMillis() - (player.getLastActionTime());
                        if (CONFIG.messageOptions.prettyDuration)
                        {
                            kickReasonString = CONFIG.messageOptions.afkKickMessage + "\n <gray>(%player:displayname% was gone for: <green>"
                                    + DurationFormatUtils.formatDurationWords(afkDuration, true, true) + "<gray>)<r>";
                            if (!kickMessageString.isEmpty())
                            {
                                kickMessageString = kickMessageString + " <gray>(Gone for: <green>"
                                        + DurationFormatUtils.formatDurationWords(afkDuration, true, true) + "<gray>)<r>";
                            }
                        }
                        else
                        {
                            kickReasonString = CONFIG.messageOptions.afkKickMessage + "\n <gray>(%player:displayname% was gone for: <green>"
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
                        kickReasonString = CONFIG.messageOptions.afkKickMessage;
                    }
                    kickReason = TextUtils.formatTextSafe(kickReasonString);
                    kickReason = Placeholders.parseText((Component) kickReason, PlaceholderContext.of(player));

                    setAfk(false);
                    clearAfkTime();
                    clearAfkReason();
                    if (!afkplus$isDamageEnabled())
                    {
                        afkplus$enableDamage();
                    }

                    player.connection.disconnect(kickReason);

                    if (!kickMessageString.isEmpty())
                    {
                        kickMessage = TextUtils.formatTextSafe(kickMessageString);
                        kickMessage = Placeholders.parseText(kickMessage, PlaceholderContext.of(player));

                        sendAfkMessage(kickMessage);
                    }
                }
                else
                {
                    kickReasonString = "<copper>AFK timeout<r>";

                    kickReason = TextUtils.formatTextSafe(kickReasonString);
                    kickReason = Placeholders.parseText(kickReason, PlaceholderContext.of(player));

                    setAfk(false);
                    clearAfkTime();
                    clearAfkReason();
                    if (!afkplus$isDamageEnabled())
                    {
                        afkplus$enableDamage();
                    }

                    player.connection.disconnect(kickReason);

                    if (!kickMessageString.isEmpty())
                    {
                        kickMessage = TextUtils.formatTextSafe(kickMessageString);
                        kickMessage = Placeholders.parseText(kickMessage, PlaceholderContext.of(player));

                        sendAfkMessage(kickMessage);
                    }
                }
            }
        }
    }

    @Unique
    public void afkplus$lockDamageDisabled()
    {
        this.isLockDamageDisabled = true;
    }

    @Unique
    public void afkplus$unlockDamageDisabled()
    {
        this.isLockDamageDisabled = false;
    }

    @Unique
    public boolean afkplus$isCreative()
    {
        return this.isCreative();
    }

    @Unique
    public boolean afkplus$isSpectator()
    {
        return this.isSpectator();
    }

    @Unique
    public boolean afkplus$isNoAfkEnabled()
    {
        return this.noAfkEnabled;
    }

    @Unique
    public void afkplus$setNoAfkEnabled()
    {
        this.noAfkEnabled = true;
    }

    @Unique
    public void afkplus$unsetNoAfkEnabled()
    {
        this.noAfkEnabled = false;
    }

    @Inject(method = "resetLastActionTime", at = @At("TAIL"))
    private void onActionTimeUpdate(CallbackInfo ci)
    {
        afkplus$unregisterAfk();
    }

    @Override
    public void setPos(double x, double y, double z)
    {
        if (CONFIG.packetOptions.resetOnMovement && (this.getX() != x || this.getY() != y || this.getZ() != z))
        {
            player.resetLastActionTime();
        }
        super.setPos(x, y, z);
    }

    @Inject(method = "getTabListDisplayName", at = @At("RETURN"), cancellable = true)
    private void replacePlayerListName(CallbackInfoReturnable<Component> cir)
    {
        if (CONFIG.playerListOptions.enableListDisplay && afkplus$isAfk())
        {
            Component listEntry = Placeholders.parseText(
                    TextUtils.formatTextSafe(CONFIG.playerListOptions.afkPlayerName),
                    PlaceholderContext.of(this));
            AfkPlusLogger.debug("replacePlayerListName-listEntry().toString(): " + listEntry.getString());
            cir.setReturnValue(listEntry.copy());
        }
    }

    @Inject(method = "tick", at = @At("HEAD"))
    private void checkAfk(CallbackInfo ci)
    {
        try
        {
            if (!this.player.connection.isAcceptingMessages())
            {
                return;
            }
            if (this.afkplus$isAfk() && CONFIG.playerListOptions.updateInterval > 0)
            {
                if (this.lastPlayerListTick <= 0)
                {
                    this.afkplus$updatePlayerList();
                }
                else
                {
                    long diff = Util.getMillis() - this.lastPlayerListTick;

                    if (diff > CONFIG.playerListOptions.updateInterval * 1000L)
                    {
                        this.afkplus$updatePlayerList();
                    }
                }
            }
            if (this.player.isCreative())
            {
                return;
            }
            if (this.player.isSpectator())
            {
                return;
            }
            if (this.afkplus$isLockDamageDisabled())
            {
                if (!this.afkplus$isDamageEnabled())
                {
                    this.afkplus$enableDamage();
                    AfkPlusLogger.debug("checkAfk() - Damage Enabled for player: " + this.afkplus$getName() + " because they are [UNLOCKED]. step 1.");
                }
            }
            else if (this.afkplus$isAfk() && CONFIG.packetOptions.disableDamage)
            {
                if (this.afkplus$isDamageEnabled())
                {
                    // Stop people from abusing the /afk command for 20 seconds to get out of a "sticky situation"
                    int cooldownSeconds = CONFIG.packetOptions.disableDamageCooldown;
                    if (cooldownSeconds > 0)
                    {
                        long diff = Util.getMillis() - this.afkTimeMs;
                        if (diff > cooldownSeconds * 1000L)
                        {
                            this.afkplus$disableDamage();
                            AfkPlusLogger.debug("checkAfk() - Damage Disabled for player: " + this.afkplus$getName() + " step 2.");
                        }
                    }
                    else
                    {
                        if (!(this.player.gameMode.getPreviousGameModeForPlayer() == GameType.CREATIVE))
                        {
                            this.afkplus$disableDamage();
                            AfkPlusLogger.debug("checkAfk() - Damage Disabled for player: " + this.afkplus$getName() + " step 4.");
                        }
                    }
                }
            }
            else
            {
                if (!this.afkplus$isDamageEnabled())
                {
                    this.afkplus$enableDamage();
                    AfkPlusLogger.debug("checkAfk() - Damage Enabled for player: " + this.afkplus$getName() + " step 5.");
                }
            }
        }
        catch (Exception e)
        {
            // Sometimes the values are null, so offer a catch
            AfkPlusLogger.info("Caught exception during checkAfk(). (" + e.getMessage() + ")");
        }
    }
}
