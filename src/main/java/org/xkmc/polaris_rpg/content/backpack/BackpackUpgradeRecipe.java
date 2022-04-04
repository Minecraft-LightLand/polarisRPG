package org.xkmc.polaris_rpg.content.backpack;

import dev.lcy0x1.base.recipe.AbstractSmithingRecipe;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import org.xkmc.polaris_rpg.init.registry.PolarisRecipeTypes;

public class BackpackUpgradeRecipe extends AbstractSmithingRecipe<BackpackUpgradeRecipe> {

	public BackpackUpgradeRecipe(ResourceLocation rl, Ingredient left, Ingredient right, ItemStack result) {
		super(rl, left, right, result);
	}

	@Override
	public boolean matches(IInventory container, World level) {
		if (!super.matches(container, level)) return false;
		return container.getItem(0).getOrCreateTag().getInt("rows") < 6;
	}

	@Override
	public ItemStack assemble(IInventory container) {
		ItemStack stack = super.assemble(container);
		stack.getOrCreateTag().putInt("rows", Math.max(1, stack.getOrCreateTag().getInt("rows")) + 1);
		return stack;
	}

	@Override
	public IRecipeSerializer<?> getSerializer() {
		return PolarisRecipeTypes.RSC_BAG_UPGRADE.get();
	}
}
