package com.github.zly2006.craftminefixes.mixin.minor;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ShieldItem;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ShieldItem.class)
public class MixinShieldItem extends Item {
    public MixinShieldItem(Properties properties) {
        super(properties);
    }

    @Inject(
            method = "use",
            at = @At(
                    value = "FIELD",
                    target = "Lnet/minecraft/world/InteractionResult;SUCCESS:Lnet/minecraft/world/InteractionResult$Success;"
            ),
            cancellable = true
    )
    private void fixResult(Level level, Player player, InteractionHand interactionHand, CallbackInfoReturnable<InteractionResult> cir) {
        cir.setReturnValue(super.use(level, player, interactionHand));
    }
}
