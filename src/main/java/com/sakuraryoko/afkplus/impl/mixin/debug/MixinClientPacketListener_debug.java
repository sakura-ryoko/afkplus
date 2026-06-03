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

package com.sakuraryoko.afkplus.impl.mixin.debug;

import net.minecraft.client.multiplayer.ClientPacketListener;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(ClientPacketListener.class)
public abstract class MixinClientPacketListener_debug
{
//	@Shadow public abstract Collection<PlayerInfo> getOnlinePlayers();
//
//	@Inject(method = "handlePlayerInfo", at = @At("HEAD"))
//	private void afkplus$onPlayerInfo_Pre(ClientboundPlayerInfoPacket packet, CallbackInfo ci)
//	{
//		if (Reference.DEBUG)
//		{
//			AfkPlus.LOGGER.warn("handlePlayerInfo_Pre(): packet: {}", packet.toString());
//
//			List<String> list = new ArrayList<>();
//
//			this.getOnlinePlayers().forEach(player ->
//					//#if MC >= 1.21.10
//					                                //$$ list.add(player.getProfile().name())
//					//#else
//					                                list.add(player.getProfile().getName())
//                    //#endif
//			);
//			AfkPlus.LOGGER.info("handlePlayerInfo_Pre(): players: {}", list.toString());
//		}
//	}
//
//	@Inject(method = "handlePlayerInfo", at = @At("TAIL"))
//	private void afkplus$onPlayerInfo_Post(ClientboundPlayerInfoPacket packet, CallbackInfo ci)
//	{
//		if (Reference.DEBUG)
//		{
//			List<String> list = new ArrayList<>();
//
//			this.getOnlinePlayers().forEach(player ->
//                    //#if MC >= 1.21.10
//					                                //$$ list.add(player.getProfile().name())
//                    //#else
//					                                list.add(player.getProfile().getName())
//					//#endif
//			);
//			AfkPlus.LOGGER.info("handlePlayerInfo_Post(): players: {}", list.toString());
//		}
//	}
}
