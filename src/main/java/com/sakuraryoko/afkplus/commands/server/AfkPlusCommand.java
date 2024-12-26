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

import java.util.List;
import me.lucko.fabric.api.permissions.v0.Permissions;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
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
import com.sakuraryoko.afkplus.compat.vanish.VanishAPICompat;
import com.sakuraryoko.afkplus.config.AfkConfigHandler;
import com.sakuraryoko.afkplus.config.ConfigWrap;
import com.sakuraryoko.afkplus.modinit.AfkPlusInit;
import com.sakuraryoko.afkplus.player.AfkPlayer;
import com.sakuraryoko.afkplus.player.AfkPlayerInfo;
import com.sakuraryoko.afkplus.player.AfkPlayerList;
//import com.sakuraryoko.afkplus.text.FormattingExample;
import com.sakuraryoko.corelib.api.commands.IServerCommand;
import com.sakuraryoko.corelib.api.modinit.ModInitData;
import com.sakuraryoko.corelib.impl.config.ConfigManager;
import com.sakuraryoko.morecolors.impl.modinit.MoreColorInit;

import static net.minecraft.commands.Commands.argument;
import static net.minecraft.commands.Commands.literal;

public class AfkPlusCommand implements IServerCommand
{
    @Override
    public void register(CommandDispatcher<CommandSourceStack> dispatcher, CommandBuildContext registryAccess, Commands.CommandSelection environment)
    {
        dispatcher.register(
                literal(this.getName())
                        .requires(Permissions.require(this.getNode(), ConfigWrap.afk().afkPlusCommandPermissions))
                        .executes(ctx -> this.about(ctx.getSource(), ctx))
                        .then(literal("reload")
                                      .requires(Permissions.require(this.getNode()+".reload", ConfigWrap.afk().afkPlusCommandPermissions))
                                      .executes(ctx -> this.reload(ctx.getSource(), ctx))
                        )
                        .then(literal("save")
                                      .requires(Permissions.require(this.getNode()+".save", ConfigWrap.afk().afkPlusCommandPermissions))
                                      .executes(ctx -> this.save(ctx.getSource(), ctx))
                        )
                        .then(literal("defaults")
                                      .requires(Permissions.require(this.getNode()+".defaults", 4))
                                      .executes(ctx -> this.reload(ctx.getSource(), ctx))
                        )
                        .then(literal("set")
                                      .requires(Permissions.require(this.getNode()+".set", ConfigWrap.afk().afkPlusCommandPermissions))
                                      .then(argument("player",
                                                     EntityArgument.player())
                                                    .executes((ctx) -> this.setAfk(ctx.getSource(), EntityArgument.getPlayer(ctx, "player"), "", ctx))
                                                    .then(argument("reason", StringArgumentType.greedyString())
                                                                  .requires(Permissions.require(this.getNode()+".set", ConfigWrap.afk().afkPlusCommandPermissions))
                                                                  .executes((ctx) -> this.setAfk(ctx.getSource(), EntityArgument.getPlayer(ctx, "player"), StringArgumentType.getString(ctx, "reason"), ctx))
                                                    )
                                      )
                        )
                        .then(literal("clear")
                                      .requires(Permissions.require(this.getNode()+".clear", ConfigWrap.afk().afkPlusCommandPermissions))
                                      .then(argument("player", EntityArgument.player())
                                                    .executes(ctx -> this.clearAfk(ctx.getSource(), EntityArgument.getPlayer(ctx, "player"), ctx))
                                      )
                        )
                        .then(literal("info")
                                      .requires(Permissions.require(this.getNode()+".info", ConfigWrap.afk().afkPlusCommandPermissions))
                                      .then(argument("player", EntityArgument.player())
                                                    .executes(ctx -> this.infoAfkPlayer(ctx.getSource(), EntityArgument.getPlayer(ctx, "player"), ctx))
                                      )
                        )
                        .then(literal("damage")
                                      .requires(Permissions.require(this.getNode()+".damage", ConfigWrap.afk().afkPlusCommandPermissions))
                                      .then(literal("disable")
                                                    .requires(Permissions.require(this.getNode()+".damage.disable", ConfigWrap.afk().afkPlusCommandPermissions))
                                                    .then(argument("player", EntityArgument.player())
                                                                  .executes(ctx -> this.disableDamagePlayer(ctx.getSource(), EntityArgument.getPlayer(ctx, "player"), ctx))
                                                    )
                                      )
                                      .then(literal("enable")
                                                    .requires(Permissions.require(this.getNode()+".damage.enable", ConfigWrap.afk().afkPlusCommandPermissions))
                                                    .then(argument("player", EntityArgument.player())
                                                                  .executes(ctx -> this.enableDamagePlayer(ctx.getSource(), EntityArgument.getPlayer(ctx, "player"), ctx))
                                                    )
                                      )
                        )
                        .then(literal("update")
                                      .requires(Permissions.require(this.getNode()+".update", ConfigWrap.afk().afkPlusCommandPermissions))
                                      .then(argument("player", EntityArgument.player())
                                                    .executes(ctx -> this.updatePlayer(ctx.getSource(), EntityArgument.getPlayer(ctx, "player"), ctx)
                                                    )
                                      )
                        )

        );
    }

    @Override
    public String getName()
    {
        return this.getModId();
    }

    @Override
    public String getModId()
    {
        return Reference.MOD_ID;
    }

    private int about(CommandSourceStack src, CommandContext<CommandSourceStack> context)
    {
        List<Component> info = AfkPlusInit.getInstance().getPlaceholderFormatted(ModInitData.ALL_INFO);
        String user = src.getTextName();

        for (Component entry : info)
        {
            //#if MC >= 12001
            //$$ context.getSource().sendSuccess(() -> entry, false);
            //#else
            context.getSource().sendSuccess(entry, false);
            //#endif
        }

        AfkPlus.debugLog("{} has executed /afkplus .", user);
        return 1;
    }

    private int reload(CommandSourceStack src, CommandContext<CommandSourceStack> context)
    {
        String user = src.getTextName();
        ConfigManager.getInstance().reloadEach(AfkConfigHandler.getInstance());
        //#if MC >= 12001
        //$$ context.getSource().sendSuccess(() -> Component.literal("Reloaded config!"), false);
        //#else
        context.getSource().sendSuccess(Component.literal("Reloaded config!"), false);
        //#endif
        AfkPlus.LOGGER.info("{} has reloaded the configuration.", user);
        return 1;
    }

    private int save(CommandSourceStack src, CommandContext<CommandSourceStack> context)
    {
        String user = src.getTextName();
        ConfigManager.getInstance().saveEach(AfkConfigHandler.getInstance());
        //#if MC >= 12001
        //$$ context.getSource().sendSuccess(() -> Component.literal("Saving config!"), false);
        //#else
        context.getSource().sendSuccess(Component.literal("Saving config!"), false);
        //#endif
        AfkPlus.LOGGER.info("{} has saved the configuration.", user);
        return 1;
    }

    private int defaults(CommandSourceStack src, CommandContext<CommandSourceStack> context)
    {
        String user = src.getTextName();
        ConfigManager.getInstance().defaultEach(AfkConfigHandler.getInstance());
        //#if MC >= 12001
        //$$ context.getSource().sendSuccess(() -> Component.literal("Set config defaults!"), false);
        //#else
        context.getSource().sendSuccess(Component.literal("Set config defaults!"), false);
        //#endif
        AfkPlus.LOGGER.info("{} has set the default configuration.", user);
        return 1;
    }

    private int setAfk(CommandSourceStack src, ServerPlayer player, String reason, CommandContext<CommandSourceStack> context)
    {
        AfkPlayer afkPlayer = AfkPlayerList.getInstance().addOrGetPlayer(player);
        String user = src.getTextName();

        if (VanishAPICompat.hasVanish() && VanishAPICompat.isVanishedByEntity(player))
        {
            String response = afkPlayer.getName() + " <red>is vanished, and shouldn't be going afk ...<r>";
            //#if MC >= 12001
            //$$ context.getSource().sendSuccess(() -> TextHandler.getInstance().formatTextSafe(response), false);
            //#else
            context.getSource().sendSuccess(TextHandler.getInstance().formatTextSafe(response), false);
            //#endif
            return 1;
        }

        if (afkPlayer.isAfk())
        {
            //#if MC >= 12001
            //$$ context.getSource().sendSuccess(() -> Component.literal(afkPlayer.getName() + " is already marked as AFK."), false);
            //#else
            context.getSource().sendSuccess(Component.literal(afkPlayer.getName() + " is already marked as AFK."), false);
            //#endif
        }
        else
        {
            if (afkPlayer.isNoAfkEnabled())
            {
                afkPlayer.setNoAfkEnabled(false);
                //AfkPlusLogger.info(user + " set player " + afkPlayer.getName() + "'s NoAfk status OFF.");
                //#if MC >= 12001
                //$$ context.getSource().sendSuccess(() -> Component.literal("Toggled player " + afkPlayer.getName() + "'s NoAFK status OFF."), true);
                //#else
                context.getSource().sendSuccess(Component.literal("Toggled player " + afkPlayer.getName() + "'s NoAFK status OFF."), true);
                //#endif
            }

            if (reason == null && ConfigWrap.mess().defaultReason == null)
            {
                afkPlayer.getHandler().registerAfk("via /afkplus set");
                AfkPlus.LOGGER.info("{} set player {} as AFK", user, afkPlayer.getName());
            }
            else if (reason == null || reason.isEmpty())
            {
                afkPlayer.getHandler().registerAfk(ConfigWrap.mess().defaultReason);
                AfkPlus.LOGGER.info("{} set player {} as AFK for reason: {}", user, afkPlayer.getName(), ConfigWrap.mess().defaultReason);
            }
            else
            {
                afkPlayer.getHandler().registerAfk(reason);
                AfkPlus.LOGGER.info("{} set player {} as AFK for reason: {}", user, afkPlayer.getName(), reason);
            }
        }
        return 1;
    }

    private int clearAfk(CommandSourceStack src, ServerPlayer player, CommandContext<CommandSourceStack> context)
    {
        AfkPlayer afkPlayer = AfkPlayerList.getInstance().addOrGetPlayer(player);
        String user = src.getTextName();

        if (VanishAPICompat.hasVanish() && VanishAPICompat.isVanishedByEntity(player))
        {
            String response = afkPlayer.getName() + " <red>is vanished, and shouldn't be afk ...<r>";
            //#if MC >= 12001
            //$$ context.getSource().sendSuccess(() -> TextHandler.getInstance().formatTextSafe(response), false);
            //#else
            context.getSource().sendSuccess(TextHandler.getInstance().formatTextSafe(response), false);
            //#endif
            return 1;
        }

        if (afkPlayer.isAfk())
        {
            afkPlayer.getHandler().unregisterAfk();
            AfkPlus.LOGGER.info("{} cleared player {} from AFK", user, afkPlayer.getName());
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

    private int infoAfkPlayer(CommandSourceStack src, ServerPlayer player, CommandContext<CommandSourceStack> context)
    {
        AfkPlayer afkPlayer = AfkPlayerList.getInstance().addOrGetPlayer(player);
        String user = src.getTextName();

        if (afkPlayer.isAfk())
        {
            String afkStatus = AfkPlayerInfo.getString(afkPlayer);
            Component afkReason = AfkPlayerInfo.getReason(afkPlayer, src);
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

    private int disableDamagePlayer(CommandSourceStack src, ServerPlayer player, CommandContext<CommandSourceStack> context)
    {
        AfkPlayer afkPlayer = AfkPlayerList.getInstance().addOrGetPlayer(player);
        String user = src.getTextName();

        if (VanishAPICompat.hasVanish() && VanishAPICompat.isVanishedByEntity(player))
        {
            String response = afkPlayer.getName() + "<red>is vanished, and shouldn't be changing their disable Damage status.<r>";
            //#if MC >= 12001
            //$$ context.getSource().sendSuccess(() -> TextHandler.getInstance().formatTextSafe(response), false);
            //#else
            context.getSource().sendSuccess(TextHandler.getInstance().formatTextSafe(response), false);
            //#endif
            return 1;
        }
        if (afkPlayer.isLockDamageEnabled())
        {
            afkPlayer.getHandler().unlockDamageEnabled();
            //#if MC >= 12001
            //$$ context.getSource().sendSuccess(() -> Component.literal("Allowing Damage Disable feature for player " + afkPlayer.getName()), true);
            //#else
            context.getSource().sendSuccess(Component.literal("Allowing Damage Disable feature for player " + afkPlayer.getName()), true);
            //#endif
            AfkPlus.LOGGER.info("{} Allowing Damage Disable feature for player {}", user, afkPlayer.getName());
        }
        else
        {
            //#if MC >= 12001
            //$$ context.getSource().sendSuccess(() -> Component.literal("Damage Disable is already allowed for player " + afkPlayer.getName()), false);
            //#else
            context.getSource().sendSuccess(Component.literal("Damage Disable is already allowed for player " + afkPlayer.getName()), false);
            //#endif
        }
        return 1;
    }

    private int enableDamagePlayer(CommandSourceStack src, ServerPlayer player, CommandContext<CommandSourceStack> context)
    {
        AfkPlayer afkPlayer = AfkPlayerList.getInstance().addOrGetPlayer(player);
        String user = src.getTextName();

        if (VanishAPICompat.hasVanish() && VanishAPICompat.isVanishedByEntity(player))
        {
            String response = afkPlayer.getName() + "<red>is vanished, and shouldn't be changing their disable Damage status.<r>";
            //#if MC >= 12001
            //$$ context.getSource().sendSuccess(() -> TextHandler.getInstance().formatTextSafe(response), false);
            //#else
            context.getSource().sendSuccess(TextHandler.getInstance().formatTextSafe(response), false);
            //#endif
            return 1;
        }
        if (!afkPlayer.isLockDamageEnabled())
        {
            afkPlayer.getHandler().lockDamageEnabled();
            //#if MC >= 12001
            //$$ context.getSource().sendSuccess(() -> Component.literal("Force-Enabling Damage for player " + afkPlayer.getName()), true);
            //#else
            context.getSource().sendSuccess(Component.literal("Force-Enabling Damage for player " + afkPlayer.getName()), true);
            //#endif
            AfkPlus.LOGGER.info("{} Force-Enabling Damage for player {}", user, afkPlayer.getName());
        }
        else
        {
            //#if MC >= 12001
            //$$ context.getSource().sendSuccess(() -> Component.literal("Damage Disable is already disallowed for player " + afkPlayer.getName()), false);
            //#else
            context.getSource().sendSuccess(Component.literal("Damage Disable is already disallowed for player " + afkPlayer.getName()), false);
            //#endif
        }
        return 1;
    }

    private int updatePlayer(CommandSourceStack src, ServerPlayer player, CommandContext<CommandSourceStack> context)
    {
        AfkPlayer afkPlayer = AfkPlayerList.getInstance().addOrGetPlayer(player);
        String user = src.getTextName();

        if (VanishAPICompat.hasVanish() && VanishAPICompat.isVanishedByEntity(player))
        {
            String response = afkPlayer.getName() + "<red>is vanished, and shouldn't be updating their player list status.<r>";
            //#if MC >= 12001
            //$$ context.getSource().sendSuccess(() -> TextHandler.getInstance().formatTextSafe(response), false);
            //#else
            context.getSource().sendSuccess(TextHandler.getInstance().formatTextSafe(response), false);
            //#endif
            return 1;
        }

        afkPlayer.getHandler().updatePlayerList();
        //#if MC >= 12001
        //$$ context.getSource().sendSuccess(() -> Component.literal("Updating player list entry for " + afkPlayer.getName()), false);
        //#else
        context.getSource().sendSuccess(Component.literal("Updating player list entry for " + afkPlayer.getName()), false);
        //#endif
        AfkPlus.LOGGER.info("{} updated player list entry for {}", user, afkPlayer.getName());
        return 1;
    }
}
