package io.github.sakuraryoko.afkplus.commands;

import static io.github.sakuraryoko.afkplus.config.ConfigManager.*;
import static net.minecraft.server.command.CommandManager.*;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.exceptions.CommandSyntaxException;

import io.github.sakuraryoko.afkplus.data.AfkPlayerData;
import me.lucko.fabric.api.permissions.v0.Permissions;
import net.fabricmc.fabric.api.command.v1.CommandRegistrationCallback;
import net.minecraft.server.command.ServerCommandSource;
//import net.minecraft.server.network.ServerPlayerEntity;

public class AfkCommand {
    public static void register() {
        CommandRegistrationCallback.EVENT.register((dispatcher, environment) -> {
            dispatcher.register(
                    literal("afk")
                            .requires(Permissions.require("afkplus.afk", 0))
                            .executes(ctx -> setAfk(ctx.getSource(), ""))
                            .then(argument("reason", StringArgumentType.greedyString())
                                    .requires(Permissions.require("afkplus.afk", 0))
                                    .executes(
                                            ctx -> setAfk(ctx.getSource(),
                                                    StringArgumentType.getString(ctx, "reason")))));
        });
    }

    private static int setAfk(ServerCommandSource src, String reason) throws CommandSyntaxException {
        AfkPlayerData player = (AfkPlayerData) src.getPlayer();
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
