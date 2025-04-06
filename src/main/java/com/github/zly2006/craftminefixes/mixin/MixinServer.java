package com.github.zly2006.craftminefixes.mixin;

import com.github.zly2006.craftminefixes.CraftmineFixes;
import net.minecraft.server.MinecraftServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecraftServer.class)
public class MixinServer {
    @Inject(
            method = "runServer",
            at = @At("HEAD")
    )
    private void initGame(CallbackInfo ci) {
    }
}
