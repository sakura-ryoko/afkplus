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

import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.sakuraryoko.afkplus.events.PlayerEventsHandler;
import com.sakuraryoko.afkplus.player.interfaces.IPlayerInvoker;

@Mixin(ServerPlayer.class)
public abstract class MixinServerPlayer extends Entity implements IPlayerInvoker
{
    @Shadow @Final public MinecraftServer server;
    @Shadow public ServerGamePacketListenerImpl connection;
    @Unique public ServerPlayer player = (ServerPlayer) (Object) this;

    public MixinServerPlayer(EntityType<?> type, Level world)
    {
        super(type, world);
    }

    @Inject(method = "resetLastActionTime", at = @At("TAIL"))
    private void afkplus$onResetLastAction(CallbackInfo ci)
    {
        PlayerEventsHandler.getInstance().onResetLastAction(this.player);
    }

    @Override
    public void setPos(double x, double y, double z)
    {
        PlayerEventsHandler.getInstance().onSetPos(this.player, x, y, z);
        super.setPos(x, y, z);
    }

    @Override
    public boolean isPushable()
    {
        return PlayerEventsHandler.getInstance().onCheckIfPushable(this.player, super.isPushable());
    }

    @Override
    public boolean isPushedByFluid()
    {
        return PlayerEventsHandler.getInstance().onCheckIfPushable(this.player, super.isPushedByFluid());
    }

    @Inject(method = "getTabListDisplayName", at = @At("RETURN"), cancellable = true)
    private void afkplus$updatePlayerListName(CallbackInfoReturnable<Component> cir)
    {
        Component listEntry = PlayerEventsHandler.getInstance().onUpdateDisplayName(this.player, cir.getReturnValue());

        if (listEntry != null)
        {
            cir.setReturnValue(listEntry);
        }
    }

    @Inject(method = "attack", at = @At("HEAD"))
    private void afkplus$onPlayerAttack(Entity entity, CallbackInfo ci)
    {
        PlayerEventsHandler.getInstance().onPlayerAttack(this.player, entity);
    }

    @Inject(method = "tick", at = @At("HEAD"))
    private void afkplus$onPlayerTick(CallbackInfo ci)
    {
        PlayerEventsHandler.getInstance().onTickPlayer(this.player);
    }

    @Override
    public ServerPlayer afkplus$player()
    {
        return this.player;
    }

    @Override
    public MinecraftServer afkplus$server()
    {
        return this.server;
    }

    @Override
    public ServerGamePacketListenerImpl afkplus$connection()
    {
        return this.connection;
    }

    @Override
    public void afkplus$setInvulnerable(boolean toggle)
    {
        this.setInvulnerable(toggle);
    }
}
