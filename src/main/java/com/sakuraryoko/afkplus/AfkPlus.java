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

package com.sakuraryoko.afkplus;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.fabricmc.api.ModInitializer;

import com.sakuraryoko.afkplus.modinit.AfkPlusInit;
import com.sakuraryoko.corelib.impl.modinit.ModInitManager;

public class AfkPlus implements ModInitializer
{
    public static Logger LOGGER = LogManager.getLogger(Reference.MOD_ID);

    public static void debugLog(String key, Object... args)
    {
        if (Reference.DEBUG)
        {
            LOGGER.info(String.format("[DEBUG] %s", key), args);
        }
    }

    @Override
    public void onInitialize()
    {
        ModInitManager.getInstance().registerModInitHandler(AfkPlusInit.getInstance());
    }
}
