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

import java.util.List;
import com.llamalad7.mixinextras.sugar.Local;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.players.SleepStatus;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.sakuraryoko.afkplus.player.IAfkPlayer;
import com.sakuraryoko.afkplus.util.AfkLogger;

import static com.sakuraryoko.afkplus.config.ConfigManager.CONFIG;

@Mixin(SleepStatus.class)
public class MixinSleepStatus
{
    @Shadow
    private int activePlayers;
    @Shadow
    private int sleepingPlayers;

    @Inject(method = "update(Ljava/util/List;)Z",
            at = @At(value = "INVOKE",
                     target = "Lnet/minecraft/server/level/ServerPlayer;isSleeping()Z",
                     shift = At.Shift.BEFORE)
    )
    private void checkSleepCount(List<ServerPlayer> players, CallbackInfoReturnable<Boolean> cir,
                                 @Local(ordinal = 0) int i, @Local(ordinal = 1) int j, @Local ServerPlayer serverPlayerEntity)
    {
        // Count AFK Players into the total, they can't be marked as Sleeping, so don't increment that value.
        IAfkPlayer player = (IAfkPlayer) serverPlayerEntity;
        AfkLogger.debug("checkSleepCount(): Current values i:" + i + " j:" + j + " // total: " + this.activePlayers + " sleeping: " + this.sleepingPlayers);
        if (player.afkplus$isAfk() && CONFIG.packetOptions.bypassSleepCount)
        {
            AfkLogger.info("AFK Player: " + player.afkplus$getName() + " is being excluded from the sleep requirements.");
            --this.activePlayers;
        }
    }
}
