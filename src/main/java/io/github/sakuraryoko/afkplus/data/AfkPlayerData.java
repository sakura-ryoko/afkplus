package io.github.sakuraryoko.afkplus.data;

public interface AfkPlayerData {
    boolean afkplus$isAfk();

    void afkplus$registerAfk(String reason);

    void afkplus$unregisterAfk();

    long afkplus$getAfkTimeMs();

    String afkplus$getAfkTimeString();

    String afkplus$getAfkReason();

    void afkplus$updatePlayerList();
}
