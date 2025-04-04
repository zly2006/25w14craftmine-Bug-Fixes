package com.github.zly2006.craftmine_fix_death.mixin;

import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.damage.DamageTypes;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(ServerPlayerEntity.class)
public class MixinServerPlayerEntity {
    @WrapWithCondition(
            method = "damage",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/server/network/ServerPlayerEntity;damage(Lnet/minecraft/server/world/ServerWorld;Lnet/minecraft/entity/damage/DamageSource;F)Z",
                    opcode = 0
            )
    )
    private boolean fixStackOverflow(ServerPlayerEntity instance, ServerWorld world, DamageSource source, float amount) {
        return !source.isOf(DamageTypes.MAGIC);
    }
}
