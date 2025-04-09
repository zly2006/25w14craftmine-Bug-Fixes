package com.github.zly2006.craftminefixes.mixin.critical;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.players.PlayerUnlocks;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.AreaEffectCloud;
import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerUnlocks.class)
public class MixinPlayerUnlocks {
    @Inject(
            method = "method_69247",
            at = @At("HEAD"),
            cancellable = true
    )
    private static void dragonFire(ServerLevel serverLevel, ServerPlayer serverPlayer, DamageSource damageSource, Float float_, CallbackInfoReturnable<Boolean> cir) {
        if (!serverPlayer.isUnlocked(PlayerUnlocks.SORCERER_SUPREME)) {
            cir.setReturnValue(false);
            return;
        }
        if (damageSource.getDirectEntity() instanceof AreaEffectCloud && damageSource.getEntity() instanceof EnderDragon) {
            cir.setReturnValue(true);
            return;
        }
        cir.setReturnValue(false);
    }
}
