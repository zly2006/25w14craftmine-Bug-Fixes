package com.github.zly2006.craftminefixes.mixin.minor;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.mines.WorldEffect;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

import java.util.List;
import java.util.function.Consumer;

@Mixin(WorldEffect.Builder.class)
public class MixinWorldEffectBuilder {
    @Shadow @Final private List<Consumer<ServerLevel>> onMineTick;

    /**
     * @author
     * @reason
     */
    @Overwrite
    public WorldEffect.Builder multiplayerOnly() {
        return (WorldEffect.Builder) (Object) this;
    }

    @WrapOperation(
            method = "build",
            at = @At(
                    value = "FIELD",
                    target = "Lnet/minecraft/world/level/mines/WorldEffect$Builder;onMineEnter:Ljava/util/List;",
                    ordinal = 2
            )
    )
    private List<Consumer<ServerLevel>> build(WorldEffect.Builder instance, Operation<List<Consumer<ServerLevel>>> original) {
        return onMineTick;
    }
}
