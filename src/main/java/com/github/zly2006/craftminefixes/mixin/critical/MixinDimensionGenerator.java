package com.github.zly2006.craftminefixes.mixin.critical;

import com.github.zly2006.craftminefixes.CraftmineFixes;
import com.github.zly2006.craftminefixes.data.BiomeTagsProvider;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.TheGame;
import net.minecraft.server.level.DimensionGenerator;
import net.minecraft.world.level.mines.SpecialMine;
import net.minecraft.world.level.mines.WorldEffect;
import net.minecraft.world.level.mines.WorldGenBuilder;
import net.minecraft.world.level.storage.ServerLevelData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
            @Local(ordinal = 0) Path path,
            @Local WorldGenBuilder worldGenBuilder
    ) throws IOException {
        path.resolve("data").toFile().mkdirs();
        List<WorldGenBuilder.ModifiedBiome> modifiedBiomes = worldGenBuilder.createModifiedBiomes(
                theGame.registryAccess().lookupOrThrow(Registries.BIOME), resourceLocation.getPath()
        );
        Files.write(
                path.resolve("com.github.zly2006.craftminefixes"),
                String.format(
                        "World: %s\nCount: %d\nDimension: %s\nMod Version: %s\nDate: %s\nModified Biomes: %s\n",
                        serverLevelData.getLevelName(),
                        serverLevelData.getLevelCount(),
                        resourceLocation,
                        CraftmineFixes.MOD_VERSION,
                        new Date(),
                        modifiedBiomes.stream()
                                .map(modifiedBiome -> modifiedBiome.modified().location().toString())
                                .collect(Collectors.joining(", "))
                ).getBytes()
        );
        BiomeTagsProvider provider = new BiomeTagsProvider(path, modifiedBiomes);
        provider.prefix = resourceLocation.getPath();
        provider.run(theGame);
    }
}
