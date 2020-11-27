package com.flashfyre.stables.structures;

import org.apache.logging.log4j.Level;

import com.flashfyre.stables.Stables;
import com.mojang.serialization.Codec;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.util.registry.DynamicRegistries;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraft.world.gen.feature.jigsaw.JigsawManager;
import net.minecraft.world.gen.feature.structure.AbstractVillagePiece;
import net.minecraft.world.gen.feature.structure.Structure;
import net.minecraft.world.gen.feature.structure.StructureStart;
import net.minecraft.world.gen.feature.structure.VillageConfig;
import net.minecraft.world.gen.feature.template.TemplateManager;

public class BarnStructure extends Structure<NoFeatureConfig> {

	public BarnStructure(Codec<NoFeatureConfig> codec) {
		super(codec);
	}
	
	@Override
    public  IStartFactory<NoFeatureConfig> getStartFactory() {
        return BarnStructure.Start::new;
    }
	
	@Override
    public GenerationStage.Decoration getDecorationStage() {
        return GenerationStage.Decoration.SURFACE_STRUCTURES;
    }
	
	public static class Start extends StructureStart<NoFeatureConfig>  {
        public Start(Structure<NoFeatureConfig> structureIn, int chunkX, int chunkZ, MutableBoundingBox mutableBoundingBox, int referenceIn, long seedIn) {
            super(structureIn, chunkX, chunkZ, mutableBoundingBox, referenceIn, seedIn);
        }

        @Override
        public void func_230364_a_(DynamicRegistries dynamicRegistryManager, ChunkGenerator chunkGenerator, TemplateManager templateManagerIn, int chunkX, int chunkZ, Biome biomeIn, NoFeatureConfig config) {

            // Turns the chunk coordinates into actual coordinates we can use. (Gets center of that chunk)
            int x = (chunkX << 4) + 7;
            int z = (chunkZ << 4) + 7;
            BlockPos blockpos = new BlockPos(x, 0, z);

            // All a structure has to do is call this method to turn it into a jigsaw based structure!
            JigsawManager.func_242837_a(
                    dynamicRegistryManager,
                    new VillageConfig(() -> dynamicRegistryManager.getRegistry(Registry.JIGSAW_POOL_KEY)
                            .getOrDefault(new ResourceLocation(Stables.MOD_ID, "barn/start_pool")),
                            50),
                    AbstractVillagePiece::new,
                    chunkGenerator,
                    templateManagerIn,
                    blockpos, // Position of the structure. Y value is ignored if last parameter is set to true.
                    this.components, // The list that will be populated with the jigsaw pieces after this method.
                    this.rand,
                    true, // Allow intersecting jigsaw pieces. If false, villages cannot generate houses. I recommend to keep this to true.
                    true); // Place at heightmap (top land). Set this to false for structure to be place at blockpos's y value instead

            //this.components.forEach(piece -> piece.offset(0, 0, 0));
            //this.components.forEach(piece -> piece.getBoundingBox().minY += 1);


            // Sets the bounds of the structure once you are finished.
            this.recalculateStructureSize();

            // I use to debug and quickly find out if the structure is spawning or not and where it is.
            Stables.LOGGER.log(Level.DEBUG, "Barn generated at " + (blockpos.getX()) + " " + blockpos.getY() + " " + (blockpos.getZ()));
        }

    }

}
