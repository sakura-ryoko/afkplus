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

package com.sakuraryoko.afkplus.commands.server;

import me.lucko.fabric.api.permissions.v0.Permissions;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;

import com.sakuraryoko.afkplus.AfkPlus;
import com.sakuraryoko.afkplus.Reference;
import com.sakuraryoko.afkplus.compat.morecolors.TextHandler;
import com.sakuraryoko.afkplus.config.ConfigWrap;
import com.sakuraryoko.afkplus.player.AfkPlayer;
import com.sakuraryoko.afkplus.player.AfkPlayerInfo;
import com.sakuraryoko.afkplus.player.AfkPlayerList;
import com.sakuraryoko.corelib.api.commands.IServerCommand;

import static net.minecraft.commands.Commands.argument;
import static net.minecraft.commands.Commands.literal;

public class AfkInfoCommand implements IServerCommand
{
    @Override
    public void register(CommandDispatcher<CommandSourceStack> dispatcher, CommandBuildContext registryAccess, Commands.CommandSelection environment)
    {
        dispatcher.register(
                literal(this.getName())
                        .requires(Permissions.require(this.getNode(), ConfigWrap.afk().afkInfoCommandPermissions))
                        .then(argument("player", EntityArgument.player())
                                      .executes(ctx -> this.infoAfkPlayer(ctx.getSource(), EntityArgument.getPlayer(ctx, "player"), ctx))
                        )
        );
    }

    @Override
    public String getName()
    {
        return "afkinfo";
    }

    @Override
    public String getModId()
    {
        return Reference.MOD_ID;
    }

    private int infoAfkPlayer(CommandSourceStack src, ServerPlayer player, CommandContext<CommandSourceStack> context)
    {
        AfkPlayer afkPlayer = AfkPlayerList.getInstance().addOrGetPlayer(player);

        if (src.getPlayer() == null || afkPlayer == null)
        {
            return 0;
        }

        if (afkPlayer.isAfk())
        {
            String afkStatus = AfkPlayerInfo.getString(afkPlayer);
            Component afkReason = AfkPlayerInfo.getReason(afkPlayer, src);
            String user = src.getTextName();

            //#if MC >= 12001
            //$$ context.getSource().sendSuccess(() -> TextHandler.getInstance().formatTextSafe(afkStatus), false);
            //$$ context.getSource().sendSuccess(() -> afkReason, false);
            //#else
            context.getSource().sendSuccess(TextHandler.getInstance().formatTextSafe(afkStatus), false);
            context.getSource().sendSuccess(afkReason, false);
            //#endif

            AfkPlus.LOGGER.info("{} displayed {}'s AFK info.", user, afkPlayer.getName());
        }
        else
        {
            //#if MC >= 12001
            //$$ context.getSource().sendSuccess(() -> Component.literal(afkPlayer.getName() + " is not marked as AFK."), false);
            //#else
            context.getSource().sendSuccess(Component.literal(afkPlayer.getName() + " is not marked as AFK."), false);
            //#endif
        }
        return 1;
    }
}
