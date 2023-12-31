package io.github.sakuraryoko.afkplus.commands;

import static io.github.sakuraryoko.afkplus.config.ConfigManager.*;
import static net.minecraft.server.command.CommandManager.*;

import com.mojang.brigadier.context.CommandContext;

import eu.pb4.placeholders.api.TextParserUtils;
import io.github.sakuraryoko.afkplus.data.IAfkPlayer;
import io.github.sakuraryoko.afkplus.util.AfkPlayerInfo;
import io.github.sakuraryoko.afkplus.util.AfkPlusLogger;
import me.lucko.fabric.api.permissions.v0.Permissions;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

public class AfkInfoCommand {
    public static void register() {
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> dispatcher.register(
                literal("afkinfo")
                        .requires(Permissions.require("afkplus.afkinfo",
                                CONFIG.afkPlusOptions.afkInfoCommandPermissions))
                        .then(argument("player", EntityArgumentType.player())
                                .executes(ctx -> infoAfkPlayer(ctx.getSource(),
                                        EntityArgumentType.getPlayer(
                                                ctx,
                                                "player"),
                                        ctx)))));
    }

    private static int infoAfkPlayer(ServerCommandSource src, ServerPlayerEntity player,
                                     CommandContext<ServerCommandSource> context) {
        IAfkPlayer afkPlayer = (IAfkPlayer) player;
        String user = src.getName();
        Text target = player.getName();
        if (afkPlayer.afkplus$isAfk()) {
            String afkStatus = AfkPlayerInfo.getString(afkPlayer, target);
            Text afkReason = AfkPlayerInfo.getReason(afkPlayer, src);
            context.getSource().sendFeedback(() -> TextParserUtils.formatTextSafe(afkStatus), false);
            context.getSource().sendFeedback(() -> afkReason, false);
            AfkPlusLogger.info(user + " displayed " + target.getLiteralString() + "'s AFK info.");
        } else {
            context.getSource().sendFeedback(
                    () -> Text.of(target.getLiteralString() + " is not marked as AFK."), false);
        }
        return 1;
    }
}
