package dev.lcy0x1.base.recipe;

import com.google.gson.JsonObject;
import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.item.crafting.ShapedRecipe;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class AbstractShapedRecipe<T extends AbstractShapedRecipe<T>> extends ShapedRecipe {

	public AbstractShapedRecipe(ResourceLocation rl, String group, int w, int h, NonNullList<Ingredient> ingredients, ItemStack result) {
		super(rl, group, w, h, ingredients, result);
	}

	@FunctionalInterface
	public interface RecipeFactory<T extends AbstractShapedRecipe<T>> {

		T create(ResourceLocation rl, String group, int w, int h, NonNullList<Ingredient> ingredients, ItemStack result);

	}

	public static class Serializer<T extends AbstractShapedRecipe<T>> extends ShapedRecipe.Serializer {

		private final RecipeFactory<T> factory;

		public Serializer(RecipeFactory<T> factory) {
			this.factory = factory;
		}

		public T fromJson(ResourceLocation id, JsonObject obj) {
			ShapedRecipe r = super.fromJson(id, obj);
			return factory.create(r.getId(), r.getGroup(), r.getRecipeWidth(), r.getRecipeHeight(), r.getIngredients(), r.getResultItem());
		}

		public T fromNetwork(ResourceLocation id, PacketBuffer obj) {
			ShapedRecipe r = super.fromNetwork(id, obj);
			if (r == null) {
				return null;
			}
			return factory.create(r.getId(), r.getGroup(), r.getRecipeWidth(), r.getRecipeHeight(), r.getIngredients(), r.getResultItem());
		}

	}

}