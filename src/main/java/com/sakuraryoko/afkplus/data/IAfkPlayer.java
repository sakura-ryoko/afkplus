package com.sakuraryoko.afkplus.data;

public interface IAfkPlayer
{
    boolean afkplus$isAfk();

    void afkplus$registerAfk(String reason);

    void afkplus$unregisterAfk();

    long afkplus$getAfkTimeMs();

    String afkplus$getAfkTimeString();

    String afkplus$getAfkReason();

    void afkplus$updatePlayerList();

    boolean afkplus$isDamageEnabled();

    boolean afkplus$isLockDamageDisabled();

    boolean afkplus$isNoAfkEnabled();

    void afkplus$setNoAfkEnabled();

    void afkplus$unsetNoAfkEnabled();

    void afkplus$enableDamage();

    void afkplus$disableDamage();

    void afkplus$lockDamageDisabled();

    void afkplus$unlockDamageDisabled();

    void afkplus$afkKick();

    String afkplus$getName();

    boolean afkplus$isCreative();

    boolean afkplus$isSpectator();
}
