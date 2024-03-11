package io.github.sakuraryoko.afkplus.mixin;

import io.github.sakuraryoko.afkplus.data.IAfkPlayer;
import io.github.sakuraryoko.afkplus.util.AfkPlusLogger;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.gen.PhantomSpawner;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.Iterator;
import java.util.Random;

import static io.github.sakuraryoko.afkplus.config.ConfigManager.CONFIG;

@Mixin(PhantomSpawner.class)
public class PhantomSpawnerMixin {
    @Unique
    private IAfkPlayer player;
    @Inject(method = "spawn(Lnet/minecraft/server/world/ServerWorld;ZZ)I",
    at = @At(value = "INVOKE",
            target = "Lnet/minecraft/entity/player/PlayerEntity;getBlockPos()Lnet/minecraft/util/math/BlockPos;"),
            locals = LocalCapture.CAPTURE_FAILHARD
    )
    private void capturePlayerForMath(ServerWorld world, boolean spawnMonsters, boolean spawnAnimals, CallbackInfoReturnable<Integer> cir,
                                      Random random, int i, Iterator var6, PlayerEntity playerEntity) {
        ServerPlayerEntity serverPlayerEntity = (ServerPlayerEntity) playerEntity;
        player = (IAfkPlayer) serverPlayerEntity;
    }
    @ModifyArg(method = "spawn(Lnet/minecraft/server/world/ServerWorld;ZZ)I",
            at = @At(value = "INVOKE",
                    target = "Lnet/minecraft/util/math/MathHelper;clamp(III)I"),
                    index = 0)
    private int checkForAfkPlayer(int value) {
        if (player == null)
            return value;
        if (player.afkplus$isAfk() && CONFIG.packetOptions.bypassInsomnia) {
            if (value > 72000)
                AfkPlusLogger.info("Afk Player: "+player.afkplus$getName()+" was just spared from a phantom spawn chance.");
            AfkPlusLogger.debug("checkPhantomSpawn(): [Player: " + player.afkplus$getName() + "] obtained TIME_SINCE_REST value of " + value + " setting value to 1");
            return 1;
        } else {
            AfkPlusLogger.debug("checkPhantomSpawn(): [Player: " + player.afkplus$getName() + "] TIME_SINCE_REST has a value of " + value + " ");
            return value;
        }
    }
}
