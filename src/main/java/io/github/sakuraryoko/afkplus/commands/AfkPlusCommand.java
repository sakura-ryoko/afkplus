package io.github.sakuraryoko.afkplus.commands;

import static io.github.sakuraryoko.afkplus.config.ConfigManager.*;
import static net.minecraft.server.command.CommandManager.*;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;

import eu.pb4.placeholders.TextParser;
import io.github.sakuraryoko.afkplus.config.ConfigManager;
import io.github.sakuraryoko.afkplus.data.AfkPlayerData;
import io.github.sakuraryoko.afkplus.util.AfkPlayerInfo;
import io.github.sakuraryoko.afkplus.util.AfkPlusInfo;
import io.github.sakuraryoko.afkplus.util.AfkPlusLogger;
import me.lucko.fabric.api.permissions.v0.Permissions;
import net.fabricmc.fabric.api.command.v1.CommandRegistrationCallback;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

public class AfkPlusCommand {
        public static void register() {
                CommandRegistrationCallback.EVENT.register((dispatcher, environment) -> {
                        dispatcher.register(
                                        literal("afkplus")
                                                        // .requires(Permissions.require("afkplus.afkplus",
                                                        // CONFIG.afkPlusOptions.afkPlusCommandPermissions))
                                                        .executes(ctx -> afkAbout(ctx.getSource(), ctx))
                                                        .then(literal("reload")
                                                                        .requires(Permissions.require(
                                                                                        "afkplus.afkplus.reload",
                                                                                        CONFIG.afkPlusOptions.afkPlusCommandPermissions))
                                                                        .executes(ctx -> afkReload(ctx.getSource(),
                                                                                        ctx)))
                                                        .then(literal("set")
                                                                        .requires(Permissions.require(
                                                                                        "afkplus.afkplus.set",
                                                                                        CONFIG.afkPlusOptions.afkPlusCommandPermissions))
                                                                        .then(CommandManager.argument("player",
                                                                                        EntityArgumentType.player())
                                                                                        .executes((ctx) -> setAfk(
                                                                                                        ctx.getSource(),
                                                                                                        EntityArgumentType
                                                                                                                        .getPlayer(ctx, "player"),
                                                                                                        "", ctx))
                                                                                        .then(CommandManager.argument(
                                                                                                        "reason",
                                                                                                        StringArgumentType
                                                                                                                        .greedyString())
                                                                                                        .requires(Permissions
                                                                                                                        .require(
                                                                                                                                        "afkplus.afkplus.set",
                                                                                                                                        CONFIG.afkPlusOptions.afkPlusCommandPermissions))
                                                                                                        .executes((ctx) -> setAfk(
                                                                                                                        ctx.getSource(),
                                                                                                                        EntityArgumentType
                                                                                                                                        .getPlayer(ctx, "player"),
                                                                                                                        StringArgumentType
                                                                                                                                        .getString(ctx, "reason"),
                                                                                                                        ctx)))))
                                                        .then(literal("clear")
                                                                        .requires(Permissions.require(
                                                                                        "afkplus.afkplus.clear",
                                                                                        CONFIG.afkPlusOptions.afkPlusCommandPermissions))
                                                                        .then(CommandManager.argument("player",
                                                                                        EntityArgumentType.player())
                                                                                        .executes(ctx -> clearAfk(
                                                                                                        ctx.getSource(),
                                                                                                        EntityArgumentType
                                                                                                                        .getPlayer(ctx,
                                                                                                                                        "player"),
                                                                                                        ctx))))
                                                        .then(literal("info")
                                                                        .requires(Permissions.require(
                                                                                        "afkplus.afkplus.info",
                                                                                        CONFIG.afkPlusOptions.afkPlusCommandPermissions))
                                                                        .then(CommandManager.argument("player",
                                                                                        EntityArgumentType.player())
                                                                                        .executes(ctx -> infoAfkPlayer(
                                                                                                        ctx.getSource(),
                                                                                                        EntityArgumentType
                                                                                                                        .getPlayer(ctx,
                                                                                                                                        "player"),
                                                                                                        ctx))))
                                                        .then(literal("update")
                                                                        .requires(Permissions.require(
                                                                                        "afkplus.afkplus.update",
                                                                                        CONFIG.afkPlusOptions.afkPlusCommandPermissions))
                                                                        .then(CommandManager.argument("player",
                                                                                        EntityArgumentType.player())
                                                                                        .executes(ctx -> updatePlayer(
                                                                                                        ctx.getSource(),
                                                                                                        EntityArgumentType
                                                                                                                        .getPlayer(ctx,
                                                                                                                                        "player"),
                                                                                                        ctx)))));
                });
        }

        private static int afkAbout(ServerCommandSource src, CommandContext<ServerCommandSource> context) {
                Text ModInfo = AfkPlusInfo.getModInfoText();
                String user = src.getName();
                context.getSource().sendFeedback(ModInfo, false);
                AfkPlusLogger.debug(user + " has executed /afkplus .");
                return 1;
        }

        private static int afkReload(ServerCommandSource src, CommandContext<ServerCommandSource> context) {
                String user = src.getName();
                ConfigManager.reloadConfig();
                context.getSource().sendFeedback(Text.of("Reloaded config!"), false);
                AfkPlusLogger.info(user + " has reloaded the configuration.");
                return 1;
        }

        private static int setAfk(ServerCommandSource src, ServerPlayerEntity player, String reason,
                        CommandContext<ServerCommandSource> context) {
                AfkPlayerData afkPlayer = (AfkPlayerData) player;
                String user = src.getName();
                Text target = player.getName();
                if (afkPlayer.isAfk()) {
                        context.getSource().sendFeedback(Text.of(target.getString() + " is already marked as AFK."),
                                        false);
                } else {
                        if (reason == null && CONFIG.messageOptions.defaultReason == null) {
                                afkPlayer.registerAfk("via /afkplus set");
                                AfkPlusLogger.info(user + " set player " + target.getString() + " as AFK");
                        } else if (reason == null || reason == "") {
                                afkPlayer.registerAfk(CONFIG.messageOptions.defaultReason);
                                AfkPlusLogger.info(user + " set player " + target.getString()
                                                + " as AFK for reason: "
                                                + CONFIG.messageOptions.defaultReason);
                        } else {
                                afkPlayer.registerAfk(reason);
                                AfkPlusLogger.info(user + " set player " + target.getString()
                                                + " as AFK for reason: "
                                                + reason);
                        }
                }
                return 1;
        }

        private static int clearAfk(ServerCommandSource src, ServerPlayerEntity player,
                        CommandContext<ServerCommandSource> context) {
                AfkPlayerData afkPlayer = (AfkPlayerData) player;
                String user = src.getName();
                Text target = player.getName();
                if (afkPlayer.isAfk()) {
                        afkPlayer.unregisterAfk();
                        AfkPlusLogger.info(user + " cleared player " + target.getString() + " from AFK");
                } else {
                        context.getSource().sendFeedback(Text.of(target.getString() + " is not marked as AFK."),
                                        false);
                }
                return 1;
        }

        private static int infoAfkPlayer(ServerCommandSource src, ServerPlayerEntity player,
                        CommandContext<ServerCommandSource> context) {
                AfkPlayerData afkPlayer = (AfkPlayerData) player;
                String user = src.getName();
                Text target = player.getName();
                if (afkPlayer.isAfk()) {
                        String afkStatus = AfkPlayerInfo.getString(afkPlayer, target, src);
                        Text afkReason = AfkPlayerInfo.getReason(afkPlayer, target, src);
                        context.getSource().sendFeedback(TextParser.parse(afkStatus), false);
                        context.getSource().sendFeedback(afkReason, false);
                        AfkPlusLogger.info(user + " displayed " + target.getString() + "'s AFK info.");
                } else {
                        context.getSource().sendFeedback(Text.of(target.getString() + " is not marked as AFK."), false);
                }
                return 1;
        }

        private static int updatePlayer(ServerCommandSource src, ServerPlayerEntity player,
                        CommandContext<ServerCommandSource> context) {
                String user = src.getName();
                Text target = player.getName();
                AfkPlayerData afkPlayer = (AfkPlayerData) player;
                afkPlayer.updatePlayerList();
                context.getSource().sendFeedback(Text.of("Updating player list entry for " + target.getString() + ""),
                                false);
                AfkPlusLogger.info(user + " updated player list entry for " + target.getString() + "");
                return 1;
        }
}
