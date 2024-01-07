package io.github.sakuraryoko.afkplus.commands;

import com.mojang.brigadier.context.CommandContext;
import io.github.sakuraryoko.afkplus.util.AfkPlusLogger;
import io.github.sakuraryoko.afkplus.util.FormattingExample;
import me.lucko.fabric.api.permissions.v0.Permissions;
import net.fabricmc.fabric.api.command.v1.CommandRegistrationCallback;
import net.minecraft.server.command.ServerCommandSource;

import static io.github.sakuraryoko.afkplus.config.ConfigManager.CONFIG;
import static net.minecraft.server.command.CommandManager.literal;

public class AfkExCommand {
    public static void register() {
        CommandRegistrationCallback.EVENT.register((dispatcher, environment) -> dispatcher.register(
                literal("afkex")
                        .requires(Permissions.require("afkplus.afkex", CONFIG.afkPlusOptions.afkExCommandPermissions))
                        .executes(ctx -> afkExample(ctx.getSource(), ctx))
        ));
    }
    private static int afkExample(ServerCommandSource src, CommandContext<ServerCommandSource> context) {
        String user = src.getName();
        context.getSource().sendFeedback(FormattingExample.runBuiltInTest(), false);
        context.getSource().sendFeedback(FormattingExample.runAliasTest(), false);
        context.getSource().sendFeedback(FormattingExample.runColorsTest(), false);
        AfkPlusLogger.debug(user + " has executed /afkex (example) .");
        return 1;
    }
}
