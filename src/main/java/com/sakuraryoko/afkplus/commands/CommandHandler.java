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

import java.util.ArrayList;
import java.util.List;
import org.jetbrains.annotations.ApiStatus;

import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;

import com.sakuraryoko.afkplus.commands.interfaces.ICommandManager;
import com.sakuraryoko.afkplus.commands.interfaces.IServerCommand;

public class CommandHandler implements ICommandManager
{
    private static final CommandHandler INSTANCE = new CommandHandler();
    private final List<IServerCommand> commands = new ArrayList<>();
    public static ICommandManager getInstance() { return INSTANCE; }

    @Override
    public void registerCommand(IServerCommand command)
    {
        if (!this.commands.contains(command))
        {
            this.commands.add(command);
        }
    }

    @ApiStatus.Internal
    public void registerCommands(CommandDispatcher<CommandSourceStack> dispatcher,
                                 CommandBuildContext registryAccess,
                                 Commands.CommandSelection environment)
    {
        this.commands.forEach((iServerCommand -> iServerCommand.register(dispatcher, registryAccess, environment)));
    }
}
