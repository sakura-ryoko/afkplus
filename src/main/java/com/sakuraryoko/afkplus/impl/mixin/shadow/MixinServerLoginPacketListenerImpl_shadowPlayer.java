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

import java.net.SocketAddress;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import org.jetbrains.annotations.ApiStatus;

import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerLoginPacketListenerImpl;
//#if MC >= 1.21.10
//$$ import net.minecraft.server.players.NameAndId;
//#else
import com.mojang.authlib.GameProfile;
//#endif
import net.minecraft.server.players.PlayerList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import com.sakuraryoko.afkplus.impl.player.AfkPlayer;
import com.sakuraryoko.afkplus.impl.player.AfkPlayerList;
import com.sakuraryoko.afkplus.impl.player.shadow.ShadowServerPlayer;
import com.sakuraryoko.morecolors.impl.text.TextUtils;

@Mixin(ServerLoginPacketListenerImpl.class)
@ApiStatus.Internal
public abstract class MixinServerLoginPacketListenerImpl_shadowPlayer
{
//#if MC >= 1.21.10
	//$$ @WrapOperation(method = "verifyLoginAndFinishConnectionSetup",
						//$$ at = @At(value = "INVOKE",
									//$$ target = "Lnet/minecraft/server/players/PlayerList;canPlayerLogin(Ljava/net/SocketAddress;Lnet/minecraft/server/players/NameAndId;)Lnet/minecraft/network/chat/Component;"))
	//$$ private Component afkplus$checkForStaleShadow(PlayerList instance, SocketAddress socketAddress,
														//$$ NameAndId nameAndId,
														//$$ Operation<Component> original)
	//$$ {
		//$$ ServerPlayer player = instance.getPlayer(nameAndId.id());
//#elseif MC >= 1.20.2
	//$$ @WrapOperation(method = "verifyLoginAndFinishConnectionSetup",
						//$$ at = @At(value = "INVOKE",
									//$$ target = "Lnet/minecraft/server/players/PlayerList;canPlayerLogin(Ljava/net/SocketAddress;Lcom/mojang/authlib/GameProfile;)Lnet/minecraft/network/chat/Component;"))
	//$$ private Component afkplus$checkForStaleShadow(PlayerList instance, SocketAddress socketAddress,
														//$$ GameProfile gameProfile,
														//$$ Operation<Component> original)
//$$ {
	//$$ ServerPlayer player = instance.getPlayer(gameProfile.getId());
//#else
	@WrapOperation(method = "handleAcceptedLogin",
	               at = @At(value = "INVOKE",
	                        target = "Lnet/minecraft/server/players/PlayerList;canPlayerLogin(Ljava/net/SocketAddress;Lcom/mojang/authlib/GameProfile;)Lnet/minecraft/network/chat/Component;"))
	private Component afkplus$checkForStaleShadow(PlayerList instance, SocketAddress socketAddress,
	                                              GameProfile gameProfile,
	                                              Operation<Component> original)
	{
		ServerPlayer player = instance.getPlayer(gameProfile.getId());
//#endif

		if (player instanceof ShadowServerPlayer sp)
		{
			AfkPlayer afkPlayer = AfkPlayerList.getInstance().getPlayer(sp);

			if (afkPlayer != null)
			{
				afkPlayer.getHandler().unregisterShadowAfk();
			}

			if (player.isInvulnerable() && player.gameMode.isSurvival())
			{
				player.setInvulnerable(false);
			}

			String str = "shadow replaced";
			sp.kill(TextUtils.getInstance().formatText(str));
			instance.remove(player);
		}

//#if MC >= 1.21.10
		//$$ return original.call(instance, socketAddress, nameAndId);
//#else
		return original.call(instance, socketAddress, gameProfile);
//#endif
	}
}
