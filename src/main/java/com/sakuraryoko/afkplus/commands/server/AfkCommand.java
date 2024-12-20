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
import com.mojang.brigadier.arguments.StringArgumentType;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;

import com.sakuraryoko.afkplus.commands.interfaces.IServerCommand;
import com.sakuraryoko.afkplus.config.ConfigWrap;
import com.sakuraryoko.afkplus.player.IAfkPlayer;

import static net.minecraft.commands.Commands.argument;
import static net.minecraft.commands.Commands.literal;

public class AfkCommand implements IServerCommand
{
    public static final AfkCommand INSTANCE = new AfkCommand();

    @Override
    public void register(CommandDispatcher<CommandSourceStack> dispatcher, CommandBuildContext registryAccess, Commands.CommandSelection environment)
    {
        dispatcher.register(
                literal(this.getName())
                        .requires(Permissions.require(this.getNode(), ConfigWrap.afk().afkCommandPermissions))
                        .executes(ctx -> this.setAfk(ctx.getSource(), ""))
                        .then(argument("reason", StringArgumentType.greedyString())
                                      .requires(Permissions.require(this.getNode(), ConfigWrap.afk().afkCommandPermissions))
                                      .executes(ctx -> this.setAfk(ctx.getSource(), StringArgumentType.getString(ctx, "reason")))
                        )
        );
    }

    @Override
    public String getName()
    {
        return "afk";
    }

    private int setAfk(CommandSourceStack src, String reason)
    {
        IAfkPlayer player = (IAfkPlayer) src.getPlayer();
        if (player == null)
        {
            return 0;
        }
        if (reason == null && ConfigWrap.mess().defaultReason == null)
        {
            player.afkplus$registerAfk("via /afk");
        }
        else if (reason == null || reason.isEmpty())
        {
            player.afkplus$registerAfk(ConfigWrap.mess().defaultReason);
        }
        else
        {
            player.afkplus$registerAfk(reason);
        }
        return 1;
    }
}
