# AfkPlus Fabric Mod for Minecraft

[![workflow](https://github.com/sakura-ryoko/afkplus/actions/workflows/gradle.yml/badge.svg)](https://github.com/sakura-ryoko/afkplus/actions/workflows/gradle.yml)

![Example](https://sakuraryoko.com/files/1260026/afkplus.jpg)

## Basic Description:
- A forked project based upon AfkDisplay by beabfc.  He has given me his blessing with proceeding to create this mod, look under my Archived Issues pane.
- Show which players are AFK in the player list, see the Reasons, Durations, and Times.
- Fully configurable and with [Placeholder API](https://placeholders.pb4.eu/user/general/), which includes all Placeholder API formatting nodes such as: yellow or bold natively. (You can even include a URL in your AFK Reason!)
- Mod can now be used on Single Player, and Integrated Servers (Open to LAN).
- [**NEW**] Mod has now been mostly rewritten as of December 2024; as a MultiVersion project based on [Fallen-Breath's Template Mod](https://github.com/Fallen-Breath/fabric-mod-template) architecture.
- [**NEW**] Mod Now is a dependency of my new library mod, [CoreLib](https://github.com/sakura-ryoko/corelib), and the More Color system has also been forked into my [More Color API](https://github.com/sakura-ryoko/more-color-api) mod.
  - They are both included in the AfkPlus JAR file.

## MOD ReWrite & NEW Features
- Entire mod ecosystem has been migrated to a multi-version architecture to more quickly port the mod to versions 1.19.2-1.21.4+ at the same time, using a single branch.
- Config no longer uses TOML, and has been converted to the Data Driven [CoreLib](https://github.com/sakura-ryoko/corelib) based JSON config system; which works similar to how Pat's Mod ecosystem works. 
- Most Player / Server Events are now managed by [CoreLib](https://github.com/sakura-ryoko/corelib) interfaces; with a few AFK Specific exceptions.
- All Commands are now registered and managed by a [CoreLib](https://github.com/sakura-ryoko/corelib) interface.
- All abstract Text Placeholder API formatting, and extra color tags are now managed by [More Color API](https://github.com/sakura-ryoko/more-color-api).  See the `/morecolors` command.
- All new abstract TimeDate / Duration formatting interfaces are managed by [CoreLib](https://github.com/sakura-ryoko/corelib).
- Entirely rewritten AfkPlayer / Afk Player List system based on abstraction; instead of being managed via Mixin classes.  This should help with any strict mod compatibility issues; and fight off ugly code.
- Entire mod EcoSystem from CoreLib to AfkPlus each has an API / Event system for other mods to implement and make compatible in their own mods.
- AfkPlus adds API compat handling for `Vanish`, `Carpet Mod`, and `Styled Player List`.  Each mod's integration varies based on any compatibility needs.
- Added several `message` options, and forked the `afkKick` and `disableDamage` systems into their own configuration categories.  See the config below for more defaulted information.
- Several other annoyances, bugs, and features have been added as requested; such as the ability to fully configure the `Gone for: XXX` messages by various config options. 

## Features
- Added a placeholder `%afkplus:afk%` for you to offer a very basic '**[AFK]**' tag to players who are AFK.
- Added a placeholder `%afkplus:name%`/`%afkplus:display_name%` so that you can use this as a replacement for the '**%player:displayname%**' placeholder under other Mods, such as [Styled Playerlist](https://modrinth.com/mod/styledplayerlist "Styled Playerlist").
***NOTE that this method was designed to be fully compliant with LuckPerms Prefixes under Styled Playerlist, because the standard method for playerlist updating fails, or you can simply use %afkplus:afk% to add formatting to existing names if you like.***
- Added a placeholder `%afkplus:duration%` so that you can get the time since someone went AFK, with configuration for a format prefix.
- Added a placeholder `%afkplus:time%` so that you can get the Time/Date when someone went AFK, with configuration for a format prefix.
- Added a placeholder `%afkplus:reason%` so that you can port the Afk Reason for why someone went AFK, with configuration for a format prefix.
- Added a placeholder `%afkplus:invulnerable%` so that you can display the "Disable Damage" status for all users, similar to the "[AFK]" placeholder tag, but more or less an "add on".
- There is a special configuration option 'prettyDuration' to configure the AFK Duration in a more human-readable format, instead of the default (HH:mm:ss.mss) format.  Each method has its advantages though. ([Styled Nicknames](https://modrinth.com/mod/styled-nicknames) or [Styled Chat](https://modrinth.com/mod/styled-chat) comes to mind here)
- There are several mod / data pack conflict warnings for administrators to help them make better decisions on what mods they want to install.
- Added a "disableDamage" configuration to make AFK players immune to damage after a 15-second cool down.  A new configurable server-wide message is now enforced by default when this occurs.
- Added a "disableDamageCooldown" configuration to allow Administrators to adjust the default "timer" that is applied after a player goes AFK.  I highly recommend not setting this to '0', unless you don't think your players will abuse this privilege.
- Added a "whenDamageDisabled" configuration so that you can customize the message displayed when your players are marked as invulnerable.
- Added a "whenDamageEnabled" configuration so that you can customize the message displayed when your players are unmarked as invulnerable.
- Now also checks for if players are in Spectator Mode, and not only Creative for managing your AFK/Disable Damage status.
- ~~Adds several color nodes that players can use for AFK Reasons.  See /afkex for a display example.~~
  - ~~brown, cyan, dark_brown, dark_pink, light_blue, light_brown, light_gray, light_pink, lime, magenta, purple, salmon,~~
  - ~~bluetiful, burnt_orange, canary, cool_mint, copper, powder_blue, royal_purple, shamrock, tickle_me_pink, ultramarine_blue~~
- Added a "bypassSleepCount" configuration so that you can allow players marked as Afk to bypass the Sleeping Requirements.
- Added a "bypassInsomnia" configuration so that you can allow players marked as Afk to block Phantom spawning.
- Added a "/noafk" command for players to stop themselves from being marked as Afk.
- Added a "displayDuration" configuration option so that you can enable/disable the "Gone for XX minutes, XX seconds" during whenReturn.
- Added additional handling to allow "afkTimeoutString" to be set to "", and passing the "defaultReason" as "" to have more of the "Original" AfkDisplay feel.
- Added a "AFK Kick" system to automatically kick players from the server who are AFK after a configured amount of time.  It has several configuration options:
  - `afkKickEnabled` - (true/false) Enables the AFK Auto Kick manager.
  - `afkKickNonSurvival` - (true/false) Allows Kicking Creative / Spectator players.
  - `afkKickTimer` - The time beyond `timeoutSeconds` (Additive) when a player gets removed from the server, so if `timeoutSeconds` is 240, and `afkKickTimer` is set to 3600, the actual time they get kicked is at **3840**, or 64 minutes at the longest.
  - `afkKickSafePermissions` - (Permission 3) The default Permissions level that is marked "safe" from being automatically kicked. (Luck Perms: afkplus.kick.safe)
    - Note; that if a player has `afkPlusCommandPermissions` (Luck Perms: afkplus.afkplus), they will also be marked as safe from being automatically kicked, but this new configuration allows you to configure these permissions separately.
  - `afkKickMessage` - The message sent to Players that get kicked as the reason for their removal. (Adds the Duration when `displayDuration` is enabled)
  - `whenKicked` - The message broadcast to the server when a player gets kicked for being AFK. (Adds the Duration when `displayDuration` is enabled)
  - NOTES: This feature *DOES NOT* automatically kick Carpet Mod Bots, because the 'fake' players do not get ticked by the server.
    - This feature *DOES NOT* work in Single Player/Open To Lan, and requires a Dedicated Server Environment to function properly.

## Commands (Permissions via [Luck Permissions](https://luckperms.net/) or the afk_plus configurations)
- '**/afkplus**' with the AfkPlusCommandPermissions (Default: 4) setting the default restrictions. (Permission: afkplus.afkplus)
  - Displays the Mod Version information.
- '**/afkplus reload**' command. (Permission: afkplus.afkplus.reload)
  - This allows an Administrator to reload the configuration while the server is running.
- ~~'**/afkplus ex**' command.  (Permission: afkplus.afkplus.ex)~~
  - ~~This displays a simple "formatting test" to show the user what basic text nodes are available to use, and this also allows them to use this for Copy/Paste by clicking on colors/formats.~~
- '**/afkplus set [Player] [Reason]**' command. (Permission: afkplus.afkplus.set)
  - This allows any administrator to set the AFK status of a player, and this also removes their NoAFK status. 
- '**/afkplus clear [Player]**' command. (Permission: afkplus.afkplus.clear)
  - This allows any administrator to clear the AFK status of a player.
- '**/afkplus damage enable [Player]**' command. (Permission: afkplus.afkplus.damage.enable)
  - This allows an Administrator to force-enable an AFK player's ability to be damaged as long as they are connected.
- '**/afkplus damage disable [Player]**' command. (Permission: afkplus.afkplus.damage.disable)
  - This allows an Administrator to revert a player's ability to use "Disable Damage" after it was forcefully removed.
- '**/afkplus info [Player]**' command. (Permission: afkplus.afkplus.info)
  - This allows any administrator to check the AFK status of a player, and display the time and duration since they went AFK.
- '**/afkplus update [Player]**' command. (Permission: afkplus.afkplus.update)
  - This allows any administrator to force a player list update for a player.
- '**/afkinfo [Player]**' with the AfkInfoCommandPermissions (Default: 2) setting the default restrictions, (Permission: afkplus.afkinfo)
  - Does the same thing as '**/afkplus info [Player]**', but can be used for Mods, or players, or however you like to configure it for people to see.
- ~~'**/afkex**' with the AfkExCommandPermissions (Default: 0) setting the default restrictions.  (Permission: afkplus.afkex)~~
  - ~~This displays a simple "formatting test" to show the user what basic text nodes are available to use, and this also allows them to use this for Copy/Paste by clicking on colors/formats.~~
- '**/afk [Reason]**' with the AfkCommandPermissions (Default: 0) setting the default restrictions.  (Permission: afkplus.afk)
  - This allows any user to use a [Reason] along with setting their AFK status.
- [NEW] '**/noafk**' with the noAfkCommandPermissions (Default: 0) setting the default restrictions.  (Permission: afkplus.noafk)
  - This allows any user to set themselves in a state where they will not go Afk based on the configured timeout value.
- [NEW] 'afkKickSafePermissions' (Default: 3) sets the default permissions to mark an AFK player as safe from being kicked. (Permission: afkplus.kick.safe)

## Potential known conflicts (Make your choice)
- [afk display Data pack](https://vanillatweaks.net/picker/datapacks/) (Vanilla Tweaks Data pack, changes the player list display) -- Mod checks for any "afk" containing data packs in the name.
- [AfkDisplay](https://modrinth.com/mod/afkdisplay) -- because this is the mod that AfkPlus is based upon, and offers fewer features.
- [AntiLogout](https://modrinth.com/mod/noexits) (/afk command, timeout handling)
- [Auto AFK](https://modrinth.com/mod/auto-afk) (/afk command, timeout handling)
- [Sessility](https://modrinth.com/mod/sessility) (timeout handling)
- [Playtime-Tracker](https://modrinth.com/mod/playtime-tracker) (timeout handling)
- [SvrUtil](https://modrinth.com/mod/svrutil) (/afk command, the rest is safe)

## Example Configuration
The configuration is located in `afkplus.json` inside your servers config folder.

```json lines
{
  "___comment": "AfkPlus-1.21.4-1.7.4-SNAPSHOT Config",
  "config_date": "Fri, 3 Jan 2025 00:05:08 -0500",
  "afk_plus": {
    "_comment01": "# Allows you to disable the /afk command to mark yourself as AFK, with an optional [Reason] (Default: true)",
    "enableAfkCommand": true,
    "_comment02": "# Allows you to disable the /noafk command to mark yourself as NoAFK, which disables the timeout (Default: true)",
    "enableNoAfkCommand": true,
    "_comment03": "# Allows you to disable the /afkinfo command to allow players to see someone's AFK status (Time, Duration, Reason). (Default: true)",
    "enableAfkInfoCommand": true,
    "_comment04": "# The /afk default command permissions, configurable with Luck Perms (afkplus.afk) node (Default: 0)",
    "afkCommandPermissions": 0,
    "_comment05": "# The /noafk default command permissions, configurable with Luck Perms (afkplus.noafk) node (Default: 0)",
    "noAfkCommandPermissions": 0,
    "_comment06": "# The /afkinfo default command permissions, configurable with Luck Perms (afkplus.afkinfo) node (Usually for Mods) (Default: 2)",
    "afkInfoCommandPermissions": 2,
    "_comment07": "# The /afkplus default command permissions, configurable with Luck Perms (afkplus.afkplus with .subcommands) node (Default: 3)",
    "afkPlusCommandPermissions": 3,
    "_comment08": "# A Basic Cooldown configurable to help prevent people from spamming the /afk command (Default: 5 seconds)",
    "afkCommandCooldown": 5,
    "_comment09": "# A boolean toggle to enable / disable debug logger messages in the mod",
    "debugMode": false
  },
  "packet": {
    "_comment01": "# The time without actions after which a player is considered AFK. Set to -1 to disable automatic AFK detection. (Default: 240)",
    "_comment02": "# --note; the Original AfkDisplay's default timeout was set to 180 seconds.",
    "timeoutSeconds": 240,
    "_comment03": "# Consider players that moved no longer AFK (enables easy bypass methods like AFK pools) (Default: false)",
    "resetOnMovement": false,
    "_comment04": "# Consider players which looked around no longer AFK (Default: false)",
    "resetOnLook": false,
    "_comment05": "# Consider players who are only swinging their sword as AFK (Default: false)",
    "ignoreAttacks": false,
    "_comment06": "# Makes it so that Afk players are not counted in the Sleep Percentage check (Default: true)",
    "bypassSleepCount": true,
    "_comment07": "# Makes it so that Afk Players block Phantom Spawning attempts. (Default: true)",
    "bypassInsomnia": true,
    "_comment08": "# The default \"timeout\" AFK reason (Default: \"<i><gray>timeout<r>\")",
    "afkTimeoutString": "<i><gray>timeout<r>",
    "_comment09": "# The default AFK timeout message for the \"ignoreAttacks\" detection",
    "afkTimeoutIgnoreAttack": "<i><gray>only swinging their sword<r>"
  },
  "disable_damage": {
    "_comment01": "# Disable damage after disableDamageCooldown seconds since a player went AFK (Default: false)",
    "disableDamage": false,
    "_comment02": "# Cooldown timer for enabling the \"DisableDamage\" feature. (Default: 15 seconds)",
    "_comment03": "# - WARNING!  Be advised that settings this too low can encourage poor player behavior.",
    "disableDamageCooldown": 15,
    "_comment04": "# The message content when an AFK player is marked as Invulnerable.",
    "_comment06": "# (Default: \"%player:displayname% <yellow>is marked as <red>Invulnerable.<r>\")",
    "whenDamageDisabled": "%player:displayname% <yellow>is marked as <red>Invulnerable.<r>",
    "_comment08": "# The message content when an AFK player is no longer marked as Invulnerable.",
    "_comment09": "# (Default: \"%player:displayname% <yellow>is no longer <red>Invulnerable.<r>\")",
    "whenDamageEnabled": "%player:displayname% <yellow>is no longer <red>Invulnerable.<r>"
  },
  "afk_kick": {
    "_comment01": "# Enables the AFK Auto-Kick system. (Default: false)",
    "afkKickEnabled": false,
    "_comment02": "# Allows Non-Survival players to be kicked (Creative, Spectator, etc) (Default: false)",
    "afkKickNonSurvival": false,
    "_comment03": "# The time after `timeoutSeconds` to automatically kick an AFK player (Default: 3600, aka. 1 hour)",
    "_comment04": "# --note; that this value is \"Additive\" to the `timeoutSeconds` value, so the actual time will be 3840, aka. 64 minutes.",
    "afkKickTimer": 3000,
    "_comment05": "# Default permission level to mark players as \"safe\" from being automatically kicked, i.e., Server Admins (Default: 3)",
    "afkKickSafePermissions": 3,
    "_comment06": "# The Kick Reason message sent to players that are removed.  Can be set to \"\" to get a basic \"AFK timeout\" reason.",
    "_comment07": "# (Default: \"<copper>AFK beyond the allowed time limit set by your Administrator.<r>\")",
    "_comment08": "# --note; that this message gets the AFK duration added to the end if `message.displayDuration` is set to true.",
    "afkKickMessage": "<copper>AFK beyond the allowed time limit set by your Administrator.<r>",
    "_comment09": "# The message content when an AFK player gets kicked from the server.",
    "_comment10": "# (Default: \"%player:displayname% <copper>was kicked for being AFK.<r>\")",
    "_comment11": "# --note; that this message gets the AFK duration added to the end if `message.displayDuration` is set to true.",
    "whenKicked": "%player:displayname% <copper>was kicked for being AFK.<r>",
    "_comment12": "# The default reason when a player is kicked",
    "_comment13": "# (Default: \"<copper>AFK timeout\")",
    "whenKickedDefaultReason": "<copper>AFK timeout",
    "_comment14": "# The AFK default Duration Prefix used when a player is kicked.  The duration format used follows the message.duration configuration.",
    "_comment15": "# (Default: \" <gray>(Gone for: <green>\")",
    "whenKickedDurationDefaultPrefix": " <gray>(Gone for: <green>",
    "_comment16": "# The AFK Player Named Duration Prefix used when a player is kicked.  The duration format used follows the message.duration configuration.",
    "_comment17": "# (Default: \" <gray>(%player:displayname% was gone for: <green\")",
    "whenKickedDurationNamedPrefix": " <gray>(%player:displayname% was gone for: <green>",
    "_comment18": "# The suffix for either kick Duration Prefix",
    "_comment19": "# (Default: \" <gray>)\"",
    "whenKickedDurationSuffix": "<gray>)"
  },
  "player_list": {
    "_comment01": "# Change the playerlist name for players who are AFK (Default: true)",
    "_comment02": "# -- note; This is for when you're NOT using a player list display mods.",
    "enableListDisplay": true,
    "_comment03": "# The name that is shown in the player list if a player is AFK, and accepts formatting nodes",
    "_comment04": "# (Default: \"<i><gray>[AFK%afkplus:invulnerable%] %player:displayname%<r>\")",
    "_comment05": "# -- note; This function works best when not using Player List mods!*",
    "afkPlayerName": "<i><gray>[AFK%afkplus:invulnerable%] %player:displayname%<r>",
    "_comment06": "# This option sets a default Player List update interval.  This config goes a long way fixing",
    "_comment07": "#  various Player list update issues; especially when other Player List management mods are installed.",
    "_comment08": "#  (Default: 10 seconds)",
    "updateInterval": 10
  },
  "place_holder": {
    "_comment01": "# This will be the value of the placeholder %afkplus: afk% if a player is AFK, option accepts full formatting nodes",
    "_comment02": "# (Default: \"<i><gray>[AFK%afkplus:invulnerable%]<r>\")",
    "_comment03": "# -- note; *DOES NOT* place an \"<r>\" at the end in the code (trying not to modify its default behavior)",
    "afkPlaceholder": "<i><gray>[AFK%afkplus:invulnerable%]<r>",
    "_comment04": "# Placeholder %afkplus:name% for backporting the entire %displayname% for use in other Mods, such as Styled Player List",
    "_comment05": "# (Default: \"<i><gray>[AFK%afkplus:invulnerable%] %player:displayname_unformatted%<r>\")",
    "_comment06": "# -- note; *DOES NOT* places an \"<r>\" at the end in the code (trying not to modify its default behavior)",
    "afkPlusNamePlaceholderAfk": "<i><gray>[AFK%afkplus:invulnerable%] %player:displayname_unformatted%<r>",
    "_comment07": "# Value for when a player is NOT AFK, (i.e., the default \"%player:displayname%\")",
    "_comment08": "# -- note; *DOES NOT* places an \"<r>\" at the end in the code (trying not to modify its default behavior)",
    "afkPlusNamePlaceholder": "%player:displayname%",
    "_comment09": "# Adds a formatting prefix node for %afkplus:duration% (default: <green>)",
    "_comment10": "# -- note; places an \"<r>\" at the end in the code, and this is used during /afkinfo",
    "afkDurationPlaceholderFormatting": "<green>",
    "_comment11": "# Adds a formatting prefix node for %afkplus: time% (default: <green>)",
    "_comment12": "# -- note; places an \"<r>\" at the end in the code, and this is used during /afkinfo",
    "afkTimePlaceholderFormatting": "<green>",
    "_comment13": "# Adds a formatting prefix node for %afkplus: reason%, I'm not sure why someone might want this enabled, because",
    "_comment14": "# formatting your [Reason] yourself is fun. (default: none)",
    "_comment15": "# -- note; places an \"<r>\" at the end of the code, and this is used during /afkinfo",
    "afkReasonPlaceholderFormatting": "",
    "_comment16": "# Adds an option to configure a basic \"Add-On\" placeholder for attaching to the \"[AFK]\" tag to mark when a player is",
    "_comment17": "# marked as Invulnerable using %afkplus: invulnerable% (Default: \":<red>I<r>\")",
    "afkInvulnerablePlaceholder": ":<red>I<r>",
    "_comment18": "# Adds an option for using custom formatters for the output of the %duration% placeholder.  The valid options are:",
    "_comment19": "# -- 'REGULAR' (HH:mm:ss.SSS) (Default: REGULAR)",
    "_comment20": "# -- 'PRETTY' (d' days 'H' hours 'm' minutes 's' seconds')",
    "_comment21": "# -- 'ISO_EXTENDED' ('P'yyyy'Y'M'M'd'DT'H'H'm'M's.SSS'S')",
    "_comment22": "# -- 'FORMATTED'; Same as ISO_EXTENDED, but this option allows you to set your own custom DurationFormatUtils Formatter,",
    "_comment23": "#     -->  SEE: \"org.apache.commons.lang3.time.DurationFormatUtils;\" for more information",
    "duration": {
      "option": "REGULAR",
      "customFormat": ""
    },
    "_comment24": "# Adds an option for using custom formatters for the output of the %afkTime% placeholder.  The valid options are:",
    "_comment25": "# -- 'REGULAR' (yyyy-MM-dd_HH.mm.ss); which is the same format that Minecraft uses and is the Default config. (Default: REGULAR)",
    "_comment26": "# -- 'ISO_LOCAL' (yyyy-MM-ddTHH:mm:ss.n)",
    "_comment27": "# -- 'ISO_OFFSET' (yyyy-MM-ddTHH:mm:ss.nZ)",
    "_comment28": "# -- 'RFC1123' (EEE, dd MMM yyyy HH:mm:ss +HHMM)",
    "_comment29": "# -- 'FORMATTED'; (yyyy-MM-dd_HH.mm.ss); This is the same as 'REGULAR', but this option allows you to set your own custom",
    "_comment30": "#     formatters based on the \"java.time.format.DateTimeFormatter\" formatter syntax; see that package for more information.",
    "timeDate": {
      "option": "REGULAR",
      "customFormat": ""
    }
  },
  "message": {
    "_comment01": "# Enables chat messages when a player goes AFk or returns. (Default: true)",
    "enableMessages": true,
    "_comment02": "# The message content when a player goes AFK, and accepts formatting nodes",
    "_comment03": "#  (Default: \"%player:displayname% <yellow>is now AFK<r>\")",
    "whenAfk": "%player:displayname% <yellow>is now AFK<r>",
    "_comment04": "# An optional configuration for the \"whenAfk\" punctuation in between that, and the Duration / Reason that follows.",
    "_comment05": "# (Default: \"<yellow>,<r> \")",
    "whenAfkPunctuation": "<yellow>,<r> ",
    "_comment06": "# The messages content when a player returns from AFK, and accepts formatting nodes.",
    "_comment07": "# (Default: \"%player:displayname% <yellow>is no longer AFK<r>\")",
    "_comment08": "# --note; that this message gets the AFK duration added to the end if `displayDuration` is set to true.",
    "whenReturn": "%player:displayname% <yellow>is no longer AFK<r>",
    "_comment09": "# The prefix formatting for the \"whenReturn\" duration formatting portion.",
    "_comment10": "# (Default: \" <gray>(Gone for: <green>\")",
    "whenReturnDurationPrefix": " <gray>(Gone for: <green>",
    "_comment11": "# The suffix formatting for the \"whenReturn\" duration formatting portion.",
    "_comment12": "# (Default: \"<gray>)\")",
    "whenReturnDurationSuffix": "<gray>)",
    "_comment13": "# Default reason for going AFK via the /afk command.  Leave in a poof of smoke without having to give a reason.",
    "_comment14": "# (Default: \"<gray>poof!<r>\")",
    "_comment15": "# --note; \"\" is a valid setting, and will disable the default /afk reason.",
    "defaultReason": "<gray>poof!<r>",
    "_comment16": "# A configurable greeting when players repeatedly type '/afk' and expect a broken result.",
    "_comment17": "# (Default: \"<yellow>Welcome back, <r>%player:display_name%<r>, <yellow>did you miss anything fun?\")",
    "afkCooldownGreeting": "<yellow>Welcome back, <r>%player:display_name%<r>, <yellow>did you miss anything fun?",
    "_comment18": "# An general error condition sent when a player is marked as Vanished by \"melius-vanish\" maintained by DrexHD",
    "_comment19": "# (Default: \"<red>You are vanished, and probably shouldn't be doing that.<r>\")",
    "whileYourVanished": "<red>You are vanished, and probably shouldn't be doing that.<r>",
    "_comment20": "# An general error condition in the third person sent when a player is marked as Vanished by \"melius-vanish\" maintained by DrexHD",
    "_comment21": "# (Default: \"<red> is vanished, and probably shouldn't be doing that.<r>\")",
    "whileVanished": "<red> is vanished, and probably shouldn't be doing that.<r>",
    "_comment22": "# This enables the duration display portion of the whenReturn message.  See the \"duration\" configuration below.",
    "_comment23": "# (Default: true)",
    "displayDuration": true,
    "_comment24": "# Re-Formats the \"duration\" in chat messages and /afkinfo and 'whenReturn', etc; to a more human-readable format.",
    "_comment25": "# -- 'REGULAR' (HH:mm:ss.SSS)",
    "_comment26": "# -- 'PRETTY' (d' days 'H' hours 'm' minutes 's' seconds') (Default: PRETTY)",
    "_comment27": "# -- 'ISO_EXTENDED' ('P'yyyy'Y'M'M'd'DT'H'H'm'M's.SSS'S')",
    "_comment28": "# -- 'FORMATTED'; Same as ISO_EXTENDED, but this option allows you to set your own custom DurationFormatUtils Formatter,",
    "_comment29": "#     -->  SEE: \"org.apache.commons.lang3.time.DurationFormatUtils;\" for more information",
    "duration": {
      "option": "PRETTY",
      "customFormat": ""
    },
    "_comment30": "# Adds an option for using custom formatters for the output of the /afkinfo command.  The valid options are:",
    "_comment31": "# -- 'REGULAR' (yyyy-MM-dd_HH.mm.ss); which is the same format that Minecraft uses and is the Default config. (Default: REGULAR)",
    "_comment32": "# -- 'ISO_LOCAL' (yyyy-MM-ddTHH:mm:ss.n)",
    "_comment33": "# -- 'ISO_OFFSET' (yyyy-MM-ddTHH:mm:ss.nZ)",
    "_comment34": "# -- 'RFC1123' (EEE, dd MMM yyyy HH:mm:ss +HHMM)",
    "_comment35": "# -- 'FORMATTED'; (yyyy-MM-dd_HH.mm.ss); This is the same as 'REGULAR', but this option allows you to set your own custom",
    "_comment36": "#     formatters based on the \"java.time.format.DateTimeFormatter\" formatter syntax; see that package for more information.",
    "timeDate": {
      "option": "REGULAR",
      "customFormat": ""
    }
  }
}
```
