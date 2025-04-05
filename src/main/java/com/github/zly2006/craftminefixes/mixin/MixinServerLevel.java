package com.github.zly2006.craftminefixes.mixin;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.Attributes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.Consumer;

@Mixin(ServerLevel.class)
public class MixinServerLevel {
    @Inject(
            method = "respawnPlayerIntoHub",
            at = @At("HEAD")
    )
    private void respawnPlayerIntoHub(ServerPlayer serverPlayer, Consumer<ServerPlayer> consumer, CallbackInfo ci) {
        AttributeInstance attributeInstance = serverPlayer.getAttribute(Attributes.MAX_HEALTH);
        if (attributeInstance != null) {
            attributeInstance.removeModifier(ResourceLocation.withDefaultNamespace("one_hp"));
        }
    }
}
