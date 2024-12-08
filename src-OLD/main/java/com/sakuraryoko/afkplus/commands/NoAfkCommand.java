package com.sakuraryoko.afkplus.commands;

import me.lucko.fabric.api.permissions.v0.Permissions;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.Component;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;

import com.sakuraryoko.afkplus.config.ConfigManager;
import com.sakuraryoko.afkplus.data.IAfkPlayer;

import static net.minecraft.commands.Commands.literal;

public class NoAfkCommand
{
    public static void register()
    {
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> dispatcher.register(
                literal("noafk")
                        .requires(Permissions.require("afkplus.noafk", ConfigManager.CONFIG.afkPlusOptions.noAfkCommandPermissions))
                        .executes(ctx -> setNoAfk(ctx.getSource(), ctx))
        ));
    }

    private static int setNoAfk(CommandSourceStack src, CommandContext<CommandSourceStack> context)
            throws CommandSyntaxException
    {
        IAfkPlayer player = (IAfkPlayer) src.getPlayer();
        //String user = src.getTextName();
        if (player.afkplus$isNoAfkEnabled())
        {
            player.afkplus$unsetNoAfkEnabled();
            context.getSource().sendSuccess(() -> Component.literal("No AFK Mode Disabled. (Timeouts enabled)"), true);
            //AfkPlusLogger.info(user+ " has disabled No AFK mode. (Timeouts enabled)");
        }
        else
        {
            player.afkplus$setNoAfkEnabled();
            context.getSource().sendSuccess(() -> Component.literal("No AFK Mode Enabled. (Timeouts disabled)"), true);
            //AfkPlusLogger.info(user+ " has enabled No AFK mode. (Timeouts disabled)");
        }
        return 1;
    }
}
