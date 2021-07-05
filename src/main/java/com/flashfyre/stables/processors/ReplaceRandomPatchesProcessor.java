package com.flashfyre.stables.processors;

import java.util.Random;
import java.util.stream.IntStream;

import com.flashfyre.stables.StablesProcessors;
import com.flashfyre.stables.util.OpenSimplex2F;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.SharedSeedRandom;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ISeedReader;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.gen.OctavesNoiseGenerator;
import net.minecraft.world.gen.feature.template.IStructureProcessorType;
import net.minecraft.world.gen.feature.template.PlacementSettings;
import net.minecraft.world.gen.feature.template.StructureProcessor;
import net.minecraft.world.gen.feature.template.Template;
import net.minecraft.world.gen.feature.template.Template.BlockInfo;

public class ReplaceRandomPatchesProcessor extends StructureProcessor {
	
	public static OctavesNoiseGenerator octaveNoiseGen;
	public static OpenSimplex2F simplexNoiseGen;
	
	private final float chance;
	public static final Codec<ReplaceRandomPatchesProcessor> CODEC = RecordCodecBuilder.create(builder -> builder.group(			
			Codec.FLOAT.fieldOf("chance").forGetter((processor) -> processor.chance)
		).apply(builder, ReplaceRandomPatchesProcessor::new));
	
	private ReplaceRandomPatchesProcessor(float chance) {
		this.chance = chance;
	}
	
	@Override
	public BlockInfo process(IWorldReader worldView, BlockPos pos, BlockPos blockPos,
			BlockInfo structureBlockInfoLocal, BlockInfo structureBlockInfoWorld, PlacementSettings structurePlacementData, Template template) {
		if(structureBlockInfoWorld.state != Blocks.AIR.getDefaultState()) {
			if(worldView instanceof ISeedReader) {
				long seed = ((ISeedReader) worldView).getSeed();
				octaveNoiseGen = new OctavesNoiseGenerator(new SharedSeedRandom(seed), IntStream.rangeClosed(0, 1));
				simplexNoiseGen = new OpenSimplex2F(seed);
				BlockState newState = null;
				Random r = structurePlacementData.getRandom(structureBlockInfoWorld.pos);
				if(r.nextFloat() < this.chance) {
					//double noiseValue = octaveNoiseGen.getValue(structureBlockInfoWorld.pos.getX() * 0.05D, structureBlockInfoWorld.pos.getY() * 0.05D, structureBlockInfoWorld.pos.getZ() * 0.05, 0.01D, 0.01D, false);
					double scale = 0.075D;
					double noiseValue = simplexNoiseGen.noise3_Classic(structureBlockInfoWorld.pos.getX()*scale, structureBlockInfoWorld.pos.getY()*scale, structureBlockInfoWorld.pos.getZ()*scale);
					
					/*if(noiseValue < 0.1D) {
						newState = Blocks.RED_WOOL.getDefaultState();
					}
					else if(noiseValue < 0.2D) {
						newState = Blocks.ORANGE_WOOL.getDefaultState();
					}
					else if(noiseValue < 0.3D) {
						newState = Blocks.YELLOW_WOOL.getDefaultState();
					}
					else if(noiseValue < 0.4D) {
						newState = Blocks.GREEN_WOOL.getDefaultState();
					}
					else if(noiseValue < 0.5D) {
						newState = Blocks.CYAN_WOOL.getDefaultState();
					}
					else if(noiseValue < 0.6D) {
						newState = Blocks.BLUE_WOOL.getDefaultState();
					}
					else if(noiseValue < 0.7D) {
						newState = Blocks.MAGENTA_WOOL.getDefaultState();
					}
					else {
						newState = Blocks.PURPLE_WOOL.getDefaultState();
					}*/
					
					if(noiseValue > 0.1D) {
						newState = Blocks.RED_WOOL.getDefaultState();
					}
				}
				return newState != null ? new Template.BlockInfo(structureBlockInfoWorld.pos, newState, structureBlockInfoWorld.nbt) : structureBlockInfoWorld;
			}			
		}
		
		return structureBlockInfoWorld;		
	}

	@Override
	protected IStructureProcessorType<?> getType() {
		return StablesProcessors.REPLACE_RANDOM_PATCHES;
	}
}
