package com.flashfyre.stables;

import com.flashfyre.stables.processors.ReplaceBlocksProcessor;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.gen.feature.template.IStructureProcessorType;

public class StablesProcessors {
	
	public static IStructureProcessorType<ReplaceBlocksProcessor> REPLACE_BLOCKS_PROCESSOR = () -> ReplaceBlocksProcessor.CODEC;
	
	public static void registerProcessors() {
		Registry.register(Registry.STRUCTURE_PROCESSOR, new ResourceLocation(Stables.MOD_ID, "replace_blocks_processor"), REPLACE_BLOCKS_PROCESSOR);
	}

}
