/*
 * This file is part of the AfkPlus project, licensed under the
 * GNU Lesser General Public License v3.0
 *
 * Copyright (C) 2024  Sakura Ryoko and contributors
 *
 * AfkPlus is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * AfkPlus is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with AfkPlus.  If not, see <https://www.gnu.org/licenses/>.
 */

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

    long afkplus$getLastPlayerListTick();

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
