package com.github.zly2006.craftminefixes.mixin.minor;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.world.entity.monster.warden.Warden;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(Warden.class)
public class MixinWarden {
    @WrapOperation(
            method = "tick",
            at = @At(
                    value = "FIELD",
                    target = "Lnet/minecraft/world/entity/monster/warden/Warden;timeUntilRockets:I",
                    ordinal = 0,
                    opcode = Opcodes.GETFIELD
            )
    )
    private int fix(Warden instance, Operation<Integer> original) {
        return instance.timeUntilMobHead;
    }
}
