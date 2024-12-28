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

import java.time.ZonedDateTime;
import javax.annotation.Nonnull;

import net.minecraft.Util;
import net.minecraft.server.level.ServerPlayer;

import com.sakuraryoko.afkplus.config.ConfigWrap;
import com.sakuraryoko.corelib.api.log.AnsiLogger;
import com.sakuraryoko.corelib.api.time.DurationFormat;
import com.sakuraryoko.corelib.api.time.TimeFormat;

public class AfkPlayer
{
    private static final AnsiLogger LOGGER = new AnsiLogger(AfkPlayer.class, true);
    private ServerPlayer player;
    private AfkHandler handler;
    private int entityId;
    private boolean afkEnabled;
    private boolean damageEnabled;
    private boolean lockDamageEnabled;
    private boolean noAfkEnabled;
    private long lastPlayerTick;
    private long afkTimeMs;
    private long afkTimeEpoch;
    private long lastMovementTime;
    private long lastLookTime;
    private long lastAttackTime;
    private long lastPlayerListUpdate;
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
        this.afkTimeEpoch = 0;
        this.lastPlayerTick = Util.getMillis();
        this.lastMovementTime = Util.getMillis();
        this.lastLookTime = Util.getMillis();
        this.lastAttackTime = Util.getMillis();
        this.lastPlayerListUpdate = -1;     // cause an immediate update upon creation
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

    public long getAfkTimeEpoch()
    {
        return this.afkTimeEpoch;
    }

    public long getLastMovementTime()
    {
        return lastMovementTime;
    }

    public void setLastMovementTime(long lastMovementTime)
    {
        this.lastMovementTime = lastMovementTime;
    }

    public long getLastLookTime()
    {
        return lastLookTime;
    }

    public void setLastLookTime(long lastLookTime)
    {
        this.lastLookTime = lastLookTime;
    }

    public long getLastAttackTime()
    {
        return lastAttackTime;
    }

    public void setLastAttackTime(long lastAttackTime)
    {
        this.lastAttackTime = lastAttackTime;
    }

    public long getLastPlayerListUpdate()
    {
        return this.lastPlayerListUpdate;
    }

    public void setLastPlayerListUpdate(long lastPlayerListUpdate)
    {
        this.lastPlayerListUpdate = lastPlayerListUpdate;
    }

    public DurationFormat getDurationType()
    {
        DurationFormat format = DurationFormat.fromName(ConfigWrap.mess().duration.option.getName());

        if (format != null)
        {
            return format;
        }

        ConfigWrap.mess().duration.option = DurationFormat.PRETTY;
        return DurationFormat.REGULAR;
    }

    public TimeFormat getTimeDateType()
    {
        TimeFormat format = TimeFormat.fromName(ConfigWrap.mess().timeDate.option.getName());

        if (format != null)
        {
            return format;
        }

        ConfigWrap.mess().timeDate.option = TimeFormat.REGULAR;
        return TimeFormat.REGULAR;
    }

    public DurationFormat getDurationTypeForPlaceholder()
    {
        DurationFormat format = DurationFormat.fromName(ConfigWrap.place().duration.option.getName());

        if (format != null)
        {
            return format;
        }

        ConfigWrap.place().duration.option = DurationFormat.REGULAR;
        return DurationFormat.REGULAR;
    }

    public TimeFormat getTimeDateTypeForPlaceholder()
    {
        TimeFormat format = TimeFormat.fromName(ConfigWrap.place().timeDate.option.getName());

        if (format != null)
        {
            return format;
        }

        ConfigWrap.place().timeDate.option = TimeFormat.REGULAR;
        return TimeFormat.REGULAR;
    }

    public String getAfkDurationString()
    {
        return this.getDurationType().getFormat((Util.getMillis() - this.getAfkTimeMs()), ConfigWrap.mess().duration.customFormat);
    }

    public String getAfkTimeString()
    {
        return this.getTimeDateType().formatTo(this.getAfkTimeEpoch(), ConfigWrap.mess().timeDate.customFormat);
    }

    public String getAfkDurationStringForPlaceholder()
    {
        return this.getDurationTypeForPlaceholder().getFormat((Util.getMillis() - this.getAfkTimeMs()), ConfigWrap.place().duration.customFormat);
    }

    public String getAfkTimeStringForPlaceholder()
    {
        return this.getTimeDateTypeForPlaceholder().formatTo(this.getAfkTimeEpoch(), ConfigWrap.place().timeDate.customFormat);
    }

    public String getAfkDurationFormat()
    {
        return this.getDurationType().getFormatString();
    }

    public String getAfkTimeFormat()
    {
        return this.getTimeDateType().getFormatString();
    }

    public String getAfkDurationFormatForPlaceholder()
    {
        return this.getDurationTypeForPlaceholder().getFormatString();
    }

    public String getAfkTimeFormatForPlaceholder()
    {
        return this.getTimeDateTypeForPlaceholder().getFormatString();
    }

    public String getAfkReason()
    {
        return this.afkReason;
    }

    public void setAfk(boolean toggle)
    {
        this.setLastPlayerListUpdate(-1);       // reset for the next AFK
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

    public void setAfkReason(String reason)
    {
        this.afkReason = reason;
    }

    public void setAfkTimeMs(long time)
    {
        this.afkTimeMs = time;
        this.afkTimeEpoch = time > 0L ? ZonedDateTime.now().toInstant().toEpochMilli() : 0L;
    }

    // todo perhaps move things here in the future?
    public boolean shouldBeAfk()
    {
        long now = Util.getMillis();
        long lastMovement = now - this.getLastMovementTime();
        long lastLook = now - this.getLastLookTime();
        long lastAttack = now - this.getLastAttackTime();
        long lastTick = now - this.lastPlayerTick;
        long lastAction = now - this.player.getLastActionTime();
        long limit = ConfigWrap.pack().timeoutSeconds * 1000L;
        int weight = 0;

        if (lastMovement > limit)
        {
            ++weight;
        }
        if (lastLook > limit)
        {
            ++weight;
        }
        if (lastAttack > limit)
        {
            ++weight;
        }
        if (lastAction > limit)
        {
            ++weight;
        }
        /*
        if (lastTick > limit)
        {
            ++weight;
        }
         */

        if (weight > 1)
        {
            LOGGER.debug("shouldBeAfk(): m: {}, l: {}, A: {}, lA: {}, T: {} [timeout: {} (s) // weight {}]",
                         lastMovement, lastLook, lastAttack, lastAction, lastTick, ConfigWrap.pack().timeoutSeconds, weight);
        }

        return weight > 2;
    }

    public boolean shouldIgnoreAttacks()
    {
        long now = Util.getMillis();
        long lastMovement = now - this.getLastMovementTime();
        long lastLook = now - this.getLastLookTime();
        long lastAttack = now - this.getLastAttackTime();
        //long lastTick = now - this.lastPlayerTick;
        //long lastAction = now - this.player.getLastActionTime();
        long limit = ConfigWrap.pack().timeoutSeconds * 1000L;

        /*
        LOGGER.debug("shouldIgnoreAttacks(): m: {}, l: {}, A: {}, lA: {}, T: {} [timeout: {} (s)]",
                     lastMovement, lastLook, lastAttack, lastAction, lastTick, ConfigWrap.pack().timeoutSeconds);
         */

        if (ConfigWrap.pack().ignoreAttacks)
        {
            return lastMovement > limit && lastLook > limit && lastAttack < limit;
        }

        return false;
    }

    public void tickPlayer(long time)
    {
        /*
        if (this.shouldBeAfk())
        {
            LOGGER.info("tickPlayer(): should be AFK [TRUE]");
        }
         */

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
        this.lastPlayerListUpdate = -1;
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
        this.lastMovementTime = 0;
        this.lastLookTime = 0;
        this.lastAttackTime = 0;
        this.clearAfkValues();
    }
}
