package org.xkmc.polaris_rpg.init.registry;

import com.tterrag.registrate.util.entry.RegistryEntry;
import dev.lcy0x1.base.recipe.AbstractShapelessRecipe;
import dev.lcy0x1.base.recipe.AbstractSmithingRecipe;
import dev.lcy0x1.base.recipe.BaseRecipe;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraftforge.fml.RegistryObject;
import org.xkmc.polaris_rpg.content.backpack.BackpackDyeRecipe;
import org.xkmc.polaris_rpg.content.backpack.BackpackUpgradeRecipe;
import org.xkmc.polaris_rpg.content.ritual.AbstractMagicCraftRecipe;
import org.xkmc.polaris_rpg.content.ritual.BasicMagicCraftRecipe;
import org.xkmc.polaris_rpg.content.ritual.RitualCore;
import org.xkmc.polaris_rpg.init.PolarisRPG;

import static org.xkmc.polaris_rpg.init.PolarisRPG.REGISTRATE;

public class PolarisRecipeTypes {

	public static final IRecipeType<AbstractMagicCraftRecipe<?>> RT_CRAFT = IRecipeType.register(PolarisRPG.MODID + ":ritual");

	public static final RegistryEntry<AbstractShapelessRecipe.Serializer<BackpackDyeRecipe>> RSC_BAG_DYE =
			REGISTRATE.simple("backpack_dye", IRecipeSerializer.class, () -> new AbstractShapelessRecipe.Serializer<>(BackpackDyeRecipe::new));
	public static final RegistryEntry<AbstractSmithingRecipe.Serializer<BackpackUpgradeRecipe>> RSC_BAG_UPGRADE =
			REGISTRATE.simple("backpack_upgrade", IRecipeSerializer.class, () -> new AbstractSmithingRecipe.Serializer<>(BackpackUpgradeRecipe::new));

	public static final RegistryEntry<BaseRecipe.RecType<BasicMagicCraftRecipe, AbstractMagicCraftRecipe<?>, RitualCore.Inv>> RSM_CRAFT =
			REGISTRATE.simple("ritual", IRecipeSerializer.class, () -> new BaseRecipe.RecType<>(BasicMagicCraftRecipe.class, RT_CRAFT));

	public static void register() {
	}
}
