package com.sakuraryoko.afkplus.commands;

import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import me.lucko.fabric.api.permissions.v0.Permissions;

import com.sakuraryoko.afkplus.compat.TextUtils;
import com.sakuraryoko.afkplus.config.ConfigManager;
import com.sakuraryoko.afkplus.data.IAfkPlayer;
import com.sakuraryoko.afkplus.util.AfkPlayerInfo;
import com.sakuraryoko.afkplus.util.AfkPlusInfo;
import com.sakuraryoko.afkplus.util.AfkPlusLogger;
import com.sakuraryoko.afkplus.util.FormattingExample;

import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;

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
                                        EntityArgumentType.player())
                                        .executes((ctx) -> setAfk(ctx.getSource(), EntityArgumentType.getPlayer(ctx, "player"), "", ctx))
                                        .then(argument("reason", StringArgumentType.greedyString())
                                                .requires(Permissions.require("afkplus.afkplus.set", ConfigManager.CONFIG.afkPlusOptions.afkPlusCommandPermissions))
                                                .executes((ctx) -> setAfk(ctx.getSource(), EntityArgumentType.getPlayer(ctx, "player"), StringArgumentType.getString(ctx, "reason"), ctx))
                                        )
                                )
                        )
                        .then(literal("clear")
                                .requires(Permissions.require("afkplus.afkplus.clear", ConfigManager.CONFIG.afkPlusOptions.afkPlusCommandPermissions))
                                .then(argument("player", EntityArgumentType.player())
                                        .executes(ctx -> clearAfk(ctx.getSource(), EntityArgumentType.getPlayer(ctx, "player"), ctx))
                                )
                        )
                        .then(literal("info")
                                .requires(Permissions.require("afkplus.afkplus.info", ConfigManager.CONFIG.afkPlusOptions.afkPlusCommandPermissions))
                                .then(argument("player", EntityArgumentType.player())
                                        .executes(ctx -> infoAfkPlayer(ctx.getSource(), EntityArgumentType.getPlayer(ctx, "player"), ctx))
                                )
                        )
                        .then(literal("damage")
                                .requires(Permissions.require("afkplus.afkplus.damage", ConfigManager.CONFIG.afkPlusOptions.afkPlusCommandPermissions))
                                .then(literal("disable")
                                        .requires(Permissions.require("afkplus.afkplus.damage.disable", ConfigManager.CONFIG.afkPlusOptions.afkPlusCommandPermissions))
                                        .then(argument("player", EntityArgumentType.player())
                                                .executes(ctx -> disableDamagePlayer(ctx.getSource(), EntityArgumentType.getPlayer(ctx, "player"), ctx))
                                        )
                                )
                                .then(literal("enable")
                                        .requires(Permissions.require("afkplus.afkplus.damage.enable", ConfigManager.CONFIG.afkPlusOptions.afkPlusCommandPermissions))
                                        .then(argument("player", EntityArgumentType.player())
                                                .executes(ctx -> enableDamagePlayer(ctx.getSource(), EntityArgumentType.getPlayer(ctx, "player"), ctx))
                                        )
                                )
                        )
                        .then(literal("update")
                                .requires(Permissions.require("afkplus.afkplus.update", ConfigManager.CONFIG.afkPlusOptions.afkPlusCommandPermissions))
                                .then(argument("player", EntityArgumentType.player())
                                        .executes(ctx -> updatePlayer(ctx.getSource(), EntityArgumentType.getPlayer(ctx, "player"), ctx)
                                        )
                                )
                        )

        ));
    }

    private static int afkAbout(ServerCommandSource src, CommandContext<ServerCommandSource> context)
    {
        Text ModInfo = AfkPlusInfo.getModInfoText();
        String user = src.getName();
        context.getSource().sendFeedback(() -> ModInfo, false);
        AfkPlusLogger.debug(user + " has executed /afkplus .");
        return 1;
    }

    private static int afkExample(ServerCommandSource src, CommandContext<ServerCommandSource> context)
    {
        String user = src.getName();
        context.getSource().sendFeedback(() -> FormattingExample.runBuiltInTest(), false);
        context.getSource().sendFeedback(() -> FormattingExample.runAliasTest(), false);
        context.getSource().sendFeedback(() -> FormattingExample.runColorsTest(), false);
        AfkPlusLogger.debug(user + " has executed /afkplus example .");
        return 1;
    }

    private static int afkReload(ServerCommandSource src, CommandContext<ServerCommandSource> context)
    {
        String user = src.getName();
        ConfigManager.reloadConfig();
        context.getSource().sendFeedback(() -> Text.of("Reloaded config!"), false);
        AfkPlusLogger.info(user + " has reloaded the configuration.");
        return 1;
    }

    private static int setAfk(ServerCommandSource src, ServerPlayerEntity player, String reason, CommandContext<ServerCommandSource> context)
    {
        IAfkPlayer afkPlayer = (IAfkPlayer) player;
        String user = src.getName();
        if (afkPlayer.afkplus$isAfk())
        {
            context.getSource().sendFeedback(() -> Text.of(afkPlayer.afkplus$getName() + " is already marked as AFK."), false);
        }
        else
        {
            if (afkPlayer.afkplus$isNoAfkEnabled())
            {
                afkPlayer.afkplus$unsetNoAfkEnabled();
                //AfkPlusLogger.info(user + " set player " + afkPlayer.afkplus$getName() + "'s NoAfk status OFF.");
                context.getSource().sendFeedback(() -> Text.of("Toggled player " + afkPlayer.afkplus$getName() + "'s NoAFK status OFF."), true);
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

    private static int clearAfk(ServerCommandSource src, ServerPlayerEntity player, CommandContext<ServerCommandSource> context)
    {
        IAfkPlayer afkPlayer = (IAfkPlayer) player;
        String user = src.getName();
            if (afkPlayer.afkplus$isAfk())
            {
                    afkPlayer.afkplus$unregisterAfk();
                    AfkPlusLogger.info(user + " cleared player " + afkPlayer.afkplus$getName() + " from AFK");
            }
            else
            {
                    context.getSource().sendFeedback(() -> Text.of(afkPlayer.afkplus$getName() + " is not marked as AFK."), false);
            }
        return 1;
    }

    private static int infoAfkPlayer(ServerCommandSource src, ServerPlayerEntity player, CommandContext<ServerCommandSource> context)
    {
        IAfkPlayer afkPlayer = (IAfkPlayer) player;
        String user = src.getName();
            if (afkPlayer.afkplus$isAfk())
            {
                    String afkStatus = AfkPlayerInfo.getString(afkPlayer);
                    Text afkReason = AfkPlayerInfo.getReason(afkPlayer, src);
                    context.getSource().sendFeedback(() -> TextUtils.formatTextSafe(afkStatus), false);
                    context.getSource().sendFeedback(() -> afkReason, false);
                    AfkPlusLogger.info(user + " displayed " + afkPlayer.afkplus$getName() + "'s AFK info.");
            }
            else
            {
                    context.getSource().sendFeedback(() -> Text.of(afkPlayer.afkplus$getName() + " is not marked as AFK."), false);
            }
        return 1;
    }

    private static int disableDamagePlayer(ServerCommandSource src, ServerPlayerEntity player, CommandContext<ServerCommandSource> context)
    {
        IAfkPlayer afkPlayer = (IAfkPlayer) player;
        String user = src.getName();
            if (afkPlayer.afkplus$isLockDamageDisabled())
            {
                    afkPlayer.afkplus$unlockDamageDisabled();
                    context.getSource().sendFeedback(() -> Text.of("Allowing Damage Disable feature for player " + afkPlayer.afkplus$getName()), true);
                    //AfkPlusLogger.info(user + " Allowing Damage Disable feature for player " + afkPlayer.afkplus$getName());
            }
            else
            {
                    context.getSource().sendFeedback(() -> Text.of("Damage Disable is already allowed for player " + afkPlayer.afkplus$getName()), false);
            }
        return 1;
    }

    private static int enableDamagePlayer(ServerCommandSource src, ServerPlayerEntity player, CommandContext<ServerCommandSource> context)
    {
        IAfkPlayer afkPlayer = (IAfkPlayer) player;
        String user = src.getName();
            if (!afkPlayer.afkplus$isLockDamageDisabled())
            {
                    afkPlayer.afkplus$lockDamageDisabled();
                    context.getSource().sendFeedback(() -> Text.of("Force-Enabling Damage for player " + afkPlayer.afkplus$getName()), true);
                    //AfkPlusLogger.info(user + " Force-Enabling Damage for player " + afkPlayer.afkplus$getName());
            }
            else
            {
                    context.getSource().sendFeedback(() -> Text.of("Damage Disable is already disallowed for player " + afkPlayer.afkplus$getName()), false);
            }
        return 1;
    }

    private static int updatePlayer(ServerCommandSource src, ServerPlayerEntity player, CommandContext<ServerCommandSource> context)
    {
        String user = src.getName();
        IAfkPlayer afkPlayer = (IAfkPlayer) player;
        afkPlayer.afkplus$updatePlayerList();
        context.getSource().sendFeedback(() -> Text.of("Updating player list entry for " + afkPlayer.afkplus$getName()), false);
        AfkPlusLogger.info(user + " updated player list entry for " + afkPlayer.afkplus$getName());
        return 1;
    }
}
