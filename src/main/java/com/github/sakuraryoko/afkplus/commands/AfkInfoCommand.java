package com.github.sakuraryoko.afkplus.commands;

import static com.github.sakuraryoko.afkplus.config.ConfigManager.*;
import static net.minecraft.server.command.CommandManager.*;

import com.mojang.brigadier.context.CommandContext;

import com.github.sakuraryoko.afkplus.data.IAfkPlayer;
import com.github.sakuraryoko.afkplus.util.AfkPlayerInfo;
import com.github.sakuraryoko.afkplus.util.AfkPlusLogger;
import eu.pb4.placeholders.api.TextParserUtils;
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
                .requires(Permissions.require("afkplus.afkinfo", CONFIG.afkPlusOptions.afkInfoCommandPermissions))
                .then(argument("player", EntityArgumentType.player())
                    .executes(ctx -> infoAfkPlayer(ctx.getSource(), EntityArgumentType.getPlayer(ctx,"player"), ctx))
                )
        ));
    }

    private static int infoAfkPlayer(ServerCommandSource src, ServerPlayerEntity player, CommandContext<ServerCommandSource> context) {
        IAfkPlayer afkPlayer = (IAfkPlayer) player;
        String user = src.getName();
        if (afkPlayer.afkplus$isAfk()) {
            String afkStatus = AfkPlayerInfo.getString(afkPlayer);
            Text afkReason = AfkPlayerInfo.getReason(afkPlayer, src);
            context.getSource().sendFeedback(() -> TextParserUtils.formatTextSafe(afkStatus), false);
            context.getSource().sendFeedback(() -> afkReason, false);
            AfkPlusLogger.info(user + " displayed " + afkPlayer.afkplus$getName() + "'s AFK info.");
        } else
            context.getSource().sendFeedback(() -> Text.of(afkPlayer.afkplus$getName() + " is not marked as AFK."), false);
        return 1;
    }
}
