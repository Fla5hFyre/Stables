package com.flashfyre.stables;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraft.util.RegistryKey;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.FlatChunkGenerator;
import net.minecraft.world.gen.feature.structure.Structure;
import net.minecraft.world.gen.settings.DimensionStructuresSettings;
import net.minecraft.world.gen.settings.StructureSeparationSettings;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.BiomeDictionary.Type;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(Stables.MOD_ID)
public class Stables
{
    public static final String MOD_ID = "stables";
	public static Stables instance;
    public static final Logger LOGGER = LogManager.getLogger();
    		
    public Stables() 
    {
        instance = this;
        
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        StablesStructures.DEFERRED_REGISTRY_STRUCTURE.register(modEventBus);
        modEventBus.addListener(this::setup);

        IEventBus forgeBus = MinecraftForge.EVENT_BUS;
        forgeBus.addListener(EventPriority.NORMAL, this::addDimensionalSpacing);

        forgeBus.addListener(EventPriority.HIGH, this::biomeModification);
    }
    
    public void setup(final FMLCommonSetupEvent event)
    {
        event.enqueueWork(() -> {
        	StablesStructures.setupStructures();
        	StablesConfiguredStructures.registerConfiguredStructures();
        });
    }
    
    public void biomeModification(final BiomeLoadingEvent event) {
    	RegistryKey<Biome> key = RegistryKey.getOrCreateKey(Registry.BIOME_KEY, event.getName());
    	Set<Type> types = BiomeDictionary.getTypes(key);
    	if(types.contains(BiomeDictionary.Type.PLAINS) && !types.contains(BiomeDictionary.Type.HOT) && !types.contains(BiomeDictionary.Type.SNOWY)) {
    		event.getGeneration().getStructures().add(() -> StablesConfiguredStructures.CONFIGURED_LARGE_BARN);
    	}
    }
    
    @SuppressWarnings("resource")
	public void addDimensionalSpacing(final WorldEvent.Load event) {
        if(event.getWorld() instanceof ServerWorld){
            ServerWorld serverWorld = (ServerWorld)event.getWorld();
            
            if (serverWorld.getDimensionKey().equals(World.OVERWORLD)) {
            	if(serverWorld.getChunkProvider().getChunkGenerator() instanceof FlatChunkGenerator) {
            		return;
            	}

            	Map<Structure<?>, StructureSeparationSettings> tempMap = new HashMap<>(serverWorld.getChunkProvider().generator.func_235957_b_().func_236195_a_());
            	tempMap.put(StablesStructures.LARGE_BARN.get(), DimensionStructuresSettings.field_236191_b_.get(StablesStructures.LARGE_BARN.get()));
            	serverWorld.getChunkProvider().generator.func_235957_b_().field_236193_d_ = tempMap;
            }           
        }
   }
}
