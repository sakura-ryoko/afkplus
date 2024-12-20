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

import com.sakuraryoko.afkplus.AfkPlusMod;
import com.sakuraryoko.afkplus.commands.interfaces.IServerCommand;
import com.sakuraryoko.afkplus.config.ConfigWrap;
import com.sakuraryoko.afkplus.text.FormattingExample;

import static net.minecraft.commands.Commands.literal;

public class AfkExCommand implements IServerCommand
{
    public static final AfkExCommand INSTANCE = new AfkExCommand();

    @Override
    public void register(CommandDispatcher<CommandSourceStack> dispatcher, CommandBuildContext registryAccess, Commands.CommandSelection environment)
    {
        dispatcher.register(
                literal(this.getName())
                        .requires(Permissions.require(this.getNode(), ConfigWrap.afk().afkExCommandPermissions))
                        .executes(ctx -> AfkExCommand.INSTANCE.afkExample(ctx.getSource(), ctx))
        );
    }

    @Override
    public String getName()
    {
        return "afkex";
    }

    private int afkExample(CommandSourceStack src, CommandContext<CommandSourceStack> context)
    {
        String user = src.getTextName();
        //#if MC >= 12001
        //$$ context.getSource().sendSuccess(FormattingExample::runBuiltInTest, false);
        //$$ context.getSource().sendSuccess(FormattingExample::runAliasTest, false);
        //$$ context.getSource().sendSuccess(FormattingExample::runColorsTest, false);
        //#else
        context.getSource().sendSuccess(FormattingExample.runBuiltInTest(), false);
        context.getSource().sendSuccess(FormattingExample.runAliasTest(), false);
        context.getSource().sendSuccess(FormattingExample.runColorsTest(), false);
        //#endif
        AfkPlusMod.debugLog("{} has executed /afkex (example) .", user);
        return 1;
    }
}
