# AfkPlus

## Basic Description:
- A forked project based upon AfkDisplay by beabfc
- Show which players are AFK in the player list, see the Reasons, Durations, and Times.
- Fully configurable and with [Placeholder API](https://placeholders.pb4.eu/user/general/), which includes all Placeholder API formatting nodes such as: yellow or bold natively. (You can even include a URL in your AFK Reason!)

## Features :blush:
- '**/afkplus reload**' command.  This allows an Administrator to reload the configuration while the server is running. (Permission: afkplus.afkplus.reload)
- '**/afkplus set [Player] [Reason]**' command.  This allows any administrator to set the AFK status of a player. (Permission: afkplus.afkplus.set)
- '**/afkplus clear [Player]**' command.  This allows any administrator to clear the AFK status of a player. (Permission: afkplus.afkplus.clear)
- '**/afkplus info [Player]**' command.  This allows any administrator to check the AFK status of a player, and display the time and duration since they went AFK. (Permission: afkplus.afkplus.info)
- '**/afkplus update [Player]**' command.  This allows any administrator to force a player list update for a player. (Permission: afkplus.afkplus.update)
- '**/afkinfo [Player]**' command.  Does the same thing as '**/afkplus info [Player]**', but can be used for Mods, or players, or however you like to configure it for people to see. (Permission: afkplus.afkinfo)
- '**/afk [Reason]**' This allows any user to use a [Reason] along with setting their AKF status.
- **[PERMISSIONS]**: Security permissions for the '**/afkplus**' command via [Luck Permissions](https://luckperms.net/) with the AfkPlusCommandPermissions setting the default restrictions, the '**/afk**' and '**/afkinfo**' command also has restrictions using AfkCommandPermissions/AfkInfoCommandPermissions setting as well.
- Added a placeholder '**%afkplus:name%**' so that you can use this as a replacement for the '**%player:displayname%**' placeholder under other Mods, such as [Styled Playerlist](https://modrinth.com/mod/styledplayerlist "Styled Playerlist").
***NOTE that this method was designed to be fully compliant with LuckPerms Prefixes under Styled Playerlist, because the standard method for playerlist updating fails, or you can simply use %afkplus:afk% to add formatting to existing names if you like.***
- Placeholder '**%afkplus:duration%**' so that you can get the time since someone went AFK, with configuration for a format prefix.
- Placeholder '**%afkplus:time%**' so that you can get the Time/Date when someone went AFK, with configuration for a format prefix.
- Placeholder '**%afkplus:reason%**' so that you can port the Afk Reason for why someone went AFK, with configuration for a format prefix.
- Placeholder '**%afkplus:afk%**' for you to offer a very basic '**[AFK]**' tag to player's who are AFK.
- There is a special configuration option 'prettyDuration' to configure the AFK Duration in a more human-readable format, instead of the default (HH:mm:ss.mss) Apache format, each method has it's advantages though. ([Styled Nicknames](https://modrinth.com/mod/styled-nicknames) or [Styled Chat](https://modrinth.com/mod/styled-chat) comes to mind here)
- There are several mod / data pack conflict warnings for administrator's to help them make better decisions on what mods they want to install.

## Potential known conflicts (Make your choice)
- [afk display Data pack](https://vanillatweaks.net/picker/datapacks/) (Vanilla Tweaks Data pack, changes player list display) -- Mod checks for any "afk" containing data packs in the name.
- [AfkDisplay](https://modrinth.com/mod/afkdisplay) -- because this is the mod that AfkPlus is based upon, and offers less features.
- [SvrUtil](https://modrinth.com/mod/svrutil) (/afk command, rest is safe)
- [AntiLogout](https://modrinth.com/mod/noexits) (/afk command, timeout handling)
- [Sessility](https://modrinth.com/mod/sessility) (timeout handling)
- [Playtime-Tracker](https://modrinth.com/mod/playtime-tracker) (timeout handling)

## Example Configuration
The configuration is located in `afkplus.toml` inside your servers config folder.

```toml
[AfkPlusOptions]
# Allows you to disable the /afk command to mark yourself or other players (only for operators) as AFK (Default: true)
enableAfkCommand = true
# Allows you to disable the /afkinfo command to allow players to see someone's AFK status (Time, Duration). (Default: true)
enableAfkInfoCommand = true
# The /afk default command permissions, configurable with Luck Perms (afkplus.afk) node (Default: 0)
afkCommandPermissions = 0
# The /afkinfo default command permissions, configurable with Luck Perms (afkplus.afkinfo) node (Usually for Mods) (Default: 2)
afkInfoCommandPermissions = 2
# The /afkplus default command permissions, configurable with Luck Perms (afkplus.afkplus with subcommand nodes) node (Default: 3)
afkPlusCommandPermissions = 3
# The default "timeout" AFK reason (Default: "<i><gray>timeout<r>")
afkTimeoutString = "<i><gray>timeout<r>"

[packetOptions]
# The time without actions after which a player is considered AFK. Set to -1 to disable automatic AFK detection. (Default: 180)
timeoutSeconds = 180
# Consider players that moved no longer AFK (enables easy bypass methods like AFK pools) (Default: false)
resetOnMovement = false
# Consider players which looked around no longer AFK (Default: false)
resetOnLook = false

[PlaceholderOptions]
# This will be the value of the placeholder %afkplus:afk% if a player is AFK, option accepts full formatting nodes (Default: "<i><gray>[AFK]<r>")
#   -- note; *DOES NOT* place an "<r>" at the end in the code (trying not to modify its default behavior)
afkPlaceholder = "<i><gray>[AFK]<r>"
# Placeholder %afkplus:name% for backporting the entire %displayname% for use in other Mods, such as Styled Playerlist (Default: "<i><gray>[AFK] %player:displayname_unformatted%<r>")
#   -- note; *DOES NOT* places an "<r>" at the end in the code (trying not to modify its default behavior)
afkPlusNamePlaceholderAfk = "<i><gray>[AFK] %player:displayname_unformatted%<r>"
# Value for when player is NOT AFK, (ie. the default "%player:displayname%")
#   -- note; *DOES NOT* places an "<r>" at the end in the code (trying not to modify its default behavior)
afkPlusNamePlaceholder = "%player:displayname%"
# Adds a formatting prefix node for %afkplus:duration% (default: <green>) -- note; places an "<r>" at the end in the code.
afkDurationPlaceholderFormatting = "<green>"
# Adds a formatting prefix node for %afkplus:time% (default: <green>) -- note; places an "<r>" at the end in the code.
afkTimePlaceholderFormatting = "<green>"
# Adds a formatting prefix node for %afkplus:reason% (default: none) -- note; places an "<r>" at the end in the code.
afkReasonPlaceholderFormatting = ""
# Adds an option for using the "pretty" human readable output for the %duration% placeholder.  This might cause an issue depending on where you use it. (Default: false)
afkDurationPretty=false

[playerListOptions]
# Change the playerlist name for players who are AFK (Default: true)
enableListDisplay = true
# The name that is shown in the playerlist if a player is AFK, [ENHANCEMENT] now accepts formatting nodes (Default: "<i><gray>[AFK] %player:displayname%<r>") *NOTE that this function works when not using Player List mods!*
afkPlayerName = "<i><gray>[AFK] %player:displayname%<r>"

[messageOptions]
# Enabled chat messages when a player goes AFk or returns. (Default: true)
enableMessages = true
# The message content when a player goes AFK [ENHANCEMENT] now accepts formatting nodes (Default: "%player:displayname% <yellow>is now AFK<r>")
whenAfk = "%player:displayname% <yellow>is now AFK<r>"
# The messages content when a player returns from AFK [ENHANCEMENT] now accepts formatting nodes. (Default: "%player:displayname% <yellow>is no longer AFK<r>")
whenReturn = "%player:displayname% <yellow>is no longer AFK<r>"
# Re-Formats the "duration" in chat messages and /afkinfo to a more human legible format. (Default: true)
prettyDuration=true
# Default reason for going AFK via the /afk command. (Default: "<gray>poof!<r>")
defaultReason="<gray>poof!<r>"
```