package org.xkmc.polaris_rpg.content.backpack;

import dev.lcy0x1.base.recipe.AbstractShapelessRecipe;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import org.xkmc.polaris_rpg.init.data.PolarisTags;
import org.xkmc.polaris_rpg.init.registry.PolarisRecipeTypes;

public class BackpackDyeRecipe extends AbstractShapelessRecipe<BackpackDyeRecipe> {

	public BackpackDyeRecipe(ResourceLocation rl, String group, ItemStack result, NonNullList<Ingredient> ingredients) {
		super(rl, group, result, ingredients);
	}

	@Override
	public ItemStack assemble(CraftingInventory container) {
		ItemStack bag = ItemStack.EMPTY;
		for (int i = 0; i < container.getContainerSize(); i++) {
			if (PolarisTags.AllItemTags.BACKPACKS.matches(container.getItem(i))) {
				bag = container.getItem(i);
			}
		}
		ItemStack stack = super.assemble(container);
		stack.setTag(bag.getTag());
		return stack;
	}

	@Override
	public IRecipeSerializer<?> getSerializer() {
		return PolarisRecipeTypes.RSC_BAG_DYE.get();
	}
}
