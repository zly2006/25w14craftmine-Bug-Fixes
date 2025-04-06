package com.github.zly2006.craftminefixes.mixin;

import com.github.zly2006.craftminefixes.CraftmineFixes;
import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.biome.Biome;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import java.util.Optional;

@Mixin(Holder.Reference.class)
public class MixinRegistryHolder {
    @Shadow private @Nullable ResourceKey<Object> key;

    @WrapMethod(
            method = "is(Lnet/minecraft/tags/TagKey;)Z"
    )
    private boolean is(TagKey tagKey, Operation<Boolean> original) {
        if (key.registry().getPath().contains("worldgen/biome") && key.location().getPath().contains("level")) {
            if (CraftmineFixes.server != null && CraftmineFixes.server.isRunning()) {
                var biome = CraftmineFixes.server.theGame().registryAccess().lookup(Registries.BIOME);
                if (biome.isPresent()) {
                    Optional<Holder.Reference<Biome>> value = biome.get().get(ResourceLocation.withDefaultNamespace(key.location().getPath().split("/")[1]));
                    if (value.isPresent()) {
                        return value.get().is(tagKey);
                    }
                }
            }
        }
        return original.call(tagKey);
    }
}
