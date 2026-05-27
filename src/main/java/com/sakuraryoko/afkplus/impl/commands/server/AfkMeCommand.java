/*
 * This file is part of the AfkPlus project, licensed under the
 * GNU Lesser General Public License v3.0
 *
 * Copyright (C) 2026  Sakura Ryoko and contributors
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

package com.sakuraryoko.afkplus.impl.commands.server;

import org.jetbrains.annotations.ApiStatus;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.LongArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;

import com.sakuraryoko.afkplus.impl.AfkPlus;
import com.sakuraryoko.afkplus.impl.Reference;
import com.sakuraryoko.afkplus.impl.commands.PermsWrap;
import com.sakuraryoko.afkplus.impl.commands.arguments.RealTimeArgument;
import com.sakuraryoko.afkplus.impl.compat.morecolors.TextHandler;
import com.sakuraryoko.afkplus.impl.compat.vanish.VanishAPICompat;
import com.sakuraryoko.afkplus.impl.config.ConfigWrap;
import com.sakuraryoko.corelib.api.commands.IServerCommand;

import static net.minecraft.commands.Commands.argument;
import static net.minecraft.commands.Commands.literal;

@ApiStatus.Internal
public class AfkMeCommand implements IServerCommand
{
    @Override
    public void register(CommandDispatcher<CommandSourceStack> dispatcher, CommandBuildContext registryAccess, Commands.CommandSelection environment)
    {
        dispatcher.register(
                literal(this.getName())
                        .requires(PermsWrap.check(this.getNode(), ConfigWrap.afk().afkMeCommandPermissions))
                        .executes(ctx -> this.setAfkMe(ctx, RealTimeArgument.DEFAULT, ""))
                        .then(Commands.argument("time", RealTimeArgument.realTime())
                                      .requires(PermsWrap.check(this.getNode(), ConfigWrap.afk().afkMeCommandPermissions))
                                      .executes(ctx -> this.setAfkMe(ctx, LongArgumentType.getLong(ctx, "time"), ""))
                                      .then(argument("reason", StringArgumentType.greedyString())
                                                    .requires(PermsWrap.check(this.getNode(), ConfigWrap.afk().afkMeCommandPermissions))
                                                    .executes(ctx -> this.setAfkMe(ctx, LongArgumentType.getLong(ctx, "time"), StringArgumentType.getString(ctx, "reason")))
                                      )
                        )
        );
    }

    @Override
    public String getName()
    {
        return "afkme";
    }

    @Override
    public String getModId()
    {
        return Reference.MOD_ID;
    }

    private int setAfkMe(CommandContext<CommandSourceStack> context, Long time, String reason)
    {
        CommandSourceStack src = context.getSource();
        if (src.getPlayer() == null) { return 0; }

        if (VanishAPICompat.hasVanish() && VanishAPICompat.isVanishedByEntity(src.getPlayer()))
        {
            //#if MC >= 1.20.1
            //$$ context.getSource().sendSuccess(() -> TextHandler.getInstance().formatTextSafe(ConfigWrap.mess().whileYourVanished), false);
            //#else
            context.getSource().sendSuccess(TextHandler.getInstance().formatTextSafe(ConfigWrap.mess().whileYourVanished), false);
            //#endif
            return 1;
        }

        AfkPlus.LOGGER.error("setAfkMe: Time: {}, Reason: {}", time, reason);

        // Not yet Implemented
        //#if MC >= 1.20.1
        //$$ context.getSource().sendSuccess(() -> TextHandler.getInstance().formatTextSafe("Not implemented"), false);
        //#else
        context.getSource().sendSuccess(TextHandler.getInstance().formatTextSafe("Not implemented"), false);
        //#endif

        return 1;
    }
}
