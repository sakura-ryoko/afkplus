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

import java.nio.file.Path;
import java.util.Collection;

import net.fabricmc.api.EnvType;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.metadata.ContactInformation;
import net.fabricmc.loader.api.metadata.Person;

public class AfkPlusReference
{
    public static final Path CONFIG_DIR = FabricLoader.getInstance().getConfigDir();
    public static final String MOD_ID = "afkplus";
    public static final boolean AFK_DEBUG = true;
    public static String AFK_VERSION;
    public static String MC_VERSION;
    public static EnvType MOD_ENV;
    public static String MOD_NAME;
    public static String MOD_DESC;
    public static Collection<Person> MOD_AUTHOR;
    public static Collection<Person> MOD_CONTRIB;
    public static ContactInformation MOD_CONTACTS;
    public static Collection<String> MOD_LICENSES;
    public static String MOD_AUTHO_STRING;
    public static String MOD_CONTRIB_STRING;
    public static String MOD_HOMEPAGE_STRING;
    public static String MOD_SOURCES_STRING;
    public static String MOD_LICENSES_STRING;
    public static boolean AFK_INIT;
}
