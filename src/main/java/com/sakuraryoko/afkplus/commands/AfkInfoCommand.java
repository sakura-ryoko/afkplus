package com.sakuraryoko.afkplus.commands;

import me.lucko.fabric.api.permissions.v0.Permissions;

import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;

import com.sakuraryoko.afkplus.compat.TextUtils;
import com.sakuraryoko.afkplus.data.IAfkPlayer;
import com.sakuraryoko.afkplus.util.AfkPlayerInfo;
import com.sakuraryoko.afkplus.util.AfkPlusLogger;

import static com.sakuraryoko.afkplus.config.ConfigManager.CONFIG;
import static net.minecraft.commands.Commands.argument;
import static net.minecraft.commands.Commands.literal;

public class AfkInfoCommand
{
    public static void register()
    {
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> dispatcher.register(
                literal("afkinfo")
                        .requires(Permissions.require("afkplus.afkinfo", CONFIG.afkPlusOptions.afkInfoCommandPermissions))
                        .then(argument("player", EntityArgument.player())
                                      .executes(ctx -> infoAfkPlayer(ctx.getSource(), EntityArgument.getPlayer(ctx, "player"), ctx))
                        )
        ));
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
}
