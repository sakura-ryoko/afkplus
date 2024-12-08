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

import com.mojang.authlib.GameProfile;
import net.minecraft.network.Connection;
//#if MC >= 12002
//$$ import net.minecraft.server.level.ClientInformation;
//#else
//#endif
import net.minecraft.server.level.ServerPlayer;
//#if MC >= 12002
//$$ import net.minecraft.server.network.CommonListenerCookie;
//#else
//#endif
import net.minecraft.server.players.PlayerList;
//#if MC >= 12101
//$$ import net.minecraft.world.entity.Entity;
//#else
//#endif
import net.minecraft.world.level.GameType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.sakuraryoko.afkplus.data.IAfkPlayer;
import com.sakuraryoko.afkplus.util.AfkPlusInfo;
import com.sakuraryoko.afkplus.util.AfkPlusLogger;

@Mixin(PlayerList.class)
public abstract class MixinPlayerList
{
    public MixinPlayerList()
    {
        super();
    }

    @Inject(method = "placeNewPlayer", at = @At("TAIL"))
    //#if MC >= 12002
    //$$ private void checkInvulnerable1(Connection connection, ServerPlayer serverPlayer, CommonListenerCookie commonListenerCookie, CallbackInfo ci)
    //#else
    private void checkInvulnerable1(Connection connection, ServerPlayer serverPlayer, CallbackInfo ci)
    //#endif
    {
        if (serverPlayer == null)
        {
            return;
        }
        IAfkPlayer iPlayer = (IAfkPlayer) serverPlayer;
        if (serverPlayer.gameMode.getGameModeForPlayer() == GameType.SURVIVAL)
        {
            if (serverPlayer.isInvulnerable())
            {
                serverPlayer.setInvulnerable(false);
                AfkPlusLogger.info("PlayerManager().onPlayerConnect() -> Marking SURVIVAL player: " + iPlayer.afkplus$getName() + " as vulnerable.");
            }
        }
        // This might simply initialize a player entry...
        iPlayer.afkplus$unregisterAfk();
        // Fixes some quirky-ness of Styled Player List
        if (AfkPlusInfo.isServer())
        {
            iPlayer.afkplus$updatePlayerList();
        }
    }

    @Inject(method = "getPlayerForLogin", at = @At("RETURN"))
    //#if MC >= 12002
    //$$ private void checkInvulnerable2(GameProfile gameProfile, ClientInformation clientInformation, CallbackInfoReturnable<ServerPlayer> cir)
    //#else
    private void checkInvulnerable2(GameProfile gameProfile, CallbackInfoReturnable<ServerPlayer> cir)
    //#endif
    {
        ServerPlayer playerEntity = cir.getReturnValue();
        IAfkPlayer iPlayer = (IAfkPlayer) playerEntity;
        if (playerEntity == null)
        {
            return;
        }
        if (playerEntity.gameMode.getGameModeForPlayer() == GameType.SURVIVAL)
        {
            if (playerEntity.isInvulnerable())
            {
                playerEntity.setInvulnerable(false);
                AfkPlusLogger.info("PlayerManager().createPlayer() -> Marking SURVIVAL player: " + iPlayer.afkplus$getName() + " as vulnerable.");
            }
        }
        // This might simply initialize a player entry...
        iPlayer.afkplus$unregisterAfk();
        // Fixes some quirky-ness of Styled Player List
//        if (AfkPlusInfo.isServer())
//            iPlayer.afkplus$updatePlayerList();
    }

    @Inject(method = "respawn", at = @At("RETURN"))
    //#if MC >= 12101
    //$$ private void checkInvulnerable3(ServerPlayer serverPlayer, boolean bl, Entity.RemovalReason removalReason, CallbackInfoReturnable<ServerPlayer> cir)
    //#else
    private void checkInvulnerable3(ServerPlayer serverPlayer, boolean bl, CallbackInfoReturnable<ServerPlayer> cir)
    //#endif
    {
        ServerPlayer playerEntity = cir.getReturnValue();
        if (playerEntity == null)
        {
            return;
        }
        if (playerEntity.gameMode.getGameModeForPlayer() == GameType.SURVIVAL)
        {
            if (playerEntity.isInvulnerable())
            {
                IAfkPlayer iPlayer = (IAfkPlayer) playerEntity;
                AfkPlusLogger.info("PlayerManager().repsawnPlayer() -> Marking SURVIVAL player: " + iPlayer.afkplus$getName() + " as vulnerable.");
                playerEntity.setInvulnerable(false);
                // This might simply initialize a player entry...
                iPlayer.afkplus$unregisterAfk();
                // Fixes some quirky-ness of Styled Player List
                if (AfkPlusInfo.isServer())
                {
                    iPlayer.afkplus$updatePlayerList();
                }
            }
        }
        if (serverPlayer.gameMode.getGameModeForPlayer() == GameType.SURVIVAL)
        {
            if (serverPlayer.isInvulnerable())
            {
                IAfkPlayer iPlayer = (IAfkPlayer) serverPlayer;
                AfkPlusLogger.info("PlayerManager().repsawnPlayer() -> Marking SURVIVAL player: " + iPlayer.afkplus$getName() + " as vulnerable.");
                serverPlayer.setInvulnerable(false);
                // This might simply initialize a player entry...
                iPlayer.afkplus$unregisterAfk();
                // Fixes some quirky-ness of Styled Player List
                if (AfkPlusInfo.isServer())
                {
                    iPlayer.afkplus$updatePlayerList();
                }
            }
        }
    }
}
