package com.github.zly2006.craftminefixes.mixin.critical;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.core.component.DataComponents;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.BundleContents;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.util.Iterator;
import java.util.NoSuchElementException;

@Mixin(ServerLevel.class)
public class MixinServerLevel {
    @WrapOperation(
            method = "cleanInventoryAndReward",
            at = @At(
                    value = "INVOKE",
                    target = "net/minecraft/world/entity/player/Inventory.iterator()Ljava/util/Iterator;"
            )
    )
    private Iterator<ItemStack> fixIterator(Inventory instance, Operation<Iterator<ItemStack>> original) {
        return new Iterator<>() {
            private final Iterator<ItemStack> rootIterator = instance.iterator();
            private Iterator<ItemStack> currentIterator = rootIterator;

            @Override
            public boolean hasNext() {
                return rootIterator.hasNext() || currentIterator.hasNext();
            }

            @Override
            public ItemStack next() {
                if (currentIterator != rootIterator) {
                    if (currentIterator.hasNext()) {
                        return currentIterator.next();
                    } else if (rootIterator.hasNext()) {
                        currentIterator = rootIterator;
                    }
                }
                if (rootIterator.hasNext()) {
                    ItemStack itemStack = rootIterator.next();
                    Iterator<ItemStack> iterator = getIterator(itemStack);
                    if (iterator != null && iterator.hasNext()) {
                        currentIterator = iterator;
                        return currentIterator.next();
                    } else {
                        return itemStack;
                    }
                } else {
                    throw new NoSuchElementException("Already reached the end of the iterator");
                }
            }

            private Iterator<ItemStack> getIterator(ItemStack stack) {
                BundleContents bundleContents = stack.get(DataComponents.BUNDLE_CONTENTS);
                if (bundleContents != null) {
                    return bundleContents.items().iterator();
                }
                return null;
            }
        };
    }
}
