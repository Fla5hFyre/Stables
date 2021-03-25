package com.flashfyre.stables;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.flashfyre.stables.entity.StableMasterEntity;

import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.SpawnEggItem;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.FlatChunkGenerator;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.structure.Structure;
import net.minecraft.world.gen.settings.DimensionStructuresSettings;
import net.minecraft.world.gen.settings.StructureSeparationSettings;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.BiomeDictionary.Type;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLEnvironment;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.IForgeRegistryEntry;

@Mod(Stables.MOD_ID)
public class Stables
{
    public static final String MOD_ID = "stables";
	public static Stables instance;
    public static final Logger LOGGER = LogManager.getLogger();
    public final EntityType<StableMasterEntity> stable_master = EntityType.Builder.create(StableMasterEntity::new, EntityClassification.CREATURE).size(0.6F, 1.95F).trackingRange(10)
    		.build(new ResourceLocation(MOD_ID, "stable_master").toString());
    
    private static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, Stables.MOD_ID);  
    private static final DeferredRegister<Feature<?>> FEATURES = DeferredRegister.create(ForgeRegistries.FEATURES, Stables.MOD_ID);      
    
    public Stables() 
    {
        instance = this;
        
        IEventBus modBus = FMLJavaModLoadingContext.get().getModEventBus();
        IEventBus forgeBus = MinecraftForge.EVENT_BUS;        
        
        Stables.ITEMS.register(modBus);
        StablesStructures.STRUCTURES.register(modBus);
        Stables.FEATURES.register(modBus);
        
        modBus.addListener(this::setup);
        modBus.addGenericListener(EntityType.class, this::onRegisterEntityTypes);
        modBus.addListener(this::createEntityAttributes);
        
        forgeBus.addListener(EventPriority.NORMAL, this::addDimensionalSpacing);
        forgeBus.addListener(EventPriority.HIGH, this::biomeModification);
        
        FEATURES.register("spawn_entity", () -> new SpawnEntityFeature(SpawnEntityFeature.EntityConfig.CODEC));
        ITEMS.register("stable_master_spawn_egg", () -> new SpawnEggItem(this.stable_master, 0x09301b, 0xbd8b72, new Item.Properties().group(ItemGroup.MISC)));
        
        if (FMLEnvironment.dist == Dist.CLIENT)
		{
			ClientEvents.subscribeClientEvents(modBus, forgeBus);
		}
    }
    
    public void setup(final FMLCommonSetupEvent event)
    {
        event.enqueueWork(() -> {
        	StablesProcessors.registerProcessors();
        	StablesStructures.setupStructures();
        	StablesConfiguredStructures.registerConfiguredStructures();        	
        });
    }
    
    public void createEntityAttributes(EntityAttributeCreationEvent event) {
    	event.put(this.stable_master, StableMasterEntity.createAttributes().create());
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
    
    public void onRegisterEntityTypes(RegistryEvent.Register<EntityType<?>> event)
	{
		BiConsumer<String,EntityType<?>> registrator = getRegistrator(event.getRegistry());
		registrator.accept("stable_master", this.stable_master);
	}
    
    public static <T extends IForgeRegistryEntry<T>> BiConsumer<String,T> getRegistrator(IForgeRegistry<T> registry)
	{
		return (name,thing) -> register(name, thing, registry);
	}
    
    public static <T extends IForgeRegistryEntry<T>> void register(String name, T thing, IForgeRegistry<T> registry)
	{
		thing.setRegistryName(new ResourceLocation(MOD_ID, name));
		registry.register(thing);
	}
}
