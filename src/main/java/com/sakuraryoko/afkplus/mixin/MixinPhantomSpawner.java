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

import com.llamalad7.mixinextras.sugar.Local;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.levelgen.PhantomSpawner;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.sakuraryoko.afkplus.data.IAfkPlayer;
import com.sakuraryoko.afkplus.util.AfkPlusLogger;

import static com.sakuraryoko.afkplus.config.ConfigManager.CONFIG;

@Mixin(PhantomSpawner.class)
public class MixinPhantomSpawner
{
    @Unique
    private IAfkPlayer player;

    @Inject(method = "tick(Lnet/minecraft/server/level/ServerLevel;ZZ)I",
            at = @At(value = "INVOKE",
                     target = "Lnet/minecraft/world/entity/player/Player;blockPosition()Lnet/minecraft/core/BlockPos;")
    )
    private void capturePlayerForMath(ServerLevel world, boolean spawnMonsters, boolean spawnAnimals, CallbackInfoReturnable<Integer> cir,
                                      @Local Player serverPlayerEntity)
    {
        player = (IAfkPlayer) serverPlayerEntity;
    }

    @ModifyArg(method = "tick(Lnet/minecraft/server/level/ServerLevel;ZZ)I",
               at = @At(value = "INVOKE",
                        target = "Lnet/minecraft/util/Mth;clamp(III)I"),
               index = 0)
    private int checkForAfkPlayer(int value)
    {
        if (player == null)
        {
            return value;
        }
        if (player.afkplus$isAfk() && CONFIG.packetOptions.bypassInsomnia)
        {
            if (value > 72000)
            {
                AfkPlusLogger.info("Afk Player: " + player.afkplus$getName() + " was just spared from a phantom spawn chance.");
            }
            AfkPlusLogger.debug("checkPhantomSpawn(): [Player: " + player.afkplus$getName() + "] obtained TIME_SINCE_REST value of " + value + " setting value to 1");
            return 1;
        }
        else
        {
            AfkPlusLogger.debug("checkPhantomSpawn(): [Player: " + player.afkplus$getName() + "] TIME_SINCE_REST has a value of " + value + " ");
            return value;
        }
    }
}
