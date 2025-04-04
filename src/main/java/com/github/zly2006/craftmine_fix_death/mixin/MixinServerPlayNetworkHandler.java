package com.github.zly2006.craftmine_fix_death.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.entity.Entity;
import net.minecraft.server.PlayerManager;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.TeleportTarget;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Slice;

import java.util.Optional;

@Mixin(ServerPlayNetworkHandler.class)
public class MixinServerPlayNetworkHandler {
    @Shadow public ServerPlayerEntity player;

    @WrapOperation(
            method = "onClientStatus",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/server/PlayerManager;respawnPlayer(Lnet/minecraft/server/network/ServerPlayerEntity;ZLnet/minecraft/entity/Entity$RemovalReason;Ljava/util/Optional;)Lnet/minecraft/server/network/ServerPlayerEntity;"
            ),
            slice = @Slice(
                    from = @At(
                            value = "INVOKE",
                            target = "Lnet/minecraft/server/world/ServerWorld;method_69112()Z"
                    ),
                    to = @At(
                            value = "INVOKE",
                            target = "Lnet/minecraft/server/network/ServerPlayerEntity;changeGameMode(Lnet/minecraft/world/GameMode;)Z"
                    )
            )
    )
    private ServerPlayerEntity onRespawn(PlayerManager instance, ServerPlayerEntity player, boolean alive, Entity.RemovalReason removalReason, Optional<TeleportTarget> optional, Operation<ServerPlayerEntity> original) {
        Vec3d playerPos = player.getPos();
        if (playerPos.getY() < player.getServerWorld().getBottomY()) {
            playerPos = new Vec3d(playerPos.getX(), player.getServerWorld().getBottomY() + 1, playerPos.getZ());
        }
        if (player.getY() > player.getServerWorld().getBottomY() + player.getServerWorld().getHeight()) {
            playerPos = new Vec3d(playerPos.getX(), player.getServerWorld().getBottomY() + player.getServerWorld().getHeight(), playerPos.getZ());
        }
        return original.call(
                instance,
                player,
                alive,
                removalReason,

                Optional.of(
                        new TeleportTarget(this.player.getServerWorld(), playerPos, Vec3d.ZERO, this.player.getYaw(), this.player.getPitch(), TeleportTarget.NO_OP)
                )
        );
    }
}
