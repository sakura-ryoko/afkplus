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
//#if MC >= 12001
//$$ import net.minecraft.server.level.ServerPlayer;
//#else
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
//#endif
import net.minecraft.world.level.levelgen.PhantomSpawner;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.sakuraryoko.afkplus.events.PlayerEventsHandler;

@Mixin(PhantomSpawner.class)
public class MixinPhantomSpawner
{
    @Unique private ServerPlayer afkPlayer;

    @Inject(method = "tick(Lnet/minecraft/server/level/ServerLevel;ZZ)I",
            at = @At(value = "INVOKE",
                     //#if MC >= 12001
                     //$$ target = "Lnet/minecraft/server/level/ServerPlayer;blockPosition()Lnet/minecraft/core/BlockPos;")
                     //#else
                     target = "Lnet/minecraft/world/entity/player/Player;blockPosition()Lnet/minecraft/core/BlockPos;")
                     //#endif
    )
    private void capturePlayerForMath(ServerLevel world, boolean spawnMonsters, boolean spawnAnimals, CallbackInfoReturnable<Integer> cir,
                                      //#if MC >= 12001
                                      //$$ @Local ServerPlayer serverPlayer)
                                      //#else
                                      @Local Player player)
                                      //#endif
    {
        //#if MC >= 12001
        //$$ afkPlayer = serverPlayer;
        //#else
        afkPlayer = (ServerPlayer) player;
        //#endif
    }

    @ModifyArg(method = "tick(Lnet/minecraft/server/level/ServerLevel;ZZ)I",
               at = @At(value = "INVOKE",
                        target = "Lnet/minecraft/util/Mth;clamp(III)I"),
               index = 0)
    private int checkForAfkPlayer(int value)
    {
        return PlayerEventsHandler.getInstance().onCheckBypassInsomnia(this.afkPlayer, value);
    }
}
