package org.xkmc.polaris_rpg.init.registry;

import com.tterrag.registrate.util.entry.RegistryEntry;
import dev.lcy0x1.base.recipe.AbstractShapelessRecipe;
import dev.lcy0x1.base.recipe.AbstractSmithingRecipe;
import net.minecraft.item.crafting.IRecipeSerializer;
import org.xkmc.polaris_rpg.content.backpack.BackpackDyeRecipe;
import org.xkmc.polaris_rpg.content.backpack.BackpackUpgradeRecipe;

import static org.xkmc.polaris_rpg.init.PolarisRPG.REGISTRATE;

public class PolarisRecipeTypes {

	public static final RegistryEntry<AbstractShapelessRecipe.Serializer<BackpackDyeRecipe>> RSC_BAG_DYE =
			REGISTRATE.simple("backpack_dye", IRecipeSerializer.class, () -> new AbstractShapelessRecipe.Serializer<>(BackpackDyeRecipe::new));
	public static final RegistryEntry<AbstractSmithingRecipe.Serializer<BackpackUpgradeRecipe>> RSC_BAG_UPGRADE =
			REGISTRATE.simple("backpack_upgrade", IRecipeSerializer.class, () -> new AbstractSmithingRecipe.Serializer<>(BackpackUpgradeRecipe::new));

	public static void register() {
	}
}
