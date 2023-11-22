package io.github.sakuraryoko.afkplus.commands;

import static io.github.sakuraryoko.afkplus.config.ConfigManager.*;

import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;

public class CommandManager {
    public static void register() {
        if (CONFIG.afkPlusOptions.enableAfkCommand) {
            CommandRegistrationCallback.EVENT
                    .register((dispatcher, registryAccess, environment) -> AfkCommand.register(dispatcher));
        }
        if (CONFIG.afkPlusOptions.enableAfkInfoCommand) {
            CommandRegistrationCallback.EVENT
                    .register((dispatcher, registryAccess, environment) -> AfkInfoCommand.register(dispatcher));
        }
        CommandRegistrationCallback.EVENT
                .register((dispatcher, registryAccess, environment) -> AfkPlusCommand.register(dispatcher));
    }
}
