/*
 * This file is part of the AfkPlus project, licensed under the
 * GNU Lesser General Public License v3.0
 *
 * Copyright (C) 2026  Sakura Ryoko and contributors
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

package com.sakuraryoko.afkplus.impl.mixin.shadow;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import org.jetbrains.annotations.ApiStatus;

import com.mojang.authlib.GameProfile;
import net.minecraft.network.Connection;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
//#if MC >= 1.20.2
//$$ import net.minecraft.server.level.ClientInformation;
//$$ import net.minecraft.server.network.CommonListenerCookie;
//#endif
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.server.players.PlayerList;
import net.minecraft.world.entity.player.ProfilePublicKey;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

import com.sakuraryoko.afkplus.impl.player.shadow.ShadowGamePacketListener;
import com.sakuraryoko.afkplus.impl.player.shadow.ShadowServerPlayer;

@Mixin(PlayerList.class)
@ApiStatus.Internal
public abstract class MixinPlayerList_shadowPlayer
{
	@Shadow @Final private MinecraftServer server;

	@WrapOperation(method = "placeNewPlayer",
	               at = @At(value = "NEW",
	                        target = "net/minecraft/server/network/ServerGamePacketListenerImpl"
	               )
	)
	private ServerGamePacketListenerImpl afkplus$spawnShadowPlayer(MinecraftServer server,
	                                                               Connection connection,
	                                                               ServerPlayer player,
																   //#if MC >= 1.20.2
                                                                   //$$ CommonListenerCookie cookie,
                                                                   //#endif
	                                                               Operation<ServerGamePacketListenerImpl> original)
	{
		//#if MC >= 1.20.2
		//$$ if (player instanceof ShadowServerPlayer shadow)
		//$$ {
			//$$ return new ShadowGamePacketListener(this.server, connection, shadow, cookie);
		//$$ }

		//$$ return original.call(server, connection, player, cookie);
		//#else
		if (player instanceof ShadowServerPlayer shadow)
		{
			return new ShadowGamePacketListener(this.server, connection, shadow);
		}

		return original.call(server, connection, player);
		//#endif
	}

	@WrapOperation(method = "respawn",
	               at = @At(value = "NEW",
	                        target = "net/minecraft/server/level/ServerPlayer"
	               )
	)
	private ServerPlayer afkplus$respawnShadow(MinecraftServer server,
	                                           ServerLevel level,
	                                           GameProfile profile,
	                                           //#if MC >= 1.20.2
	                                           //$$ ClientInformation ci,
											   //#elseif MC >= 1.19.3
                                               //#else
	                                           ProfilePublicKey profilePublicKey,
											   //#endif
	                                           Operation<ServerPlayer> original,
	                                           @Local(argsOnly = true) ServerPlayer player)
	{
		//#if MC >= 1.20.2
		//$$ if (player instanceof ShadowServerPlayer)
		//$$ {
		//$$ return ShadowServerPlayer.respawnShadow(server, level, profile, ci);
		//$$ }

		//$$ return original.call(server, level, profile, ci);
		//#elseif MC >= 1.19.3
		//$$ if (player instanceof ShadowServerPlayer)
		//$$ {
			//$$ return ShadowServerPlayer.respawnShadow(server, level, profile);
		//$$ }

		//$$ return original.call(server, level, profile);
		//#else
		if (player instanceof ShadowServerPlayer)
		{
			return ShadowServerPlayer.respawnShadow(server, level, profile, profilePublicKey);
		}

		return original.call(server, level, profile, profilePublicKey);
		//#endif
	}
}
