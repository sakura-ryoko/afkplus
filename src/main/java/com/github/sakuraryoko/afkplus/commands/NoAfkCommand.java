package com.github.sakuraryoko.afkplus.commands;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.github.sakuraryoko.afkplus.config.ConfigManager;
import com.github.sakuraryoko.afkplus.data.IAfkPlayer;
import me.lucko.fabric.api.permissions.v0.Permissions;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;

import static net.minecraft.server.command.CommandManager.literal;

public class NoAfkCommand {
    public static void register() {
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> dispatcher.register(
            literal("noafk")
                .requires(Permissions.require("afkplus.noafk", ConfigManager.CONFIG.afkPlusOptions.noAfkCommandPermissions))
                .executes(ctx -> setNoAfk(ctx.getSource(), ctx))
        ));
    }

    private static int setNoAfk(ServerCommandSource src, CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        IAfkPlayer player = (IAfkPlayer) src.getPlayerOrThrow();
        String user = src.getName();
        if (player.afkplus$isNoAfkEnabled()) {
            player.afkplus$unsetNoAfkEnabled();
            context.getSource().sendFeedback(() -> Text.of("No AFK Mode Disabled. (Timeouts enabled)"), true);
            //AfkPlusLogger.info(user+ " has disabled No AFK mode. (Timeouts enabled)");
        } else {
            player.afkplus$setNoAfkEnabled();
            context.getSource().sendFeedback(() -> Text.of("No AFK Mode Enabled. (Timeouts disabled)"), true);
            //AfkPlusLogger.info(user+ " has enabled No AFK mode. (Timeouts disabled)");
        }
        return 1;
    }
}
