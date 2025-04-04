package com.github.zly2006.craftminefixes.mixin;

import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(ServerPlayer.class)
public class MixinServerPlayerEntity {
    @WrapWithCondition(
            method = "hurtServer",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/server/level/ServerPlayer;hurtServer(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/damagesource/DamageSource;F)Z",
                    opcode = 0
            )
    )
    private boolean fixStackOverflow(
            ServerPlayer instance,
            ServerLevel serverLevel,
            net.minecraft.world.damagesource.DamageSource damageSource1,
            float f,
            @Local(argsOnly = true) DamageSource damageSource) {
        return !damageSource.is(DamageTypes.MAGIC);
    }
}
