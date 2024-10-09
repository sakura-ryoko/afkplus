package com.sakuraryoko.afkplus.mixin;

import java.util.List;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.SleepManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.sakuraryoko.afkplus.data.IAfkPlayer;
import com.sakuraryoko.afkplus.util.AfkPlusLogger;

import static com.sakuraryoko.afkplus.config.ConfigManager.CONFIG;

@Mixin(SleepManager.class)
public class SleepManagerMixin
{
    @Shadow
    private int total;
    @Shadow
    private int sleeping;

    @Inject(method = "update(Ljava/util/List;)Z",
            at = @At(value = "INVOKE",
                    target = "Lnet/minecraft/server/network/ServerPlayerEntity;isSleeping()Z",
                    shift = At.Shift.BEFORE)
    )
    private void checkSleepCount(List<ServerPlayerEntity> players, CallbackInfoReturnable<Boolean> cir,
                                 @Local(ordinal = 0) int i, @Local(ordinal = 1) int j, @Local ServerPlayerEntity serverPlayerEntity)
    {
        // Count AFK Players into the total, they can't be marked as Sleeping, so don't increment that value.
        IAfkPlayer player = (IAfkPlayer) serverPlayerEntity;
        AfkPlusLogger.debug("checkSleepCount(): Current values i:" + i + " j:" + j + " // total: " + this.total + " sleeping: " + this.sleeping);
        if (player.afkplus$isAfk() && CONFIG.packetOptions.bypassSleepCount)
        {
            AfkPlusLogger.info("AFK Player: " + player.afkplus$getName() + " is being excluded from the sleep requirements.");
            --this.total;
        }
    }
}
