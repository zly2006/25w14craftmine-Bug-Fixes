package com.github.zly2006.craftminefixes.mixin;

import net.minecraft.world.level.mines.WorldEffect;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(WorldEffect.Builder.class)
public class MixinWorldEffectBuilder {
    /**
     * @author
     * @reason
     */
    @Overwrite
    public WorldEffect.Builder multiplayerOnly() {
        return (WorldEffect.Builder) (Object) this;
    }
}
