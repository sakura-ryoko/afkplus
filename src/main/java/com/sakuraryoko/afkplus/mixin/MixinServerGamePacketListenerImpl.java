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

import com.sakuraryoko.afkplus.player.IAfkPlayer;
import com.sakuraryoko.afkplus.AfkPlusReference;
import com.sakuraryoko.afkplus.util.AfkLogger;

import static com.sakuraryoko.afkplus.config.ConfigManager.CONFIG;

@Mixin(ServerGamePacketListenerImpl.class)
public abstract class MixinServerGamePacketListenerImpl
{
    @Shadow
    public ServerPlayer player;

    @Inject(method = "tick", at = @At("HEAD"))
    private void updateAfkStatus(CallbackInfo ci)
    {
        IAfkPlayer afkPlayer = (IAfkPlayer) player;
        int timeoutSeconds = CONFIG.packetOptions.timeoutSeconds;
        long afkDuration = Util.getMillis() - this.player.getLastActionTime();
        if (afkPlayer.afkplus$isAfk() || timeoutSeconds <= 0)
        {
            if (CONFIG.packetOptions.afkKickEnabled && CONFIG.packetOptions.afkKickTimer > -1
                    && AfkPlusReference.AFK_ENV.equals(EnvType.SERVER))
            {
                if ((afkPlayer.afkplus$isCreative() || afkPlayer.afkplus$isSpectator()) && !CONFIG.packetOptions.afkKickNonSurvival)
                {
                    return;
                }
                int kickTimeout = CONFIG.packetOptions.afkKickTimer + CONFIG.packetOptions.timeoutSeconds;
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
                if (CONFIG.afkPlusOptions.afkTimeoutString.isEmpty())
                {
                    afkPlayer.afkplus$registerAfk("");
                }
                else
                {
                    afkPlayer.afkplus$registerAfk(CONFIG.afkPlusOptions.afkTimeoutString);
                }
                AfkLogger.debug("Setting player " + this.player.getName().getString() + " as AFK (timeout)");
            }
        }
    }

    @Inject(method = "handleMovePlayer", at = @At("HEAD"))
    private void checkPlayerLook(ServerboundMovePlayerPacket packet, CallbackInfo ci)
    {
        if (CONFIG.packetOptions.resetOnLook && packet.hasRotation())
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
