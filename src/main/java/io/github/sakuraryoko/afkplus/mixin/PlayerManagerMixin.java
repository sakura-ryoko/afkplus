package io.github.sakuraryoko.afkplus.mixin;

import com.mojang.authlib.GameProfile;
import io.github.sakuraryoko.afkplus.data.IAfkPlayer;
import io.github.sakuraryoko.afkplus.util.AfkPlusLogger;
import net.minecraft.server.PlayerManager;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.world.GameMode;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerManager.class)
public abstract class PlayerManagerMixin {
    public PlayerManagerMixin() {
        super();
    }
    @Inject(method = "createPlayer", at = @At("RETURN"))
    private void checkInvulnerable1(GameProfile profile, CallbackInfoReturnable<ServerPlayerEntity> cir) {
        ServerPlayerEntity playerEntity = cir.getReturnValue();
        if (playerEntity == null)
            return;
        if (playerEntity.interactionManager.getGameMode() == GameMode.SURVIVAL) {
            if (playerEntity.isInvulnerable()) {
                playerEntity.setInvulnerable(false);
                AfkPlusLogger.info("PlayerManager().createPlayer() -> Marking SURVIVAL player: "+playerEntity.getName()+" as vulnerable.");
            }
        }
        // This might simply initialize a player entry...
        IAfkPlayer iPlayer = (IAfkPlayer) playerEntity;
        iPlayer.afkplus$unregisterAfk();
    }
    @Inject(method = "respawnPlayer", at = @At("RETURN"))
    private void checkInvulnerable2(ServerPlayerEntity player, boolean alive, CallbackInfoReturnable<ServerPlayerEntity> cir) {
        ServerPlayerEntity playerEntity = cir.getReturnValue();
        if (playerEntity == null)
            return;
        if (playerEntity.interactionManager.getGameMode() == GameMode.SURVIVAL) {
            if (playerEntity.isInvulnerable()) {
                AfkPlusLogger.info("PlayerManager().repsawnPlayer() -> Marking SURVIVAL player: "+playerEntity.getName()+" as vulnerable.");
                playerEntity.setInvulnerable(false);
            }
        }
        if (player.interactionManager.getGameMode() == GameMode.SURVIVAL) {
            if (player.isInvulnerable()) {
                AfkPlusLogger.info("PlayerManager().repsawnPlayer() -> Marking SURVIVAL player: "+player.getName()+" as vulnerable.");
                player.setInvulnerable(false);
            }
        }
        // This might simply initialize a player entry...
        IAfkPlayer iPlayer = (IAfkPlayer) playerEntity;
        iPlayer.afkplus$unregisterAfk();
    }
}
