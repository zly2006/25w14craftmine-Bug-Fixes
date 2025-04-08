package com.github.zly2006.craftminefixes.mixin.minor;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.inventory.ResultSlot;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.item.crafting.RecipeType;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import java.util.Optional;

@Mixin(ResultSlot.class)
public abstract class MixinResultSlot extends Slot {
    @Shadow
    @Final
    private CraftingContainer craftSlots;

    public MixinResultSlot(Container container, int i, int j, int k) {
        super(container, i, j, k);
    }

    @SuppressWarnings("resource")
    @Inject(
            method = "onTake",
            at = @At(
                    value = "TAIL"
            )
    )
    private void onTake(Player player, ItemStack itemStack, CallbackInfo ci) {
        if (player instanceof ServerPlayer serverPlayer) {
            RecipeManager recipeManager = serverPlayer.theGame().getRecipeManager();
            Optional<RecipeHolder<CraftingRecipe>> optional = recipeManager
                    .getRecipeFor(RecipeType.CRAFTING, craftSlots.asCraftInput(), serverPlayer.serverLevel());
            if (optional.isPresent()) {
                RecipeHolder<CraftingRecipe> recipeHolder = optional.get();
                CraftingRecipe recipe = recipeHolder.value();
                set(recipe.assemble(craftSlots.asCraftInput(), serverPlayer.theGame().registryAccess()));
            }
        }
    }
}
