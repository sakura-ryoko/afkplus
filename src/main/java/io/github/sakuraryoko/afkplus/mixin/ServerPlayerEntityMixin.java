package io.github.sakuraryoko.afkplus.mixin;

import static io.github.sakuraryoko.afkplus.config.ConfigManager.CONFIG;

import eu.pb4.placeholders.PlaceholderAPI;
import eu.pb4.placeholders.TextParser;
import me.lucko.fabric.api.permissions.v0.Permissions;
import net.minecraft.network.MessageType;
import net.minecraft.world.GameMode;
import org.apache.commons.lang3.time.DurationFormatUtils;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import io.github.sakuraryoko.afkplus.data.IAfkPlayer;
import io.github.sakuraryoko.afkplus.util.AfkPlusLogger;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.network.packet.s2c.play.PlayerListS2CPacket;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Util;
import net.minecraft.world.World;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Objects;

@Mixin(ServerPlayerEntity.class)
public abstract class ServerPlayerEntityMixin extends Entity implements IAfkPlayer {
    @Shadow
    @Final
    public MinecraftServer server;
    @Shadow public abstract boolean isCreative();

    @Shadow public abstract boolean isSpectator();

    @Unique
    public ServerPlayerEntity player = (ServerPlayerEntity) (Object) this;
    @Unique
    private boolean isAfk = false;
    @Unique
    private long afkTimeMs = 0;
    @Unique
    private String afkTimeString = "";
    @Unique
    private String afkReason = "";
    @Unique
    private boolean isDamageEnabled = true;
    @Unique
    private boolean isLockDamageDisabled = false;
    @Unique
    private boolean noAfkEnabled = false;
    public ServerPlayerEntityMixin(EntityType<?> type, World world) {
        super(type, world);
    }
    @Unique
    public boolean afkplus$isAfk() { return this.isAfk; }
    @Unique
    public long afkplus$getAfkTimeMs() { return this.afkTimeMs; }
    @Unique
    public String afkplus$getAfkTimeString() { return this.afkTimeString; }
    @Unique
    public String afkplus$getAfkReason() { return this.afkReason; }
    @Unique
    public boolean afkplus$isDamageEnabled() { return this.isDamageEnabled; }
    @Unique
    public boolean afkplus$isLockDamageDisabled() { return this.isLockDamageDisabled; }
    @Unique
    public void afkplus$registerAfk(String reason) {
        if (afkplus$isAfk())
            return;
        setAfkTime();
        if (reason == null && CONFIG.messageOptions.defaultReason == null) {
            setAfkReason("<red>none");
        } else if (reason == null || reason.isEmpty()) {
            setAfkReason("<red>none");
            Text mess = PlaceholderAPI.parseText(TextParser.parse(CONFIG.messageOptions.whenAfk), Objects.requireNonNull(this.getServer()).getPlayerManager().getPlayer(this.getUuid()));

            //AfkPlusLogger.debug("registerafk-mess().toString(): " + mess.toString());
            sendAfkMessage(mess);
        } else {
            setAfkReason(reason);
            String mess1 = CONFIG.messageOptions.whenAfk + "<yellow>,<r> " + reason;
            Text mess2 = PlaceholderAPI.parseText(TextParser.parse(mess1), Objects.requireNonNull(this.getServer()).getPlayerManager().getPlayer(this.getUuid()));
            sendAfkMessage(mess2);
        }
        setAfk(true);
        if (CONFIG.packetOptions.disableDamage && CONFIG.packetOptions.disableDamageCooldown < 1)
            afkplus$disableDamage();
        afkplus$updatePlayerList();
    }
    @Unique
    public void afkplus$unregisterAfk() {
        if (!afkplus$isAfk()) {
            // Maybe it was called by PlayerManagerMixin?
            setAfk(false);
            clearAfkTime();
            clearAfkReason();
            return;
        }
        if (CONFIG.messageOptions.prettyDuration && CONFIG.messageOptions.displayDuration) {
            long duration = Util.getMeasuringTimeMs() - (this.afkTimeMs);
            String ret = CONFIG.messageOptions.whenReturn + " <gray>(Gone for: <green>"
                    + DurationFormatUtils.formatDurationWords(duration, true, true) + "<gray>)<r>";

            Text mess1 = TextParser.parse(ret);
            Text mess2 = PlaceholderAPI.parseText(mess1, Objects.requireNonNull(this.getServer()).getPlayerManager().getPlayer(this.getUuid()));
            sendAfkMessage(mess2);
        } else if (CONFIG.messageOptions.displayDuration) {
            long duration = Util.getMeasuringTimeMs() - (this.afkTimeMs);
            String ret = CONFIG.messageOptions.whenReturn + " <gray>(Gone for: <green>"
                    + DurationFormatUtils.formatDurationHMS(duration) + "<gray>)<r>";

            Text mess1 = TextParser.parse(ret);
            Text mess2 = PlaceholderAPI.parseText(mess1, Objects.requireNonNull(this.getServer()).getPlayerManager().getPlayer(this.getUuid()));
            sendAfkMessage(mess2);
        } else {
            String ret = CONFIG.messageOptions.whenReturn + "<r>";

            Text mess1 = TextParser.parse(ret);
            Text mess2 = PlaceholderAPI.parseText(mess1, Objects.requireNonNull(this.getServer()).getPlayerManager().getPlayer(this.getUuid()));
            sendAfkMessage(mess2);
        }
        setAfk(false);
        clearAfkTime();
        clearAfkReason();
        if (!afkplus$isDamageEnabled())
            afkplus$enableDamage();
        afkplus$updatePlayerList();
    }
    @Unique
    public void afkplus$updatePlayerList() {
        this.server.getPlayerManager().sendToAll(new PlayerListS2CPacket(PlayerListS2CPacket.Action.UPDATE_DISPLAY_NAME, player));
        AfkPlusLogger.debug("sending player list update for " + afkplus$getName());
    }
    @Unique
    public String afkplus$getName() {
        return player.getName().getString();
    }
    @Unique
    private void sendAfkMessage(Text text) {
        if (!CONFIG.messageOptions.enableMessages || text.getString().trim().isEmpty())
            return;
        server.sendSystemMessage(text, uuid);
        for (ServerPlayerEntity player : this.server.getPlayerManager().getPlayerList()) {
            player.sendMessage(text, MessageType.CHAT, uuid);
        }
    }

    @Unique
    private void setAfk(boolean isAfk) { this.isAfk = isAfk; }

    @Unique
    private void setAfkTime() {
        this.afkTimeMs = Util.getMeasuringTimeMs();
        this.afkTimeString = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH.mm.ss", Locale.ROOT).format(ZonedDateTime.now());
    }

    @Unique
    private void clearAfkTime() {
        this.afkTimeMs = 0;
        this.afkTimeString = "";
    }

    @Unique
    private void setAfkReason(String reason) {
        if (reason == null || reason.isEmpty()) {
            this.afkReason = "";
        } else {
            this.afkReason = reason;
        }
    }

    @Unique
    private void clearAfkReason() { this.afkReason = ""; }

    @Unique
    public void afkplus$disableDamage() {
        AfkPlusLogger.debug("disableDamage() has been invoked for: "+afkplus$getName());
        if (player.isCreative())
            return;
        if (player.isSpectator())
            return;
        if (!CONFIG.packetOptions.disableDamage)
            return;
        if (afkplus$isLockDamageDisabled()) {
            AfkPlusLogger.info("Disable Damage is locked from player: " + afkplus$getName());
            return;
        }
        if (afkplus$isAfk()) {
            if (afkplus$isDamageEnabled()) {
                this.isDamageEnabled = false;
                if (!player.isInvulnerable()) {
                    player.setInvulnerable(true);
                    AfkPlusLogger.info("Damage Disabled for player: " + afkplus$getName());
                }
                // Send announcement
                if (!CONFIG.messageOptions.whenDamageDisabled.isEmpty()) {
                    Text mess1 = TextParser.parse(CONFIG.messageOptions.whenDamageDisabled);
                    Text mess2 = PlaceholderAPI.parseText(mess1, Objects.requireNonNull(this.getServer()).getPlayerManager().getPlayer(this.getUuid()));
                    sendAfkMessage(mess2);
                }
            }
            afkplus$updatePlayerList();
        }
    }
    @Unique
    public void afkplus$enableDamage() {
        // Doesn't matter if they are marked as AFK --> make them not Invulnerable.
        AfkPlusLogger.debug("enableDamage() has been invoked for: "+afkplus$getName());
        if (player.isCreative())
            return;
        if (player.isSpectator())
            return;
        // They don't need to be AFK, and 'public' makes it so /afkplus damage [Player] works
        if (!afkplus$isDamageEnabled()) {
            this.isDamageEnabled = true;
            if (player.isInvulnerable()) {
                player.setInvulnerable(false);
                AfkPlusLogger.info("Damage Enabled for player: " + afkplus$getName());
            }
            // Send announcement
            if (!CONFIG.messageOptions.whenDamageEnabled.isEmpty()) {
                Text mess1 = TextParser.parse(CONFIG.messageOptions.whenDamageEnabled);
                Text mess2 = PlaceholderAPI.parseText(mess1, Objects.requireNonNull(this.getServer()).getPlayerManager().getPlayer(this.getUuid()));
                sendAfkMessage(mess2);
            }
        }
        afkplus$updatePlayerList();
    }
    @Unique
    public void afkplus$afkKick()
    {
        if (afkplus$isAfk() && CONFIG.packetOptions.afkKickEnabled)
        {
            if ((player.isCreative() || player.isSpectator()) && !CONFIG.packetOptions.afkKickNonSurvival)
            {
                return;
            }
            else if (Permissions.check(player, "afkplus.kick.safe", CONFIG.packetOptions.afkKickSafePermissions))
            {
                return;
            }
            else if (Permissions.check(player, "afkplus.afkplus", CONFIG.afkPlusOptions.afkPlusCommandPermissions))
            {
                return;
            }
            else
            {
                String kickReasonString;
                String kickMessageString;
                Text kickReason;
                Text kickMessage;

                if (CONFIG.messageOptions.whenKicked.isEmpty())
                    kickMessageString = "";
                else
                    kickMessageString = CONFIG.messageOptions.whenKicked;

                AfkPlusLogger.warn("Configured timeout has been reached for player "+ afkplus$getName() +" --> removing from server.");

                if (!CONFIG.messageOptions.afkKickMessage.isEmpty())
                {
                    if (CONFIG.messageOptions.displayDuration)
                    {
                        long afkDuration = Util.getMeasuringTimeMs() - (player.getLastActionTime());
                        if (CONFIG.messageOptions.prettyDuration)
                        {
                            kickReasonString = CONFIG.messageOptions.afkKickMessage + "\n <gray>(%player:displayname% was gone for: <green>"
                                    + DurationFormatUtils.formatDurationWords(afkDuration, true, true) + "<gray>)<r>";
                            if (!kickMessageString.isEmpty())
                            {
                                kickMessageString = kickMessageString + " <gray>(Gone for: <green>"
                                        + DurationFormatUtils.formatDurationWords(afkDuration, true, true) + "<gray>)<r>";
                            }
                        }
                        else
                        {
                            kickReasonString = CONFIG.messageOptions.afkKickMessage + "\n <gray>(%player:displayname% was gone for: <green>"
                                    + DurationFormatUtils.formatDurationHMS(afkDuration) + "<gray>)<r>";
                            if (!kickMessageString.isEmpty())
                            {
                                kickMessageString = kickMessageString + " <gray>(Gone for: <green>"
                                        + DurationFormatUtils.formatDurationHMS(afkDuration) + "<gray>)<r>";
                            }
                        }
                    }
                    else
                    {
                        kickReasonString = CONFIG.messageOptions.afkKickMessage;
                    }
                    kickReason = TextParser.parse(kickReasonString);
                    kickReason = PlaceholderAPI.parseText(kickReason, Objects.requireNonNull(this.getServer()).getPlayerManager().getPlayer(this.getUuid()));

                    setAfk(false);
                    clearAfkTime();
                    clearAfkReason();
                    if (!afkplus$isDamageEnabled())
                    {
                        afkplus$enableDamage();
                    }

                    player.networkHandler.disconnect(kickReason);

                    if (!kickMessageString.isEmpty())
                    {
                        kickMessage = TextParser.parse(kickMessageString);
                        kickMessage = PlaceholderAPI.parseText(kickMessage, Objects.requireNonNull(this.getServer()).getPlayerManager().getPlayer(this.getUuid()));

                        sendAfkMessage(kickMessage);
                    }
                }
                else
                {
                    kickReasonString = "<copper>AFK timeout<r>";

                    kickReason = TextParser.parse(kickReasonString);
                    kickReason = PlaceholderAPI.parseText(kickReason, Objects.requireNonNull(this.getServer()).getPlayerManager().getPlayer(this.getUuid()));

                    setAfk(false);
                    clearAfkTime();
                    clearAfkReason();
                    if (!afkplus$isDamageEnabled())
                    {
                        afkplus$enableDamage();
                    }

                    player.networkHandler.disconnect(kickReason);

                    if (!kickMessageString.isEmpty())
                    {
                        kickMessage = TextParser.parse(kickMessageString);
                        kickMessage = PlaceholderAPI.parseText(kickMessage, Objects.requireNonNull(this.getServer()).getPlayerManager().getPlayer(this.getUuid()));

                        sendAfkMessage(kickMessage);
                    }
                }
            }
        }
    }
    @Unique
    public void afkplus$lockDamageDisabled() { this.isLockDamageDisabled = true; }
    @Unique
    public void afkplus$unlockDamageDisabled() { this.isLockDamageDisabled = false; }
    @Unique
    public boolean afkplus$isCreative() { return this.isCreative(); }
    @Unique
    public boolean afkplus$isSpectator() { return this.isSpectator(); }
    @Unique
    public boolean afkplus$isNoAfkEnabled() { return this.noAfkEnabled; }
    @Unique
    public void afkplus$setNoAfkEnabled() { this.noAfkEnabled = true; }
    @Unique
    public void afkplus$unsetNoAfkEnabled() { this.noAfkEnabled = false; }
    @Inject(method = "updateLastActionTime", at = @At("TAIL"))
    private void onActionTimeUpdate(CallbackInfo ci) { afkplus$unregisterAfk(); }

    //@Override
    public void setPosition(double x, double y, double z) {
        if (CONFIG.packetOptions.resetOnMovement && (this.getX() != x || this.getY() != y || this.getZ() != z)) {
            player.updateLastActionTime();
        }
        super.setPosition(x, y, z);
    }

    /**
     * @author SakuraRyoko
     * @reason 1.17.1's call is broken, so we overwrite it.
     */
    @Overwrite
    public @Nullable Text getPlayerListName()
    {
        if (CONFIG.playerListOptions.enableListDisplay && this.afkplus$isAfk())
        {
            Text mess1 = TextParser.parse(CONFIG.playerListOptions.afkPlayerName);
            Text listEntry = PlaceholderAPI.parseText(mess1, Objects.requireNonNull(this.getServer()).getPlayerManager().getPlayer(this.getUuid()));
            AfkPlusLogger.debug("replacePlayerListName-listEntry(): " + listEntry);
            return listEntry;
        }
        else
        {
            Text mess1 = TextParser.parse("%afkplus:name%");
            return PlaceholderAPI.parseText(mess1, Objects.requireNonNull(this.getServer()).getPlayerManager().getPlayer(this.getUuid()));
        }
    }

    @Inject(method = "playerTick", at = @At("HEAD"))
    private void checkAfk(CallbackInfo ci) {
        try {
            if (this.player.isCreative())
                return;
            if (this.player.isSpectator())
                return;
            if (this.afkplus$isLockDamageDisabled()) {
                if (!this.afkplus$isDamageEnabled()) {
                    this.afkplus$enableDamage();
                    AfkPlusLogger.debug("checkAfk() - Damage Enabled for player: " + this.afkplus$getName() +" because they are [UNLOCKED]. step 1.");
                }
            }
            else if (this.afkplus$isAfk() && CONFIG.packetOptions.disableDamage) {
                if (this.afkplus$isDamageEnabled()) {
                    // Stop people from abusing the /afk command for 20 seconds to get out of a "sticky situation"
                    int cooldownSeconds = CONFIG.packetOptions.disableDamageCooldown;
                    if (cooldownSeconds > 0) {
                        long diff = Util.getMeasuringTimeMs() - this.afkTimeMs;
                        if (diff > cooldownSeconds * 1000L) {
                            this.afkplus$disableDamage();
                            AfkPlusLogger.debug("checkAfk() - Damage Disabled for player: " + this.afkplus$getName()+" step 2.");
                        }
                    } else {
                        if (!(this.player.interactionManager.getPreviousGameMode() == GameMode.CREATIVE)) {
                            this.afkplus$disableDamage();
                            AfkPlusLogger.debug("checkAfk() - Damage Disabled for player: " + this.afkplus$getName() + " step 4.");
                        }
                    }
                }
            } else {
                if (!this.afkplus$isDamageEnabled()) {
                    this.afkplus$enableDamage();
                    AfkPlusLogger.debug("checkAfk() - Damage Enabled for player: " + this.afkplus$getName()+" step 5.");
                }
            }
        } catch (Exception e) {
            // Sometimes the values are null, so offer a catch
            AfkPlusLogger.info("Caught exception during checkAfk(). ("+e.getMessage()+")");
        }
    }
}
