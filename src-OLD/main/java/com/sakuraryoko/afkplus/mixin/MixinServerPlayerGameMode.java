package com.sakuraryoko.afkplus.mixin;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.level.ServerPlayerGameMode;
import net.minecraft.world.level.GameType;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.sakuraryoko.afkplus.data.IAfkPlayer;
import com.sakuraryoko.afkplus.util.AfkPlusLogger;

import static com.sakuraryoko.afkplus.config.ConfigManager.CONFIG;


@Mixin(ServerPlayerGameMode.class)
public abstract class MixinServerPlayerGameMode
{
    @Shadow
    @Final
    protected ServerPlayer player;

    @Inject(method = "changeGameModeForPlayer", at = @At("RETURN"))
    private void checkGameMode(GameType gameType, CallbackInfoReturnable<Boolean> cir)
    {
        IAfkPlayer afkPlayer = (IAfkPlayer) this.player;
        if (cir.getReturnValue())
        {
            AfkPlusLogger.debug("checkGameMode() -- Invoked for player " + afkPlayer.afkplus$getName() + " GameMode: " + gameType.getName());
            if (afkPlayer.afkplus$isAfk())
            {
                // Fixes uncommon de-sync when switching Game Modes.
                afkPlayer.afkplus$unregisterAfk();
                if (gameType == GameType.SURVIVAL)
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
