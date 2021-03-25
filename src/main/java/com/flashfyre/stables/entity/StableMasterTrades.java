package com.flashfyre.stables.entity;

import com.google.common.collect.ImmutableMap;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.minecraft.entity.merchant.villager.VillagerTrades;
import net.minecraft.entity.merchant.villager.VillagerTrades.ITrade;
import net.minecraft.item.Items;

public class StableMasterTrades {
	
	public static final Int2ObjectMap<ITrade[]> STABLE_MASTER_TRADES = get(ImmutableMap.of(1, 
		new VillagerTrades.ITrade[] {
			new VillagerTrades.EmeraldForItemsTrade(Items.COAL, 12, 16, 2),
			new VillagerTrades.EmeraldForItemsTrade(Items.BREAD, 10, 16, 2),
			new VillagerTrades.EmeraldForItemsTrade(Items.BEETROOT, 15, 16, 2),			
			new VillagerTrades.EmeraldForItemsTrade(Items.LEATHER, 6, 16, 2),
			new VillagerTrades.EmeraldForItemsTrade(Items.IRON_INGOT, 5, 16, 2)
		}, 2,
		new VillagerTrades.ITrade[] {
			new VillagerTrades.ItemsForEmeraldsTrade(Items.LEAD, 4, 1, Integer.MAX_VALUE, 10),
			new VillagerTrades.ItemsForEmeraldsTrade(Items.SADDLE, 6, 1, Integer.MAX_VALUE, 10)
			
		}, 3,
		new VillagerTrades.ITrade[] {
			new VillagerTrades.ItemsForEmeraldsTrade(Items.LEATHER_HORSE_ARMOR, 5, 1, Integer.MAX_VALUE, 10),
			new VillagerTrades.ItemsForEmeraldsTrade(Items.IRON_HORSE_ARMOR, 8, 1, Integer.MAX_VALUE, 10),
			new VillagerTrades.ItemsForEmeraldsTrade(Items.GOLDEN_HORSE_ARMOR, 12, 1, Integer.MAX_VALUE, 10),
			new VillagerTrades.ItemsForEmeraldsTrade(Items.DIAMOND_HORSE_ARMOR, 15, 1, Integer.MAX_VALUE, 10)
		}
		));
			
	private static Int2ObjectMap<VillagerTrades.ITrade[]> get(ImmutableMap<Integer, VillagerTrades.ITrade[]> trades) {
		return new Int2ObjectOpenHashMap<>(trades);
	}
}
