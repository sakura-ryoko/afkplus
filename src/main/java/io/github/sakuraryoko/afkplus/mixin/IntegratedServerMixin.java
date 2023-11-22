package io.github.sakuraryoko.afkplus.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import io.github.sakuraryoko.afkplus.events.ServerEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.server.integrated.IntegratedServer;

@Mixin(IntegratedServer.class)
public abstract class IntegratedServerMixin {
    private MinecraftClient cli = MinecraftClient.getInstance();
    private IntegratedServer s;

    @Inject(method = "setupServer", at = @At("TAIL"))
    private void startServer(CallbackInfoReturnable<Boolean> ci) {
        if (ci.isCancelled())
            return;
        s = cli.isIntegratedServerRunning() ? cli.getServer() : null;
        ServerEvents.integratedServerStart(s);
    }

    @Inject(method = "stop", at = @At("HEAD"))
    private void stopServer(CallbackInfo ci) {
        if (ci.isCancelled())
            return;
        s = cli.isIntegratedServerRunning() ? cli.getServer() : null;
        ServerEvents.integratedServerStopping(s);
    }

}
