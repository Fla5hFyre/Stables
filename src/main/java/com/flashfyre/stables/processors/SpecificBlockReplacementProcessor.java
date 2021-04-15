package com.flashfyre.stables.processors;

import java.util.List;
import java.util.Random;

import com.flashfyre.stables.StablesProcessors;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.gen.feature.template.IStructureProcessorType;
import net.minecraft.world.gen.feature.template.PlacementSettings;
import net.minecraft.world.gen.feature.template.StructureProcessor;
import net.minecraft.world.gen.feature.template.Template;
import net.minecraft.world.gen.feature.template.Template.BlockInfo;

/*
 * Replaces all of one type of block per structure, creating variations of the same structure.
 */
public class SpecificBlockReplacementProcessor extends StructureProcessor {
	
	private final String block;
	private final List<Block> replacements;
	@SuppressWarnings("deprecation")
	public static final Codec<SpecificBlockReplacementProcessor> CODEC = RecordCodecBuilder.create(builder -> builder.group(
			Codec.STRING.fieldOf("block").forGetter((processor) -> processor.block),
			Codec.list(Registry.BLOCK).fieldOf("replacements").forGetter((processor) -> processor.replacements)
		).apply(builder, SpecificBlockReplacementProcessor::new));
	
	private SpecificBlockReplacementProcessor(String toReplace, List<Block> replacements) {
		this.block = toReplace;
		this.replacements = replacements;
	}
	
	@SuppressWarnings("deprecation")
	@Override
	public BlockInfo process(IWorldReader worldView, BlockPos pos, BlockPos blockPos,
			BlockInfo structureBlockInfoLocal, BlockInfo structureBlockInfoWorld, PlacementSettings structurePlacementData, Template template) {
		BlockState state = structureBlockInfoWorld.state;
		BlockState newState = null;
		BlockState stateToCheckFor = Registry.BLOCK.getOrDefault(new ResourceLocation(block)).getDefaultState();
		if(state == stateToCheckFor) {
			Random r = structurePlacementData.getRandom(pos);
			newState = replacements.get(r.nextInt(replacements.size())).getDefaultState();
		}		
		
		return newState != null ? new Template.BlockInfo(structureBlockInfoWorld.pos, newState, structureBlockInfoWorld.nbt) : structureBlockInfoWorld;
		
	}

	@Override
	protected IStructureProcessorType<?> getType() {
		return StablesProcessors.REPLACE_SPECIFIC_BLOCK;
	}

}
