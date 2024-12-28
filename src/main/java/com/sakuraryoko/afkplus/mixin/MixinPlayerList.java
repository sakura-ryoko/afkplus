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

//#if MC >= 11903
//$$ import java.util.EnumSet;
//#endif
import net.minecraft.Util;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientboundPlayerInfoPacket;
import net.minecraft.server.players.PlayerList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.sakuraryoko.afkplus.api.AfkPlusEvents;
import com.sakuraryoko.afkplus.config.ConfigWrap;
import com.sakuraryoko.afkplus.player.AfkPlayerList;

@Mixin(PlayerList.class)
public abstract class MixinPlayerList
{
    @Shadow public abstract void broadcastAll(Packet<?> packet);
    @Unique private long lastTick;

    public MixinPlayerList()
    {
        super();
    }

    @Inject(method = "tick",
            at = @At(value = "INVOKE",
                     target = "Lnet/minecraft/server/players/PlayerList;broadcastAll(Lnet/minecraft/network/protocol/Packet;)V"))
    private void afkplus$onPlayerListTick(CallbackInfo ci)
    {
        // This follows the Vanilla "ping" update packet
        if (ConfigWrap.list().enableListDisplay && ConfigWrap.list().updateInterval > 0)
        {
            if ((Util.getMillis() - this.lastTick) > (ConfigWrap.list().updateInterval * 1000L))
            {
                //#if MC >= 11903
                //$$ this.broadcastAll(new ClientboundPlayerInfoUpdatePacket(EnumSet.of(ClientboundPlayerInfoUpdatePacket.Action.UPDATE_DISPLAY_NAME), AfkPlayerList.getInstance().listAllAfk()));
                //#else
                this.broadcastAll(new ClientboundPlayerInfoPacket(ClientboundPlayerInfoPacket.Action.UPDATE_DISPLAY_NAME, AfkPlayerList.getInstance().listAllAfk()));
                //#endif
                AfkPlusEvents.UPDATE_PLAYER_LIST.invoker().onPlayerListUpdate(null);
            }

            this.lastTick = Util.getMillis();
        }
    }
}
