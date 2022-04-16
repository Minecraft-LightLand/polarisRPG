package org.xkmc.polaris_rpg.init.data;

import com.tterrag.registrate.providers.RegistrateRecipeProvider;
import com.tterrag.registrate.util.DataIngredient;
import dev.lcy0x1.base.recipe.CustomShapelessBuilder;
import net.minecraft.advancements.ICriterionInstance;
import net.minecraft.data.ShapedRecipeBuilder;
import net.minecraft.data.SmithingRecipeBuilder;
import net.minecraft.item.DyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.tags.ITag;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.Tags;
import net.minecraftforge.registries.ForgeRegistries;
import org.xkmc.polaris_rpg.init.registry.PolarisItems;
import org.xkmc.polaris_rpg.init.registry.PolarisRecipeTypes;
import org.xkmc.polaris_rpg.init.registry.SimpleItems;

import java.util.function.BiFunction;

public class RecipeGen {

	public static void genRecipe(RegistrateRecipeProvider pvd) {
		{
			for (int i = 0; i < 16; i++) {
				DyeColor color = DyeColor.values()[i];
				Item wool = ForgeRegistries.ITEMS.getValue(new ResourceLocation(color.getName() + "_wool"));
				Item dye = ForgeRegistries.ITEMS.getValue(new ResourceLocation(color.getName() + "_dye"));
				Item backpack = PolarisItems.BACKPACKS[i].get();

				unlock(pvd, new ShapedRecipeBuilder(backpack, 1)::unlockedBy, backpack)
						.group("backpack_craft").pattern("ADA").pattern("BCB").pattern("ADA")
						.define('A', Tags.Items.LEATHER).define('B', wool)
						.define('C', Items.CHEST).define('D', Items.IRON_INGOT)
						.save(pvd, "polaris_rpg:shaped/craft_backpack_" + color.getName());

				unlock(pvd, new CustomShapelessBuilder<>(PolarisRecipeTypes.RSC_BAG_DYE, backpack, 1)::unlockedBy, backpack)
						.group("backpack_dye").requires(Ingredient.of(PolarisTags.AllItemTags.BACKPACKS.tag))
						.requires(Ingredient.of(dye)).save(pvd, "polaris_rpg:shapeless/dye_backpack_" + color.getName());

				unlock(pvd, new SmithingRecipeBuilder(PolarisRecipeTypes.RSC_BAG_UPGRADE.get(), Ingredient.of(backpack),
						Ingredient.of(SimpleItems.DIM_0.get()), backpack)::unlocks, backpack)
						.save(pvd, "polaris_rpg:smithing/upgrade_backpack_" + color.getName());

				Item storage = PolarisItems.DIMENSIONAL_STORAGE[i].get();

				unlock(pvd, new ShapedRecipeBuilder(storage, 1)::unlockedBy, storage)
						.group("dimensional_storage_craft").pattern("DAD").pattern("ACA").pattern("BAB")
						.define('A', SimpleItems.DIM_0.get()).define('B', wool)
						.define('C', Items.ENDER_CHEST).define('D', Items.POPPED_CHORUS_FRUIT)
						.save(pvd, "polaris_rpg:shaped/craft_storage_" + color.getName());
			}
			Item ender = PolarisItems.ENDER_BACKPACK.get();
			unlock(pvd, new ShapedRecipeBuilder(ender, 1)::unlockedBy, ender)
					.pattern("ADA").pattern("BCB").pattern("ADA")
					.define('A', Tags.Items.LEATHER).define('B', Items.ENDER_PEARL)
					.define('C', Items.ENDER_CHEST).define('D', Items.IRON_INGOT)
					.save(pvd);
		}
		{

			symmetric(pvd, SimpleItems.ROUGH_CLOTH.get(), Items.LEATHER, ItemTags.WOOL, Items.STRING);
			symmetric(pvd, SimpleItems.SMOOTH_CLOTH.get(), SimpleItems.ROUGH_CLOTH.get(), Items.RABBIT_HIDE, Items.STRING);
			symmetric(pvd, SimpleItems.FINE_CLOTH.get(), SimpleItems.SMOOTH_CLOTH.get(), Items.PHANTOM_MEMBRANE, Items.GOLD_NUGGET);

			symmetric(pvd, SimpleItems.DIM_0.get(), Items.EMERALD, Items.ENDER_PEARL, Items.GOLD_NUGGET);
			symmetric(pvd, SimpleItems.DIM_1.get(), SimpleItems.ENDER_EMERALD.get(), SimpleItems.DIM_0.get(), Items.GOLD_NUGGET);
			symmetric(pvd, SimpleItems.DIM_2.get(), SimpleItems.END_SEED.get(), SimpleItems.DIM_1.get(), SimpleItems.SHULKER_HEART.get());

			pvd.singleItem(DataIngredient.items(SimpleItems.WITHER_BONE.get()), SimpleItems.WITHER_BONEMEAL::get, 1, 3);
			pvd.square(DataIngredient.items(SimpleItems.SOUL_REMNANT.get()), SimpleItems.SOUL_ESSENCE::get, true);
			pvd.smelting(DataIngredient.items(SimpleItems.SOUL_ESSENCE.get()), SimpleItems.SOUL_FLAME::get, 100, 2400);
			pvd.blasting(DataIngredient.items(SimpleItems.SOUL_ESSENCE.get()), SimpleItems.SOUL_FLAME::get, 100, 1200);

			//SOUL_INGOT
			//GHAST_CRY
			//SOULGOLD_INGOT
			//TRIPLE_INGOT
			//SHINING_NETHER_STAR
		}


	}

	private static <T> T unlock(RegistrateRecipeProvider pvd, BiFunction<String, ICriterionInstance, T> func, Item item) {
		return func.apply("has_" + pvd.safeName(item), DataIngredient.items(item).getCritereon(pvd));
	}


	private static void symmetric(RegistrateRecipeProvider pvd, Item result, IItemProvider a, ITag<Item> b, IItemProvider c) {
		unlock(pvd, new ShapedRecipeBuilder(result, 1)::unlockedBy, result)
				.pattern("CBC").pattern("BAB").pattern("CBC")
				.define('A', a).define('B', b)
				.define('C', c).save(pvd);
	}

	private static void symmetric(RegistrateRecipeProvider pvd, Item result, IItemProvider a, IItemProvider b, IItemProvider c) {
		unlock(pvd, new ShapedRecipeBuilder(result, 1)::unlockedBy, result)
				.pattern("CBC").pattern("BAB").pattern("CBC")
				.define('A', a).define('B', b)
				.define('C', c).save(pvd);
	}

}
