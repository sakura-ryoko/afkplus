package com.sakuraryoko.afkplus.commands;

import me.lucko.fabric.api.permissions.v0.Permissions;

import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.CommandSourceStack;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;

import com.sakuraryoko.afkplus.config.ConfigManager;
import com.sakuraryoko.afkplus.util.AfkPlusLogger;
import com.sakuraryoko.afkplus.util.FormattingExample;

import static net.minecraft.commands.Commands.literal;

public class AfkExCommand
{
    public static void register()
    {
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> dispatcher.register(
                literal("afkex")
                        .requires(Permissions.require("afkplus.afkex", ConfigManager.CONFIG.afkPlusOptions.afkExCommandPermissions))
                        .executes(ctx -> afkExample(ctx.getSource(), ctx))
        ));
    }

    private static int afkExample(CommandSourceStack src, CommandContext<CommandSourceStack> context)
    {
        String user = src.getTextName();
        context.getSource().sendSuccess(FormattingExample::runBuiltInTest, false);
        context.getSource().sendSuccess(FormattingExample::runAliasTest, false);
        context.getSource().sendSuccess(FormattingExample::runColorsTest, false);
        AfkPlusLogger.debug(user + " has executed /afkex (example) .");
        return 1;
    }
}
