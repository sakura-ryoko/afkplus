package com.sakuraryoko.afkplus.mixin;

import net.minecraft.entity.Entity;
import net.minecraft.network.ClientConnection;
import net.minecraft.network.packet.c2s.common.SyncedClientOptions;
import net.minecraft.server.PlayerManager;
import net.minecraft.server.network.ConnectedClientData;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.world.GameMode;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.mojang.authlib.GameProfile;

import com.sakuraryoko.afkplus.data.IAfkPlayer;
import com.sakuraryoko.afkplus.util.AfkPlusInfo;
import com.sakuraryoko.afkplus.util.AfkPlusLogger;

@Mixin(PlayerManager.class)
public abstract class PlayerManagerMixin
{
    public PlayerManagerMixin()
    {
        super();
    }

    @Inject(method = "onPlayerConnect", at = @At("TAIL"))
    private void checkInvulnerable1(ClientConnection connection, ServerPlayerEntity player, ConnectedClientData clientData, CallbackInfo ci)
    {
        if (player == null)
        {
            return;
        }
        IAfkPlayer iPlayer = (IAfkPlayer) player;
        if (player.interactionManager.getGameMode() == GameMode.SURVIVAL)
        {
            if (player.isInvulnerable())
            {
                player.setInvulnerable(false);
                AfkPlusLogger.info("PlayerManager().onPlayerConnect() -> Marking SURVIVAL player: " + iPlayer.afkplus$getName() + " as vulnerable.");
            }
        }
        // This might simply initialize a player entry...
        iPlayer.afkplus$unregisterAfk();
        // Fixes some quirky-ness of Styled Player List
        if (AfkPlusInfo.isServer())
        {
            iPlayer.afkplus$updatePlayerList();
        }
    }

    @Inject(method = "createPlayer", at = @At("RETURN"))
    private void checkInvulnerable2(GameProfile profile, SyncedClientOptions syncedOptions, CallbackInfoReturnable<ServerPlayerEntity> cir)
    {
        ServerPlayerEntity playerEntity = cir.getReturnValue();
        IAfkPlayer iPlayer = (IAfkPlayer) playerEntity;
        if (playerEntity == null)
        {
            return;
        }
        if (playerEntity.interactionManager.getGameMode() == GameMode.SURVIVAL)
        {
            if (playerEntity.isInvulnerable())
            {
                playerEntity.setInvulnerable(false);
                AfkPlusLogger.info("PlayerManager().createPlayer() -> Marking SURVIVAL player: " + iPlayer.afkplus$getName() + " as vulnerable.");
            }
        }
        // This might simply initialize a player entry...
        iPlayer.afkplus$unregisterAfk();
        // Fixes some quirky-ness of Styled Player List
//        if (AfkPlusInfo.isServer())
//            iPlayer.afkplus$updatePlayerList();
    }

    @Inject(method = "respawnPlayer", at = @At("RETURN"))
    private void checkInvulnerable3(ServerPlayerEntity player, boolean alive, Entity.RemovalReason removalReason, CallbackInfoReturnable<ServerPlayerEntity> cir)
    {
        ServerPlayerEntity playerEntity = cir.getReturnValue();
        if (playerEntity == null)
        {
            return;
        }
        if (playerEntity.interactionManager.getGameMode() == GameMode.SURVIVAL)
        {
            if (playerEntity.isInvulnerable())
            {
                IAfkPlayer iPlayer = (IAfkPlayer) playerEntity;
                AfkPlusLogger.info("PlayerManager().repsawnPlayer() -> Marking SURVIVAL player: " + iPlayer.afkplus$getName() + " as vulnerable.");
                playerEntity.setInvulnerable(false);
                // This might simply initialize a player entry...
                iPlayer.afkplus$unregisterAfk();
                // Fixes some quirky-ness of Styled Player List
                if (AfkPlusInfo.isServer())
                {
                    iPlayer.afkplus$updatePlayerList();
                }
            }
        }
        if (player.interactionManager.getGameMode() == GameMode.SURVIVAL)
        {
            if (player.isInvulnerable())
            {
                IAfkPlayer iPlayer = (IAfkPlayer) player;
                AfkPlusLogger.info("PlayerManager().repsawnPlayer() -> Marking SURVIVAL player: " + iPlayer.afkplus$getName() + " as vulnerable.");
                player.setInvulnerable(false);
                // This might simply initialize a player entry...
                iPlayer.afkplus$unregisterAfk();
                // Fixes some quirky-ness of Styled Player List
                if (AfkPlusInfo.isServer())
                {
                    iPlayer.afkplus$updatePlayerList();
                }
            }
        }
    }
}
