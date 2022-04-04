package org.xkmc.polaris_rpg.content.archer.entity;

import net.minecraft.client.renderer.entity.ArrowRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.util.ResourceLocation;
import org.xkmc.polaris_rpg.content.archer.item.GenericArrowItem;

public class GenericArrowRenderer extends ArrowRenderer<GenericArrowEntity> {

	public GenericArrowRenderer(EntityRendererManager ctx) {
		super(ctx);
	}

	@SuppressWarnings("ConstantConditions")
	@Override
	public ResourceLocation getTextureLocation(GenericArrowEntity entity) {
		GenericArrowItem arrow = entity.data.arrow.item;
		return new ResourceLocation(arrow.getRegistryName().getNamespace(), "textures/entity/arrow/" + arrow.getRegistryName().getPath() + ".png");
	}

}
