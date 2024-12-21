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
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;

import com.sakuraryoko.afkplus.AfkPlusMod;
import com.sakuraryoko.afkplus.commands.interfaces.IServerCommand;
import com.sakuraryoko.afkplus.compat.vanish.VanishAPICompat;
import com.sakuraryoko.afkplus.config.AfkConfigManager;
import com.sakuraryoko.afkplus.config.ConfigWrap;
import com.sakuraryoko.afkplus.player.AfkPlayerInfo;
import com.sakuraryoko.afkplus.player.IAfkPlayer;
import com.sakuraryoko.afkplus.text.FormattingExample;
import com.sakuraryoko.afkplus.text.TextUtils;
import com.sakuraryoko.afkplus.util.AfkPlusInfo;

import static net.minecraft.commands.Commands.argument;
import static net.minecraft.commands.Commands.literal;

public class AfkPlusCommand implements IServerCommand
{
    public static final AfkPlusCommand INSTANCE = new AfkPlusCommand();
    
    @Override
    public void register(CommandDispatcher<CommandSourceStack> dispatcher, CommandBuildContext registryAccess, Commands.CommandSelection environment)
    {
        dispatcher.register(
                literal(this.getName())
                        .requires(Permissions.require(this.getNode(), ConfigWrap.afk().afkPlusCommandPermissions))
                        .executes(ctx -> this.afkAbout(ctx.getSource(), ctx))
                        .then(literal("ex")
                                      .requires(Permissions.require(this.getNode()+".ex", 4))
                                      .executes(ctx -> this.afkExample(ctx.getSource(), ctx))
                        )
                        .then(literal("reload")
                                      .requires(Permissions.require(this.getNode()+".reload", ConfigWrap.afk().afkPlusCommandPermissions))
                                      .executes(ctx -> this.afkReload(ctx.getSource(), ctx))
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
        return "afkplus";
    }

    private int afkAbout(CommandSourceStack src, CommandContext<CommandSourceStack> context)
    {
        Component ModInfo = AfkPlusInfo.getModInfoText();
        String user = src.getTextName();
        //#if MC >= 12001
        //$$ context.getSource().sendSuccess(() -> ModInfo, false);
        //#else
        context.getSource().sendSuccess(ModInfo, false);
        //#endif
        AfkPlusMod.debugLog("{} has executed /afkplus .", user);
        return 1;
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
        AfkPlusMod.debugLog("{} has executed /afkplus example .", user);
        return 1;
    }

    private int afkReload(CommandSourceStack src, CommandContext<CommandSourceStack> context)
    {
        String user = src.getTextName();
        //TomlConfigManager.reloadConfig();
        AfkConfigManager.getInstance().reloadAllConfigs();
        //#if MC >= 12001
        //$$ context.getSource().sendSuccess(() -> Component.literal("Reloaded config!"), false);
        //#else
        context.getSource().sendSuccess(Component.literal("Reloaded config!"), false);
        //#endif
        AfkPlusMod.LOGGER.info(user + " has reloaded the configuration.");
        return 1;
    }

    private int setAfk(CommandSourceStack src, ServerPlayer player, String reason, CommandContext<CommandSourceStack> context)
    {
        IAfkPlayer afkPlayer = (IAfkPlayer) player;
        String user = src.getTextName();

        if (VanishAPICompat.hasVanish() && VanishAPICompat.isVanishedByEntity(player))
        {
            String response = afkPlayer.afkplus$getName() + " <red>is vanished, and shouldn't be going afk ...<r>";
            //#if MC >= 12001
            //$$ context.getSource().sendSuccess(() -> TextUtils.formatTextSafe(response), false);
            //#else
            context.getSource().sendSuccess(TextUtils.formatTextSafe(response), false);
            //#endif
            return 1;
        }
        if (afkPlayer.afkplus$isAfk())
        {
            //#if MC >= 12001
            //$$ context.getSource().sendSuccess(() -> Component.literal(afkPlayer.afkplus$getName() + " is already marked as AFK."), false);
            //#else
            context.getSource().sendSuccess(Component.literal(afkPlayer.afkplus$getName() + " is already marked as AFK."), false);
            //#endif
        }
        else
        {
            if (afkPlayer.afkplus$isNoAfkEnabled())
            {
                afkPlayer.afkplus$unsetNoAfkEnabled();
                //AfkPlusLogger.info(user + " set player " + afkPlayer.afkplus$getName() + "'s NoAfk status OFF.");
                //#if MC >= 12001
                //$$ context.getSource().sendSuccess(() -> Component.literal("Toggled player " + afkPlayer.afkplus$getName() + "'s NoAFK status OFF."), true);
                //#else
                context.getSource().sendSuccess(Component.literal("Toggled player " + afkPlayer.afkplus$getName() + "'s NoAFK status OFF."), true);
                //#endif
            }
            if (reason == null && ConfigWrap.mess().defaultReason == null)
            {
                afkPlayer.afkplus$registerAfk("via /afkplus set");
                AfkPlusMod.LOGGER.info("{} set player {} as AFK", user, afkPlayer.afkplus$getName());
            }
            else if (reason == null || reason.isEmpty())
            {
                afkPlayer.afkplus$registerAfk(ConfigWrap.mess().defaultReason);
                AfkPlusMod.LOGGER.info("{} set player {} as AFK for reason: {}", user, afkPlayer.afkplus$getName(), ConfigWrap.mess().defaultReason);
            }
            else
            {
                afkPlayer.afkplus$registerAfk(reason);
                AfkPlusMod.LOGGER.info("{} set player {} as AFK for reason: {}", user, afkPlayer.afkplus$getName(), reason);
            }
        }
        return 1;
    }

    private int clearAfk(CommandSourceStack src, ServerPlayer player, CommandContext<CommandSourceStack> context)
    {
        IAfkPlayer afkPlayer = (IAfkPlayer) player;
        String user = src.getTextName();

        if (VanishAPICompat.hasVanish() && VanishAPICompat.isVanishedByEntity(player))
        {
            String response = afkPlayer.afkplus$getName() + " <red>is vanished, and shouldn't be afk ...<r>";
            //#if MC >= 12001
            //$$ context.getSource().sendSuccess(() -> TextUtils.formatTextSafe(response), false);
            //#else
            context.getSource().sendSuccess(TextUtils.formatTextSafe(response), false);
            //#endif
            return 1;
        }
        if (afkPlayer.afkplus$isAfk())
        {
            afkPlayer.afkplus$unregisterAfk();
            AfkPlusMod.LOGGER.info("{} cleared player {} from AFK", user, afkPlayer.afkplus$getName());
        }
        else
        {
            //#if MC >= 12001
            //$$ context.getSource().sendSuccess(() -> Component.literal(afkPlayer.afkplus$getName() + " is not marked as AFK."), false);
            //#else
            context.getSource().sendSuccess(Component.literal(afkPlayer.afkplus$getName() + " is not marked as AFK."), false);
            //#endif
        }
        return 1;
    }

    private int infoAfkPlayer(CommandSourceStack src, ServerPlayer player, CommandContext<CommandSourceStack> context)
    {
        IAfkPlayer afkPlayer = (IAfkPlayer) player;
        String user = src.getTextName();
        if (afkPlayer.afkplus$isAfk())
        {
            String afkStatus = AfkPlayerInfo.getString(afkPlayer);
            Component afkReason = AfkPlayerInfo.getReason(afkPlayer, src);
            //#if MC >= 12001
            //$$ context.getSource().sendSuccess(() -> TextUtils.formatTextSafe(afkStatus), false);
            //$$ context.getSource().sendSuccess(() -> afkReason, false);
            //#else
            context.getSource().sendSuccess(TextUtils.formatTextSafe(afkStatus), false);
            context.getSource().sendSuccess(afkReason, false);
            //#endif
            AfkPlusMod.LOGGER.info(user + " displayed " + afkPlayer.afkplus$getName() + "'s AFK info.");
        }
        else
        {
            //#if MC >= 12001
            //$$ context.getSource().sendSuccess(() -> Component.literal(afkPlayer.afkplus$getName() + " is not marked as AFK."), false);
            //#else
            context.getSource().sendSuccess(Component.literal(afkPlayer.afkplus$getName() + " is not marked as AFK."), false);
            //#endif
        }
        return 1;
    }

    private int disableDamagePlayer(CommandSourceStack src, ServerPlayer player, CommandContext<CommandSourceStack> context)
    {
        IAfkPlayer afkPlayer = (IAfkPlayer) player;
        //String user = src.getTextName();

        if (VanishAPICompat.hasVanish() && VanishAPICompat.isVanishedByEntity(player))
        {
            String response = afkPlayer.afkplus$getName() + "<red>is vanished, and shouldn't be changing their disable Damage status.<r>";
            //#if MC >= 12001
            //$$ context.getSource().sendSuccess(() -> TextUtils.formatTextSafe(response), false);
            //#else
            context.getSource().sendSuccess(TextUtils.formatTextSafe(response), false);
            //#endif
            return 1;
        }
        if (afkPlayer.afkplus$isLockDamageDisabled())
        {
            afkPlayer.afkplus$unlockDamageDisabled();
            //#if MC >= 12001
            //$$ context.getSource().sendSuccess(() -> Component.literal("Allowing Damage Disable feature for player " + afkPlayer.afkplus$getName()), true);
            //#else
            context.getSource().sendSuccess(Component.literal("Allowing Damage Disable feature for player " + afkPlayer.afkplus$getName()), true);
            //#endif
            //AfkPlusLogger.info(user + " Allowing Damage Disable feature for player " + afkPlayer.afkplus$getName());
        }
        else
        {
            //#if MC >= 12001
            //$$ context.getSource().sendSuccess(() -> Component.literal("Damage Disable is already allowed for player " + afkPlayer.afkplus$getName()), false);
            //#else
            context.getSource().sendSuccess(Component.literal("Damage Disable is already allowed for player " + afkPlayer.afkplus$getName()), false);
            //#endif
        }
        return 1;
    }

    private int enableDamagePlayer(CommandSourceStack src, ServerPlayer player, CommandContext<CommandSourceStack> context)
    {
        IAfkPlayer afkPlayer = (IAfkPlayer) player;
        //String user = src.getTextName();
        if (VanishAPICompat.hasVanish() && VanishAPICompat.isVanishedByEntity(player))
        {
            String response = afkPlayer.afkplus$getName() + "<red>is vanished, and shouldn't be changing their disable Damage status.<r>";
            //#if MC >= 12001
            //$$ context.getSource().sendSuccess(() -> TextUtils.formatTextSafe(response), false);
            //#else
            context.getSource().sendSuccess(TextUtils.formatTextSafe(response), false);
            //#endif
            return 1;
        }
        if (!afkPlayer.afkplus$isLockDamageDisabled())
        {
            afkPlayer.afkplus$lockDamageDisabled();
            //#if MC >= 12001
            //$$ context.getSource().sendSuccess(() -> Component.literal("Force-Enabling Damage for player " + afkPlayer.afkplus$getName()), true);
            //#else
            context.getSource().sendSuccess(Component.literal("Force-Enabling Damage for player " + afkPlayer.afkplus$getName()), true);
            //#endif
            //AfkPlusLogger.info(user + " Force-Enabling Damage for player " + afkPlayer.afkplus$getName());
        }
        else
        {
            //#if MC >= 12001
            //$$ context.getSource().sendSuccess(() -> Component.literal("Damage Disable is already disallowed for player " + afkPlayer.afkplus$getName()), false);
            //#else
            context.getSource().sendSuccess(Component.literal("Damage Disable is already disallowed for player " + afkPlayer.afkplus$getName()), false);
            //#endif
        }
        return 1;
    }

    private int updatePlayer(CommandSourceStack src, ServerPlayer player, CommandContext<CommandSourceStack> context)
    {
        String user = src.getTextName();
        IAfkPlayer afkPlayer = (IAfkPlayer) player;
        if (VanishAPICompat.hasVanish() && VanishAPICompat.isVanishedByEntity(player))
        {
            String response = afkPlayer.afkplus$getName() + "<red>is vanished, and shouldn't be updating their player list status.<r>";
            //#if MC >= 12001
            //$$ context.getSource().sendSuccess(() -> TextUtils.formatTextSafe(response), false);
            //#else
            context.getSource().sendSuccess(TextUtils.formatTextSafe(response), false);
            //#endif
            return 1;
        }
        afkPlayer.afkplus$updatePlayerList();
        //#if MC >= 12001
        //$$ context.getSource().sendSuccess(() -> Component.literal("Updating player list entry for " + afkPlayer.afkplus$getName()), false);
        //#else
        context.getSource().sendSuccess(Component.literal("Updating player list entry for " + afkPlayer.afkplus$getName()), false);
        //#endif
        AfkPlusMod.LOGGER.info("{} updated player list entry for {}", user, afkPlayer.afkplus$getName());
        return 1;
    }
}
