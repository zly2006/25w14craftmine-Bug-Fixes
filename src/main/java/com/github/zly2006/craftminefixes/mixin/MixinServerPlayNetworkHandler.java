package com.github.zly2006.craftminefixes.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.server.players.PlayerList;
import net.minecraft.world.level.portal.TeleportTransition;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Slice;

import java.util.Optional;

@Mixin(ServerGamePacketListenerImpl.class)
public class MixinServerPlayNetworkHandler {
    @Shadow public ServerPlayer player;

    @SuppressWarnings("resource")
    @WrapOperation(
            method = "handleClientCommand",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/server/players/PlayerList;respawn(Lnet/minecraft/server/level/ServerPlayer;ZLnet/minecraft/world/entity/Entity$RemovalReason;Ljava/util/Optional;)Lnet/minecraft/server/level/ServerPlayer;"
            ),
            slice = @Slice(
                    from = @At(
                            value = "INVOKE",
                            target = "Lnet/minecraft/server/level/ServerLevel;isMine()Z"
                    ),
                    to = @At(
                            value = "INVOKE",
                            target = "Lnet/minecraft/server/level/ServerPlayer;setGameMode(Lnet/minecraft/world/level/GameType;)Z"
                    )
            )
    )
    private ServerPlayer onRespawn(PlayerList instance, ServerPlayer serverPlayer, boolean alive, net.minecraft.world.entity.Entity.RemovalReason removalReason, Optional<TeleportTransition> optional, Operation<ServerPlayer> original) {
        Vec3 playerPos = player.position();
        if (playerPos.y() < player.serverLevel().getMinY()) {
            playerPos = new Vec3(playerPos.x(), player.serverLevel().getMinY() + 1, playerPos.z());
        }
        if (player.getY() > player.serverLevel().getMaxY()) {
            playerPos = new Vec3(playerPos.x(), player.serverLevel().getMaxY(), playerPos.z());
        }
        return original.call(
                instance,
                player,
                alive,
                removalReason,

                Optional.of(
                        new TeleportTransition(this.player.serverLevel(), playerPos, Vec3.ZERO, this.player.getXRot(), this.player.getYRot(), TeleportTransition.DO_NOTHING)
                )
        );
    }
}
