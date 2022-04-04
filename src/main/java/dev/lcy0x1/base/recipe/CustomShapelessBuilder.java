package dev.lcy0x1.base.recipe;

import com.tterrag.registrate.providers.RegistrateRecipeProvider;
import com.tterrag.registrate.util.DataIngredient;
import com.tterrag.registrate.util.entry.RegistryEntry;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementRewards;
import net.minecraft.advancements.IRequirementsStrategy;
import net.minecraft.advancements.criterion.RecipeUnlockedTrigger;
import net.minecraft.data.IFinishedRecipe;
import net.minecraft.data.ShapelessRecipeBuilder;
import net.minecraft.item.Item;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.ResourceLocation;

import java.util.List;
import java.util.function.Consumer;

public class CustomShapelessBuilder<T extends AbstractShapelessRecipe<T>> extends ShapelessRecipeBuilder {

	private final RegistryEntry<AbstractShapelessRecipe.Serializer<T>> serializer;

	public CustomShapelessBuilder(RegistryEntry<AbstractShapelessRecipe.Serializer<T>> serializer, IItemProvider result, int count) {
		super(result, count);
		this.serializer = serializer;
	}

	public void save(Consumer<IFinishedRecipe> p_200485_1_, ResourceLocation p_200485_2_) {
		this.ensureValid(p_200485_2_);
		this.advancement.parent(new ResourceLocation("recipes/root")).addCriterion("has_the_recipe", RecipeUnlockedTrigger.unlocked(p_200485_2_)).rewards(AdvancementRewards.Builder.recipe(p_200485_2_)).requirements(IRequirementsStrategy.OR);
		p_200485_1_.accept(new Result(p_200485_2_, this.result, this.count, this.group == null ? "" : this.group, this.ingredients, this.advancement, new ResourceLocation(p_200485_2_.getNamespace(), "recipes/" + this.result.getItemCategory().getRecipeFolderName() + "/" + p_200485_2_.getPath())));
	}



	public CustomShapelessBuilder<T> unlockedBy(RegistrateRecipeProvider pvd, IItemProvider item) {
		this.advancement.addCriterion("has_" + pvd.safeName(item.asItem()),
				DataIngredient.items(item.asItem()).getCritereon(pvd));
		return this;
	}

	class Result extends ShapelessRecipeBuilder.Result {

		public Result(ResourceLocation id, Item out, int count, String group, List<Ingredient> in,
					  Advancement.Builder advancement, ResourceLocation folder) {
			super(id, out, count, group, in, advancement, folder);
		}

		@Override
		public IRecipeSerializer<?> getType() {
			return serializer.get();
		}
	}


}
