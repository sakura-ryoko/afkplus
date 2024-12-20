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

import java.net.InetSocketAddress;
import java.net.SocketAddress;
import javax.annotation.Nullable;

import io.netty.util.NetUtil;
import org.apache.http.conn.util.InetAddressUtils;
import org.joml.Vector3d;

import com.mojang.authlib.GameProfile;
import net.minecraft.network.Connection;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ServerboundMovePlayerPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.GameType;

import com.sakuraryoko.afkplus.AfkPlusMod;

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
    }

    public void onJoinPre(ServerPlayer player, Connection connection)
    {
        AfkPlusMod.debugLog("onJoinPre(): Player [{}] // Joining", player.getName().getString());
    }

    // ConnectedClientData ?
    public void onJoinPost(ServerPlayer player, Connection connection)
    {
        AfkPlusMod.debugLog("onJoinPost(): Player [{}] // Joined", player.getName().getString());
    }

    public void onRespawn(ServerPlayer player)
    {
        AfkPlusMod.debugLog("onRespawn(): Player [{}] // Respawned", player.getName().getString());
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
        // NO-OP
    }

    public void onTickPlayer(ServerPlayer player, long lastPlayerTick)
    {
        // NO-OP
    }

    public void onMovement(ServerPlayer player, ServerboundMovePlayerPacket packet)
    {
        AfkPlusMod.debugLog("onMovement(): Player [{}] // (moved)", player.getName().getString());
    }

    public void onChangeGameMode(ServerPlayer player, GameType mode, boolean result)
    {
        AfkPlusMod.debugLog("onChangeGameMode(): Player [{}] // newGameMode [{}]", player.getName().getString(), mode.getName());
    }

    public void onResetLastAction(ServerPlayer player)
    {
        AfkPlusMod.debugLog("onResetLastAction(): Player [{}] // Reset Last Action", player.getName().getString());
    }

    public void onSetPos(@Nullable ServerPlayer player, double x, double y, double z)
    {
        if (player != null)
        {
            AfkPlusMod.debugLog("onSetPos(): Player [{}] // setPos[{}]", player.getName().getString(), new Vector3d(x, y, z));
        }
    }

    public static Component onUpdateDisplayName(ServerPlayer player, @Nullable Component name)
    {
        AfkPlusMod.debugLog("onUpdateDisplayName(): Player [{}] // setName [{}]", player.getName().getString(), name != null ? name.getString() : "<empty>");
        return name;
    }
}
