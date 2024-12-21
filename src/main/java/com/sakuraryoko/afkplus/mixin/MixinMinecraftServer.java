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

import java.util.Collection;
import java.util.concurrent.CompletableFuture;

import net.minecraft.server.MinecraftServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.sakuraryoko.afkplus.events.ServerEventsHandler;

@Mixin(MinecraftServer.class)
public abstract class MixinMinecraftServer
{
    @Inject(method = "runServer",
            at = @At(value = "INVOKE",
                     target = "Lnet/minecraft/server/MinecraftServer;initServer()Z"))
    private void afkplus$onServerStarting(CallbackInfo ci)
    {
        ServerEventsHandler.getInstance().onStarting((MinecraftServer) (Object) this);
    }

    @Inject(method = "runServer",
            at = @At(value = "INVOKE",
                     //#if MC >= 11904
                     //$$ target = "Lnet/minecraft/server/MinecraftServer;buildServerStatus()Lnet/minecraft/network/protocol/status/ServerStatus;",
                     //#else
                     target = "Lnet/minecraft/server/MinecraftServer;updateStatusIcon(Lnet/minecraft/network/protocol/status/ServerStatus;)V",
                     //#endif
                     ordinal = 0))
    private void afkplus$onServerStarted(CallbackInfo ci)
    {
        ServerEventsHandler.getInstance().onStarted((MinecraftServer) (Object) this);
    }

    @Inject(method = "reloadResources", at = @At("TAIL"))
    private void afkplus$onReloadResources(Collection<String> collection, CallbackInfoReturnable<CompletableFuture<Void>> cir)
    {
        ServerEventsHandler.getInstance().onReloadComplete((MinecraftServer) (Object) this, collection);
    }

    @Inject(method = "stopServer", at = @At("HEAD"))
    private void afkplus$onStoppingServer(CallbackInfo ci)
    {
        ServerEventsHandler.getInstance().onStopping((MinecraftServer) (Object) this);
    }

    @Inject(method = "stopServer", at = @At("TAIL"))
    private void afkplus$onStoppedServer(CallbackInfo ci)
    {
        ServerEventsHandler.getInstance().onStopped((MinecraftServer) (Object) this);
    }
}
