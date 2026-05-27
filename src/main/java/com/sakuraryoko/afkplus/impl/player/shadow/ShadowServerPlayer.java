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

package com.sakuraryoko.afkplus.impl.player.shadow;

import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

import com.mojang.authlib.GameProfile;
//#if MC >= 1.20.1
//$$ import net.minecraft.core.BlockPos;
//$$ import net.minecraft.world.level.block.state.BlockState;
//#endif
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.PacketFlow;
import net.minecraft.network.protocol.game.ClientboundPlayerInfoPacket;
import net.minecraft.network.protocol.game.ClientboundRotateHeadPacket;
import net.minecraft.network.protocol.game.ServerboundClientCommandPacket;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.TickTask;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
//#if MC >= 1.20.2
//$$ import net.minecraft.server.network.CommonListenerCookie;
//$$ import net.minecraft.server.level.ClientInformation;
//#else
import net.minecraft.world.entity.player.ProfilePublicKey;
//#endif
import net.minecraft.world.food.FoodData;
import net.minecraft.world.item.ItemStack;

import com.sakuraryoko.afkplus.impl.compat.morecolors.TextHandler;

public class ShadowServerPlayer extends ServerPlayer
{
	//#if MC >= 1.20.2
	//$$ public ShadowServerPlayer(MinecraftServer server, ServerLevel level, GameProfile profile, ClientInformation ci)
	//$$ {
		//$$ super(server, level, profile, ci);
	//$$ }
	//#elseif MC >= 1.19.3
	//$$ public ShadowServerPlayer(MinecraftServer server, ServerLevel level, GameProfile profile)
	//$$ {
		//$$ super(server, level, profile);
	//$$ }
	//#else
	public ShadowServerPlayer(MinecraftServer server, ServerLevel level, GameProfile profile, @Nullable ProfilePublicKey profilePublicKey)
	{
		super(server, level, profile, profilePublicKey);
	}
	//#endif

	public static ShadowServerPlayer createShadow(MinecraftServer server, ServerPlayer player)
	{
		player.getServer().getPlayerList().remove(player);
		player.connection.disconnect(Component.translatable("multiplayer.disconnect.duplicate_login"));

		//#if MC >= 1.20.1
		//$$ ServerLevel level = player.serverLevel();
		//#else
		ServerLevel level = player.getLevel();
		//#endif
		GameProfile profile = player.getGameProfile();
		//#if MC >= 1.20.2
		//$$ ShadowServerPlayer shadow = new ShadowServerPlayer(server, level, profile, player.clientInformation());
		//#elseif MC >= 1.19.3
		//$$ ShadowServerPlayer shadow = new ShadowServerPlayer(server, level, profile);
		//#else
		ShadowServerPlayer shadow = new ShadowServerPlayer(server, level, profile, player.getProfilePublicKey());
		//#endif

		// new CommonListenerCookie(gameprofile, 0, player.clientInformation())
		//#if MC >= 1.20.2
		//$$ server.getPlayerList().placeNewPlayer(new ShadowConnection(PacketFlow.SERVERBOUND), shadow, new CommonListenerCookie(profile, 0, player.clientInformation()));
		//#else
		server.getPlayerList().placeNewPlayer(new ShadowConnection(PacketFlow.SERVERBOUND), shadow);
		//#endif
		shadow.setHealth(player.getHealth());
		shadow.connection.teleport(player.getX(), player.getY(), player.getZ(), player.getYRot(), player.getXRot());
		shadow.gameMode.changeGameModeForPlayer(player.gameMode.getGameModeForPlayer());
		//#if MC >= 1.19.3
		//$$ shadow.setChatSession(player.getChatSession());
		//#else
		//#endif
		//#if MC >= 1.19.4
		//$$ shadow.setMaxUpStep(0.6F);
		//#else
		shadow.maxUpStep = 0.6f;
		//#endif
		shadow.entityData.set(DATA_PLAYER_MODE_CUSTOMISATION, player.getEntityData().get(DATA_PLAYER_MODE_CUSTOMISATION));

		server.getPlayerList().broadcastAll(new ClientboundRotateHeadPacket(shadow, (byte) (player.yHeadRot * 256 / 360)),
											//#if MC >= 1.20.1
											//$$ shadow.serverLevel().dimension());
		                                    //#else
		                                    shadow.level.dimension());
											//#endif
		server.getPlayerList().broadcastAll(new ClientboundPlayerInfoPacket(ClientboundPlayerInfoPacket.Action.ADD_PLAYER, shadow));
		shadow.getAbilities().flying = player.getAbilities().flying;

		return shadow;
	}

	//#if MC >= 1.20.2
	//$$ public static ShadowServerPlayer respawnShadow(MinecraftServer server, ServerLevel level, GameProfile profile, ClientInformation ci)
	//$$ {
	//$$ return new ShadowServerPlayer(server, level, profile, ci);
	//$$ }
	//#elseif MC >= 1.19.3
	//$$ public static ShadowServerPlayer respawnShadow(MinecraftServer server, ServerLevel level, GameProfile profile)
	//$$ {
		//$$ return new ShadowServerPlayer(server, level, profile);
	//$$ }
	//#else
	public static ShadowServerPlayer respawnShadow(MinecraftServer server, ServerLevel level, GameProfile profile, @Nullable ProfilePublicKey profilePublicKey)
	{
		return new ShadowServerPlayer(server, level, profile, profilePublicKey);
	}
	//#endif

	@Override
	public @NonNull String getIpAddress()
	{
		return "127.0.0.1";
	}

	@Override
	public void onEquipItem(final @NonNull EquipmentSlot slot, final @NonNull ItemStack previous, final @NonNull ItemStack stack)
	{
		if (!this.isUsingItem())
		{
			super.onEquipItem(slot, previous, stack);
		}
	}

	@Override
	public void tick()
	{
		if (this.getServer().getTickCount() % 10 == 0)
		{
			this.connection.resetPosition();
			//#if MC >= 1.20.1
			//$$ this.serverLevel().getChunkSource().move(this);
			//#else
			this.getLevel().getChunkSource().move(this);
			//#endif
//			this.hasChangedDimension();
		}

		try
		{
			super.tick();
			this.doTick();
		}
		catch (NullPointerException ignored) {}
	}

	@Override
	public Entity changeDimension(@NonNull ServerLevel level)
	{
		super.changeDimension(level);

		// Handle freeing the End
		if (this.wonGame)
		{
			ServerboundClientCommandPacket packet = new ServerboundClientCommandPacket(ServerboundClientCommandPacket.Action.PERFORM_RESPAWN);
			this.connection.handleClientCommand(packet);
		}

		if (this.connection.player.isChangingDimension())
		{
			this.connection.player.hasChangedDimension();
		}

		return this.connection.player;
	}

	//#if MC >= 1.20.1
	//$$ @Override
	//$$ protected void checkFallDamage(double y, boolean onGround, @NonNull BlockState state, @NonNull BlockPos pos)
	//$$ {
		//$$ this.doCheckFallDamage(0.0, y, 0.0, onGround);
	//$$ }
	//#endif

	@Override
	public void die(@NonNull DamageSource damageSource)
	{
		this.dismount();
		super.die(damageSource);
		this.setHealth(20.0F);
		this.foodData = new FoodData();
		this.kill(this.getCombatTracker().getDeathMessage());
	}

	@Override
	public void kill()
	{
		this.kill(TextHandler.getInstance().formatTextSafe("Killed"));
	}

	public void kill(Component message)
	{
		this.dismount();
		this.server.tell(
				new TickTask(this.server.getTickCount(),
				             () -> this.connection.disconnect(message)
		));
	}

	private void dismount()
	{
		if (this.getVehicle() != null)
		{
			if (this.getVehicle() instanceof Player)
			{
				this.stopRiding();
			}

			for (Entity entry : this.getVehicle().getPassengers())
			{
				if (entry instanceof Player)
				{
					entry.stopRiding();
				}
			}
		}
	}
}
