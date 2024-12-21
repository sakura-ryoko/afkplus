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
import com.sakuraryoko.afkplus.player.IAfkPlayer;
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
    public void onConnection(SocketAddress addr, GameProfile profile, @Nullable Component result)
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

    public void onCreatePlayer(ServerPlayer player, GameProfile profile)
    {
        AfkPlusMod.debugLog("onCreatePlayer(): Player created [{}] // Profile [{}]", player.getName().getString(), profile.getName());

        // checkInvulnerable2
        IAfkPlayer iPlayer = (IAfkPlayer) player;

        if (player.gameMode.getGameModeForPlayer() == GameType.SURVIVAL)
        {
            if (player.isInvulnerable())
            {
                player.setInvulnerable(false);
                AfkPlusMod.LOGGER.info("PlayerManager().createPlayer() -> Marking SURVIVAL player: {} as vulnerable.", iPlayer.afkplus$getName());
            }
        }
        // This might simply initialize a player entry...
        iPlayer.afkplus$unregisterAfk();
        // checkInvulnerable2
    }

    public void onJoinPre(ServerPlayer player, Connection connection)
    {
        AfkPlusMod.debugLog("onJoinPre(): Player [{}] // Joining", player.getName().getString());
    }

    // ConnectedClientData ?
    public void onJoinPost(ServerPlayer player, Connection connection)
    {
        AfkPlusMod.debugLog("onJoinPost(): Player [{}] // Joined", player.getName().getString());

        // checkInvulnerable1
        IAfkPlayer iPlayer = (IAfkPlayer) player;
        if (player.gameMode.getGameModeForPlayer() == GameType.SURVIVAL)
        {
            if (player.isInvulnerable())
            {
                player.setInvulnerable(false);
                AfkPlusMod.LOGGER.info("PlayerManager().onPlayerConnect() -> Marking SURVIVAL player: {} as vulnerable.", iPlayer.afkplus$getName());
            }
        }
        if (VanishAPICompat.hasVanish() && VanishAPICompat.isVanishedByEntity(player))
        {
            return;
        }

        // This might simply initialize a player entry...
        iPlayer.afkplus$unregisterAfk();
        // Fixes some quirky-ness of Styled Player List
        if (AfkPlusInfo.isServer())
        {
            iPlayer.afkplus$updatePlayerList();
        }
        // checkInvulnerable1
    }

    public void onRespawn(ServerPlayer player)
    {
        AfkPlusMod.debugLog("onRespawn(): Player [{}] // Respawned", player.getName().getString());

        // checkInvulnerable3
        if (player.gameMode.getGameModeForPlayer() == GameType.SURVIVAL)
        {
            if (player.isInvulnerable())
            {
                IAfkPlayer iPlayer = (IAfkPlayer) player;
                AfkPlusMod.LOGGER.info("PlayerManager().repsawnPlayer() -> Marking SURVIVAL player: {} as vulnerable.", iPlayer.afkplus$getName());
                player.setInvulnerable(false);

                if (VanishAPICompat.hasVanish() && VanishAPICompat.isVanishedByEntity(player))
                {
                    return;
                }

                // This might simply initialize a player entry...
                iPlayer.afkplus$unregisterAfk();
                // Fixes some quirky-ness of Styled Player List
                if (AfkPlusInfo.isServer())
                {
                    iPlayer.afkplus$updatePlayerList();
                }
            }
        }
        // checkInvulnerable3
    }

    public void onLeave(ServerPlayer player)
    {
        AfkPlusMod.debugLog("onRespawn(): Player [{}] // Disconnected", player.getName().getString());
    }

    public void onDisconnectAll()
    {
        AfkPlusMod.debugLog("onDisconnectAll()");
    }

    public void onTickPacket(ServerPlayer player)
    {
        // updateAfkStatus
        if (VanishAPICompat.hasVanish() && VanishAPICompat.isVanishedByEntity(player))
        {
            return;
        }

        IAfkPlayer afkPlayer = (IAfkPlayer) player;
        int timeoutSeconds = ConfigWrap.pack().timeoutSeconds;
        long afkDuration = Util.getMillis() - player.getLastActionTime();
        if (afkPlayer.afkplus$isAfk() || timeoutSeconds <= 0)
        {
            if (ConfigWrap.pack().afkKickEnabled && ConfigWrap.pack().afkKickTimer > -1
                    && AfkPlusReference.MOD_ENV.equals(EnvType.SERVER))
            {
                if ((afkPlayer.afkplus$isCreative() || afkPlayer.afkplus$isSpectator()) && !ConfigWrap.pack().afkKickNonSurvival)
                {
                    return;
                }
                int kickTimeout = ConfigWrap.pack().afkKickTimer + ConfigWrap.pack().timeoutSeconds;
                if (afkDuration > (kickTimeout * 1000L))
                {
                    afkPlayer.afkplus$afkKick();
                }
            }
        }
        else if (afkPlayer.afkplus$isNoAfkEnabled())
        {
            return;
        }
        else
        {
            if (afkDuration > (timeoutSeconds * 1000L))
            {
                if (ConfigWrap.afk().afkTimeoutString.isEmpty())
                {
                    afkPlayer.afkplus$registerAfk("");
                }
                else
                {
                    afkPlayer.afkplus$registerAfk(ConfigWrap.afk().afkTimeoutString);
                }
                AfkPlusMod.debugLog("Setting player {} as AFK (timeout)", player.getName().getString());
            }
        }
        // updateAfkStatus
    }

    public void onTickPlayer(ServerPlayer player, long lastPlayerTick)
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

            IAfkPlayer iAfkPlayer = (IAfkPlayer) player;

            if (iAfkPlayer.afkplus$isAfk() && ConfigWrap.list().updateInterval > 0)
            {
                if (lastPlayerTick <= 0)
                {
                    iAfkPlayer.afkplus$updatePlayerList();
                }
                else
                {
                    long diff = Util.getMillis() - lastPlayerTick;

                    if (diff > ConfigWrap.list().updateInterval * 1000L)
                    {
                        iAfkPlayer.afkplus$updatePlayerList();
                    }
                }
            }
            if (player.isCreative())
            {
                return;
            }
            if (player.isSpectator())
            {
                return;
            }
            if (iAfkPlayer.afkplus$isLockDamageDisabled())
            {
                if (!iAfkPlayer.afkplus$isDamageEnabled())
                {
                    iAfkPlayer.afkplus$enableDamage();
                    AfkPlusMod.debugLog("checkAfk() - Damage Enabled for player: {} because they are [UNLOCKED]. step 1.", iAfkPlayer.afkplus$getName());
                }
            }
            else if (iAfkPlayer.afkplus$isAfk() && ConfigWrap.pack().disableDamage)
            {
                if (iAfkPlayer.afkplus$isDamageEnabled())
                {
                    // Stop people from abusing the /afk command for 20 seconds to get out of a "sticky situation"
                    int cooldownSeconds = ConfigWrap.pack().disableDamageCooldown;
                    if (cooldownSeconds > 0)
                    {
                        long diff = Util.getMillis() - iAfkPlayer.afkplus$getAfkTimeMs();
                        if (diff > cooldownSeconds * 1000L)
                        {
                            iAfkPlayer.afkplus$disableDamage();
                            AfkPlusMod.debugLog("checkAfk() - Damage Disabled for player: {} step 2.", iAfkPlayer.afkplus$getName());
                        }
                    }
                    else
                    {
                        if (!(player.gameMode.getPreviousGameModeForPlayer() == GameType.CREATIVE))
                        {
                            iAfkPlayer.afkplus$disableDamage();
                            AfkPlusMod.debugLog("checkAfk() - Damage Disabled for player: {} step 4.", iAfkPlayer.afkplus$getName());
                        }
                    }
                }
            }
            else
            {
                if (!iAfkPlayer.afkplus$isDamageEnabled())
                {
                    iAfkPlayer.afkplus$enableDamage();
                    AfkPlusMod.debugLog("checkAfk() - Damage Enabled for player: {} step 5.", iAfkPlayer.afkplus$getName());
                }
            }
        }
        catch (Exception e)
        {
            // Sometimes the values are null, so offer a catch
            AfkPlusMod.LOGGER.info("Caught exception during checkAfk(). ({})", e.getMessage());
        }
        // checkAfk
    }

    public void onMovement(ServerPlayer player, ServerboundMovePlayerPacket packet)
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

    public void onChangeGameMode(ServerPlayer player, GameType mode, boolean result)
    {
        AfkPlusMod.debugLog("onChangeGameMode(): Player [{}] // newGameMode [{}]", player.getName().getString(), mode.getName());

        // checkGameMode
        IAfkPlayer afkPlayer = (IAfkPlayer) player;

        if (VanishAPICompat.hasVanish() && VanishAPICompat.isVanishedByEntity(player))
        {
            return;
        }

        if (result)
        {
            AfkPlusMod.debugLog("checkGameMode() -- Invoked for player {} GameMode: {}", afkPlayer.afkplus$getName(), mode.getName());

            if (afkPlayer.afkplus$isAfk())
            {
                // Fixes uncommon de-sync when switching Game Modes.
                afkPlayer.afkplus$unregisterAfk();

                if (mode == GameType.SURVIVAL)
                {
                    if (ConfigWrap.pack().disableDamage)
                    {
                        if (player.isInvulnerable())
                        {
                            player.setInvulnerable(false);
                        }

                        afkPlayer.afkplus$enableDamage();
                    }
                }
            }
        }
        // checkGameMode
    }

    public void onResetLastAction(ServerPlayer player)
    {
        //AfkPlusMod.debugLog("onResetLastAction(): Player [{}] // Reset Last Action", player.getName().getString());

        // onActionTimeUpdate
        if (VanishAPICompat.hasVanish() && VanishAPICompat.isVanishedByEntity(player))
        {
            return;
        }

        IAfkPlayer iAfkPlayer = (IAfkPlayer) player;

        if (iAfkPlayer.afkplus$isAfk())
        {
            iAfkPlayer.afkplus$unregisterAfk();
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

    public @Nullable Component onUpdateDisplayName(ServerPlayer player, @Nullable Component name)
    {
        AfkPlusMod.debugLog("onUpdateDisplayName(): Player [{}] // setName [{}]", player.getName().getString(), name != null ? name.getString() : "<empty>");

        // replacePlayerListName
        if (VanishAPICompat.hasVanish() && VanishAPICompat.isVanishedByEntity(player))
        {
            return null;
        }

        IAfkPlayer iAfkPlayer = (IAfkPlayer) player;

        if (ConfigWrap.list().enableListDisplay && iAfkPlayer.afkplus$isAfk())
        {
            Component listEntry = Placeholders.parseText(
                    TextUtils.formatTextSafe(ConfigWrap.list().afkPlayerName),
                    PlaceholderContext.of(player)
            );
            AfkPlusMod.debugLog("replacePlayerListName-listEntry().toString(): {}", listEntry.getString());

            return listEntry.copy();
        }
        // replacePlayerListName

        return null;
    }

    public boolean onCheckSleepCount(ServerPlayer player, int countActive, int countSleeping, int totalActive, int totalSleeping)
    {
        // checkSleepCount
        // Count AFK Players into the total, they can't be marked as Sleeping, so don't increment that value.
        IAfkPlayer iPlayer = (IAfkPlayer) player;

        AfkPlusMod.debugLog("checkSleepCount(): Current: countActive {}, countSleeping {} // total {}, sleeping {}",
                            countActive, countSleeping, totalActive, totalSleeping);

        if (iPlayer.afkplus$isAfk() && ConfigWrap.pack().bypassSleepCount)
        {
            AfkPlusMod.LOGGER.info("AFK Player: {} is being excluded from the sleep requirements.", iPlayer.afkplus$getName());
            return true;
        }
        // checkSleepCount

        return false;
    }

    // Fixme IAfkPlayer
    public int onCheckBypassInsomnia(@Nullable IAfkPlayer player, int currentValue)
    {
        // checkForAfkPlayer
        if (player == null)
        {
            return currentValue;
        }

        if (player.afkplus$isAfk() && ConfigWrap.pack().bypassInsomnia)
        {
            if (currentValue > 72000)
            {
                AfkPlusMod.LOGGER.info("Afk Player: {} was just spared from a phantom spawn chance.", player.afkplus$getName());
            }
            AfkPlusMod.debugLog("checkPhantomSpawn(): [Player: {}] obtained TIME_SINCE_REST value of {} setting value to 1", player.afkplus$getName(), currentValue);
            return 1;
        }
        else
        {
            AfkPlusMod.debugLog("checkPhantomSpawn(): [Player: {}] TIME_SINCE_REST has a value of {} ", player.afkplus$getName(), currentValue);
            return currentValue;
        }
        // checkForAfkPlayer
    }

    public void onVanish(ServerPlayer player, boolean isVanished)
    {
        AfkPlusMod.debugLog("onVanish(): Player [{}] / Vanish [{}]", player.getName().getString(), isVanished);

        IAfkPlayer iAfkPlayer = (IAfkPlayer) player;

        if (iAfkPlayer.afkplus$isAfk() && isVanished)
        {
            iAfkPlayer.afkplus$unregisterAfk();
        }
    }

    public @Nullable Component getVanishMessage(ServerPlayer player)
    {
        AfkPlusMod.debugLog("getVanishMessage(): Player [{}] / Message Callback [???]", player.getName().getString());

        IAfkPlayer iAfkPlayer = (IAfkPlayer) player;

        if (iAfkPlayer.afkplus$isAfk())
        {
            iAfkPlayer.afkplus$unregisterAfk();
        }

        return null;
    }

    public @Nullable Component getUnVanishMessage(ServerPlayer player)
    {
        AfkPlusMod.debugLog("getUnVanishMessage(): Player [{}] / Message Callback [???]", player.getName().getString());
        return null;
    }
}
