package io.github.sakuraryoko.afkplus.data;

public interface IAfkPlayer {
    boolean afkplus$isAfk();
    void afkplus$registerAfk(String reason);
    void afkplus$unregisterAfk();
    long afkplus$getAfkTimeMs();
    String afkplus$getAfkTimeString();
    String afkplus$getAfkReason();
    void afkplus$updatePlayerList();
    boolean afkplus$isDamageEnabled();
    boolean afkplus$isLockDamageDisabled();
    void afkplus$enableDamage();
    void afkplus$disableDamage();
    void afkplus$lockDamageDisabled();
    void afkplus$unlockDamageDisabled();
    String afkplus$getName();
    boolean afkplus$isCreative();
}
