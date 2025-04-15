package com.github.zly2006.craftminefixes.mixin.minor;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.mines.WorldEffects;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(WorldEffects.class)
public class MixinWorldEffects {
    @WrapOperation(
            method = "<clinit>",
            at = @At(
                    value = "FIELD",
                    target = "Lnet/minecraft/world/item/Items;ZOMBIFIED_PIGLIN_SPAWN_EGG:Lnet/minecraft/world/item/Item;",
                    ordinal = 1
            )
    )
    private static Item fixWitherSkeletonSpawnEgg(Operation<Item> original) {
        return Items.WITHER_SKELETON_SPAWN_EGG;
    }
}
