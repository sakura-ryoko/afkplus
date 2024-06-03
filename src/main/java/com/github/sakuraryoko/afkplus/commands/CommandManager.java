package com.github.sakuraryoko.afkplus.commands;

import com.github.sakuraryoko.afkplus.config.ConfigManager;

public class CommandManager {
    public static void register() {
        if (ConfigManager.CONFIG.afkPlusOptions.enableAfkCommand) {
            AfkCommand.register();
        }
        if (ConfigManager.CONFIG.afkPlusOptions.enableNoAfkCommand) {
            NoAfkCommand.register();
        }
        if (ConfigManager.CONFIG.afkPlusOptions.enableAfkInfoCommand) {
            AfkInfoCommand.register();
        }
        if (ConfigManager.CONFIG.afkPlusOptions.enableAfkExCommand) {
            AfkExCommand.register();
        }
        AfkPlusCommand.register();
    }
}
