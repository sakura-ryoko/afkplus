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

import java.util.Objects;
import me.lucko.fabric.api.permissions.v0.Permissions;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;

import com.sakuraryoko.afkplus.commands.interfaces.IServerCommand;
import com.sakuraryoko.afkplus.compat.vanish.VanishAPICompat;
import com.sakuraryoko.afkplus.config.ConfigWrap;
import com.sakuraryoko.afkplus.player.AfkPlayer;
import com.sakuraryoko.afkplus.player.AfkPlayerList;
import com.sakuraryoko.afkplus.text.TextUtils;

import static net.minecraft.commands.Commands.literal;

public class NoAfkCommand implements IServerCommand
{
    public static final NoAfkCommand INSTANCE = new NoAfkCommand();
    
    @Override
    public void register(CommandDispatcher<CommandSourceStack> dispatcher, CommandBuildContext registryAccess, Commands.CommandSelection environment)
    {
        dispatcher.register(
                literal(this.getName())
                        .requires(Permissions.require(this.getNode(), ConfigWrap.afk().noAfkCommandPermissions))
                        .executes(ctx -> this.setNoAfk(ctx.getSource(), ctx))
        );
    }

    @Override
    public String getName()
    {
        return "noafk";
    }

    private int setNoAfk(CommandSourceStack src, CommandContext<CommandSourceStack> context)
    {
        //String user = src.getTextName();
        if (src.getPlayer() == null)
        {
            return 0;
        }
        if (VanishAPICompat.hasVanish() && VanishAPICompat.isVanishedByEntity(src.getPlayer()))
        {
            String response = "<red>You are vanished, and shouldn't be using the /noafk command ...<r>";
            //#if MC >= 12001
            //$$ context.getSource().sendSuccess(() -> TextUtils.formatTextSafe(response), false);
            //#else
            context.getSource().sendSuccess(TextUtils.formatTextSafe(response), false);
            //#endif
            return 1;
        }

        AfkPlayer afkPlayer = AfkPlayerList.getInstance().addOrGetPlayer(src.getPlayer());

        if (afkPlayer.isNoAfkEnabled())
        {
            afkPlayer.setNoAfkEnabled(false);
            //#if MC >= 12001
            //$$ context.getSource().sendSuccess(() -> Component.literal("No AFK Mode Disabled. (Timeouts enabled)"), true);
            //#else
            context.getSource().sendSuccess(Component.literal("No AFK Mode Disabled. (Timeouts enabled)"), true);
            //#endif
            //AfkPlusLogger.info(user+ " has disabled No AFK mode. (Timeouts enabled)");
        }
        else
        {
            afkPlayer.setNoAfkEnabled(true);
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
