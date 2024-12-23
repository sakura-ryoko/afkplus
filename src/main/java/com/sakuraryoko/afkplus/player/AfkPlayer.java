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

import net.minecraft.server.level.ServerPlayer;

public class AfkPlayer
{
    private ServerPlayer player;
    private AfkHandler handler;
    private int entityId;
    private boolean afkEnabled;
    private boolean damageEnabled;
    private boolean lockDamageEnabled;
    private boolean noAfkEnabled;
    private long lastPlayerTick;
    private long afkTimeMs;
    private String afkTimeString;
    private String afkReason;

    private AfkPlayer(@Nonnull ServerPlayer player)
    {
        this.player = player;
        this.entityId = player.getId();
        this.afkEnabled = false;
        this.damageEnabled = true;
        this.lockDamageEnabled = false;
        this.noAfkEnabled = false;
        this.afkTimeMs = 0;
        this.lastPlayerTick = 0;
        this.afkTimeString = "";
        this.afkReason = "";
        this.handler = new AfkHandler(this);
    }

    public static AfkPlayer init(@Nonnull ServerPlayer player)
    {
        return new AfkPlayer(player);
    }

    public AfkHandler getHandler()
    {
        if (this.handler == null)
        {
            this.handler = new AfkHandler(this);
        }

        return this.handler;
    }

    public ServerPlayer getPlayer()
    {
        return this.player;
    }

    public int getEntityId()
    {
        return this.entityId;
    }

    public String getName()
    {
        return this.player.getName().getString();
    }

    public boolean isAfk()
    {
        return this.afkEnabled;
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

    public long getAfkTimeMs()
    {
        return this.afkTimeMs;
    }

    public String getAfkTimeString()
    {
        return this.afkTimeString;
    }

    public String getAfkReason()
    {
        return this.afkReason;
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

    public void setAfkTimeString(String timeString)
    {
        this.afkTimeString = timeString;
    }

    public void setAfkReason(String reason)
    {
        this.afkReason = reason;
    }

    public void setAfkTimeMs(long time)
    {
        this.afkTimeMs = time;
    }

    public void tickPlayer(long time)
    {
        this.lastPlayerTick = time;
    }

    public AfkPlayer setPlayer(@Nonnull ServerPlayer player)
    {
        this.player = player;
        this.entityId = player.getId();
        return this;
    }

    public boolean matches(@Nonnull ServerPlayer player)
    {
        return player.getId() == this.entityId || this.player.getName().equals(player.getName()) || this.player.equals(player);
    }

    public void clearAfkValues()
    {
        this.afkTimeMs = 0;
        this.afkTimeString = "";
        this.afkReason = "";
    }

    public void reset()
    {
        this.handler.reset();
        this.afkEnabled = false;
        this.damageEnabled = true;
        this.lockDamageEnabled = false;
        this.noAfkEnabled = false;
        this.lastPlayerTick = 0;
        this.clearAfkValues();
    }
}
