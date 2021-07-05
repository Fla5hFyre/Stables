package com.flashfyre.stables;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.WorldGenRegistries;
import net.minecraft.world.gen.FlatGenerationSettings;
import net.minecraft.world.gen.feature.IFeatureConfig;
import net.minecraft.world.gen.feature.StructureFeature;

public class StablesConfiguredStructures {
	
	public static StructureFeature<?, ?> configured_oak_stable = StablesStructures.OAK_STABLE.get().withConfiguration(IFeatureConfig.NO_FEATURE_CONFIG);
	public static StructureFeature<?, ?> configured_spruce_stable = StablesStructures.SPRUCE_STABLE.get().withConfiguration(IFeatureConfig.NO_FEATURE_CONFIG);

	public static void registerConfiguredStructures() {
        Registry<StructureFeature<?, ?>> registry = WorldGenRegistries.CONFIGURED_STRUCTURE_FEATURE;
        
        Registry.register(registry, new ResourceLocation(Stables.MOD_ID, "configured_oak_stable"), configured_oak_stable);
        FlatGenerationSettings.STRUCTURES.put(StablesStructures.OAK_STABLE.get(), configured_oak_stable);
        
        Registry.register(registry, new ResourceLocation(Stables.MOD_ID, "configured_spruce_stable"), configured_spruce_stable);
        FlatGenerationSettings.STRUCTURES.put(StablesStructures.SPRUCE_STABLE.get(), configured_spruce_stable);
    }
}
