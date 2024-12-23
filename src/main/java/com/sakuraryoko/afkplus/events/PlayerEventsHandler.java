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

package com.sakuraryoko.afkplus.events;

import java.net.SocketAddress;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import eu.pb4.placeholders.api.PlaceholderContext;
import eu.pb4.placeholders.api.Placeholders;

import com.mojang.authlib.GameProfile;
import net.minecraft.Util;
import net.minecraft.network.Connection;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ServerboundMovePlayerPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.GameType;
import net.fabricmc.api.EnvType;

import com.sakuraryoko.afkplus.AfkPlusMod;
import com.sakuraryoko.afkplus.AfkPlusReference;
import com.sakuraryoko.afkplus.compat.vanish.VanishAPICompat;
import com.sakuraryoko.afkplus.config.ConfigWrap;
import com.sakuraryoko.afkplus.player.AfkPlayer;
import com.sakuraryoko.afkplus.player.AfkPlayerList;
import com.sakuraryoko.afkplus.text.TextUtils;
import com.sakuraryoko.afkplus.util.AfkPlusInfo;

public class PlayerEventsHandler
{
    private static final PlayerEventsHandler INSTANCE = new PlayerEventsHandler();
    public static PlayerEventsHandler getInstance() { return INSTANCE; }

    public PlayerEventsHandler()
    {
        // NO-OP
    }
    
    // Player List Events
    public void onConnection(@Nonnull SocketAddress addr, @Nonnull GameProfile profile, @Nullable Component result)
    {
        if (result == null)
        {
            AfkPlusMod.debugLog("onConnection(): Client connection from Profile [{}]", profile.getName());
        }
        else
        {
            AfkPlusMod.debugLog("onConnection(): Client connection from Profile [{}] --> REFUSED [{}]", profile.getName(), result.getString());
        }
    }

    public void onCreatePlayer(@Nonnull ServerPlayer player, @Nonnull GameProfile profile)
    {
        AfkPlusMod.debugLog("onCreatePlayer(): Player created [{}] // Profile [{}]", player.getName().getString(), profile.getName());

        // checkInvulnerable2
        AfkPlayer afkPlayer = AfkPlayerList.getInstance().addOrGetPlayer(player);

        if (player.gameMode.getGameModeForPlayer() == GameType.SURVIVAL)
        {
            if (player.isInvulnerable())
            {
                player.setInvulnerable(false);
                AfkPlusMod.LOGGER.info("PlayerManager().createPlayer() -> Marking SURVIVAL player: {} as vulnerable.", afkPlayer.getName());
            }
        }

        // This might simply initialize a player entry...
        //afkPlayer.getHandler().unregisterAfk();

        // checkInvulnerable2
    }

    public void onJoinPre(@Nonnull ServerPlayer player, @Nonnull Connection connection)
    {
        AfkPlusMod.debugLog("onJoinPre(): Player [{}] // Joining", player.getName().getString());
    }

    // ConnectedClientData ?
    public void onJoinPost(@Nonnull ServerPlayer player, @Nonnull Connection connection)
    {
        AfkPlusMod.debugLog("onJoinPost(): Player [{}] // Joined", player.getName().getString());

        // checkInvulnerable1
        AfkPlayer afkPlayer = AfkPlayerList.getInstance().addOrGetPlayer(player);

        if (player.gameMode.getGameModeForPlayer() == GameType.SURVIVAL)
        {
            if (player.isInvulnerable())
            {
                player.setInvulnerable(false);
                AfkPlusMod.LOGGER.info("PlayerManager().onPlayerConnect() -> Marking SURVIVAL player: {} as vulnerable.", afkPlayer.getName());
            }
        }

        if (VanishAPICompat.hasVanish() && VanishAPICompat.isVanishedByEntity(player))
        {
            return;
        }

        // This might simply initialize a player entry...
        //afkPlayer.getHandler().unregisterAfk();

        // Fixes some quirky-ness of Styled Player List
        if (AfkPlusInfo.isServer())
        {
            afkPlayer.getHandler().updatePlayerList();
        }
        // checkInvulnerable1
    }

    public void onRespawn(@Nonnull ServerPlayer player)
    {
        AfkPlusMod.debugLog("onRespawn(): Player [{}] // Respawned", player.getName().getString());

        // checkInvulnerable3
        if (player.gameMode.getGameModeForPlayer() == GameType.SURVIVAL)
        {
            if (player.isInvulnerable())
            {
                AfkPlayer afkPlayer = AfkPlayerList.getInstance().addOrGetPlayer(player);

                AfkPlusMod.LOGGER.info("PlayerManager().repsawnPlayer() -> Marking SURVIVAL player: {} as vulnerable.", afkPlayer.getName());
                player.setInvulnerable(false);

                if (VanishAPICompat.hasVanish() && VanishAPICompat.isVanishedByEntity(player))
                {
                    return;
                }

                // This might simply initialize a player entry...
                //afkPlayer.getHandler().unregisterAfk();

                // Fixes some quirky-ness of Styled Player List
                if (AfkPlusInfo.isServer())
                {
                    afkPlayer.getHandler().updatePlayerList();
                }
            }
        }
        // checkInvulnerable3
    }

    public void onLeave(@Nonnull ServerPlayer player)
    {
        AfkPlusMod.debugLog("onRespawn(): Player [{}] // Disconnected", player.getName().getString());
        AfkPlayerList.getInstance().removePlayer(player);
    }

    public void onDisconnectAll()
    {
        AfkPlusMod.debugLog("onDisconnectAll()");
        AfkPlayerList.getInstance().removeAllPlayers();
    }

    public void onTickPacket(@Nonnull ServerPlayer player)
    {
        // updateAfkStatus
        if (VanishAPICompat.hasVanish() && VanishAPICompat.isVanishedByEntity(player))
        {
            return;
        }

        AfkPlayer afkPlayer = AfkPlayerList.getInstance().addOrGetPlayer(player);
        int timeoutSeconds = ConfigWrap.pack().timeoutSeconds;
        long afkDuration = Util.getMillis() - player.getLastActionTime();

        if (afkPlayer.isAfk() || timeoutSeconds <= 0)
        {
            if (ConfigWrap.pack().afkKickEnabled && ConfigWrap.pack().afkKickTimer > -1
                && AfkPlusReference.MOD_ENV.equals(EnvType.SERVER))
            {
                if ((afkPlayer.getPlayer().isCreative() || afkPlayer.getPlayer().isSpectator()) && !ConfigWrap.pack().afkKickNonSurvival)
                {
                    return;
                }

                int kickTimeout = ConfigWrap.pack().afkKickTimer + ConfigWrap.pack().timeoutSeconds;

                if (afkDuration > (kickTimeout * 1000L))
                {
                    AfkPlusMod.debugLog("onTickPacket(): Kicking player {} from AFK (timeout)", afkPlayer.getName());
                    afkPlayer.getHandler().afkKick();
                    // They should get removed by the onRemove()
                }
            }
        }
        else if (!afkPlayer.isNoAfkEnabled())
        {
            if (afkDuration > (timeoutSeconds * 1000L))
            {
                if (ConfigWrap.afk().afkTimeoutString.isEmpty())
                {
                    afkPlayer.getHandler().registerAfk("");
                }
                else
                {
                    afkPlayer.getHandler().registerAfk(ConfigWrap.afk().afkTimeoutString);
                }

                AfkPlusMod.debugLog("onTickPacket(): Setting player {} as AFK (timeout)", afkPlayer.getName());
            }
        }
        // updateAfkStatus
    }

    public void onTickPlayer(@Nonnull ServerPlayer player)
    {
        // checkAfk
        try
        {
            //#if MC >= 11904
            //$$ if (!player.connection.isAcceptingMessages())
            //#else
            if (!player.connection.getConnection().isConnected())
            //#endif
            {
                return;
            }

            if (VanishAPICompat.hasVanish() && VanishAPICompat.isVanishedByEntity(player))
            {
                return;
            }

            AfkPlayer afkPlayer = AfkPlayerList.getInstance().addOrGetPlayer(player);

            if (afkPlayer.isAfk() && ConfigWrap.list().updateInterval > 0)
            {
                if (afkPlayer.getLastPlayerTick() <= 0)
                {
                    afkPlayer.getHandler().updatePlayerList();
                }
                else
                {
                    long diff = Util.getMillis() - afkPlayer.getLastPlayerTick();

                    if (diff > ConfigWrap.list().updateInterval * 1000L)
                    {
                        afkPlayer.getHandler().updatePlayerList();
                    }
                }
            }

            if (player.isCreative() || player.isSpectator())
            {
                return;
            }

            if (afkPlayer.isLockDamageEnabled())
            {
                if (!afkPlayer.isDamageEnabled())
                {
                    afkPlayer.getHandler().enableDamage();
                    AfkPlusMod.debugLog("onTickPlayer() - Damage Enabled for player: {} because they are [LOCKED]. step 1.", afkPlayer.getName());
                }
            }
            else if (afkPlayer.isAfk() && ConfigWrap.pack().disableDamage)
            {
                if (afkPlayer.isDamageEnabled())
                {
                    // Stop people from abusing the /afk command for 20 seconds to get out of a "sticky situation"
                    int cooldownSeconds = ConfigWrap.pack().disableDamageCooldown;

                    if (cooldownSeconds > 0)
                    {
                        long diff = Util.getMillis() - afkPlayer.getAfkTimeMs();

                        if (diff > cooldownSeconds * 1000L)
                        {
                            afkPlayer.getHandler().disableDamage();
                            AfkPlusMod.debugLog("onTickPlayer() - Damage Disabled for player: {} step 2.", afkPlayer.getName());
                        }
                    }
                    else
                    {
                        if (!(player.gameMode.getPreviousGameModeForPlayer() == GameType.CREATIVE))
                        {
                            afkPlayer.getHandler().disableDamage();
                            AfkPlusMod.debugLog("onTickPlayer() - Damage Disabled for player: {} step 4.", afkPlayer.getName());
                        }
                    }
                }
            }
            else
            {
                if (!afkPlayer.isDamageEnabled())
                {
                    afkPlayer.getHandler().enableDamage();
                    AfkPlusMod.debugLog("onTickPlayer() - Damage Enabled for player: {} step 5.", afkPlayer.getName());
                }
            }

            afkPlayer.getHandler().tickPlayer(player);
        }
        catch (Exception e)
        {
            // Sometimes the values are null, so offer a catch
            AfkPlusMod.LOGGER.info("Caught exception during onTickPlayer(). ({})", e.getMessage());
        }
        // checkAfk
    }

    public void onMovement(@Nonnull ServerPlayer player, ServerboundMovePlayerPacket packet)
    {
        //AfkPlusMod.debugLog("onMovement(): Player [{}] // (moved)", player.getName().getString());

        // checkPlayerLook
        if (VanishAPICompat.hasVanish() && VanishAPICompat.isVanishedByEntity(player))
        {
            return;
        }

        if (ConfigWrap.pack().resetOnLook && packet.hasRotation())
        {
            player.getEyeY();
            float yaw = player.getYRot();
            float pitch = player.getXRot();
            if (pitch != packet.getXRot(pitch) || yaw != packet.getYRot(yaw))
            {
                player.resetLastActionTime();
            }
        }
        // checkPlayerLook
    }

    public void onChangeGameMode(@Nonnull ServerPlayer player, GameType mode, boolean result)
    {
        AfkPlusMod.debugLog("onChangeGameMode(): Player [{}] // newGameMode [{}]", player.getName().getString(), mode.getName());

        // checkGameMode
        AfkPlayer afkPlayer = AfkPlayerList.getInstance().addOrGetPlayer(player);

        if (VanishAPICompat.hasVanish() && VanishAPICompat.isVanishedByEntity(player))
        {
            return;
        }

        if (result)
        {
            AfkPlusMod.debugLog("onChangeGameMode() -- Invoked for player {} GameMode: {}", afkPlayer.getName(), mode.getName());

            if (afkPlayer.isAfk())
            {
                // Fixes uncommon de-sync when switching Game Modes.
                afkPlayer.getHandler().unregisterAfk();

                if (mode == GameType.SURVIVAL)
                {
                    if (ConfigWrap.pack().disableDamage)
                    {
                        if (player.isInvulnerable())
                        {
                            player.setInvulnerable(false);
                        }

                        afkPlayer.getHandler().enableDamage();
                    }
                }
            }
        }
        // checkGameMode
    }

    public void onResetLastAction(@Nonnull ServerPlayer player)
    {
        // onActionTimeUpdate
        if (VanishAPICompat.hasVanish() && VanishAPICompat.isVanishedByEntity(player))
        {
            return;
        }

        AfkPlayer afkPlayer = AfkPlayerList.getInstance().addOrGetPlayer(player);

        if (afkPlayer.isAfk())
        {
            AfkPlusMod.debugLog("onResetLastAction(): Player [{}] // Reset Last Action (Remove AFK)", afkPlayer.getName());
            afkPlayer.getHandler().unregisterAfk();
        }
        // onActionTimeUpdate
    }

    public void onSetPos(@Nullable ServerPlayer player, double x, double y, double z)
    {
        if (player != null)
        {
            if (VanishAPICompat.hasVanish() && VanishAPICompat.isVanishedByEntity(player))
            {
                return;
            }

            //AfkPlusMod.debugLog("onSetPos(): Player [{}] // setPos[{}]", player.getName().getString(), new Vector3d(x, y, z));

            if (ConfigWrap.pack().resetOnMovement && (player.getX() != x || player.getY() != y || player.getZ() != z))
            {
                player.resetLastActionTime();
            }
        }
    }

    public boolean onCheckIfPushable(@Nonnull ServerPlayer player, boolean value)
    {
        AfkPlayer afkPlayer = AfkPlayerList.getInstance().addOrGetPlayer(player);

        if (afkPlayer.isAfk() && !afkPlayer.isDamageEnabled() && player.isInvulnerable())
        {
            return false;
        }

        return value;
    }

    public @Nullable Component onUpdateDisplayName(@Nonnull ServerPlayer player, @Nullable Component name)
    {
        //AfkPlusMod.debugLog("onUpdateDisplayName(): Player [{}] // setName [{}]", player.getName().getString(), name != null ? name.getString() : "<empty>");

        // replacePlayerListName
        if (VanishAPICompat.hasVanish() && VanishAPICompat.isVanishedByEntity(player))
        {
            return null;
        }

        AfkPlayer afkPlayer = AfkPlayerList.getInstance().addOrGetPlayer(player);

        if (ConfigWrap.list().enableListDisplay && afkPlayer.isAfk())
        {
            Component listEntry = Placeholders.parseText(
                    TextUtils.formatTextSafe(ConfigWrap.list().afkPlayerName),
                    PlaceholderContext.of(player)
            );

            //AfkPlusMod.debugLog("replacePlayerListName-listEntry().toString(): {}", listEntry.getString());

            return listEntry.copy();
        }
        // replacePlayerListName

        return null;
    }

    public boolean onCheckSleepCount(@Nonnull ServerPlayer player, int countActive, int countSleeping, int totalActive, int totalSleeping)
    {
        // checkSleepCount
        // Count AFK Players into the total, they can't be marked as Sleeping, so don't increment that value.
        AfkPlayer afkPlayer = AfkPlayerList.getInstance().addOrGetPlayer(player);

        AfkPlusMod.debugLog("checkSleepCount(): Current: countActive {}, countSleeping {} // total {}, sleeping {}",
                            countActive, countSleeping, totalActive, totalSleeping);

        if (afkPlayer.isAfk() && ConfigWrap.pack().bypassSleepCount)
        {
            AfkPlusMod.LOGGER.info("AFK Player: {} is being excluded from the sleep requirements.", afkPlayer.getName());
            return true;
        }
        // checkSleepCount

        return false;
    }

    // Fixme IAfkPlayer
    public int onCheckBypassInsomnia(@Nullable ServerPlayer player, int currentValue)
    {
        // checkForAfkPlayer
        if (player == null)
        {
            return currentValue;
        }

        AfkPlayer afkPlayer = AfkPlayerList.getInstance().addOrGetPlayer(player);

        if (afkPlayer.isAfk() && ConfigWrap.pack().bypassInsomnia)
        {
            if (currentValue > 72000)
            {
                AfkPlusMod.LOGGER.info("Afk Player: {} was just spared from a phantom spawn chance.", afkPlayer.getName());
            }

            AfkPlusMod.debugLog("checkPhantomSpawn(): [Player: {}] obtained TIME_SINCE_REST value of {} setting value to 1", afkPlayer.getName(), currentValue);

            return 1;
        }
        else
        {
            AfkPlusMod.debugLog("checkPhantomSpawn(): [Player: {}] TIME_SINCE_REST has a value of {} ", afkPlayer.getName(), currentValue);
            return currentValue;
        }
        // checkForAfkPlayer
    }

    public void onVanish(@Nonnull ServerPlayer player, boolean isVanished)
    {
        AfkPlusMod.debugLog("onVanish(): Player [{}] / Vanish [{}]", player.getName().getString(), isVanished);

        AfkPlayer afkPlayer = AfkPlayerList.getInstance().addOrGetPlayer(player);

        if (afkPlayer.isAfk() && isVanished)
        {
            afkPlayer.getHandler().unregisterAfkSilently();
        }
    }

    public @Nullable Component getVanishMessage(@Nonnull ServerPlayer player)
    {
        AfkPlusMod.debugLog("getVanishMessage(): Player [{}] / Message Callback [???]", player.getName().getString());

        AfkPlayer afkPlayer = AfkPlayerList.getInstance().addOrGetPlayer(player);

        if (afkPlayer.isAfk())
        {
            afkPlayer.getHandler().unregisterAfkSilently();
        }

        return null;
    }

    public @Nullable Component getUnVanishMessage(@Nonnull ServerPlayer player)
    {
        AfkPlusMod.debugLog("getUnVanishMessage(): Player [{}] / Message Callback [???]", player.getName().getString());
        //AfkPlayer afkPlayer = AfkPlayerList.getInstance().addOrGetPlayer(player);
        return null;
    }
}
