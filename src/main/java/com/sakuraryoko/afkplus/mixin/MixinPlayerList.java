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

import java.net.SocketAddress;

import com.mojang.authlib.GameProfile;
import net.minecraft.network.Connection;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
//#if MC >= 12002
//$$ import net.minecraft.server.level.ClientInformation;
//$$ import net.minecraft.server.network.CommonListenerCookie;
//#endif
//#if MC >= 12101
//$$ import net.minecraft.world.entity.Entity;
//#endif
import net.minecraft.server.players.PlayerList;
//#if MC >= 11903
//#else
import net.minecraft.world.entity.player.ProfilePublicKey;
//#endif
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.sakuraryoko.afkplus.events.PlayerEventsHandler;

@Mixin(PlayerList.class)
public abstract class MixinPlayerList
{
    public MixinPlayerList()
    {
        super();
    }

    // Player Events
    // checkCanJoin
    @Inject(method = "canPlayerLogin", at = @At("RETURN"))
    private void afkplus$canPlayerLogin(SocketAddress socketAddress, GameProfile gameProfile, CallbackInfoReturnable<Component> cir)
    {
        // This might get called twice
        PlayerEventsHandler.getInstance().onConnection(socketAddress, gameProfile, cir.getReturnValue());
    }

    @Inject(method = "getPlayerForLogin", at = @At("RETURN"))
    //#if MC >= 12002
    //$$ private void afkplus$onGetPlayerForLogin(GameProfile gameProfile, ClientInformation clientInformation, CallbackInfoReturnable<ServerPlayer> cir)
    //#elseif MC >= 11903
    //$$ private void afkplus$onGetPlayerForLogin(GameProfile gameProfile, CallbackInfoReturnable<ServerPlayer> cir)
    //#else
    private void afkplus$onGetPlayerForLogin(GameProfile gameProfile, ProfilePublicKey profilePublicKey, CallbackInfoReturnable<ServerPlayer> cir)
    //#endif
    {
        PlayerEventsHandler.getInstance().onCreatePlayer(cir.getReturnValue(), gameProfile);
    }

    // onPlayerConnect
    //#if MC >= 12002
    //$$ @Inject(method = "placeNewPlayer", at = @At("HEAD"))
    //$$ private void afkplus$onPlaceNewPlayerPre(Connection connection, ServerPlayer serverPlayer, CommonListenerCookie commonListenerCookie, CallbackInfo ci)
    //$$ {
        //$$ PlayerEventsHandler.getInstance().onJoinPre(serverPlayer, connection);
    //$$ }

    //$$ @Inject(method = "placeNewPlayer", at = @At("TAIL"))
    //$$ private void afkplus$onPlaceNewPlayerPost(Connection connection, ServerPlayer serverPlayer, CommonListenerCookie commonListenerCookie, CallbackInfo ci)
    //$$ {
        //$$ PlayerEventsHandler.getInstance().onJoinPost(serverPlayer, connection);
    //$$ }

    //#else
    @Inject(method = "placeNewPlayer", at = @At("HEAD"))
    private void afkplus$onPlaceNewPlayerPre(Connection connection, ServerPlayer serverPlayer, CallbackInfo ci)
    {
        PlayerEventsHandler.getInstance().onJoinPre(serverPlayer, connection);
    }

    @Inject(method = "placeNewPlayer", at = @At("TAIL"))
    private void afkplus$onPlaceNewPlayerPost(Connection connection, ServerPlayer serverPlayer, CallbackInfo ci)
    {
        PlayerEventsHandler.getInstance().onJoinPost(serverPlayer, connection);
    }
    //#endif

    // respawnPlayer
    //#if MC >= 12101
    //$$ @Inject(method = "respawn", at = @At("RETURN"))
    //$$ private void afkplus$onRespawn(ServerPlayer serverPlayer, boolean bl, Entity.RemovalReason removalReason, CallbackInfoReturnable<ServerPlayer> cir)
    //$$ {
    //$$ PlayerEventsHandler.getInstance().onRespawn(serverPlayer);
    //$$ }
    //#else
    @Inject(method = "respawn", at = @At("RETURN"))
    private void afkplus$onRespawn(ServerPlayer serverPlayer, boolean bl, CallbackInfoReturnable<ServerPlayer> cir)
    {
        // serverPlayer = oldObject
        PlayerEventsHandler.getInstance().onRespawn(cir.getReturnValue());
    }
    //#endif

    // remove
    @Inject(method = "remove", at = @At("HEAD"))
    private void afkplus$onRemove(ServerPlayer serverPlayer, CallbackInfo ci)
    {
        PlayerEventsHandler.getInstance().onLeave(serverPlayer);
    }

    // disconnectAllPlayers
    @Inject(method = "removeAll", at = @At("HEAD"))
    private void afkplus$onRemoveAll(CallbackInfo ci)
    {
        PlayerEventsHandler.getInstance().onDisconnectAll();
    }
}
