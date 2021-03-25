package com.flashfyre.stables.entity;

import com.flashfyre.stables.Stables;

import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.layers.CrossedArmsItemLayer;
import net.minecraft.client.renderer.entity.layers.HeadLayer;
import net.minecraft.client.renderer.entity.model.VillagerModel;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class StableMasterRenderer extends MobRenderer<StableMasterEntity, VillagerModel<StableMasterEntity>> {
	
	private static final ResourceLocation TEXTURE = new ResourceLocation(Stables.MOD_ID, "textures/entity/stable_master.png");

	public StableMasterRenderer(EntityRendererManager renderManagerIn) {
		super(renderManagerIn, new VillagerModel<>(0.0F), 0.5F);
		this.addLayer(new HeadLayer<>(this));
	    this.addLayer(new CrossedArmsItemLayer<>(this));
	}

	@Override
	public ResourceLocation getEntityTexture(StableMasterEntity entity) {
		return TEXTURE;
	}
}
