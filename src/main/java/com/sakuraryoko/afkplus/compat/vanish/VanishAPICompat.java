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

package com.sakuraryoko.afkplus.compat.vanish;

import java.util.UUID;
import javax.annotation.Nonnull;
import me.drex.vanish.api.VanishAPI;

import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.fabricmc.loader.api.FabricLoader;

import com.sakuraryoko.afkplus.AfkPlus;

public class VanishAPICompat
{
    private static final boolean hasVanish;

    static
    {
        hasVanish = FabricLoader.getInstance().isModLoaded("melius-vanish");
    }

    public static boolean hasVanish()
    {
        return hasVanish;
    }

    public static boolean isVanishedByEntity(@Nonnull Entity entity)
    {
        if (!hasVanish)
        {
            return false;
        }

        try
        {
            return VanishAPI.isVanished(entity);
        }
        catch (Throwable e)
        {
            AfkPlus.LOGGER.error("VanishCompat#isVanishedEntity(): has thrown an error: [{}]", e.getMessage());
        }

        return false;
    }

    public static boolean isVanishedByUUIO(@Nonnull MinecraftServer server, @Nonnull UUID uuid)
    {
        if (!hasVanish)
        {
            return false;
        }

        try
        {
            return VanishAPI.isVanished(server, uuid);
        }
        catch (Throwable e)
        {
            AfkPlus.LOGGER.error("VanishCompat#isVanishedUUID(): has thrown an error: [{}]", e.getMessage());
        }

        return false;
    }

    public static void broadcastHiddenMessage(@Nonnull ServerPlayer player, @Nonnull Component message)
    {
        if (!hasVanish)
        {
            return;
        }

        try
        {
            VanishAPI.broadcastHiddenMessage(player, message);
        }
        catch (Throwable e)
        {
            AfkPlus.LOGGER.error("VanishCompat#broadcastHiddenMessage(): has thrown an error: [{}]", e.getMessage());
        }
    }

    //#if MC >= 12001
    //$$ public static void sendHiddenMessage(@Nonnull ServerPlayer sender, @Nonnull ServerPlayer observer, @Nonnull Component message)
    //$$ {
        //$$ if (!hasVanish)
        //$$ {
            //$$ return;
        //$$ }

        //$$ try
        //$$ {
            //$$ VanishAPI.sendHiddenMessage(sender, observer, message);
        //$$ }
        //$$ catch (Throwable e)
        //$$ {
            //$$ AfkPlus.LOGGER.error("VanishCompat#sendHiddenMessage(): has thrown an error: [{}]", e.getMessage());
        //$$ }
    //$$ }
    //#else
    public static void sendHiddenMessage(@Nonnull ServerPlayer sender, @Nonnull ServerPlayer observer, @Nonnull Component message)
    {
        // NO-OP
    }
    //#endif
}
