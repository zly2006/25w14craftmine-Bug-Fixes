package com.github.zly2006.craftminefixes.mixin.critical;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.Parrot;
import net.minecraft.world.entity.animal.Rabbit;
import net.minecraft.world.level.mines.WorldEffects;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(WorldEffects.class)
public class MixinWorldEffects {
    @WrapOperation(
            method = "method_69980",
            at = @At(
                    value = "FIELD",
                    target = "Lnet/minecraft/world/entity/EntityType;PARROT:Lnet/minecraft/world/entity/EntityType;"
            )
    )
    private static EntityType<Rabbit> fixRabbit(Operation<EntityType<Parrot>> original) {
        return EntityType.RABBIT;
    }
}
