package com.github.zly2006.craftminefixes.data;


import com.google.gson.FormattingStyle;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BiomeTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.MultiNoiseBiomeSourceParameterList;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class BiomeTagsProvider {
    private final Gson gson = new GsonBuilder().setFormattingStyle(FormattingStyle.PRETTY).create();
    private final Path root;
    private final @Unmodifiable Set<ResourceLocation> modifiedBiomes;
    public String prefix;

    public BiomeTagsProvider(Path root, @NotNull @Unmodifiable Set<@NotNull ResourceLocation> modifiedBiomes) {
        this.root = root;
        this.modifiedBiomes = modifiedBiomes;
    }

    List<TagAppender> tagAppenders = new ArrayList<>();

    record Data(List<String> values) { }

    class TagAppender {
        ResourceLocation location;

        public TagAppender(ResourceLocation location) {
            this.location = location;
        }

        List<String> values = new ArrayList<>();

        public TagAppender addTag(TagKey<Biome> biomeTags) {
            return this;
        }

        public TagAppender add(ResourceKey<Biome> biome) {
            if (modifiedBiomes.contains(biome.location())) {
                this.values.add(biome.location().toString());
            }
            return this;
        }

        public TagAppender add(ResourceLocation resourceLocation) {
            if (modifiedBiomes.contains(resourceLocation)) {
                this.values.add(resourceLocation.toString());
            }
            return this;
        }

        void write() {
            Path path = root.resolve("data")
                    .resolve(location.getNamespace())
                    .resolve("tags")
                    .resolve(Registries.BIOME.location().getPath())
                    .resolve(location.getPath() + ".json");
            try {
                Files.createDirectories(path.getParent());
                Data data = new Data(values);
                String json = gson.toJson(data);
                Files.writeString(path, json);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private TagAppender tag(TagKey<Biome> biomeTags) {
        TagAppender tagAppender = new TagAppender(biomeTags.location());
        tagAppenders.add(tagAppender);
        return tagAppender;
    }

    protected void addTags() {
        this.tag(BiomeTags.IS_DEEP_OCEAN).add(BiomesGet("deep_frozen_ocean")).add(BiomesGet("deep_cold_ocean")).add(BiomesGet("deep_ocean")).add(BiomesGet("deep_lukewarm_ocean"));
        this.tag(BiomeTags.IS_OCEAN)
                .addTag(BiomeTags.IS_DEEP_OCEAN)
                .add(BiomesGet("frozen_ocean"))
                .add(BiomesGet("ocean"))
                .add(BiomesGet("cold_ocean"))
                .add(BiomesGet("lukewarm_ocean"))
                .add(BiomesGet("warm_ocean"));
        this.tag(BiomeTags.IS_BEACH).add(BiomesGet("beach")).add(BiomesGet("snowy_beach"));
        this.tag(BiomeTags.IS_RIVER).add(BiomesGet("river")).add(BiomesGet("frozen_river"));
        this.tag(BiomeTags.IS_MOUNTAIN)
                .add(BiomesGet("meadow"))
                .add(BiomesGet("frozen_peaks"))
                .add(BiomesGet("jagged_peaks"))
                .add(BiomesGet("stony_peaks"))
                .add(BiomesGet("snowy_slopes"))
                .add(BiomesGet("cherry_grove"));
        this.tag(BiomeTags.IS_BADLANDS).add(BiomesGet("badlands")).add(BiomesGet("eroded_badlands")).add(BiomesGet("wooded_badlands"));
        this.tag(BiomeTags.IS_HILL).add(BiomesGet("windswept_hills")).add(BiomesGet("windswept_forest")).add(BiomesGet("windswept_gravelly_hills"));
        this.tag(BiomeTags.IS_TAIGA).add(BiomesGet("taiga")).add(BiomesGet("snowy_taiga")).add(BiomesGet("old_growth_pine_taiga")).add(BiomesGet("old_growth_spruce_taiga"));
        this.tag(BiomeTags.IS_JUNGLE).add(BiomesGet("bamboo_jungle")).add(BiomesGet("jungle")).add(BiomesGet("sparse_jungle"));
        this.tag(BiomeTags.IS_FOREST)
                .add(BiomesGet("forest"))
                .add(BiomesGet("flower_forest"))
                .add(BiomesGet("birch_forest"))
                .add(BiomesGet("old_growth_birch_forest"))
                .add(BiomesGet("dark_forest"))
                .add(BiomesGet("pale_garden"))
                .add(BiomesGet("grove"));
        this.tag(BiomeTags.IS_SAVANNA).add(BiomesGet("savanna")).add(BiomesGet("savanna_plateau")).add(BiomesGet("windswept_savanna"));
        var tagAppender = this.tag(BiomeTags.IS_NETHER);
        MultiNoiseBiomeSourceParameterList.Preset.NETHER.usedBiomes().forEach(tagAppender::add);
        var tagAppender2 = this.tag(BiomeTags.IS_OVERWORLD);
        MultiNoiseBiomeSourceParameterList.Preset.OVERWORLD.usedBiomes().forEach(tagAppender2::add);
        this.tag(BiomeTags.IS_END).add(BiomesGet("the_end")).add(BiomesGet("end_highlands")).add(BiomesGet("end_midlands")).add(BiomesGet("small_end_islands")).add(BiomesGet("end_barrens"));
        this.tag(BiomeTags.HAS_BURIED_TREASURE).addTag(BiomeTags.IS_BEACH);
        this.tag(BiomeTags.HAS_DESERT_PYRAMID).add(BiomesGet("desert"));
        this.tag(BiomeTags.HAS_IGLOO).add(BiomesGet("snowy_taiga")).add(BiomesGet("snowy_plains")).add(BiomesGet("snowy_slopes"));
        this.tag(BiomeTags.HAS_JUNGLE_TEMPLE).add(BiomesGet("bamboo_jungle")).add(BiomesGet("jungle"));
        this.tag(BiomeTags.HAS_MINESHAFT)
                .addTag(BiomeTags.IS_OCEAN)
                .addTag(BiomeTags.IS_RIVER)
                .addTag(BiomeTags.IS_BEACH)
                .addTag(BiomeTags.IS_MOUNTAIN)
                .addTag(BiomeTags.IS_HILL)
                .addTag(BiomeTags.IS_TAIGA)
                .addTag(BiomeTags.IS_JUNGLE)
                .addTag(BiomeTags.IS_FOREST)
                .add(BiomesGet("stony_shore"))
                .add(BiomesGet("mushroom_fields"))
                .add(BiomesGet("ice_spikes"))
                .add(BiomesGet("windswept_savanna"))
                .add(BiomesGet("desert"))
                .add(BiomesGet("savanna"))
                .add(BiomesGet("snowy_plains"))
                .add(BiomesGet("plains"))
                .add(BiomesGet("sunflower_plains"))
                .add(BiomesGet("swamp"))
                .add(BiomesGet("mangrove_swamp"))
                .add(BiomesGet("savanna_plateau"))
                .add(BiomesGet("dripstone_caves"))
                .add(BiomesGet("lush_caves"));
        this.tag(BiomeTags.HAS_MINESHAFT_MESA).addTag(BiomeTags.IS_BADLANDS);
        this.tag(BiomeTags.MINESHAFT_BLOCKING).add(BiomesGet("deep_dark"));
        this.tag(BiomeTags.HAS_OCEAN_MONUMENT).addTag(BiomeTags.IS_DEEP_OCEAN);
        this.tag(BiomeTags.REQUIRED_OCEAN_MONUMENT_SURROUNDING).addTag(BiomeTags.IS_OCEAN).addTag(BiomeTags.IS_RIVER);
        this.tag(BiomeTags.HAS_OCEAN_RUIN_COLD)
                .add(BiomesGet("frozen_ocean"))
                .add(BiomesGet("cold_ocean"))
                .add(BiomesGet("ocean"))
                .add(BiomesGet("deep_frozen_ocean"))
                .add(BiomesGet("deep_cold_ocean"))
                .add(BiomesGet("deep_ocean"));
        this.tag(BiomeTags.HAS_OCEAN_RUIN_WARM).add(BiomesGet("lukewarm_ocean")).add(BiomesGet("warm_ocean")).add(BiomesGet("deep_lukewarm_ocean"));
        this.tag(BiomeTags.HAS_PILLAGER_OUTPOST)
                .add(BiomesGet("desert"))
                .add(BiomesGet("plains"))
                .add(BiomesGet("savanna"))
                .add(BiomesGet("snowy_plains"))
                .add(BiomesGet("taiga"))
                .addTag(BiomeTags.IS_MOUNTAIN)
                .add(BiomesGet("grove"));
        this.tag(BiomeTags.HAS_RUINED_PORTAL_DESERT).add(BiomesGet("desert"));
        this.tag(BiomeTags.HAS_RUINED_PORTAL_JUNGLE).addTag(BiomeTags.IS_JUNGLE);
        this.tag(BiomeTags.HAS_RUINED_PORTAL_OCEAN).addTag(BiomeTags.IS_OCEAN);
        this.tag(BiomeTags.HAS_RUINED_PORTAL_SWAMP).add(BiomesGet("swamp")).add(BiomesGet("mangrove_swamp"));
        this.tag(BiomeTags.HAS_RUINED_PORTAL_MOUNTAIN)
                .addTag(BiomeTags.IS_BADLANDS)
                .addTag(BiomeTags.IS_HILL)
                .add(BiomesGet("savanna_plateau"))
                .add(BiomesGet("windswept_savanna"))
                .add(BiomesGet("stony_shore"))
                .addTag(BiomeTags.IS_MOUNTAIN);
        this.tag(BiomeTags.HAS_RUINED_PORTAL_STANDARD)
                .addTag(BiomeTags.IS_BEACH)
                .addTag(BiomeTags.IS_RIVER)
                .addTag(BiomeTags.IS_TAIGA)
                .addTag(BiomeTags.IS_FOREST)
                .add(BiomesGet("mushroom_fields"))
                .add(BiomesGet("ice_spikes"))
                .add(BiomesGet("dripstone_caves"))
                .add(BiomesGet("lush_caves"))
                .add(BiomesGet("savanna"))
                .add(BiomesGet("snowy_plains"))
                .add(BiomesGet("plains"))
                .add(BiomesGet("sunflower_plains"));
        this.tag(BiomeTags.HAS_SHIPWRECK_BEACHED).addTag(BiomeTags.IS_BEACH);
        this.tag(BiomeTags.HAS_SHIPWRECK).addTag(BiomeTags.IS_OCEAN);
        this.tag(BiomeTags.HAS_SWAMP_HUT).add(BiomesGet("swamp"));
        this.tag(BiomeTags.HAS_VILLAGE_DESERT).add(BiomesGet("desert"));
        this.tag(BiomeTags.HAS_VILLAGE_PLAINS).add(BiomesGet("plains")).add(BiomesGet("meadow"));
        this.tag(BiomeTags.HAS_VILLAGE_SAVANNA).add(BiomesGet("savanna"));
        this.tag(BiomeTags.HAS_VILLAGE_SNOWY).add(BiomesGet("snowy_plains"));
        this.tag(BiomeTags.HAS_VILLAGE_TAIGA).add(BiomesGet("taiga"));
        this.tag(BiomeTags.HAS_TRAIL_RUINS)
                .add(BiomesGet("taiga"))
                .add(BiomesGet("snowy_taiga"))
                .add(BiomesGet("old_growth_pine_taiga"))
                .add(BiomesGet("old_growth_spruce_taiga"))
                .add(BiomesGet("old_growth_birch_forest"))
                .add(BiomesGet("jungle"));
        this.tag(BiomeTags.HAS_WOODLAND_MANSION).add(BiomesGet("dark_forest")).add(BiomesGet("pale_garden"));
        this.tag(BiomeTags.STRONGHOLD_BIASED_TO)
                .add(BiomesGet("plains"))
                .add(BiomesGet("sunflower_plains"))
                .add(BiomesGet("snowy_plains"))
                .add(BiomesGet("ice_spikes"))
                .add(BiomesGet("desert"))
                .add(BiomesGet("forest"))
                .add(BiomesGet("flower_forest"))
                .add(BiomesGet("birch_forest"))
                .add(BiomesGet("dark_forest"))
                .add(BiomesGet("pale_garden"))
                .add(BiomesGet("old_growth_birch_forest"))
                .add(BiomesGet("old_growth_pine_taiga"))
                .add(BiomesGet("old_growth_spruce_taiga"))
                .add(BiomesGet("taiga"))
                .add(BiomesGet("snowy_taiga"))
                .add(BiomesGet("savanna"))
                .add(BiomesGet("savanna_plateau"))
                .add(BiomesGet("windswept_hills"))
                .add(BiomesGet("windswept_gravelly_hills"))
                .add(BiomesGet("windswept_forest"))
                .add(BiomesGet("windswept_savanna"))
                .add(BiomesGet("jungle"))
                .add(BiomesGet("sparse_jungle"))
                .add(BiomesGet("bamboo_jungle"))
                .add(BiomesGet("badlands"))
                .add(BiomesGet("eroded_badlands"))
                .add(BiomesGet("wooded_badlands"))
                .add(BiomesGet("meadow"))
                .add(BiomesGet("grove"))
                .add(BiomesGet("snowy_slopes"))
                .add(BiomesGet("frozen_peaks"))
                .add(BiomesGet("jagged_peaks"))
                .add(BiomesGet("stony_peaks"))
                .add(BiomesGet("mushroom_fields"))
                .add(BiomesGet("dripstone_caves"))
                .add(BiomesGet("lush_caves"));
        this.tag(BiomeTags.HAS_STRONGHOLD).addTag(BiomeTags.IS_OVERWORLD);
        var tagAppender3 = this.tag(BiomeTags.HAS_TRIAL_CHAMBERS);
        modifiedBiomes.stream().filter(x -> !x.getPath().contains("deep_dark"))
                .forEach(tagAppender3::add);
        this.tag(BiomeTags.HAS_NETHER_FORTRESS).addTag(BiomeTags.IS_NETHER);
        this.tag(BiomeTags.HAS_NETHER_FOSSIL).add(BiomesGet("soul_sand_valley"));
        this.tag(BiomeTags.HAS_BASTION_REMNANT).add(BiomesGet("crimson_forest")).add(BiomesGet("nether_wastes")).add(BiomesGet("soul_sand_valley")).add(BiomesGet("warped_forest"));
        this.tag(BiomeTags.HAS_ANCIENT_CITY).add(BiomesGet("deep_dark"));
        this.tag(BiomeTags.HAS_RUINED_PORTAL_NETHER).addTag(BiomeTags.IS_NETHER);
        this.tag(BiomeTags.HAS_END_CITY).add(BiomesGet("end_highlands")).add(BiomesGet("end_midlands"));
        this.tag(BiomeTags.PRODUCES_CORALS_FROM_BONEMEAL).add(BiomesGet("warm_ocean"));
        this.tag(BiomeTags.PLAYS_UNDERWATER_MUSIC).addTag(BiomeTags.IS_OCEAN).addTag(BiomeTags.IS_RIVER);
        this.tag(BiomeTags.HAS_CLOSER_WATER_FOG).add(BiomesGet("swamp")).add(BiomesGet("mangrove_swamp"));
        this.tag(BiomeTags.WATER_ON_MAP_OUTLINES).addTag(BiomeTags.IS_OCEAN).addTag(BiomeTags.IS_RIVER).add(BiomesGet("swamp")).add(BiomesGet("mangrove_swamp"));
        this.tag(BiomeTags.WITHOUT_ZOMBIE_SIEGES).add(BiomesGet("mushroom_fields"));
        this.tag(BiomeTags.WITHOUT_PATROL_SPAWNS).add(BiomesGet("mushroom_fields"));
        this.tag(BiomeTags.WITHOUT_WANDERING_TRADER_SPAWNS).add(BiomesGet("the_void")).add(BiomesGet("hub"));
        this.tag(BiomeTags.SPAWNS_COLD_VARIANT_FROGS)
                .add(BiomesGet("snowy_plains"))
                .add(BiomesGet("ice_spikes"))
                .add(BiomesGet("frozen_peaks"))
                .add(BiomesGet("jagged_peaks"))
                .add(BiomesGet("snowy_slopes"))
                .add(BiomesGet("frozen_ocean"))
                .add(BiomesGet("deep_frozen_ocean"))
                .add(BiomesGet("grove"))
                .add(BiomesGet("deep_dark"))
                .add(BiomesGet("frozen_river"))
                .add(BiomesGet("snowy_taiga"))
                .add(BiomesGet("snowy_beach"))
                .addTag(BiomeTags.IS_END);
        this.tag(BiomeTags.SPAWNS_WARM_VARIANT_FROGS)
                .add(BiomesGet("desert"))
                .add(BiomesGet("warm_ocean"))
                .addTag(BiomeTags.IS_JUNGLE)
                .addTag(BiomeTags.IS_SAVANNA)
                .addTag(BiomeTags.IS_NETHER)
                .addTag(BiomeTags.IS_BADLANDS)
                .add(BiomesGet("mangrove_swamp"));
        this.tag(BiomeTags.SPAWNS_COLD_VARIANT_FARM_ANIMALS)
                .add(BiomesGet("snowy_plains"))
                .add(BiomesGet("ice_spikes"))
                .add(BiomesGet("frozen_peaks"))
                .add(BiomesGet("jagged_peaks"))
                .add(BiomesGet("snowy_slopes"))
                .add(BiomesGet("frozen_ocean"))
                .add(BiomesGet("deep_frozen_ocean"))
                .add(BiomesGet("grove"))
                .add(BiomesGet("deep_dark"))
                .add(BiomesGet("frozen_river"))
                .add(BiomesGet("snowy_taiga"))
                .add(BiomesGet("snowy_beach"))
                .addTag(BiomeTags.IS_END)
                .add(BiomesGet("cold_ocean"))
                .add(BiomesGet("deep_cold_ocean"))
                .add(BiomesGet("old_growth_pine_taiga"))
                .add(BiomesGet("old_growth_spruce_taiga"))
                .add(BiomesGet("taiga"))
                .add(BiomesGet("windswept_forest"))
                .add(BiomesGet("windswept_gravelly_hills"))
                .add(BiomesGet("windswept_hills"))
                .add(BiomesGet("stony_peaks"));
        this.tag(BiomeTags.SPAWNS_WARM_VARIANT_FARM_ANIMALS)
                .add(BiomesGet("desert"))
                .add(BiomesGet("warm_ocean"))
                .addTag(BiomeTags.IS_JUNGLE)
                .addTag(BiomeTags.IS_SAVANNA)
                .addTag(BiomeTags.IS_NETHER)
                .addTag(BiomeTags.IS_BADLANDS)
                .add(BiomesGet("mangrove_swamp"))
                .add(BiomesGet("deep_lukewarm_ocean"))
                .add(BiomesGet("lukewarm_ocean"));
        this.tag(BiomeTags.SPAWNS_GOLD_RABBITS).add(BiomesGet("desert"));
        this.tag(BiomeTags.SPAWNS_WHITE_RABBITS)
                .add(BiomesGet("snowy_plains"))
                .add(BiomesGet("ice_spikes"))
                .add(BiomesGet("frozen_ocean"))
                .add(BiomesGet("snowy_taiga"))
                .add(BiomesGet("frozen_river"))
                .add(BiomesGet("snowy_beach"))
                .add(BiomesGet("frozen_peaks"))
                .add(BiomesGet("jagged_peaks"))
                .add(BiomesGet("snowy_slopes"))
                .add(BiomesGet("grove"));
        this.tag(BiomeTags.REDUCED_WATER_AMBIENT_SPAWNS).addTag(BiomeTags.IS_RIVER);
        this.tag(BiomeTags.ALLOWS_TROPICAL_FISH_SPAWNS_AT_ANY_HEIGHT).add(BiomesGet("lush_caves"));
        this.tag(BiomeTags.POLAR_BEARS_SPAWN_ON_ALTERNATE_BLOCKS).add(BiomesGet("frozen_ocean")).add(BiomesGet("deep_frozen_ocean"));
        this.tag(BiomeTags.MORE_FREQUENT_DROWNED_SPAWNS).addTag(BiomeTags.IS_RIVER);
        this.tag(BiomeTags.ALLOWS_SURFACE_SLIME_SPAWNS).add(BiomesGet("swamp")).add(BiomesGet("mangrove_swamp"));
        this.tag(BiomeTags.SPAWNS_SNOW_FOXES)
                .add(BiomesGet("snowy_plains"))
                .add(BiomesGet("ice_spikes"))
                .add(BiomesGet("frozen_ocean"))
                .add(BiomesGet("snowy_taiga"))
                .add(BiomesGet("frozen_river"))
                .add(BiomesGet("snowy_beach"))
                .add(BiomesGet("frozen_peaks"))
                .add(BiomesGet("jagged_peaks"))
                .add(BiomesGet("snowy_slopes"))
                .add(BiomesGet("grove"));
        this.tag(BiomeTags.INCREASED_FIRE_BURNOUT)
                .add(BiomesGet("bamboo_jungle"))
                .add(BiomesGet("mushroom_fields"))
                .add(BiomesGet("mangrove_swamp"))
                .add(BiomesGet("snowy_slopes"))
                .add(BiomesGet("frozen_peaks"))
                .add(BiomesGet("jagged_peaks"))
                .add(BiomesGet("swamp"))
                .add(BiomesGet("jungle"));
        this.tag(BiomeTags.SNOW_GOLEM_MELTS)
                .add(BiomesGet("badlands"))
                .add(BiomesGet("basalt_deltas"))
                .add(BiomesGet("crimson_forest"))
                .add(BiomesGet("desert"))
                .add(BiomesGet("eroded_badlands"))
                .add(BiomesGet("nether_wastes"))
                .add(BiomesGet("savanna"))
                .add(BiomesGet("savanna_plateau"))
                .add(BiomesGet("soul_sand_valley"))
                .add(BiomesGet("warped_forest"))
                .add(BiomesGet("windswept_savanna"))
                .add(BiomesGet("wooded_badlands"));
    }

    private ResourceKey<Biome> BiomesGet(String key) {
        return ResourceKey.create(Registries.BIOME, ResourceLocation.withDefaultNamespace(prefix + "/" + key));
    }

    public void run() {
        tagAppenders.clear();
        addTags();
        tagAppenders.forEach(TagAppender::write);
    }
}
