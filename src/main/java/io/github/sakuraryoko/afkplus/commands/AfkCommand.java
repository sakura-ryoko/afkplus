package io.github.sakuraryoko.afkplus.commands;

import static io.github.sakuraryoko.afkplus.config.ConfigManager.*;
import static net.minecraft.server.command.CommandManager.*;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.exceptions.CommandSyntaxException;

import io.github.sakuraryoko.afkplus.data.AfkPlayerData;
import me.lucko.fabric.api.permissions.v0.Permissions;
import net.minecraft.server.command.ServerCommandSource;

public class AfkCommand {
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(
                literal("afk")
                        .requires(Permissions.require("afkplus.afk", 0))
                        .executes(ctx -> setAfk(ctx.getSource(), ""))
                        .then(argument("reason", StringArgumentType.greedyString())
                                .requires(Permissions.require("afkplus.afk", 0))
                                .executes(
                                        ctx -> setAfk(ctx.getSource(), StringArgumentType.getString(ctx, "reason")))));
    }

    private static int setAfk(ServerCommandSource src, String reason) throws CommandSyntaxException {
        AfkPlayerData player = (AfkPlayerData) src.getPlayerOrThrow();
        if (reason == null && CONFIG.messageOptions.defaultReason == null) {
            player.registerAfk("via /afk");
        } else if (reason == null || reason == "") {
            player.registerAfk(CONFIG.messageOptions.defaultReason);
        } else {
            player.registerAfk(reason);
        }

        return 1;
    }
}
