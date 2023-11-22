package io.github.sakuraryoko.afkplus.placeholders;

public class PlaceholderManager {
    public static void register() {
        AfkPlusAfk.register();
        AfkPlusDuration.register();
        AfkPlusName.register();
        AfkPlusReason.register();
        AfkPlusTime.register();
    }
}
