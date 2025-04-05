package com.github.zly2006.craftminefixes.mixin;

import net.minecraft.core.Holder;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.BiomeTags;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.mines.WorldEffects;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(WorldEffects.class)
public class MixinWorldEffects {
    /**
     * Ported from <a href="https://github.com/Le-Petit-C/25w14craftmineFix">Le-Petit-C</a>
     */
    @Inject(method = "method_70146", at = @At("HEAD"), cancellable = true)
    private static void fixStackOverflow(ServerLevel serverLevel, ServerPlayer serverPlayer, ItemStack itemStack, CallbackInfoReturnable<Boolean> cir) {
        if (itemStack.is(Items.LAVA_BUCKET)) {
            Holder<Biome> biome = serverLevel.getBiome(serverPlayer.blockPosition());
            if (biome.unwrapKey().get().registry().toString().contains("badland")) {
                cir.setReturnValue(true);
            }
        }
    }
}
