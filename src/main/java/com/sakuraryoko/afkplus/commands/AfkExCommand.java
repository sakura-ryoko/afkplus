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
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;

import com.sakuraryoko.afkplus.config.ConfigManager;
import com.sakuraryoko.afkplus.util.AfkLogger;
import com.sakuraryoko.afkplus.text.FormattingExample;

import static net.minecraft.commands.Commands.literal;

public class AfkExCommand
{
    public static void register()
    {
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> dispatcher.register(
                literal("afkex")
                        .requires(Permissions.require("afkplus.afkex", ConfigManager.CONFIG.afkPlusOptions.afkExCommandPermissions))
                        .executes(ctx -> afkExample(ctx.getSource(), ctx))
        ));
    }

    private static int afkExample(CommandSourceStack src, CommandContext<CommandSourceStack> context)
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
        AfkLogger.debug(user + " has executed /afkex (example) .");
        return 1;
    }
}
