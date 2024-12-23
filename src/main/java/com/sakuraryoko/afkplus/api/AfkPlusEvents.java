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

package com.sakuraryoko.afkplus.api;

import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;

public class AfkPlusEvents
{
    public static final Event<AfkEvent> AFK_EVENT = EventFactory.createArrayBacked(AfkEvent.class, cb ->
        (player, reason) ->
        {
            for (var call : cb)
            {
                call.onAfk(player, reason);
            }
        });

    public static final Event<AfkReturnEvent> AFK_RETURN_EVENT = EventFactory.createArrayBacked(AfkReturnEvent.class, cb ->
            (player, duration) ->
            {
                for (var call : cb)
                {
                    call.onReturn(player, duration);
                }
            });

    public static final Event<AfkDisableDamage> AFK_DISABLE_DAMAGE = EventFactory.createArrayBacked(AfkDisableDamage.class, cb ->
            (player) ->
            {
                for (var call : cb)
                {
                    call.onDamageDisabled(player);
                }
            });

    public static final Event<AfkEnableDamage> AFK_ENABLE_DAMAGE = EventFactory.createArrayBacked(AfkEnableDamage.class, cb ->
            (player) ->
            {
                for (var call : cb)
                {
                    call.onDamageEnabled(player);
                }
            });

    public interface AfkEvent
    {
        void onAfk(ServerPlayer player, Component reason);
    }

    public interface AfkReturnEvent
    {
        void onReturn(ServerPlayer player, long duration);
    }

    public interface AfkDisableDamage
    {
        void onDamageDisabled(ServerPlayer player);
    }

    public interface AfkEnableDamage
    {
        void onDamageEnabled(ServerPlayer player);
    }
}
