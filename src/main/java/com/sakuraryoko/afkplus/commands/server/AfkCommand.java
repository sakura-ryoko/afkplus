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

import eu.pb4.placeholders.api.PlaceholderContext;
import eu.pb4.placeholders.api.Placeholders;
import me.lucko.fabric.api.permissions.v0.Permissions;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.Util;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;

import com.sakuraryoko.afkplus.Reference;
import com.sakuraryoko.afkplus.compat.morecolors.TextHandler;
import com.sakuraryoko.afkplus.compat.vanish.VanishAPICompat;
import com.sakuraryoko.afkplus.config.ConfigWrap;
import com.sakuraryoko.afkplus.player.AfkPlayer;
import com.sakuraryoko.afkplus.player.AfkPlayerList;
import com.sakuraryoko.corelib.api.commands.IServerCommand;

import static net.minecraft.commands.Commands.argument;
import static net.minecraft.commands.Commands.literal;

public class AfkCommand implements IServerCommand
{
    @Override
    public void register(CommandDispatcher<CommandSourceStack> dispatcher, CommandBuildContext registryAccess, Commands.CommandSelection environment)
    {
        dispatcher.register(
                literal(this.getName())
                        .requires(Permissions.require(this.getNode(), ConfigWrap.afk().afkCommandPermissions))
                        .executes(ctx -> this.setAfk(ctx.getSource(), "", ctx))
                        .then(argument("reason", StringArgumentType.greedyString())
                                      .requires(Permissions.require(this.getNode(), ConfigWrap.afk().afkCommandPermissions))
                                      .executes(ctx -> this.setAfk(ctx.getSource(), StringArgumentType.getString(ctx, "reason"), ctx))
                        )
        );
    }

    @Override
    public String getName()
    {
        return "afk";
    }

    @Override
    public String getModId()
    {
        return Reference.MOD_ID;
    }

    private int setAfk(CommandSourceStack src, String reason, CommandContext<CommandSourceStack> context)
    {
        if (src.getPlayer() == null)
        {
            return 0;
        }

        if (VanishAPICompat.hasVanish() && VanishAPICompat.isVanishedByEntity(src.getPlayer()))
        {
            //#if MC >= 12001
            //$$ context.getSource().sendSuccess(() -> TextHandler.getInstance().formatTextSafe(ConfigWrap.mess().whileYourVanished), false);
            //#else
            context.getSource().sendSuccess(TextHandler.getInstance().formatTextSafe(ConfigWrap.mess().whileYourVanished), false);
            //#endif
            return 1;
        }

        AfkPlayer afkPlayer = AfkPlayerList.getInstance().addOrGetPlayer(src.getPlayer());

        if (afkPlayer.isAfk())
        {
            afkPlayer.getHandler().unregisterAfk();
        }
        else if ((Util.getMillis() - afkPlayer.getLastAfkTimeMs()) < (ConfigWrap.afk().afkCommandCooldown * 1000L))
        {
            Component result = Placeholders.parseText(
                    TextHandler.getInstance().formatTextSafe(ConfigWrap.mess().afkCooldownGreeting),
                    PlaceholderContext.of(src));

            //#if MC >= 12001
            //$$ context.getSource().sendSuccess(() -> result, false);
            //#else
            context.getSource().sendSuccess(result, false);
            //#endif
            return 1;
        }
        else
        {
            if (reason == null && ConfigWrap.mess().defaultReason == null)
            {
                afkPlayer.getHandler().registerAfk(null);
            }
            else if (reason == null || reason.isEmpty())
            {
                afkPlayer.getHandler().registerAfk(ConfigWrap.mess().defaultReason);
            }
            else
            {
                afkPlayer.getHandler().registerAfk(reason);
            }
        }

        return 1;
    }
}
