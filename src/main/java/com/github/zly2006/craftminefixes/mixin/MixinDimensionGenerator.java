package com.github.zly2006.craftminefixes.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.TheGame;
import net.minecraft.server.level.DimensionGenerator;
import net.minecraft.world.level.mines.SpecialMine;
import net.minecraft.world.level.mines.WorldEffect;
import net.minecraft.world.level.storage.ServerLevelData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;

@Mixin(DimensionGenerator.class)
public class MixinDimensionGenerator {
    @Inject(
            method = "generateDimension",
            at = @At("TAIL")
    )
    private static void generateDimension(
            TheGame theGame,
            List<WorldEffect> list,
            Optional<SpecialMine> optional,
            CallbackInfoReturnable<DimensionGenerator.GeneratedDimension> cir,
            @Local ResourceLocation resourceLocation,
            @Local ServerLevelData serverLevelData,
            @Local(ordinal = 0) Path path
    ) throws IOException {
        Path pathBiomeTags = path.resolve("data")
                .resolve(resourceLocation.getNamespace())
                .resolve("tags")
                .resolve("worldgen")
                .resolve("biome");
        pathBiomeTags.toFile().mkdirs();
        Files.write(
                path.resolve("com.github.zly2006.craftminefixes"),
                String.format(
                        "World: %s\nCount: %d\nDimension: %s\n",
                        serverLevelData.getLevelName(),
                        serverLevelData.getLevelCount(),
                        resourceLocation
                ).getBytes()
        );
    }
}
