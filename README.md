# AfkPlus Fabric Mod for Minecraft

![Example](https://sakuraryoko.com/files/1260026/afkplus.jpg)

## Basic Description:
- A forked project based upon AfkDisplay by beabfc.  He has given me his blessing with proceeding to create this mod, look under my Archived Issues pane.
- Show which players are AFK in the player list, see the Reasons, Durations, and Times.
- Fully configurable and with [Placeholder API](https://placeholders.pb4.eu/user/general/), which includes all Placeholder API formatting nodes such as: yellow or bold natively. (You can even include a URL in your AFK Reason!)
- Mod can now be used on Single Player, and Integrated Servers (Open to LAN).

## Features :blush:
- Added a placeholder `%afkplus:afk%` for you to offer a very basic '**[AFK]**' tag to players who are AFK.
- Added a placeholder `%afkplus:name%`/`%afkplus:display_name%` so that you can use this as a replacement for the '**%player:displayname%**' placeholder under other Mods, such as [Styled Playerlist](https://modrinth.com/mod/styledplayerlist "Styled Playerlist").
***NOTE that this method was designed to be fully compliant with LuckPerms Prefixes under Styled Playerlist, because the standard method for playerlist updating fails, or you can simply use %afkplus:afk% to add formatting to existing names if you like.***
- Added a placeholder `%afkplus:duration%` so that you can get the time since someone went AFK, with configuration for a format prefix.
- Added a placeholder `%afkplus:time%` so that you can get the Time/Date when someone went AFK, with configuration for a format prefix.
- Added a placeholder `%afkplus:reason%` so that you can port the Afk Reason for why someone went AFK, with configuration for a format prefix.
- [NEW] Added a placeholder `%afkplus:invulnerable%` so that you can display the "Disable Damage" status for all users, similar to the "[AFK]" placeholder tag, but more or less an "add on".
- There is a special configuration option 'prettyDuration' to configure the AFK Duration in a more human-readable format, instead of the default (HH:mm:ss.mss) format.  Each method has its advantages though. ([Styled Nicknames](https://modrinth.com/mod/styled-nicknames) or [Styled Chat](https://modrinth.com/mod/styled-chat) comes to mind here)
- There are several mod / data pack conflict warnings for administrators to help them make better decisions on what mods they want to install.
- [NEW] Added a "disableDamage" configuration to make AFK players immune to damage after a 20-second cool down.  A new configurable server-wide message is now enforced by default when this occurs.
- [NEW] Added a "disableDamageCooldown" configuration to allow Administrators to adjust the default "timer" that is applied after a player goes AFK.  I highly recommend not setting this to '0', unless you don't think your players will abuse this privilege.
- [NEW] Added a "whenDamageDisabled" configuration so that you can customize the message displayed when your players are marked as invulnerable.
- [NEW] Added a "whenDamageEnabled" configuration so that you can customize the message displayed when your players are unmarked as invulnerable.
- [NEW] Now also checks for if players are in Spectator Mode, and not only Creative for managing your AFK/Disable Damage status.
- [NEW] Adds several color nodes that players can use for AFK Reasons.  See /afkex for a display example.
  - brown, cyan, dark_brown, dark_pink, light_blue, light_brown, light_gray, light_pink, lime, magenta, purple, salmon,
  - bluetiful, burnt_orange, canary, cool_mint, copper, powder_blue, royal_purple, shamrock, tickle_me_pink, ultramarine_blue
- [NEW] Added a "bypassSleepCount" configuration so that you can allow players marked as Afk to bypass the Sleeping Requirements.
- [NEW] Added a "bypassInsomnia" configuration so that you can allow players marked as Afk to block Phantom spawning.
- [NEW] Added a "/noafk" command for players to stop themselves from being marked as Afk.

## Commands (Permissions via [Luck Permissions](https://luckperms.net/) or the AfkPermissions configurations)
- '**/afkplus**' with the AfkPlusCommandPermissions (Default: 4) setting the default restrictions. (Permission: afkplus.afkplus)
  - Displays the Mod Version information.
- '**/afkplus reload**' command. (Permission: afkplus.afkplus.reload)
  - This allows an Administrator to reload the configuration while the server is running.
- '**/afkplus ex**' command.  (Permission: afkplus.afkplus.ex)
  - This displays a simple "formatting test" to show the user what basic text nodes are available to use, and this also allows them to use this for Copy/Paste by clicking on colors/formats.
- '**/afkplus set [Player] [Reason]**' command. (Permission: afkplus.afkplus.set)
  - This allows any administrator to set the AFK status of a player, and this also removes their NoAFK status. 
- '**/afkplus clear [Player]**' command. (Permission: afkplus.afkplus.clear)
  - This allows any administrator to clear the AFK status of a player.
- [NEW] '**/afkplus damage enable [Player]**' command. (Permission: afkplus.afkplus.damage.enable)
  - This allows an Administrator to force-enable an AFK player's ability to be damaged as long as they are connected.
- [NEW] '**/afkplus damage disable [Player]**' command. (Permission: afkplus.afkplus.damage.disable)
  - This allows an Administrator to revert a player's ability to use "Disable Damage" after it was forcefully removed.
- '**/afkplus info [Player]**' command. (Permission: afkplus.afkplus.info)
  - This allows any administrator to check the AFK status of a player, and display the time and duration since they went AFK.
- '**/afkplus update [Player]**' command. (Permission: afkplus.afkplus.update)
  - This allows any administrator to force a player list update for a player.
- '**/afkinfo [Player]**' with the AfkInfoCommandPermissions (Default: 2) setting the default restrictions, (Permission: afkplus.afkinfo)
  - Does the same thing as '**/afkplus info [Player]**', but can be used for Mods, or players, or however you like to configure it for people to see.
- '**/afkex**' with the AfkExCommandPermissions (Default: 0) setting the default restrictions.  (Permission: afkplus.afkex)
  - This displays a simple "formatting test" to show the user what basic text nodes are available to use, and this also allows them to use this for Copy/Paste by clicking on colors/formats.
- '**/afk [Reason]**' with the AfkCommandPermissions (Default: 0) setting the default restrictions.  (Permission: afkplus.afk)
  - This allows any user to use a [Reason] along with setting their AFK status.
- [NEW] '**/noafk**' with the noAfkCommandPermissions (Default: 0) setting the default restrictions.  (Permission: afkplus.noafk)
  - This allows any user to set themselves in a state where they will not go Afk based on the configured timeout value.

## Potential known conflicts (Make your choice)
- [afk display Data pack](https://vanillatweaks.net/picker/datapacks/) (Vanilla Tweaks Data pack, changes the player list display) -- Mod checks for any "afk" containing data packs in the name.
- [AfkDisplay](https://modrinth.com/mod/afkdisplay) -- because this is the mod that AfkPlus is based upon, and offers fewer features.
- [AntiLogout](https://modrinth.com/mod/noexits) (/afk command, timeout handling)
- [Sessility](https://modrinth.com/mod/sessility) (timeout handling)
- [Playtime-Tracker](https://modrinth.com/mod/playtime-tracker) (timeout handling)
- [SvrUtil](https://modrinth.com/mod/svrutil) (/afk command, the rest is safe)

## Example Configuration
The configuration is located in `afkplus.toml` inside your servers config folder.

```toml
[AfkPlusOptions]
# Allows you to disable the /afk command to mark yourself as AFK, with an optional [Reason] (Default: true)
enableAfkCommand = true
# Allows you to disable the /noafk command to mark yourself as NoAFK, which disables the timeout (Default: true)
enableNoAfkCommand = true
# Allows you to disable the /afkex command to allow players to see some default formattting nodes available. (Default: true)
enableAfkExCommand = true
# Allows you to disable the /afkinfo command to allow players to see someone's AFK status (Time, Duration, Reason). (Default: true)
enableAfkInfoCommand = true
# The /afk default command permissions, configurable with Luck Perms (afkplus.afk) node (Default: 0)
afkCommandPermissions = 0
# The /noafk default command permissions, configurable with Luck Perms (afkplus.noafk) node (Default: 0)
noAfkCommandPermissions = 0
# The /afkex default command permissions, configurable with Luck Perms (afkplus.afkex) node (Default: 0)
afkExCommandPermissions = 0
# The /afkinfo default command permissions, configurable with Luck Perms (afkplus.afkinfo) node (Usually for Mods) (Default: 2)
afkInfoCommandPermissions = 2
# The /afkplus default command permissions, configurable with Luck Perms (afkplus.afkplus with .subcommands) node (Default: 3)
afkPlusCommandPermissions = 3
# The default "timeout" AFK reason (Default: "<i><gray>timeout<r>")
afkTimeoutString = "<i><gray>timeout<r>"

[packetOptions]
# The time without actions after which a player is considered AFK. Set to -1 to disable automatic AFK detection. (Default: 240)
timeoutSeconds = 240
# Consider players that moved no longer AFK (enables easy bypass methods like AFK pools) (Default: false)
resetOnMovement = false
# Consider players which looked around no longer AFK (Default: false)
resetOnLook = false
# Disable damage after 20 seconds since a player went AFK (Default: false)
disableDamage = false
# Cooldown timer for enabling the "DisableDamage" feature. (Default: 15 seconds)
# - WARNING!  Be advised that settings this too low can encourage poor player behavior.
disableDamageCooldown = 15
# Makes it so that Afk players are not counted in the Sleep Percentage check (Default: true)
bypassSleepCount = true
# Makes it so that Afk Players block Phantom Spawning attempts. (Default: true)
bypassInsomnia = true

[PlaceholderOptions]
# This will be the value of the placeholder %afkplus:afk% if a player is AFK, option accepts full formatting nodes
# (Default: "<i><gray>[AFK%afkplus:invulnerable%]<r>")
# -- note; *DOES NOT* place an "<r>" at the end in the code (trying not to modify its default behavior)
afkPlaceholder = "<i><gray>[AFK%afkplus:invulnerable%]<r>"
# Placeholder %afkplus:name% for backporting the entire %displayname% for use in other Mods, such as Styled Playerlist
# (Default: "<i><gray>[AFK%afkplus:invulnerable%] %player:displayname_unformatted%<r>")
# -- note; *DOES NOT* places an "<r>" at the end in the code (trying not to modify its default behavior)
afkPlusNamePlaceholderAfk = "<i><gray>[AFK%afkplus:invulnerable%] %player:displayname_unformatted%<r>"
# Value for when a player is NOT AFK, (i.e., the default "%player:displayname%")
# -- note; *DOES NOT* places an "<r>" at the end in the code (trying not to modify its default behavior)
afkPlusNamePlaceholder = "%player:displayname%"
# Adds a formatting prefix node for %afkplus:duration% (default: <green>)
# -- note; places an "<r>" at the end in the code, and this is used during /afkinfo
afkDurationPlaceholderFormatting = "<green>"
# Adds a formatting prefix node for %afkplus:time% (default: <green>)
# -- note; places an "<r>" at the end in the code, and this is used during /afkinfo
afkTimePlaceholderFormatting = "<green>"
# Adds a formatting prefix node for %afkplus:reason%, I'm not sure why someone might want this enabled, because
# formatting your [Reason] yourself is fun. (default: none)
# -- note; places an "<r>" at the end of the code, and this is used during /afkinfo
afkReasonPlaceholderFormatting = ""
# Adds an option for using the "pretty" human-readable output for the %duration% placeholder.  This might cause unexpected
# issues depending on where you use the placeholder. (Default: false)
afkDurationPretty=false
# Adds an option to configure a basic "Add-On" placeholder for attaching to the "[AFK]" tag to mark when a player is
# marked as Invulnerable using %afkplus:invulnerable% (Default: ":<red>I<r>")
afkInvulnerablePlaceholder=":<red>I<r>"

[playerListOptions]
# Change the playerlist name for players who are AFK (Default: true)
# -- note; This is for when you're NOT using a player list display mods.
enableListDisplay = true
# The name that is shown in the playerlist if a player is AFK, and accepts formatting nodes
# (Default: "<i><gray>[AFK%afkplus:invulnerable%] %player:displayname%<r>")
# -- note; This function works best when not using Player List mods!*
afkPlayerName = "<i><gray>[AFK%afkplus:invulnerable%] %player:displayname%<r>"

[messageOptions]
# Enables chat messages when a player goes AFk or returns. (Default: true)
enableMessages = true
# The message content when a player goes AFK, and accepts formatting nodes (Default: "%player:displayname% <yellow>is now AFK<r>")
whenAfk = "%player:displayname% <yellow>is now AFK<r>"
# The messages content when a player returns from AFK, and accepts formatting nodes.
# (Default: "%player:displayname% <yellow>is no longer AFK<r>")
whenReturn = "%player:displayname% <yellow>is no longer AFK<r>"
# Re-Formats the "duration" in chat messages and /afkinfo to a more human-readable format. (Default: true)
prettyDuration=true
# Default reason for going AFK via the /afk command.  Leave in a poof of smoke without having to give a reason.
# (Default: "<gray>poof!<r>")
defaultReason="<gray>poof!<r>"
# The message content when an AFK player is marked as Invulnerable.
# (Default: "%player:displayname% <yellow>is marked as <red>Invulnerable.<r>")
whenDamageDisabled="%player:displayname% <yellow>is marked as <red>Invulnerable.<r>"
# The message content when an AFK player is no longer marked as Invulnerable.
# (Default: "%player:displayname% <yellow>is no longer <red>Invulnerable.<r>")
whenDamageEnabled="%player:displayname% <yellow>is no longer <red>Invulnerable.<r>"
```
