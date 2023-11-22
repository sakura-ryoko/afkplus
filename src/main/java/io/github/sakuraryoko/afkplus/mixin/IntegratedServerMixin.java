package io.github.sakuraryoko.afkplus.mixin;

import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import io.github.sakuraryoko.afkplus.AfkPlusMod;
import net.minecraft.server.integrated.IntegratedServer;
import net.minecraft.world.GameMode;

@Mixin(IntegratedServer.class)
public abstract class IntegratedServerMixin {
    @Inject(method = "openToLan", at = @At("RETURN"))
    private void startLAN(@Nullable GameMode gameMode, boolean cheatsAllowed, int port,
            CallbackInfoReturnable<Boolean> ci) {
        if (!ci.getReturnValue()) {
            return;
        } else {
            AfkPlusMod.startServer();
        }
    }

}
