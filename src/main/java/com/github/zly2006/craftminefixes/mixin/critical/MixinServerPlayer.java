package com.github.zly2006.craftminefixes.mixin.critical;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayer.class)
public abstract class MixinServerPlayer {
    @Shadow
    public abstract ServerLevel serverLevel();
    @Shadow
    public abstract boolean isRevisiting();

    @Inject(
            method = "tick",
            at = @At("HEAD")
    )
    private void tick(CallbackInfo ci) {
        if (this.serverLevel().isMineCompleted() && !this.isRevisiting()) {
            this.serverLevel().handleCompletedMine();
        }
    }
}
