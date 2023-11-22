package io.github.sakuraryoko.afkplus.commands.server;

import static io.github.sakuraryoko.afkplus.config.ConfigManager.*;
import static net.minecraft.server.command.CommandManager.*;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;

import eu.pb4.placeholders.api.TextParserUtils;
import io.github.sakuraryoko.afkplus.config.ConfigManager;
import io.github.sakuraryoko.afkplus.data.AfkPlayerData;
import io.github.sakuraryoko.afkplus.util.AfkPlayerInfo;
import io.github.sakuraryoko.afkplus.util.AfkPlusInfo;
import io.github.sakuraryoko.afkplus.util.AfkPlusLogger;
import me.lucko.fabric.api.permissions.v0.Permissions;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

public class AfkPlusCommand {
        public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
                dispatcher.register(
                                literal("afkplus")
                                                .requires(Permissions.require("afkplus.afkplus",
                                                                CONFIG.afkPlusOptions.afkPlusCommandPermissions))
                                                .executes(ctx -> afkAbout(ctx.getSource(), ctx))
                                                .then(literal("reload")
                                                                .requires(Permissions.require(
                                                                                "afkplus.afkplus.reload",
                                                                                CONFIG.afkPlusOptions.afkPlusCommandPermissions))
                                                                .executes(ctx -> afkReload(ctx.getSource(), ctx)))
                                                .then(literal("set")
                                                                .requires(Permissions.require(
                                                                                "afkplus.afkplus.set",
                                                                                CONFIG.afkPlusOptions.afkPlusCommandPermissions))
                                                                .then(argument("player", EntityArgumentType.player())
                                                                                .executes((ctx) -> setAfk(
                                                                                                ctx.getSource(),
                                                                                                EntityArgumentType
                                                                                                                .getPlayer(ctx, "player"),
                                                                                                ""))
                                                                                .then(argument("reason",
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
                                                                                                                                .getString(ctx, "reason"))))))
                                                .then(literal("clear")
                                                                .requires(Permissions.require(
                                                                                "afkplus.afkplus.clear",
                                                                                CONFIG.afkPlusOptions.afkPlusCommandPermissions))
                                                                .then(argument("player", EntityArgumentType.player())
                                                                                .executes(ctx -> clearAfk(
                                                                                                ctx.getSource(),
                                                                                                EntityArgumentType
                                                                                                                .getPlayer(ctx,
                                                                                                                                "player")))))
                                                .then(literal("info")
                                                                .requires(Permissions.require(
                                                                                "afkplus.afkplus.info",
                                                                                CONFIG.afkPlusOptions.afkPlusCommandPermissions))
                                                                .then(argument("player", EntityArgumentType.player())
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
                                                                .then(argument("player", EntityArgumentType.player())
                                                                                .executes(ctx -> updatePlayer(
                                                                                                ctx.getSource(),
                                                                                                EntityArgumentType
                                                                                                                .getPlayer(ctx,
                                                                                                                                "player"),
                                                                                                ctx)))));
        }

        private static int afkAbout(ServerCommandSource src, CommandContext<ServerCommandSource> context) {
                Text ModInfo = AfkPlusInfo.getModInfoText();
                context.getSource().sendFeedback(() -> ModInfo, false);
                return 1;
        }

        private static int afkReload(ServerCommandSource src, CommandContext<ServerCommandSource> context) {
                ConfigManager.reloadConfig();
                context.getSource().sendFeedback(() -> Text.literal("Reloaded config!"), false);
                return 1;
        }

        private static int setAfk(ServerCommandSource src, ServerPlayerEntity player, String reason) {
                AfkPlayerData afkPlayer = (AfkPlayerData) player;
                String user = src.getName();
                String target = player.getNameForScoreboard();
                if (reason == null && CONFIG.messageOptions.defaultReason == null) {
                        afkPlayer.registerAfk("via /afkplus set");
                        AfkPlusLogger.info(user + " set player " + target + " as AFK");
                } else if (reason == null || reason == "") {
                        afkPlayer.registerAfk(CONFIG.messageOptions.defaultReason);
                        AfkPlusLogger.info(user + " set player " + target + " as AFK for reason: "
                                        + CONFIG.messageOptions.defaultReason);
                } else {
                        afkPlayer.registerAfk(reason);
                        AfkPlusLogger.info(user + " set player " + target + " as AFK for reason: " + reason);
                }
                return 1;
        }

        private static int clearAfk(ServerCommandSource src, ServerPlayerEntity player) {
                AfkPlayerData afkPlayer = (AfkPlayerData) player;
                String user = src.getName();
                String target = player.getNameForScoreboard();
                afkPlayer.unregisterAfk();
                AfkPlusLogger.info(user + " cleared player " + target + " from AFK");
                return 1;
        }

        private static int infoAfkPlayer(ServerCommandSource src, ServerPlayerEntity player,
                        CommandContext<ServerCommandSource> context) {
                AfkPlayerData afkPlayer = (AfkPlayerData) player;
                String user = src.getName();
                String target = player.getNameForScoreboard();
                String AfkStatus = AfkPlayerInfo.getString(afkPlayer, user, target);
                context.getSource().sendFeedback(() -> TextParserUtils.formatTextSafe(AfkStatus), false);
                return 1;
        }

        private static int updatePlayer(ServerCommandSource src, ServerPlayerEntity player,
                        CommandContext<ServerCommandSource> context) {
                String user = src.getName();
                String target = player.getNameForScoreboard();
                AfkPlayerData afkPlayer = (AfkPlayerData) player;
                afkPlayer.updatePlayerList();
                context.getSource().sendFeedback(() -> Text.literal("Updating player list entry for " + target + ""),
                                false);
                AfkPlusLogger.info(user + " updated player list entry for " + target + "");
                return 1;
        }
}
