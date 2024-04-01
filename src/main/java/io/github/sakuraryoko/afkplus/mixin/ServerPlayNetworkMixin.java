package io.github.sakuraryoko.afkplus.mixin;

import static io.github.sakuraryoko.afkplus.config.ConfigManager.*;

import io.github.sakuraryoko.afkplus.data.ModData;
import net.fabricmc.api.EnvType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import io.github.sakuraryoko.afkplus.data.IAfkPlayer;
import io.github.sakuraryoko.afkplus.util.AfkPlusLogger;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Util;

@Mixin(ServerPlayNetworkHandler.class)
public abstract class ServerPlayNetworkMixin
{
    @Shadow
    public ServerPlayerEntity player;

    @Inject(method = "tick", at = @At("HEAD"))
    private void updateAfkStatus(CallbackInfo ci)
    {
        IAfkPlayer afkPlayer = (IAfkPlayer) player;
        int timeoutSeconds = CONFIG.packetOptions.timeoutSeconds;
        long afkDuration = Util.getMeasuringTimeMs() - this.player.getLastActionTime();
        if (afkPlayer.afkplus$isAfk() || timeoutSeconds <= 0)
        {
            if (CONFIG.packetOptions.afkKickEnabled && CONFIG.packetOptions.afkKickTimer > -1
                    && ModData.AFK_ENV.equals(EnvType.SERVER))
            {
                if ((afkPlayer.afkplus$isCreative() || afkPlayer.afkplus$isSpectator()) && !CONFIG.packetOptions.afkKickNonSurvival)
                    return;
                int kickTimeout = CONFIG.packetOptions.afkKickTimer + CONFIG.packetOptions.timeoutSeconds;
                if (afkDuration > (kickTimeout * 1000L))
                {
                    afkPlayer.afkplus$afkKick();
                }
            }
        }
        else if (afkPlayer.afkplus$isNoAfkEnabled())
        {
            return;
        }
        else
        {
            if (afkDuration > (timeoutSeconds * 1000L))
            {
                if (CONFIG.afkPlusOptions.afkTimeoutString.isEmpty())
                {
                    afkPlayer.afkplus$registerAfk("");
                }
                else
                {
                    afkPlayer.afkplus$registerAfk(CONFIG.afkPlusOptions.afkTimeoutString);
                }
                AfkPlusLogger.debug("Setting player " + this.player.getName().getString() + " as AFK (timeout)");
            }
        }
    }

    @Inject(method = "onPlayerMove", at = @At("HEAD"))
    private void checkPlayerLook(PlayerMoveC2SPacket packet, CallbackInfo ci)
    {
        if (CONFIG.packetOptions.resetOnLook && packet.changesLook())
        {
            float yaw = player.getYaw();
            float pitch = player.getPitch();
            if (pitch != packet.getPitch(pitch) || yaw != packet.getYaw(yaw))
                player.updateLastActionTime();
        }
    }
}
