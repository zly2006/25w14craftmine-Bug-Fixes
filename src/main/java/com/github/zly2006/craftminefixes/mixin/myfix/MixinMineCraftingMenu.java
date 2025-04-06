package com.github.zly2006.craftminefixes.mixin.myfix;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.inventory.MineCraftingMenu;
import net.minecraft.world.level.mines.WorldEffect;
import net.minecraft.world.level.mines.WorldEffects;
import org.apache.logging.log4j.LogManager;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

@Mixin(MineCraftingMenu.class)
public class MixinMineCraftingMenu {
    @Shadow @Final private Optional<ServerPlayer> serverPlayer;

    @WrapOperation(
            method = "slotsChanged",
            at = @At(
                    value = "INVOKE",
                    target = "Ljava/util/stream/Stream;toList()Ljava/util/List;"
            )
    )
    private List<WorldEffect> fixSoulLink(Stream<WorldEffect> instance, Operation<List<WorldEffect>> original) {
        var list = new ArrayList<>(original.call(instance));
        if (serverPlayer.isPresent()) {
            if (serverPlayer.get().theGame().server().getPlayerCount() == 1) {
                LogManager.getLogger(this.getClass()).info("Single player, disabling soul link");
                list.remove(WorldEffects.SOUL_LINK);
            }
        }
        return list;
    }
}
