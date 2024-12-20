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

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.level.ServerPlayerGameMode;
import net.minecraft.world.level.GameType;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.sakuraryoko.afkplus.AfkPlusMod;
import com.sakuraryoko.afkplus.config.ConfigWrap;
import com.sakuraryoko.afkplus.events.PlayerEventsHandler;
import com.sakuraryoko.afkplus.player.IAfkPlayer;

@Mixin(ServerPlayerGameMode.class)
public abstract class MixinServerPlayerGameMode
{
    @Shadow
    @Final
    protected ServerPlayer player;

    @Inject(method = "changeGameModeForPlayer", at = @At("RETURN"))
    private void checkGameMode(GameType gameType, CallbackInfoReturnable<Boolean> cir)
    {
        PlayerEventsHandler.getInstance().onChangeGameMode(this.player, gameType, cir.getReturnValue());

        IAfkPlayer afkPlayer = (IAfkPlayer) this.player;
        if (cir.getReturnValue())
        {
            AfkPlusMod.debugLog("checkGameMode() -- Invoked for player {} GameMode: {}", afkPlayer.afkplus$getName(), gameType.getName());
            if (afkPlayer.afkplus$isAfk())
            {
                // Fixes uncommon de-sync when switching Game Modes.
                afkPlayer.afkplus$unregisterAfk();
                if (gameType == GameType.SURVIVAL)
                {
                    if (ConfigWrap.pack().disableDamage)
                    {
                        if (this.player.isInvulnerable())
                        {
                            this.player.setInvulnerable(false);
                        }
                        afkPlayer.afkplus$enableDamage();
                    }
                }
            }
        }
    }
}
