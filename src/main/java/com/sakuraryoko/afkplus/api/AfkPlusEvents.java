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

import javax.annotation.Nullable;

import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;

/**
 * AfkPlus API Events
 */
public class AfkPlusEvents
{
    /**
     * Executes when a player is marked as AFK
     */
    public static final Event<AfkEvent> AFK_EVENT = EventFactory.createArrayBacked(AfkEvent.class, cb ->
        (player, reason) ->
        {
            for (var call : cb)
            {
                call.onAfk(player, reason);
            }
        });

    /**
     * Executes when an AFK player is marked as not AFK
     */
    public static final Event<AfkReturnEvent> AFK_RETURN_EVENT = EventFactory.createArrayBacked(AfkReturnEvent.class, cb ->
            (player, duration) ->
            {
                for (var call : cb)
                {
                    call.onReturn(player, duration);
                }
            });

    /**
     * Executes when a player's Disable Damage status has been enabled (Invulnerable / Immovable),
     * which usually only happens if an administrator has enabled this feature,
     * and after a couple seconds has elapsed
     */
    public static final Event<AfkDisableDamageEvent> AFK_DISABLE_DAMAGE_EVENT = EventFactory.createArrayBacked(AfkDisableDamageEvent.class, cb ->
            (player) ->
            {
                for (var call : cb)
                {
                    call.onDamageDisabled(player);
                }
            });

    /**
     * Executes when a player's Disable Damage status has been disabled (Invulnerable / Immovable),
     * which usually only happens if an administrator has enabled this feature,
     * and a player returns from being AFK.
     */
    public static final Event<AfkEnableDamageEvent> AFK_ENABLE_DAMAGE_EVENT = EventFactory.createArrayBacked(AfkEnableDamageEvent.class, cb ->
            (player) ->
            {
                for (var call : cb)
                {
                    call.onDamageEnabled(player);
                }
            });

    /**
     * Executes when an AFK player gets removed from the server,
     * which should happen infrequently; and only when an Administrator has configured this feature,
     * after a configurable amount of time has elapsed.
     */
    public static final Event<AfkRemovalKickEvent> AFK_REMOVAL_KICK_EVENT = EventFactory.createArrayBacked(AfkRemovalKickEvent.class, cb ->
            (player, reason) ->
            {
                for (var call : cb)
                {
                    call.onPlayerKick(player, reason);
                }
            });

    /**
     * Executes whenever a Player List update operation has been sent.
     * If the ServerPlayer is NULL then it indicates a general Player List
     * update for all players has been performed.
     */
    public static final Event<UpdatePlayerListEvent> UPDATE_PLAYER_LIST = EventFactory.createArrayBacked(UpdatePlayerListEvent.class, cb ->
            (player) ->
            {
                for (var call : cb)
                {
                    call.onPlayerListUpdate(player);
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

    public interface AfkDisableDamageEvent
    {
        void onDamageDisabled(ServerPlayer player);
    }

    public interface AfkEnableDamageEvent
    {
        void onDamageEnabled(ServerPlayer player);
    }

    public interface AfkRemovalKickEvent
    {
        void onPlayerKick(ServerPlayer player, Component reason);
    }

    public interface UpdatePlayerListEvent
    {
        void onPlayerListUpdate(@Nullable ServerPlayer player);
    }
}
