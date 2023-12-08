package io.github.sakuraryoko.afkplus.commands;

import static io.github.sakuraryoko.afkplus.config.ConfigManager.*;

//import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;

public class CommandManager {
    public static void register() {
        if (CONFIG.afkPlusOptions.enableAfkCommand) {
            AfkCommand.register();
        }
        if (CONFIG.afkPlusOptions.enableAfkInfoCommand) {
            AfkInfoCommand.register();
        }
        AfkPlusCommand.register();
    }
}
