package com.github.zly2006.craftminefixes.mixin.fatal;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.inventory.MineCraftingMenu;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MineCraftingMenu.class)
public class MixinMineCraftingMenu {
    @Inject(
            method = "doClick",
            at = @At("HEAD"),
            cancellable = true
    )
    private void fixDrop(int i, int j, ClickType clickType, Player player, CallbackInfo ci) {
        if (clickType == ClickType.THROW) {
            ci.cancel();
        }
    }
}
