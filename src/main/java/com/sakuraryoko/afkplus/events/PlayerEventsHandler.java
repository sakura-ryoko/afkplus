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
import org.jetbrains.annotations.ApiStatus;

import com.mojang.authlib.GameProfile;
import net.minecraft.Util;
import net.minecraft.network.Connection;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ServerboundMovePlayerPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.GameType;

import com.sakuraryoko.afkplus.AfkPlus;
import com.sakuraryoko.afkplus.compat.morecolors.TextHandler;
import com.sakuraryoko.afkplus.compat.vanish.VanishAPICompat;
import com.sakuraryoko.afkplus.config.ConfigWrap;
import com.sakuraryoko.afkplus.modinit.AfkPlusInit;
import com.sakuraryoko.afkplus.player.AfkPlayer;
import com.sakuraryoko.afkplus.player.AfkPlayerList;
import com.sakuraryoko.corelib.api.events.IPlayerEventsDispatch;

@ApiStatus.Internal
public class PlayerEventsHandler implements IPlayerEventsDispatch
{
    //private static final AnsiLogger LOGGER = new AnsiLogger(PlayerEventsHandler.class, true);
    private static final PlayerEventsHandler INSTANCE = new PlayerEventsHandler();
    public static PlayerEventsHandler getInstance() { return INSTANCE; }

    @ApiStatus.Internal
    public PlayerEventsHandler() { }
    
    // Player List Events
    @Override
    @ApiStatus.Internal
    public void onConnection(@Nonnull SocketAddress addr, @Nonnull GameProfile profile, @Nullable Component result)
    {
        if (result == null)
        {
            AfkPlus.debugLog("onConnection(): Client connection from Profile [{}]", profile.getName());
        }
        else
        {
            AfkPlus.debugLog("onConnection(): Client connection from Profile [{}] --> REFUSED [{}]", profile.getName(), result.getString());
        }
    }

    @Override
    @ApiStatus.Internal
    public void onCreatePlayer(@Nonnull ServerPlayer player, @Nonnull GameProfile profile)
    {
        AfkPlus.debugLog("onCreatePlayer(): Player created [{}] // Profile [{}]", player.getName().getString(), profile.getName());

        // checkInvulnerable2
        AfkPlayer afkPlayer = AfkPlayerList.getInstance().addOrGetPlayer(player);

        if (player.gameMode.getGameModeForPlayer() == GameType.SURVIVAL)
        {
            if (player.isInvulnerable())
            {
                AfkPlus.LOGGER.warn("PlayerManager().createPlayer() -> Marking SURVIVAL player: {} as vulnerable.", afkPlayer.getName());
                player.setInvulnerable(false);
            }
        }
        // checkInvulnerable2
    }

    @Override
    @ApiStatus.Internal
    public void onPlayerJoinPre(ServerPlayer player, Connection connection)
    {
        AfkPlus.debugLog("onJoinPre(): Player [{}] // Joining", player.getName().getString());
    }

    @Override
    @ApiStatus.Internal
    public void onPlayerJoinPost(ServerPlayer player, Connection connection)
    {
        AfkPlus.debugLog("onJoinPost(): Player [{}] // Joined", player.getName().getString());

        // checkInvulnerable1
        AfkPlayer afkPlayer = AfkPlayerList.getInstance().addOrGetPlayer(player);

        if (player.gameMode.getGameModeForPlayer() == GameType.SURVIVAL)
        {
            if (player.isInvulnerable())
            {
                AfkPlus.LOGGER.warn("PlayerManager().onPlayerConnect() -> Marking SURVIVAL player: {} as vulnerable.", afkPlayer.getName());
                player.setInvulnerable(false);
            }
        }

        if (VanishAPICompat.hasVanish() && VanishAPICompat.isVanishedByEntity(player))
        {
            return;
        }

        // Fixes some quirky-ness of Styled Player List
        if (AfkPlusInit.getInstance().isServer())
        {
            afkPlayer.getHandler().updatePlayerList();
        }
        // checkInvulnerable1
    }

    @Override
    @ApiStatus.Internal
    public void onPlayerRespawn(ServerPlayer player)
    {
        AfkPlus.debugLog("onRespawn(): Player [{}] // Respawned", player.getName().getString());

        // checkInvulnerable3
        if (player.gameMode.getGameModeForPlayer() == GameType.SURVIVAL)
        {
            if (player.isInvulnerable())
            {
                AfkPlayer afkPlayer = AfkPlayerList.getInstance().addOrGetPlayer(player);

                AfkPlus.LOGGER.warn("PlayerManager().repsawnPlayer() -> Marking SURVIVAL player: {} as vulnerable.", afkPlayer.getName());
                player.setInvulnerable(false);

                if (VanishAPICompat.hasVanish() && VanishAPICompat.isVanishedByEntity(player))
                {
                    return;
                }

                // Fixes some quirky-ness of Styled Player List
                if (AfkPlusInit.getInstance().isServer())
                {
                    afkPlayer.getHandler().updatePlayerList();
                }
            }
        }
        // checkInvulnerable3
    }

    @Override
    @ApiStatus.Internal
    public void onPlayerLeave(ServerPlayer player)
    {
        AfkPlus.debugLog("onRespawn(): Player [{}] // Disconnected", player.getName().getString());
        AfkPlayerList.getInstance().removePlayer(player);
    }

    @Override
    @ApiStatus.Internal
    public void onDisconnectAll()
    {
        AfkPlus.debugLog("onDisconnectAll()");
        AfkPlayerList.getInstance().removeAllPlayers();
    }

    @Override
    @ApiStatus.Internal
    public void onSetViewDistance(int i)
    {
        // NO-OP
    }

    @Override
    @ApiStatus.Internal
    public void onSetSimulationDistance(int i)
    {
        // NO-OP
    }

    @ApiStatus.Internal
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
            if (ConfigWrap.kick().afkKickEnabled && ConfigWrap.kick().afkKickTimer > -1
                && AfkPlusInit.getInstance().isServer())
            {
                if ((afkPlayer.getPlayer().isCreative() || afkPlayer.getPlayer().isSpectator()) && !ConfigWrap.kick().afkKickNonSurvival)
                {
                    return;
                }

                int kickTimeout = ConfigWrap.kick().afkKickTimer + ConfigWrap.pack().timeoutSeconds;

                if (afkDuration > (kickTimeout * 1000L))
                {
                    AfkPlus.debugLog("onTickPacket(): Kicking player {} from AFK (timeout)", afkPlayer.getName());
                    afkPlayer.getHandler().afkKick();
                    // They should get removed by the onRemove()
                }
            }
        }
        else if (!afkPlayer.isNoAfkEnabled())
        {
            if (afkDuration > (timeoutSeconds * 1000L))
            {
                if (ConfigWrap.pack().afkTimeoutString.isEmpty())
                {
                    afkPlayer.getHandler().registerAfk("");
                }
                else
                {
                    afkPlayer.getHandler().registerAfk(ConfigWrap.pack().afkTimeoutString);
                }

                AfkPlus.debugLog("onTickPacket(): Setting player {} as AFK (timeout)", afkPlayer.getName());
            }
            else if (afkPlayer.shouldIgnoreAttacks())
            {
                if (ConfigWrap.pack().afkTimeoutString.isEmpty())
                {
                    afkPlayer.getHandler().registerAfk("");
                }
                else
                {
                    afkPlayer.getHandler().registerAfk(ConfigWrap.pack().afkTimeoutIgnoreAttack);
                }

                AfkPlus.debugLog("onTickPacket(): Setting player {} as AFK (movement/looking timeout; but they are still attacking)", afkPlayer.getName());
            }
        }

        afkPlayer.getHandler().tickPlayer(player);
        // updateAfkStatus
    }

    @ApiStatus.Internal
    public void onTickPlayer(@Nonnull ServerPlayer player)
    {
        // checkAfk
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
            if (afkPlayer.getLastPlayerListUpdate() <= 0)
            {
                afkPlayer.getHandler().updatePlayerList();
            }
            else
            {
                long diff = Util.getMillis() - afkPlayer.getLastPlayerListUpdate();

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
                AfkPlus.debugLog("onTickPlayer() - Damage Enabled for player: {} because they are [LOCKED]. step 1.", afkPlayer.getName());
            }
        }
        else if (afkPlayer.isAfk() && ConfigWrap.dmg().disableDamage)
        {
            if (afkPlayer.isDamageEnabled())
            {
                // Stop people from abusing the /afk command for 20 seconds to get out of a "sticky situation"
                int cooldownSeconds = ConfigWrap.dmg().disableDamageCooldown;

                if (cooldownSeconds > 0)
                {
                    long diff = Util.getMillis() - afkPlayer.getAfkTimeMs();

                    if (diff > cooldownSeconds * 1000L)
                    {
                        afkPlayer.getHandler().disableDamage();
                        AfkPlus.debugLog("onTickPlayer() - Damage Disabled for player: {} step 2.", afkPlayer.getName());
                    }
                }
                else
                {
                    if (!(player.gameMode.getPreviousGameModeForPlayer() == GameType.CREATIVE))
                    {
                        afkPlayer.getHandler().disableDamage();
                        AfkPlus.debugLog("onTickPlayer() - Damage Disabled for player: {} step 4.", afkPlayer.getName());
                    }
                }
            }
        }
        else
        {
            if (!afkPlayer.isDamageEnabled())
            {
                afkPlayer.getHandler().enableDamage();
                AfkPlus.debugLog("onTickPlayer() - Damage Enabled for player: {} step 5.", afkPlayer.getName());
            }
        }
        // checkAfk
    }

    @ApiStatus.Internal
    public void onMovement(@Nonnull ServerPlayer player, ServerboundMovePlayerPacket packet)
    {
        //AfkPlusMod.debugLog("onMovement(): Player [{}] // (moved)", player.getName().getString());

        // checkPlayerLook
        if (VanishAPICompat.hasVanish() && VanishAPICompat.isVanishedByEntity(player))
        {
            return;
        }

        if (packet.hasRotation())
        {
            player.getEyeY();
            float yaw = player.getYRot();
            float pitch = player.getXRot();

            if (pitch != packet.getXRot(pitch) || yaw != packet.getYRot(yaw))
            {
                AfkPlayer afkPlayer = AfkPlayerList.getInstance().addOrGetPlayer(player);
                afkPlayer.setLastLookTime(Util.getMillis());

                if (ConfigWrap.pack().resetOnLook)
                {
                    player.resetLastActionTime();
                }
            }
        }
        // checkPlayerLook
    }

    @ApiStatus.Internal
    public void onChangeGameMode(@Nonnull ServerPlayer player, GameType mode, boolean result)
    {
        AfkPlus.debugLog("onChangeGameMode(): Player [{}] // newGameMode [{}]", player.getName().getString(), mode.getName());

        // checkGameMode
        AfkPlayer afkPlayer = AfkPlayerList.getInstance().addOrGetPlayer(player);

        if (VanishAPICompat.hasVanish() && VanishAPICompat.isVanishedByEntity(player))
        {
            return;
        }

        if (result)
        {
            AfkPlus.debugLog("onChangeGameMode() -- Invoked for player {} GameMode: {}", afkPlayer.getName(), mode.getName());

            if (afkPlayer.isAfk())
            {
                // Fixes uncommon de-sync when switching Game Modes.
                afkPlayer.getHandler().unregisterAfk();

                if (mode == GameType.SURVIVAL)
                {
                    if (ConfigWrap.dmg().disableDamage)
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

    @ApiStatus.Internal
    public void onPlayerAttack(@Nonnull ServerPlayer player, Entity entity)
    {
        if (VanishAPICompat.hasVanish() && VanishAPICompat.isVanishedByEntity(player))
        {
            return;
        }

        //LOGGER.debug("onPlayerAttack(): player [{}/{}] --> Entity [{}]", player.getId(), player.getName().getString(), entity.getId());

        AfkPlayer afkPlayer = AfkPlayerList.getInstance().addOrGetPlayer(player);
        afkPlayer.setLastAttackTime(Util.getMillis());
    }

    @ApiStatus.Internal
    public void onResetLastAction(@Nonnull ServerPlayer player)
    {
        // onActionTimeUpdate
        if (VanishAPICompat.hasVanish() && VanishAPICompat.isVanishedByEntity(player))
        {
            return;
        }

        //LOGGER.debug("onResetLastAction(): player [{}/{}]", player.getId(), player.getName().getString());

        AfkPlayer afkPlayer = AfkPlayerList.getInstance().addOrGetPlayer(player);

        if (afkPlayer.isAfk() && !afkPlayer.shouldIgnoreAttacks())
        {
            AfkPlus.debugLog("onResetLastAction(): Player [{}] // Reset Last Action (Remove AFK)", afkPlayer.getName());
            afkPlayer.getHandler().unregisterAfk();
        }
        // onActionTimeUpdate
    }

    @ApiStatus.Internal
    public void onSetPos(@Nullable ServerPlayer player, double x, double y, double z)
    {
        if (player != null)
        {
            if (VanishAPICompat.hasVanish() && VanishAPICompat.isVanishedByEntity(player))
            {
                return;
            }

            //AfkPlusMod.debugLog("onSetPos(): Player [{}] // setPos[{}]", player.getName().getString(), new Vector3d(x, y, z));

            if ((player.getX() != x || player.getY() != y || player.getZ() != z))
            {
                AfkPlayer afkPlayer = AfkPlayerList.getInstance().addOrGetPlayer(player);
                afkPlayer.setLastMovementTime(Util.getMillis());

                if (ConfigWrap.pack().resetOnMovement)
                {
                    player.resetLastActionTime();
                }
            }
        }
    }

    @ApiStatus.Internal
    public boolean onCheckIfPushable(@Nonnull ServerPlayer player, boolean value)
    {
        AfkPlayer afkPlayer = AfkPlayerList.getInstance().addOrGetPlayer(player);

        if (afkPlayer.isAfk() && !afkPlayer.isDamageEnabled() && player.isInvulnerable())
        {
            return false;
        }

        return value;
    }

    @ApiStatus.Internal
    public @Nullable Component onUpdateDisplayName(@Nonnull ServerPlayer player, @Nullable Component oldName)
    {
        AfkPlus.debugLog("onUpdateDisplayName(): Player [{}] // oldName [{}]", player.getName().getString(), oldName != null ? oldName.getString() : "<empty>");

        // replacePlayerListName
        if (VanishAPICompat.hasVanish() && VanishAPICompat.isVanishedByEntity(player))
        {
            return null;
        }

        AfkPlayer afkPlayer = AfkPlayerList.getInstance().addOrGetPlayer(player);

        if (ConfigWrap.list().enableListDisplay && afkPlayer.isAfk())
        {
            Component listEntry = Placeholders.parseText(
                    TextHandler.getInstance().formatTextSafe(ConfigWrap.list().afkPlayerName),
                    PlaceholderContext.of(player)
            );

            AfkPlus.debugLog("replacePlayerListName-listEntry().toString(): {}", listEntry.getString());
            afkPlayer.setLastPlayerListUpdate(Util.getMillis());

            return listEntry.copy();
        }
        // replacePlayerListName

        return null;
    }

    @ApiStatus.Internal
    public boolean onCheckSleepCount(@Nonnull ServerPlayer player, int countActive, int countSleeping, int totalActive, int totalSleeping)
    {
        // checkSleepCount
        // Count AFK Players into the total, they can't be marked as Sleeping, so don't increment that value.
        AfkPlayer afkPlayer = AfkPlayerList.getInstance().addOrGetPlayer(player);

        AfkPlus.debugLog("checkSleepCount(): Current: countActive {}, countSleeping {} // total {}, sleeping {}",
                         countActive, countSleeping, totalActive, totalSleeping);

        if (afkPlayer.isAfk() && ConfigWrap.pack().bypassSleepCount)
        {
            AfkPlus.debugLog("AFK Player: {} is being excluded from the sleep requirements.", afkPlayer.getName());
            return true;
        }
        // checkSleepCount

        return false;
    }

    @ApiStatus.Internal
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
                AfkPlus.debugLog("Afk Player: {} was just spared from a phantom spawn chance.", afkPlayer.getName());
            }

            AfkPlus.debugLog("checkPhantomSpawn(): [Player: {}] obtained TIME_SINCE_REST value of {} setting value to 1", afkPlayer.getName(), currentValue);

            return 1;
        }
        else
        {
            AfkPlus.debugLog("checkPhantomSpawn(): [Player: {}] TIME_SINCE_REST has a value of {} ", afkPlayer.getName(), currentValue);
            return currentValue;
        }
        // checkForAfkPlayer
    }

    @ApiStatus.Internal
    public void onVanish(@Nonnull ServerPlayer player, boolean isVanished)
    {
        AfkPlus.debugLog("onVanish(): Player [{}] / Vanish [{}]", player.getName().getString(), isVanished);

        AfkPlayer afkPlayer = AfkPlayerList.getInstance().addOrGetPlayer(player);

        if (afkPlayer.isAfk() && isVanished)
        {
            afkPlayer.getHandler().unregisterAfkSilently();
        }
    }

    /*
    @ApiStatus.Internal
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

    @ApiStatus.Internal
    public @Nullable Component getUnVanishMessage(@Nonnull ServerPlayer player)
    {
        AfkPlusMod.debugLog("getUnVanishMessage(): Player [{}] / Message Callback [???]", player.getName().getString());
        //AfkPlayer afkPlayer = AfkPlayerList.getInstance().addOrGetPlayer(player);
        return null;
    }
     */
}
