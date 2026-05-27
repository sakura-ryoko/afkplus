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
import org.jetbrains.annotations.ApiStatus;
import org.objectweb.asm.Opcodes;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import com.sakuraryoko.afkplus.impl.player.shadow.ShadowServerPlayer;

@Mixin(Player.class)
@ApiStatus.Internal
public abstract class MixinPlayer_shadowPlayer
{
	@WrapOperation(method = "attack",
	               at = @At(value = "FIELD",
	                        target = "Lnet/minecraft/world/entity/Entity;hurtMarked:Z",
	                        ordinal = 0,
	                        opcode = Opcodes.GETFIELD)
	)
	private boolean afkplus$onKnockback(Entity instance, Operation<Boolean> original)
	{
		//		boolean orig = original.call(instance);
		return instance.hurtMarked && !(instance instanceof ShadowServerPlayer);
	}
}
