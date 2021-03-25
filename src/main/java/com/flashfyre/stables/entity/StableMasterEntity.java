package com.flashfyre.stables.entity;

import net.minecraft.entity.AgeableEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.goal.AvoidEntityGoal;
import net.minecraft.entity.ai.goal.LookAtCustomerGoal;
import net.minecraft.entity.ai.goal.LookAtGoal;
import net.minecraft.entity.ai.goal.LookAtWithoutMovingGoal;
import net.minecraft.entity.ai.goal.MoveTowardsRestrictionGoal;
import net.minecraft.entity.ai.goal.PanicGoal;
import net.minecraft.entity.ai.goal.SwimGoal;
import net.minecraft.entity.ai.goal.TradeWithPlayerGoal;
import net.minecraft.entity.ai.goal.WaterAvoidingRandomWalkingGoal;
import net.minecraft.entity.item.ExperienceOrbEntity;
import net.minecraft.entity.merchant.villager.AbstractVillagerEntity;
import net.minecraft.entity.merchant.villager.VillagerTrades;
import net.minecraft.entity.monster.EvokerEntity;
import net.minecraft.entity.monster.IllusionerEntity;
import net.minecraft.entity.monster.PillagerEntity;
import net.minecraft.entity.monster.VexEntity;
import net.minecraft.entity.monster.VindicatorEntity;
import net.minecraft.entity.monster.ZoglinEntity;
import net.minecraft.entity.monster.ZombieEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.MerchantOffer;
import net.minecraft.item.MerchantOffers;
import net.minecraft.stats.Stats;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

public class StableMasterEntity extends AbstractVillagerEntity {

	public StableMasterEntity(EntityType<? extends AbstractVillagerEntity> type, World worldIn) {
		super(type, worldIn);
	}
	
	public static AttributeModifierMap.MutableAttribute createAttributes()
	{
		return MobEntity.func_233666_p_()
			.createMutableAttribute(Attributes.FOLLOW_RANGE, 16.0D)
			.createMutableAttribute(Attributes.MAX_HEALTH, 20.0D)
			.createMutableAttribute(Attributes.MOVEMENT_SPEED, 0.5F);
	}
	
	@Override
	protected void registerGoals() {
	      this.goalSelector.addGoal(0, new SwimGoal(this));
	      this.goalSelector.addGoal(1, new TradeWithPlayerGoal(this));
	      this.goalSelector.addGoal(1, new AvoidEntityGoal<>(this, ZombieEntity.class, 8.0F, 0.5D, 0.5D));
	      this.goalSelector.addGoal(1, new AvoidEntityGoal<>(this, EvokerEntity.class, 12.0F, 0.5D, 0.5D));
	      this.goalSelector.addGoal(1, new AvoidEntityGoal<>(this, VindicatorEntity.class, 8.0F, 0.5D, 0.5D));
	      this.goalSelector.addGoal(1, new AvoidEntityGoal<>(this, VexEntity.class, 8.0F, 0.5D, 0.5D));
	      this.goalSelector.addGoal(1, new AvoidEntityGoal<>(this, PillagerEntity.class, 15.0F, 0.5D, 0.5D));
	      this.goalSelector.addGoal(1, new AvoidEntityGoal<>(this, IllusionerEntity.class, 12.0F, 0.5D, 0.5D));
	      this.goalSelector.addGoal(1, new AvoidEntityGoal<>(this, ZoglinEntity.class, 10.0F, 0.5D, 0.5D));
	      this.goalSelector.addGoal(1, new PanicGoal(this, 0.5D));
	      this.goalSelector.addGoal(1, new LookAtCustomerGoal(this));
	      this.goalSelector.addGoal(4, new MoveTowardsRestrictionGoal(this, 0.35D));
	      this.goalSelector.addGoal(8, new WaterAvoidingRandomWalkingGoal(this, 0.35D));
	      this.goalSelector.addGoal(9, new LookAtWithoutMovingGoal(this, PlayerEntity.class, 3.0F, 1.0F));
	      this.goalSelector.addGoal(10, new LookAtGoal(this, MobEntity.class, 8.0F));
	}

	@Override
	protected void onVillagerTrade(MerchantOffer offer) {
		if (offer.getDoesRewardExp()) {
			int i = 3 + this.rand.nextInt(4);
			this.world.addEntity(new ExperienceOrbEntity(this.world, this.getPosX(), this.getPosY() + 0.5D, this.getPosZ(), i));
		}
	}

	@Override
	protected void populateTradeData() {
		VillagerTrades.ITrade[] emeraldForItemsTrades = StableMasterTrades.STABLE_MASTER_TRADES.get(1);
		VillagerTrades.ITrade[] itemsForEmeraldsTrades = StableMasterTrades.STABLE_MASTER_TRADES.get(2);
		VillagerTrades.ITrade[] horseArmourForEmeraldsTrades = StableMasterTrades.STABLE_MASTER_TRADES.get(3);
		if (emeraldForItemsTrades != null && itemsForEmeraldsTrades != null) {
			MerchantOffers merchantoffers = this.getOffers();
			this.addTrades(merchantoffers, emeraldForItemsTrades, 9);
			this.addTrades(merchantoffers, itemsForEmeraldsTrades, 9);			
			int i = this.rand.nextInt(horseArmourForEmeraldsTrades.length);
			VillagerTrades.ITrade horseArmourTrade = horseArmourForEmeraldsTrades[i];
			MerchantOffer merchantoffer = horseArmourTrade.getOffer(this, this.rand);
			if (merchantoffer != null) {
				merchantoffers.add(merchantoffer);
	         }
		}
	}

	@Override
	public AgeableEntity func_241840_a(ServerWorld p_241840_1_, AgeableEntity p_241840_2_) {
		return null;
	}
	
	 public ActionResultType func_230254_b_(PlayerEntity player, Hand hand) {
	      ItemStack itemstack = player.getHeldItem(hand);
	      if (itemstack.getItem() != Items.VILLAGER_SPAWN_EGG && this.isAlive() && !this.hasCustomer() && !this.isChild()) {
	         if (hand == Hand.MAIN_HAND) {
	            player.addStat(Stats.TALKED_TO_VILLAGER);
	         }

	         if (this.getOffers().isEmpty()) {
	            return ActionResultType.func_233537_a_(this.world.isRemote);
	         } else {
	            if (!this.world.isRemote) {
	               this.setCustomer(player);
	               this.openMerchantContainer(player, this.getDisplayName(), 1);
	            }

	            return ActionResultType.func_233537_a_(this.world.isRemote);
	         }
	      } else {
	         return super.func_230254_b_(player, hand);
	      }
	   }
	
	@Override
	public boolean hasXPBar() {
		return false;
	}
	
	@Override
	protected SoundEvent getAmbientSound() {
		return this.hasCustomer() ? SoundEvents.ENTITY_VILLAGER_TRADE : SoundEvents.ENTITY_VILLAGER_AMBIENT;
	}
	
	@Override
	public SoundEvent getYesSound() {
		return SoundEvents.ENTITY_VILLAGER_YES;
	}
	
	protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
		return SoundEvents.ENTITY_VILLAGER_HURT;
	}

	protected SoundEvent getDeathSound() {
		return SoundEvents.ENTITY_VILLAGER_DEATH;
	}

	protected SoundEvent getVillagerYesNoSound(boolean getYesSound) {
		return getYesSound ? SoundEvents.ENTITY_VILLAGER_YES : SoundEvents.ENTITY_VILLAGER_NO;
	}
	
	public boolean canDespawn(double distanceToClosestPlayer) {
	      return false;
	}

}
