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

package com.sakuraryoko.afkplus.impl.mixin;

import org.jetbrains.annotations.ApiStatus;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
//#if MC >= 26.1
//$$ import net.minecraft.server.level.ServerPlayer;
//$$ import net.minecraft.world.entity.Entity;
//$$ import org.spongepowered.asm.mixin.injection.At;
//$$ import org.spongepowered.asm.mixin.injection.Inject;
//$$ import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

//$$ import com.sakuraryoko.afkplus.impl.events.PlayerEventsHandler;
//#endif

@Mixin(Player.class)
@ApiStatus.Internal
public abstract class MixinPlayer extends LivingEntity
{
	protected MixinPlayer(EntityType<? extends LivingEntity> entityType, Level level)
	{
		super(entityType, level);
	}

	//#if MC >= 26.1
	//$$ @Inject(method = "attack", at = @At("HEAD"))
	//$$ private void afkplus$onPlayerAttack(Entity target, CallbackInfo ci)
	//$$ {
		//$$ if (((Object) this) instanceof ServerPlayer)
		//$$ {
			//$$ PlayerEventsHandler.getInstance().onPlayerAttack((ServerPlayer) ((Object) this), target);
		//$$ }
	//$$ }
	//#endif
}
