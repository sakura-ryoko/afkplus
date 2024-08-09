package com.sakuraryoko.afkplus.commands;

import net.minecraft.server.command.ServerCommandSource;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import me.lucko.fabric.api.permissions.v0.Permissions;

import com.sakuraryoko.afkplus.config.ConfigManager;
import com.sakuraryoko.afkplus.data.IAfkPlayer;

import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;

public class AfkCommand
{
    public static void register()
    {
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> dispatcher.register(
                literal("afk")
                        .requires(Permissions.require("afkplus.afk", 0))
                        .executes(ctx -> setAfk(ctx.getSource(), ""))
                        .then(argument("reason", StringArgumentType.greedyString())
                                .requires(Permissions.require("afkplus.afk", 0))
                                .executes(ctx -> setAfk(ctx.getSource(), StringArgumentType.getString(ctx, "reason")))
                        )
        ));
    }

    private static int setAfk(ServerCommandSource src, String reason) throws CommandSyntaxException
    {
        IAfkPlayer player = (IAfkPlayer) src.getPlayerOrThrow();
        if (reason == null && ConfigManager.CONFIG.messageOptions.defaultReason == null)
        {
            player.afkplus$registerAfk("via /afk");
        }
        else if (reason == null || reason.isEmpty())
        {
            player.afkplus$registerAfk(ConfigManager.CONFIG.messageOptions.defaultReason);
        }
        else
        {
            player.afkplus$registerAfk(reason);
        }
        return 1;
    }
}
