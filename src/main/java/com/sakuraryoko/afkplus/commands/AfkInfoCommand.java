package com.sakuraryoko.afkplus.commands;

import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;

import com.mojang.brigadier.context.CommandContext;
import me.lucko.fabric.api.permissions.v0.Permissions;

import com.sakuraryoko.afkplus.compat.TextUtils;
import com.sakuraryoko.afkplus.data.IAfkPlayer;
import com.sakuraryoko.afkplus.util.AfkPlayerInfo;
import com.sakuraryoko.afkplus.util.AfkPlusLogger;

import static com.sakuraryoko.afkplus.config.ConfigManager.CONFIG;
import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;

public class AfkInfoCommand
{
    public static void register()
    {
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> dispatcher.register(
                literal("afkinfo")
                        .requires(Permissions.require("afkplus.afkinfo", CONFIG.afkPlusOptions.afkInfoCommandPermissions))
                        .then(argument("player", EntityArgumentType.player())
                                .executes(ctx -> infoAfkPlayer(ctx.getSource(), EntityArgumentType.getPlayer(ctx, "player"), ctx))
                        )
        ));
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
}
