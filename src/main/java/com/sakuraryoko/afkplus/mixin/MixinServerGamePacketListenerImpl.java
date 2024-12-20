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

import net.minecraft.Util;
import net.minecraft.network.protocol.game.ServerboundMovePlayerPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.fabricmc.api.EnvType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.sakuraryoko.afkplus.AfkPlusMod;
import com.sakuraryoko.afkplus.config.ConfigWrap;
import com.sakuraryoko.afkplus.events.PlayerEventsHandler;
import com.sakuraryoko.afkplus.player.IAfkPlayer;
import com.sakuraryoko.afkplus.AfkPlusReference;

@Mixin(ServerGamePacketListenerImpl.class)
public abstract class MixinServerGamePacketListenerImpl
{
    @Shadow
    public ServerPlayer player;

    @Inject(method = "tick", at = @At("HEAD"))
    private void updateAfkStatus(CallbackInfo ci)
    {
        PlayerEventsHandler.getInstance().onTickPacket(this.player);

        IAfkPlayer afkPlayer = (IAfkPlayer) player;
        int timeoutSeconds = ConfigWrap.pack().timeoutSeconds;
        long afkDuration = Util.getMillis() - this.player.getLastActionTime();
        if (afkPlayer.afkplus$isAfk() || timeoutSeconds <= 0)
        {
            if (ConfigWrap.pack().afkKickEnabled && ConfigWrap.pack().afkKickTimer > -1
                && AfkPlusReference.MOD_ENV.equals(EnvType.SERVER))
            {
                if ((afkPlayer.afkplus$isCreative() || afkPlayer.afkplus$isSpectator()) && !ConfigWrap.pack().afkKickNonSurvival)
                {
                    return;
                }
                int kickTimeout = ConfigWrap.pack().afkKickTimer + ConfigWrap.pack().timeoutSeconds;
                if (afkDuration > (kickTimeout * 1000L))
                {
                    afkPlayer.afkplus$afkKick();
                }
            }
        }
        else if (afkPlayer.afkplus$isNoAfkEnabled())
        {
            return;
        }
        else
        {
            if (afkDuration > (timeoutSeconds * 1000L))
            {
                if (ConfigWrap.afk().afkTimeoutString.isEmpty())
                {
                    afkPlayer.afkplus$registerAfk("");
                }
                else
                {
                    afkPlayer.afkplus$registerAfk(ConfigWrap.afk().afkTimeoutString);
                }
                AfkPlusMod.debugLog("Setting player {} as AFK (timeout)", this.player.getName().getString());
            }
        }
    }

    @Inject(method = "handleMovePlayer", at = @At("HEAD"))
    private void checkPlayerLook(ServerboundMovePlayerPacket packet, CallbackInfo ci)
    {
        PlayerEventsHandler.getInstance().onMovement(this.player, packet);

        if (ConfigWrap.pack().resetOnLook && packet.hasRotation())
        {
            player.getEyeY();
            float yaw = player.getYRot();
            float pitch = player.getXRot();
            if (pitch != packet.getXRot(pitch) || yaw != packet.getYRot(yaw))
            {
                player.resetLastActionTime();
            }
        }
    }
}
