package com.flashfyre.stables.processors;

import java.util.Map;
import java.util.Random;

import com.flashfyre.stables.StablesProcessors;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.StairsBlock;
import net.minecraft.block.WallBlock;
import net.minecraft.block.WallHeight;
import net.minecraft.state.properties.Half;
import net.minecraft.state.properties.StairsShape;
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

/*
 * Replaces a type of block in a structure randomly with others, with a chance, creating variation within one structure.
 * Automatically re-rotates stairs correctly when they are replaced.
 */
public class RandomBlockReplacementProcessor extends StructureProcessor {
	
	private final float chance;
	private final Map<Block, Block> replacements;
	@SuppressWarnings("deprecation")
	public static final Codec<RandomBlockReplacementProcessor> CODEC = RecordCodecBuilder.create(builder -> builder.group(			
			Codec.FLOAT.fieldOf("chance").forGetter((processor) -> processor.chance),
			Codec.unboundedMap(Registry.BLOCK, Registry.BLOCK).fieldOf("replacements").forGetter((processor) -> { return processor.replacements;})
		).apply(builder, RandomBlockReplacementProcessor::new));
	
	private RandomBlockReplacementProcessor(float chance, Map<Block, Block> replacements) {
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
 				else if(state.isIn(BlockTags.WALLS)) {
 					newState = getStateWall(state);
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
		return StablesProcessors.REPLACE_BLOCKS_RANDOMLY;
	}
	
	public BlockState getStateStairs(BlockState state) { // Could easily check waterlogged state here too, but stables are generally not in the ocean
		Direction direction = state.get(StairsBlock.FACING);
	    Half half = state.get(StairsBlock.HALF);
	    StairsShape shape = state.get(StairsBlock.SHAPE);
	    BlockState newState = getReplacementState(state).with(StairsBlock.FACING, direction).with(StairsBlock.HALF, half).with(StairsBlock.SHAPE, shape);
	    return newState;
	}
	
	public BlockState getStateWall(BlockState state) { // And here
		boolean up = state.get(WallBlock.UP);
		WallHeight whEast = state.get(WallBlock.WALL_HEIGHT_EAST);
		WallHeight whNorth = state.get(WallBlock.WALL_HEIGHT_NORTH);
		WallHeight whSouth = state.get(WallBlock.WALL_HEIGHT_SOUTH);
		WallHeight whWest = state.get(WallBlock.WALL_HEIGHT_WEST);
		BlockState newState = getReplacementState(state).with(WallBlock.UP, up).with(WallBlock.WALL_HEIGHT_EAST, whEast).with(WallBlock.WALL_HEIGHT_NORTH, whNorth).with(WallBlock.WALL_HEIGHT_SOUTH, whSouth).with(WallBlock.WALL_HEIGHT_WEST, whWest);
		return newState;
	}
	
	public BlockState getReplacementState(BlockState state) {
		return replacements.get(state.getBlock()).getDefaultState();
	}

}
