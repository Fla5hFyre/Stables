package com.flashfyre.stables;

import com.flashfyre.stables.processors.RandomBlockReplacementProcessor;
import com.flashfyre.stables.processors.SpecificBlockReplacementProcessor;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.gen.feature.template.IStructureProcessorType;

public class StablesProcessors {
	
	public static IStructureProcessorType<SpecificBlockReplacementProcessor> REPLACE_SPECIFIC_BLOCK = () -> SpecificBlockReplacementProcessor.CODEC;
	public static IStructureProcessorType<RandomBlockReplacementProcessor> REPLACE_BLOCKS_RANDOMLY = () -> RandomBlockReplacementProcessor.CODEC;
	
	public static void registerProcessors() {
		Registry.register(Registry.STRUCTURE_PROCESSOR, new ResourceLocation(Stables.MOD_ID, "replace_specific_block"), REPLACE_SPECIFIC_BLOCK);
		Registry.register(Registry.STRUCTURE_PROCESSOR, new ResourceLocation(Stables.MOD_ID, "replace_blocks_randomly"), REPLACE_BLOCKS_RANDOMLY);
	}
}