package com.flashfyre.stables;

import java.util.function.Supplier;

import com.flashfyre.stables.structures.LargeBarnStructure;
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
	
	public static final RegistryObject<Structure<NoFeatureConfig>> LARGE_BARN = setupStructure("large_barn", () -> (new LargeBarnStructure(NoFeatureConfig.field_236558_a_)));

	private static <T extends Structure<?>> RegistryObject<T> setupStructure(String name, Supplier<T> structure) {
        return STRUCTURES.register(name, structure);
    }
	
	public static void setupStructures() {
        setupStructure(
                LARGE_BARN.get(), /* The instance of the structure */
                new StructureSeparationSettings(15 /* maximum distance apart in chunks between spawn attempts */,
                        5 /* minimum distance apart in chunks between spawn attempts */,
                        694206969 /* this modifies the seed of the structure so no two structures always spawn over each-other. Make this large and unique. */),
                true);

        // Add more structures here and so on
    }
	
	public static <F extends Structure<?>> void setupStructure(
            F structure,
            StructureSeparationSettings structureSeparationSettings,
            boolean transformSurroundingLand)
    {
        /*
         * We need to add our structures into the map in Structure alongside vanilla
         * structures or else it will cause errors. Called by registerStructure.
         *
         * If the registration is setup properly for the structure,
         * getRegistryName() should never return null.
         */
        Structure.NAME_STRUCTURE_BIMAP.put(structure.getRegistryName().toString(), structure);

        /*
         * Will add land at the base of the structure like it does for Villages and Outposts.
         * Doesn't work well on structure that have pieces stacked vertically or change in heights.
         */
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
