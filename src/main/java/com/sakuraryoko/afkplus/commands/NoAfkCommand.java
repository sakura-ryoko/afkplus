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

package com.sakuraryoko.afkplus.commands;

import me.lucko.fabric.api.permissions.v0.Permissions;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.Component;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;

import com.sakuraryoko.afkplus.config.ConfigManager;
import com.sakuraryoko.afkplus.data.IAfkPlayer;

import static net.minecraft.commands.Commands.literal;

public class NoAfkCommand
{
    public static void register()
    {
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> dispatcher.register(
                literal("noafk")
                        .requires(Permissions.require("afkplus.noafk", ConfigManager.CONFIG.afkPlusOptions.noAfkCommandPermissions))
                        .executes(ctx -> setNoAfk(ctx.getSource(), ctx))
        ));
    }

    private static int setNoAfk(CommandSourceStack src, CommandContext<CommandSourceStack> context)
            throws CommandSyntaxException
    {
        IAfkPlayer player = (IAfkPlayer) src.getPlayer();
        //String user = src.getTextName();
        if (player.afkplus$isNoAfkEnabled())
        {
            player.afkplus$unsetNoAfkEnabled();
            //#if MC >= 12001
            //$$ context.getSource().sendSuccess(() -> Component.literal("No AFK Mode Disabled. (Timeouts enabled)"), true);
            //#else
            context.getSource().sendSuccess(Component.literal("No AFK Mode Disabled. (Timeouts enabled)"), true);
            //#endif
            //AfkPlusLogger.info(user+ " has disabled No AFK mode. (Timeouts enabled)");
        }
        else
        {
            player.afkplus$setNoAfkEnabled();
            //#if MC >= 12001
            //$$ context.getSource().sendSuccess(() -> Component.literal("No AFK Mode Enabled. (Timeouts disabled)"), true);
            //#else
            context.getSource().sendSuccess(Component.literal("No AFK Mode Enabled. (Timeouts disabled)"), true);
            //#endif
            //AfkPlusLogger.info(user+ " has enabled No AFK mode. (Timeouts disabled)");
        }
        return 1;
    }
}
