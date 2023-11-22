package io.github.sakuraryoko.afkplus.data;

public interface AfkPlayerData {
    boolean isAfk();

    void registerAfk(String reason);

    void unregisterAfk();

    long getAfkTimeMs();

    String getAfkTimeString();

    String getAfkReason();

    void updatePlayerList();
}
