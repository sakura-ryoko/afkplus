package io.github.sakuraryoko.afkplus.mixin;

import static io.github.sakuraryoko.afkplus.config.ConfigManager.CONFIG;

import org.apache.commons.lang3.time.DurationFormatUtils;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import eu.pb4.placeholders.api.PlaceholderContext;
import eu.pb4.placeholders.api.Placeholders;
import eu.pb4.placeholders.api.TextParserUtils;
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

import java.util.concurrent.TimeUnit;

@Mixin(ServerPlayerEntity.class)
public abstract class ServerPlayerMixin extends Entity implements IAfkPlayer {
    @Shadow
    @Final
    public MinecraftServer server;

    @Unique
    public ServerPlayerEntity afkplus$player = (ServerPlayerEntity) (Object) this;
    @Unique
    private boolean afkplus$isAfk;
    @Unique
    private long afkplus$afkTimeMs;
    @Unique
    private String afkplus$afkTimeString;
    @Unique
    private String afkplus$afkReason;

    public ServerPlayerMixin(EntityType<?> type, World world) {
        super(type, world);
    }

    @Unique
    public boolean afkplus$isAfk() {
        return this.afkplus$isAfk;
    }

    @Unique
    public long afkplus$getAfkTimeMs() {
        return this.afkplus$afkTimeMs;
    }

    @Unique
    public String afkplus$getAfkTimeString() {
        return this.afkplus$afkTimeString;
    }

    @Unique
    public String afkplus$getAfkReason() {
        return this.afkplus$afkReason;
    }

    @Unique
    public void afkplus$registerAfk(String reason) {
        if (afkplus$isAfk())
            return;
        setAfkTime();
        if (reason == null && CONFIG.messageOptions.defaultReason == null) {
            setAfkReason("<red>none");
        } else if (reason == null || reason.isEmpty()) {
            setAfkReason("<red>none");
            Text mess = Placeholders.parseText(TextParserUtils.formatTextSafe(CONFIG.messageOptions.whenAfk),
                    PlaceholderContext.of(this));
            //AfkPlusLogger.debug("registerafk-mess().toString(): " + mess.toString());
            sendAfkMessage(mess);
        } else {
            setAfkReason(reason);
            String input = CONFIG.messageOptions.whenAfk + "<yellow>,<r> " + reason;
            //AfkPlusLogger.debug("registerafk-input: " + input);
            //Text mess1 = TextParserUtils.formatTextSafe(input);
            //AfkPlusLogger.debug("registerafk-mess1().toString(): " + mess1.toString());
            Text mess2 = Placeholders.parseText(TextParserUtils.formatTextSafe(input), PlaceholderContext.of(this));
            //AfkPlusLogger.debug("registerafk-mess2().toString(): " + mess2.toString());
            sendAfkMessage(mess2);
        }
        setAfk(true);
//        if (CONFIG.packetOptions.disableDamage)
//            this.afkplus$disableDamage = true;
    }

    @Unique
    public void afkplus$unregisterAfk() {
        if (!afkplus$isAfk)
            return;
        if (CONFIG.messageOptions.prettyDuration) {
            long duration = Util.getMeasuringTimeMs() - (this.afkplus$afkTimeMs);
            String ret = CONFIG.messageOptions.whenReturn + " <gray>(Gone for: <green>"
                    + DurationFormatUtils.formatDurationWords(duration, true, true) + "<gray>)<r>";
            //AfkPlusLogger.debug("unregisterAfk-ret: " + ret);
            Text mess1 = TextParserUtils.formatTextSafe(ret);
            //AfkPlusLogger.debug("unregisterafk-mess1().toString(): " + mess1.toString());
            Text mess2 = Placeholders.parseText(mess1, PlaceholderContext.of(this));
            //AfkPlusLogger.debug("unregisterafk-mess2().toString(): " + mess2.toString());
            sendAfkMessage(mess2);
        } else {
            long duration = Util.getMeasuringTimeMs() - (this.afkplus$afkTimeMs);
            String ret = CONFIG.messageOptions.whenReturn + " <gray>(Gone for: <green>"
                    + DurationFormatUtils.formatDurationHMS(duration) + "<gray>)<r>";
            //AfkPlusLogger.debug("unregisterAfk-ret: " + ret);
            Text mess1 = TextParserUtils.formatTextSafe(ret);
            //AfkPlusLogger.debug("unregisterafk-mess1().toString(): " + mess1.toString());
            Text mess2 = Placeholders.parseText(mess1, PlaceholderContext.of(this));
            //AfkPlusLogger.debug("unregisterafk-mess2().toString(): " + mess2.toString());
            sendAfkMessage(mess2);
        }
        setAfk(false);
        clearAfkTime();
        clearAfkReason();
//        if (afkplus$disableDamage)
//            this.afkplus$disableDamage = false;
    }

    @Unique
    public void afkplus$updatePlayerList() {
        this.server
                .getPlayerManager()
                .sendToAll(new PlayerListS2CPacket(PlayerListS2CPacket.Action.UPDATE_DISPLAY_NAME, afkplus$player));
        AfkPlusLogger.debug("sending player list update for " + afkplus$player.getName());

    }

    @Unique
    private void sendAfkMessage(Text text) {
        if (!CONFIG.messageOptions.enableMessages || text.getString().trim().isEmpty())
            return;
        server.sendMessage(text);
        for (ServerPlayerEntity player : this.server.getPlayerManager().getPlayerList()) {
            player.sendMessage(text);
        }
    }

    @Unique
    private void setAfk(boolean isAfk) {
        this.afkplus$isAfk = isAfk;
        this.server
                .getPlayerManager()
                .sendToAll(new PlayerListS2CPacket(PlayerListS2CPacket.Action.UPDATE_DISPLAY_NAME, afkplus$player));
    }

    @Unique
    private void setAfkTime() {
        this.afkplus$afkTimeMs = Util.getMeasuringTimeMs();
        this.afkplus$afkTimeString = Util.getFormattedCurrentTime();
    }

    @Unique
    private void clearAfkTime() {
        this.afkplus$afkTimeMs = 0;
        this.afkplus$afkTimeString = "";
    }

    @Unique
    private void setAfkReason(String reason) {
        if (reason == null || reason.isEmpty()) {
            this.afkplus$afkReason = "<red>none";
        } else {
            this.afkplus$afkReason = reason;
        }
    }

    @Unique
    private void clearAfkReason() {
        this.afkplus$afkReason = "";
    }

    @Inject(method = "updateLastActionTime", at = @At("TAIL"))
    private void onActionTimeUpdate(CallbackInfo ci) {
        afkplus$unregisterAfk();
    }

    public void setPosition(double x, double y, double z) {
        if (CONFIG.packetOptions.resetOnMovement && (this.getX() != x || this.getY() != y || this.getZ() != z)) {
            afkplus$player.updateLastActionTime();
        }
        super.setPosition(x, y, z);
    }

    @Inject(method = "getPlayerListName", at = @At("RETURN"), cancellable = true)
    private void replacePlayerListName(CallbackInfoReturnable<Text> cir) {
        if (CONFIG.playerListOptions.enableListDisplay && afkplus$isAfk) {
            Text listEntry = Placeholders.parseText(
                    TextParserUtils.formatTextSafe(CONFIG.playerListOptions.afkPlayerName),
                    PlaceholderContext.of(this));
            //AfkPlusLogger.debug("replacePlayerListName-listEntry().toString(): " + listEntry.toString());
            cir.setReturnValue(listEntry.copy());
        }
    }

    @Inject(method = "playerTick", at = @At("HEAD"))
    private void checkAfk(CallbackInfo ci) {
        try {
            if (this.afkplus$isAfk && CONFIG.packetOptions.disableDamage) {
                if (!this.isInvulnerable()) {
                    // Stop people from abusing the /afk command for 20 seconds to get out of a "sticky situation"
                    long diff = TimeUnit.MILLISECONDS.toSeconds(Util.getMeasuringTimeMs() - this.afkplus$afkTimeMs);
                    if (diff > 20)
                        this.setInvulnerable(true);
                }
            } else {
                if (this.isInvulnerable())
                    this.setInvulnerable(false);
            }
        } catch (Exception e) {
            // Sometimes the values are null, so offer a catch
            return;
        }
    }
}
