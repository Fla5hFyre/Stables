package com.flashfyre.stables;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.WorldGenRegistries;
import net.minecraft.world.gen.FlatGenerationSettings;
import net.minecraft.world.gen.feature.IFeatureConfig;
import net.minecraft.world.gen.feature.StructureFeature;

public class StablesConfiguredStructures {
	
	public static StructureFeature<?, ?> CONFIGURED_BARN = StablesStructures.BARN.get().withConfiguration(IFeatureConfig.NO_FEATURE_CONFIG);

	public static void registerConfiguredStructures() {
        Registry<StructureFeature<?, ?>> registry = WorldGenRegistries.CONFIGURED_STRUCTURE_FEATURE;
        Registry.register(registry, new ResourceLocation(Stables.MOD_ID, "configured_run_down_house"), CONFIGURED_BARN);

        FlatGenerationSettings.STRUCTURES.put(StablesStructures.BARN.get(), CONFIGURED_BARN);
    }
}
