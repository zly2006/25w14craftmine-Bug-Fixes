package com.github.zly2006.craftminefixes.mixin.critical;

import com.github.zly2006.craftminefixes.ItemStackIterator;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.util.Iterator;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

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
        Stream<ItemStack> stream = StreamSupport.stream(
                Spliterators.spliteratorUnknownSize(new ItemStackIterator(instance.iterator()), Spliterator.ORDERED), false
        ).filter(itemStack -> !itemStack.isEmpty());
        String join = String.join(", ", stream.map(ItemStack::toString).collect(Collectors.toList()));
        System.out.println("ItemStack: " + join);
        return new ItemStackIterator(instance.iterator());
    }
}
