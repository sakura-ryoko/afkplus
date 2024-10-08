package com.sakuraryoko.afkplus.mixin;

import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.network.ServerPlayerInteractionManager;
import net.minecraft.world.GameMode;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.sakuraryoko.afkplus.data.IAfkPlayer;
import com.sakuraryoko.afkplus.util.AfkPlusLogger;

import static com.sakuraryoko.afkplus.config.ConfigManager.CONFIG;


@Mixin(ServerPlayerInteractionManager.class)
public abstract class ServerPlayerInteractionManagerMixin
{
    @Shadow
    @Final
    protected ServerPlayerEntity player;

    @Inject(method = "changeGameMode", at = @At("RETURN"))
    private void checkGameMode(GameMode gameMode, CallbackInfoReturnable<Boolean> cir)
    {
        IAfkPlayer afkPlayer = (IAfkPlayer) this.player;
        if (cir.getReturnValue())
        {
            AfkPlusLogger.debug("checkGameMode() -- Invoked for player " + afkPlayer.afkplus$getName() + " GameMode: " + gameMode.getName());
            if (afkPlayer.afkplus$isAfk())
            {
                // Fixes uncommon de-sync when switching Game Modes.
                afkPlayer.afkplus$unregisterAfk();
                if (gameMode == GameMode.SURVIVAL)
                {
                    if (CONFIG.packetOptions.disableDamage)
                    {
                        if (this.player.isInvulnerable())
                        {
                            this.player.setInvulnerable(false);
                        }
                        afkPlayer.afkplus$enableDamage();
                    }
                }
            }
        }
    }
}
