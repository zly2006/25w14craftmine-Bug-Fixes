package com.github.zly2006.craftminefixes.mixin.critical;

import com.github.zly2006.craftminefixes.ItemStackIterator;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.entity.LevelCallback;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

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

    @WrapOperation(
            method = "respawnPlayersIntoHub",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/server/level/ServerLevel;isMineWon()Z", ordinal = 1)
    )
    private boolean fixRespawnConditionForLostGame(ServerLevel instance, Operation<Boolean> original) {
        return instance.isMineCompleted();
    }

    @Mixin(targets = "net.minecraft.server.level.ServerLevel.EntityCallbacks")
    static abstract class EntityCallbacksMixin implements LevelCallback<Entity> {
        @Shadow(remap = false) @Final ServerLevel field_26936;

        @Inject(
                method = "onTrackingStart(Lnet/minecraft/world/entity/Entity;)V",
                at = @At(
                        value = "INVOKE",
                        target = "Lnet/minecraft/server/level/ServerLevel;updateSleepingPlayerList()V"
                )
        )
        private void onTrackingStart(CallbackInfo ci) {
            if (field_26936.isMineCompleted()) {
                field_26936.handleCompletedMine();
            }
        }
    }
}
