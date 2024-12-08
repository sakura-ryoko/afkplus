package com.sakuraryoko.afkplus.commands;

import me.lucko.fabric.api.permissions.v0.Permissions;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;

import com.sakuraryoko.afkplus.compat.TextUtils;
import com.sakuraryoko.afkplus.config.ConfigManager;
import com.sakuraryoko.afkplus.data.IAfkPlayer;
import com.sakuraryoko.afkplus.util.AfkPlayerInfo;
import com.sakuraryoko.afkplus.util.AfkPlusInfo;
import com.sakuraryoko.afkplus.util.AfkPlusLogger;
import com.sakuraryoko.afkplus.util.FormattingExample;

import static net.minecraft.commands.Commands.argument;
import static net.minecraft.commands.Commands.literal;

public class AfkPlusCommand
{
    public static void register()
    {
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> dispatcher.register(
                literal("afkplus")
                        .requires(Permissions.require("afkplus.afkplus", ConfigManager.CONFIG.afkPlusOptions.afkPlusCommandPermissions))
                        .executes(ctx -> afkAbout(ctx.getSource(), ctx))
                        .then(literal("ex")
                                      .requires(Permissions.require("afkplus.afkplus.ex", 4))
                                      .executes(ctx -> afkExample(ctx.getSource(), ctx))
                        )
                        .then(literal("reload")
                                      .requires(Permissions.require("afkplus.afkplus.reload", ConfigManager.CONFIG.afkPlusOptions.afkPlusCommandPermissions))
                                      .executes(ctx -> afkReload(ctx.getSource(), ctx))
                        )
                        .then(literal("set")
                                      .requires(Permissions.require("afkplus.afkplus.set", ConfigManager.CONFIG.afkPlusOptions.afkPlusCommandPermissions))
                                      .then(argument("player",
                                                     EntityArgument.player())
                                                    .executes((ctx) -> setAfk(ctx.getSource(), EntityArgument.getPlayer(ctx, "player"), "", ctx))
                                                    .then(argument("reason", StringArgumentType.greedyString())
                                                                  .requires(Permissions.require("afkplus.afkplus.set", ConfigManager.CONFIG.afkPlusOptions.afkPlusCommandPermissions))
                                                                  .executes((ctx) -> setAfk(ctx.getSource(), EntityArgument.getPlayer(ctx, "player"), StringArgumentType.getString(ctx, "reason"), ctx))
                                                    )
                                      )
                        )
                        .then(literal("clear")
                                      .requires(Permissions.require("afkplus.afkplus.clear", ConfigManager.CONFIG.afkPlusOptions.afkPlusCommandPermissions))
                                      .then(argument("player", EntityArgument.player())
                                                    .executes(ctx -> clearAfk(ctx.getSource(), EntityArgument.getPlayer(ctx, "player"), ctx))
                                      )
                        )
                        .then(literal("info")
                                      .requires(Permissions.require("afkplus.afkplus.info", ConfigManager.CONFIG.afkPlusOptions.afkPlusCommandPermissions))
                                      .then(argument("player", EntityArgument.player())
                                                    .executes(ctx -> infoAfkPlayer(ctx.getSource(), EntityArgument.getPlayer(ctx, "player"), ctx))
                                      )
                        )
                        .then(literal("damage")
                                      .requires(Permissions.require("afkplus.afkplus.damage", ConfigManager.CONFIG.afkPlusOptions.afkPlusCommandPermissions))
                                      .then(literal("disable")
                                                    .requires(Permissions.require("afkplus.afkplus.damage.disable", ConfigManager.CONFIG.afkPlusOptions.afkPlusCommandPermissions))
                                                    .then(argument("player", EntityArgument.player())
                                                                  .executes(ctx -> disableDamagePlayer(ctx.getSource(), EntityArgument.getPlayer(ctx, "player"), ctx))
                                                    )
                                      )
                                      .then(literal("enable")
                                                    .requires(Permissions.require("afkplus.afkplus.damage.enable", ConfigManager.CONFIG.afkPlusOptions.afkPlusCommandPermissions))
                                                    .then(argument("player", EntityArgument.player())
                                                                  .executes(ctx -> enableDamagePlayer(ctx.getSource(), EntityArgument.getPlayer(ctx, "player"), ctx))
                                                    )
                                      )
                        )
                        .then(literal("update")
                                      .requires(Permissions.require("afkplus.afkplus.update", ConfigManager.CONFIG.afkPlusOptions.afkPlusCommandPermissions))
                                      .then(argument("player", EntityArgument.player())
                                                    .executes(ctx -> updatePlayer(ctx.getSource(), EntityArgument.getPlayer(ctx, "player"), ctx)
                                                    )
                                      )
                        )

        ));
    }

    private static int afkAbout(CommandSourceStack src, CommandContext<CommandSourceStack> context)
    {
        Component ModInfo = AfkPlusInfo.getModInfoText();
        String user = src.getTextName();
        context.getSource().sendSuccess(() -> ModInfo, false);
        AfkPlusLogger.debug(user + " has executed /afkplus .");
        return 1;
    }

    private static int afkExample(CommandSourceStack src, CommandContext<CommandSourceStack> context)
    {
        String user = src.getTextName();
        context.getSource().sendSuccess(FormattingExample::runBuiltInTest, false);
        context.getSource().sendSuccess(FormattingExample::runAliasTest, false);
        context.getSource().sendSuccess(FormattingExample::runColorsTest, false);
        AfkPlusLogger.debug(user + " has executed /afkplus example .");
        return 1;
    }

    private static int afkReload(CommandSourceStack src, CommandContext<CommandSourceStack> context)
    {
        String user = src.getTextName();
        ConfigManager.reloadConfig();
        context.getSource().sendSuccess(() -> Component.literal("Reloaded config!"), false);
        AfkPlusLogger.info(user + " has reloaded the configuration.");
        return 1;
    }

    private static int setAfk(CommandSourceStack src, ServerPlayer player, String reason, CommandContext<CommandSourceStack> context)
    {
        IAfkPlayer afkPlayer = (IAfkPlayer) player;
        String user = src.getTextName();
        if (afkPlayer.afkplus$isAfk())
        {
            context.getSource().sendSuccess(() -> Component.literal(afkPlayer.afkplus$getName() + " is already marked as AFK."), false);
        }
        else
        {
            if (afkPlayer.afkplus$isNoAfkEnabled())
            {
                afkPlayer.afkplus$unsetNoAfkEnabled();
                //AfkPlusLogger.info(user + " set player " + afkPlayer.afkplus$getName() + "'s NoAfk status OFF.");
                context.getSource().sendSuccess(() -> Component.literal("Toggled player " + afkPlayer.afkplus$getName() + "'s NoAFK status OFF."), true);
            }
            if (reason == null && ConfigManager.CONFIG.messageOptions.defaultReason == null)
            {
                afkPlayer.afkplus$registerAfk("via /afkplus set");
                AfkPlusLogger.info(user + " set player " + afkPlayer.afkplus$getName() + " as AFK");
            }
            else if (reason == null || reason.isEmpty())
            {
                afkPlayer.afkplus$registerAfk(ConfigManager.CONFIG.messageOptions.defaultReason);
                AfkPlusLogger.info(user + " set player " + afkPlayer.afkplus$getName() + " as AFK for reason: " + ConfigManager.CONFIG.messageOptions.defaultReason);
            }
            else
            {
                afkPlayer.afkplus$registerAfk(reason);
                AfkPlusLogger.info(user + " set player " + afkPlayer.afkplus$getName() + " as AFK for reason: " + reason);
            }
        }
        return 1;
    }

    private static int clearAfk(CommandSourceStack src, ServerPlayer player, CommandContext<CommandSourceStack> context)
    {
        IAfkPlayer afkPlayer = (IAfkPlayer) player;
        String user = src.getTextName();
        if (afkPlayer.afkplus$isAfk())
        {
            afkPlayer.afkplus$unregisterAfk();
            AfkPlusLogger.info(user + " cleared player " + afkPlayer.afkplus$getName() + " from AFK");
        }
        else
        {
            context.getSource().sendSuccess(() -> Component.literal(afkPlayer.afkplus$getName() + " is not marked as AFK."), false);
        }
        return 1;
    }

    private static int infoAfkPlayer(CommandSourceStack src, ServerPlayer player, CommandContext<CommandSourceStack> context)
    {
        IAfkPlayer afkPlayer = (IAfkPlayer) player;
        String user = src.getTextName();
        if (afkPlayer.afkplus$isAfk())
        {
            String afkStatus = AfkPlayerInfo.getString(afkPlayer);
            Component afkReason = AfkPlayerInfo.getReason(afkPlayer, src);
            context.getSource().sendSuccess(() -> TextUtils.formatTextSafe(afkStatus), false);
            context.getSource().sendSuccess(() -> afkReason, false);
            AfkPlusLogger.info(user + " displayed " + afkPlayer.afkplus$getName() + "'s AFK info.");
        }
        else
        {
            context.getSource().sendSuccess(() -> Component.literal(afkPlayer.afkplus$getName() + " is not marked as AFK."), false);
        }
        return 1;
    }

    private static int disableDamagePlayer(CommandSourceStack src, ServerPlayer player, CommandContext<CommandSourceStack> context)
    {
        IAfkPlayer afkPlayer = (IAfkPlayer) player;
        String user = src.getTextName();
        if (afkPlayer.afkplus$isLockDamageDisabled())
        {
            afkPlayer.afkplus$unlockDamageDisabled();
            context.getSource().sendSuccess(() -> Component.literal("Allowing Damage Disable feature for player " + afkPlayer.afkplus$getName()), true);
            //AfkPlusLogger.info(user + " Allowing Damage Disable feature for player " + afkPlayer.afkplus$getName());
        }
        else
        {
            context.getSource().sendSuccess(() -> Component.literal("Damage Disable is already allowed for player " + afkPlayer.afkplus$getName()), false);
        }
        return 1;
    }

    private static int enableDamagePlayer(CommandSourceStack src, ServerPlayer player, CommandContext<CommandSourceStack> context)
    {
        IAfkPlayer afkPlayer = (IAfkPlayer) player;
        String user = src.getTextName();
        if (!afkPlayer.afkplus$isLockDamageDisabled())
        {
            afkPlayer.afkplus$lockDamageDisabled();
            context.getSource().sendSuccess(() -> Component.literal("Force-Enabling Damage for player " + afkPlayer.afkplus$getName()), true);
            //AfkPlusLogger.info(user + " Force-Enabling Damage for player " + afkPlayer.afkplus$getName());
        }
        else
        {
            context.getSource().sendSuccess(() -> Component.literal("Damage Disable is already disallowed for player " + afkPlayer.afkplus$getName()), false);
        }
        return 1;
    }

    private static int updatePlayer(CommandSourceStack src, ServerPlayer player, CommandContext<CommandSourceStack> context)
    {
        String user = src.getTextName();
        IAfkPlayer afkPlayer = (IAfkPlayer) player;
        afkPlayer.afkplus$updatePlayerList();
        context.getSource().sendSuccess(() -> Component.literal("Updating player list entry for " + afkPlayer.afkplus$getName()), false);
        AfkPlusLogger.info(user + " updated player list entry for " + afkPlayer.afkplus$getName());
        return 1;
    }
}
