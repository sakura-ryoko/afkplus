package com.sakuraryoko.afkplus.mixin;

import com.llamalad7.mixinextras.sugar.Local;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.levelgen.PhantomSpawner;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.sakuraryoko.afkplus.data.IAfkPlayer;
import com.sakuraryoko.afkplus.util.AfkPlusLogger;

import static com.sakuraryoko.afkplus.config.ConfigManager.CONFIG;

@Mixin(PhantomSpawner.class)
public class MixinPhantomSpawner
{
    @Unique
    private IAfkPlayer player;

    @Inject(method = "tick(Lnet/minecraft/server/level/ServerLevel;ZZ)I",
            at = @At(value = "INVOKE",
                     target = "Lnet/minecraft/server/level/ServerPlayer;isSpectator()Z")
    )
    private void capturePlayerForMath(ServerLevel world, boolean spawnMonsters, boolean spawnAnimals, CallbackInfoReturnable<Integer> cir,
                                      @Local ServerPlayer serverPlayerEntity)
    {
        player = (IAfkPlayer) serverPlayerEntity;
    }

    @ModifyArg(method = "tick(Lnet/minecraft/server/level/ServerLevel;ZZ)I",
               at = @At(value = "INVOKE",
                        target = "Lnet/minecraft/util/Mth;clamp(III)I"),
               index = 0)
    private int checkForAfkPlayer(int value)
    {
        if (player == null)
        {
            return value;
        }
        if (player.afkplus$isAfk() && CONFIG.packetOptions.bypassInsomnia)
        {
            if (value > 72000)
            {
                AfkPlusLogger.info("Afk Player: " + player.afkplus$getName() + " was just spared from a phantom spawn chance.");
            }
            AfkPlusLogger.debug("checkPhantomSpawn(): [Player: " + player.afkplus$getName() + "] obtained TIME_SINCE_REST value of " + value + " setting value to 1");
            return 1;
        }
        else
        {
            AfkPlusLogger.debug("checkPhantomSpawn(): [Player: " + player.afkplus$getName() + "] TIME_SINCE_REST has a value of " + value + " ");
            return value;
        }
    }
}
