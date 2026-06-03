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

import com.mojang.authlib.GameProfile;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
//#if MC >= 1.21.10
//$$ import net.minecraft.server.players.NameAndId;
//#endif

import com.sakuraryoko.afkplus.impl.Reference;
import com.sakuraryoko.afkplus.impl.commands.PermsWrap;
import com.sakuraryoko.afkplus.impl.compat.morecolors.TextHandler;
import com.sakuraryoko.afkplus.impl.compat.vanish.VanishAPICompat;
import com.sakuraryoko.afkplus.impl.config.ConfigWrap;
import com.sakuraryoko.afkplus.impl.player.shadow.ShadowServerPlayer;
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
                        .executes(ctx -> this.setAfkMe(ctx, -1, ""))
                        .then(argument("time", IntegerArgumentType.integer(0))
                                      .requires(PermsWrap.check(this.getNode(), ConfigWrap.afk().afkMeCommandPermissions))
                                      .executes(ctx -> this.setAfkMe(ctx, IntegerArgumentType.getInteger(ctx, "time"), ""))
                                      .then(argument("reason", StringArgumentType.greedyString())
                                                    .requires(PermsWrap.check(this.getNode(), ConfigWrap.afk().afkMeCommandPermissions))
                                                    .executes(ctx -> this.setAfkMe(ctx, IntegerArgumentType.getInteger(ctx, "time"), StringArgumentType.getString(ctx, "reason")))
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

    private int setAfkMe(CommandContext<CommandSourceStack> context, int time, String reason)
    {
        CommandSourceStack src = context.getSource();
        if (src.getPlayer() == null)
        {
            return 0;
        }

        if (VanishAPICompat.hasVanish() && VanishAPICompat.isVanishedByEntity(src.getPlayer()))
        {
            //#if MC >= 1.20.1
            //$$ context.getSource().sendSuccess(() -> TextHandler.getInstance().formatTextSafe(ConfigWrap.mess().whileYourVanished), false);
            //#else
            context.getSource().sendSuccess(TextHandler.getInstance().formatTextSafe(ConfigWrap.mess().whileYourVanished), false);
            //#endif
            return 1;
        }

        if (!ConfigWrap.afkMe().afkMeEnabled)
        {
            String msg = "<red>/afkme Command is not enabled<r>";
            //#if MC >= 1.20.1
            //$$ context.getSource().sendSuccess(() -> TextHandler.getInstance().formatTextSafe(msg), false);
            //#else
            context.getSource().sendSuccess(TextHandler.getInstance().formatTextSafe(msg), false);
            //#endif
            return 1;
        }

        MinecraftServer server = src.getServer();
        ServerPlayer player = src.getPlayer();
        GameProfile profile = player.getGameProfile();

        //#if MC >= 1.21.10
        //$$ if (server.isSingleplayerOwner(new NameAndId(profile)))
        //#else
        if (server.isSingleplayerOwner(profile))
        //#endif
        {
            String msg = "<red>Can't use shadow as the single player server owner<r>";
            //#if MC >= 1.20.1
            //$$ context.getSource().sendSuccess(() -> TextHandler.getInstance().formatTextSafe(msg), false);
            //#else
            context.getSource().sendSuccess(TextHandler.getInstance().formatTextSafe(msg), false);
            //#endif
            return 1;
        }

        if (time < 0)
        {
            time = ConfigWrap.afkMe().defaultShadowTimeout;

            if (time < 0)
            {
                time = 0;
            }
        }
        if (reason == null || reason.isEmpty())
        {
            reason = ConfigWrap.afkMe().defaultShadowReason;

            if (reason == null || reason.isEmpty())
            {
                reason = "<r>none";
            }
        }

        ShadowServerPlayer.createShadow(server, player, time, reason);

        return 1;
    }
}
