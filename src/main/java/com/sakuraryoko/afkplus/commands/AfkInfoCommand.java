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
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;

import com.sakuraryoko.afkplus.AfkPlusMod;
import com.sakuraryoko.afkplus.config.ConfigWrap;
import com.sakuraryoko.afkplus.text.TextUtils;
import com.sakuraryoko.afkplus.player.IAfkPlayer;
import com.sakuraryoko.afkplus.player.AfkPlayerInfo;

import static net.minecraft.commands.Commands.argument;
import static net.minecraft.commands.Commands.literal;

public class AfkInfoCommand
{
    public static void register()
    {
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> dispatcher.register(
                literal("afkinfo")
                        .requires(Permissions.require("afkplus.afkinfo", ConfigWrap.afk().afkInfoCommandPermissions))
                        .then(argument("player", EntityArgument.player())
                                      .executes(ctx -> infoAfkPlayer(ctx.getSource(), EntityArgument.getPlayer(ctx, "player"), ctx))
                        )
        ));
    }

    private static int infoAfkPlayer(CommandSourceStack src, ServerPlayer player, CommandContext<CommandSourceStack> context)
    {
        IAfkPlayer afkPlayer = (IAfkPlayer) player;
        String user = src.getTextName();
        if (afkPlayer.afkplus$isAfk())
        {
            String afkStatus = AfkPlayerInfo.getString(afkPlayer);
            Component afkReason = AfkPlayerInfo.getReason(afkPlayer, src);
            //#if MC >= 12001
            //$$ context.getSource().sendSuccess(() -> TextUtils.formatTextSafe(afkStatus), false);
            //$$ context.getSource().sendSuccess(() -> afkReason, false);
            //#else
            context.getSource().sendSuccess(TextUtils.formatTextSafe(afkStatus), false);
            context.getSource().sendSuccess(afkReason, false);
            //#endif
            AfkPlusMod.LOGGER.info("{} displayed {}'s AFK info.", user, afkPlayer.afkplus$getName());
        }
        else
        {
            //#if MC >= 12001
            //$$ context.getSource().sendSuccess(() -> Component.literal(afkPlayer.afkplus$getName() + " is not marked as AFK."), false);
            //#else
            context.getSource().sendSuccess(Component.literal(afkPlayer.afkplus$getName() + " is not marked as AFK."), false);
            //#endif
        }
        return 1;
    }
}
