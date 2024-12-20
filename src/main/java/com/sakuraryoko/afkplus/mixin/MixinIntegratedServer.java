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

import net.minecraft.client.server.IntegratedServer;
import net.minecraft.world.level.GameType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.sakuraryoko.afkplus.events.ServerEventsHandler;

@Mixin(IntegratedServer.class)
public class MixinIntegratedServer
{
    @Inject(method = "initServer", at = @At("RETURN"))
    private void afkplus$onInitServer(CallbackInfoReturnable<Boolean> cir)
    {
        if (cir.getReturnValue())
        {
            ServerEventsHandler.getInstance().onIntegratedStarted((IntegratedServer) (Object) this);
        }
    }

    @Inject(method = "publishServer", at = @At("RETURN"))
    private void afkplus$onPublishServer(GameType gameType, boolean bl, int i, CallbackInfoReturnable<Boolean> cir)
    {
        if (cir.getReturnValue())
        {
            ServerEventsHandler.getInstance().onOpenToLan((IntegratedServer) (Object) this, gameType);
        }
    }
}