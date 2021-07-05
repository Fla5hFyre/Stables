package com.flashfyre.stables;

import java.util.function.Supplier;

import com.flashfyre.stables.structures.OakStableStructure;
import com.flashfyre.stables.structures.SpruceStableStructure;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;

import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraft.world.gen.feature.structure.Structure;
import net.minecraft.world.gen.settings.DimensionStructuresSettings;
import net.minecraft.world.gen.settings.StructureSeparationSettings;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class StablesStructures {
	
	public static final DeferredRegister<Structure<?>> STRUCTURES = DeferredRegister.create(ForgeRegistries.STRUCTURE_FEATURES, Stables.MOD_ID);
	
	public static final RegistryObject<Structure<NoFeatureConfig>> OAK_STABLE = setupStructure("oak_stable", () -> (new OakStableStructure(NoFeatureConfig.field_236558_a_)));
	public static final RegistryObject<Structure<NoFeatureConfig>> SPRUCE_STABLE = setupStructure("spruce_stable", () -> (new SpruceStableStructure(NoFeatureConfig.field_236558_a_)));

	private static <T extends Structure<?>> RegistryObject<T> setupStructure(String name, Supplier<T> structure) {
        return STRUCTURES.register(name, structure);
    }
	
	public static void setupStructures() {
        setupStructure(
                OAK_STABLE.get(),
                new StructureSeparationSettings(24 /*average distance apart in chunks between spawn attempts */,
                        8 /* minimum distance apart in chunks between spawn attempts */,
                        694206969),
                true);

        setupStructure(
        		SPRUCE_STABLE.get(),
                new StructureSeparationSettings(20 /*average distance apart in chunks between spawn attempts */,
                        8 /* minimum distance apart in chunks between spawn attempts */,
                        694206969),
                true);
    }
	
	public static <F extends Structure<?>> void setupStructure(
            F structure,
            StructureSeparationSettings structureSeparationSettings,
            boolean transformSurroundingLand) {
		
        Structure.NAME_STRUCTURE_BIMAP.put(structure.getRegistryName().toString(), structure);
        
        if(transformSurroundingLand){
            Structure.field_236384_t_ =
                    ImmutableList.<Structure<?>>builder()
                            .addAll(Structure.field_236384_t_)
                            .add(structure)
                            .build();
        }
        
        DimensionStructuresSettings.field_236191_b_ =
                ImmutableMap.<Structure<?>, StructureSeparationSettings>builder()
                        .putAll(DimensionStructuresSettings.field_236191_b_)
                        .put(structure, structureSeparationSettings)
                        .build();
    }
}
