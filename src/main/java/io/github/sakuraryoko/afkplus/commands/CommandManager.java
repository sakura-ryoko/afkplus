package io.github.sakuraryoko.afkplus.commands;

import static io.github.sakuraryoko.afkplus.config.ConfigManager.*;

public class CommandManager {
    public static void register() {
        if (CONFIG.afkPlusOptions.enableAfkCommand) {
            AfkCommand.register();
        }
        if (CONFIG.afkPlusOptions.enableAfkInfoCommand) {
            AfkInfoCommand.register();
        }
        if (CONFIG.afkPlusOptions.enableAfkExCommand) {
            AfkExCommand.register();
        }
        AfkPlusCommand.register();
    }
}
