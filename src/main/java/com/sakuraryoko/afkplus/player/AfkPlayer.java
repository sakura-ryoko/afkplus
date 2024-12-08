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

package com.sakuraryoko.afkplus.player;

import javax.annotation.Nonnull;

public class AfkPlayer
{
    private IAfkPlayer player;
    private final int entityId;
    private boolean afkEnabled;
    private boolean damageEnabled;
    private boolean lockDamageEnabled;
    private boolean noAfkEnabled;
    private long lastPlayerTick;
    private long afkTime;

    public AfkPlayer(@Nonnull IAfkPlayer player)
    {
        this.player = player;
        this.entityId = player.afkplus$getEntityId();
        this.afkEnabled = false;
        this.damageEnabled = true;
        this.lockDamageEnabled = false;
        this.noAfkEnabled = false;
        this.afkTime = 0;
        this.lastPlayerTick = 0;
    }

    public IAfkPlayer getPlayer()
    {
        return this.player;
    }

    public int getEntityId()
    {
        return this.entityId;
    }

    public String getName()
    {
        return this.player.afkplus$getName();
    }

    public boolean isAfk()
    {
        return this.afkEnabled;
    }

    public boolean isCreative()
    {
        return this.player.afkplus$isCreative();
    }

    public boolean isSpectator()
    {
        return this.player.afkplus$isSpectator();
    }

    public boolean isDamageEnabled()
    {
        return this.damageEnabled;
    }

    public boolean isLockDamageEnabled()
    {
        return this.lockDamageEnabled;
    }

    public boolean isNoAfkEnabled()
    {
        return this.noAfkEnabled;
    }

    public long getLastPlayerTick()
    {
        return this.lastPlayerTick;
    }

    public long getAfkTime()
    {
        return this.afkTime;
    }

    public void setAfk(boolean toggle)
    {
        this.afkEnabled = toggle;
    }

    public void setDamageEnabled(boolean toggle)
    {
        this.damageEnabled = toggle;
    }

    public void setLockDamageEnabled(boolean toggle)
    {
        this.lockDamageEnabled = toggle;
    }

    public void setNoAfkEnabled(boolean toggle)
    {
        this.noAfkEnabled = toggle;
    }

    public void tickPlayer(long time)
    {
        this.lastPlayerTick = time;
    }

    public void setAfkTime(long time)
    {
        this.afkTime = time;
    }

    public AfkPlayer setPlayer(@Nonnull IAfkPlayer player)
    {
        this.player = player;
        return this;
    }

    public static AfkPlayer init(@Nonnull IAfkPlayer player)
    {
        return new AfkPlayer(player);
    }

    public boolean matches(@Nonnull IAfkPlayer player)
    {
        return player.afkplus$getEntityId() == this.entityId || this.getName().equals(player.afkplus$getName()) || this.player.equals(player);
    }
}
