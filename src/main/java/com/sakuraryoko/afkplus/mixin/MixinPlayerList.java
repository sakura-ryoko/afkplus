package com.sakuraryoko.afkplus.mixin;

import com.mojang.authlib.GameProfile;
import net.minecraft.network.Connection;
import net.minecraft.server.level.ClientInformation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.CommonListenerCookie;
import net.minecraft.server.players.PlayerList;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.GameType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.sakuraryoko.afkplus.data.IAfkPlayer;
import com.sakuraryoko.afkplus.util.AfkPlusInfo;
import com.sakuraryoko.afkplus.util.AfkPlusLogger;

@Mixin(PlayerList.class)
public abstract class MixinPlayerList
{
    public MixinPlayerList()
    {
        super();
    }

    @Inject(method = "placeNewPlayer", at = @At("TAIL"))
    private void checkInvulnerable1(Connection connection, ServerPlayer player, CommonListenerCookie commonListenerCookie, CallbackInfo ci)
    {
        if (player == null)
        {
            return;
        }
        IAfkPlayer iPlayer = (IAfkPlayer) player;
        if (player.gameMode.getGameModeForPlayer() == GameType.SURVIVAL)
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

    @Inject(method = "getPlayerForLogin", at = @At("RETURN"))
    private void checkInvulnerable2(GameProfile gameProfile, ClientInformation clientInformation, CallbackInfoReturnable<ServerPlayer> cir)
    {
        ServerPlayer playerEntity = cir.getReturnValue();
        IAfkPlayer iPlayer = (IAfkPlayer) playerEntity;
        if (playerEntity == null)
        {
            return;
        }
        if (playerEntity.gameMode.getGameModeForPlayer() == GameType.SURVIVAL)
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

    @Inject(method = "respawn", at = @At("RETURN"))
    private void checkInvulnerable3(ServerPlayer player, boolean bl, Entity.RemovalReason removalReason, CallbackInfoReturnable<ServerPlayer> cir)
    {
        ServerPlayer playerEntity = cir.getReturnValue();
        if (playerEntity == null)
        {
            return;
        }
        if (playerEntity.gameMode.getGameModeForPlayer() == GameType.SURVIVAL)
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
        if (player.gameMode.getGameModeForPlayer() == GameType.SURVIVAL)
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
