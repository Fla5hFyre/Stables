package com.flashfyre.stables.processors;

import java.util.Map;
import java.util.Random;

import com.flashfyre.stables.StablesProcessors;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.StairsBlock;
import net.minecraft.state.properties.Half;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.gen.feature.template.IStructureProcessorType;
import net.minecraft.world.gen.feature.template.PlacementSettings;
import net.minecraft.world.gen.feature.template.StructureProcessor;
import net.minecraft.world.gen.feature.template.Template;
import net.minecraft.world.gen.feature.template.Template.BlockInfo;

public class ReplaceBlocksProcessor extends StructureProcessor {
	
	private final float chance;
	private final Map<Block, Block> replacements;
	@SuppressWarnings("deprecation")
	public static final Codec<ReplaceBlocksProcessor> CODEC = RecordCodecBuilder.create(builder -> builder.group(			
			Codec.FLOAT.fieldOf("chance").forGetter((processor) -> processor.chance),
			Codec.unboundedMap(Registry.BLOCK, Registry.BLOCK).fieldOf("replacements").forGetter((processor) -> { return processor.replacements;})
		).apply(builder, ReplaceBlocksProcessor::new));
	
	private ReplaceBlocksProcessor(float chance, Map<Block, Block> replacements) {
		this.chance = chance;
		this.replacements = replacements;
	}
	
	@Override
	public BlockInfo process(IWorldReader worldView, BlockPos pos, BlockPos blockPos,
			BlockInfo structureBlockInfoLocal, BlockInfo structureBlockInfoWorld, PlacementSettings structurePlacementData, Template template) {
		BlockState state = structureBlockInfoWorld.state;
		BlockState newState = null;
		Random r = structurePlacementData.getRandom(structureBlockInfoWorld.pos);
		if(replacements.containsKey(state.getBlock())) {
			if(r.nextFloat() < this.chance) {	
 				if(state.isIn(BlockTags.STAIRS)) {
					newState = getStateStairs(state);
				}
				else {
					newState = getReplacementState(state);
				}
			}
		}
    return newState != null ? new Template.BlockInfo(structureBlockInfoWorld.pos, newState, structureBlockInfoWorld.nbt) : structureBlockInfoWorld;
	}

	@Override
	protected IStructureProcessorType<?> getType() {
		return StablesProcessors.REPLACE_BLOCKS_PROCESSOR;
	}
	
	public BlockState getStateStairs(BlockState state) {
		Direction direction = state.get(StairsBlock.FACING);
	    Half half = state.get(StairsBlock.HALF);
	    BlockState newState = getReplacementState(state).with(StairsBlock.FACING, direction).with(StairsBlock.HALF, half);
	    return newState;
	}
	
	public BlockState getReplacementState(BlockState state) {
		return replacements.get(state.getBlock()).getDefaultState();
	}

}
