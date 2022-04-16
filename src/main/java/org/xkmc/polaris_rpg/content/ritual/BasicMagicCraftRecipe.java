package org.xkmc.polaris_rpg.content.ritual;

import dev.lcy0x1.core.util.SerialClass;
import net.minecraft.util.ResourceLocation;
import org.xkmc.polaris_rpg.init.registry.PolarisRecipeTypes;

@SerialClass
public class BasicMagicCraftRecipe extends AbstractMagicCraftRecipe<BasicMagicCraftRecipe> {

	public BasicMagicCraftRecipe(ResourceLocation id) {
		super(id, PolarisRecipeTypes.RSM_CRAFT.get());
	}
}
