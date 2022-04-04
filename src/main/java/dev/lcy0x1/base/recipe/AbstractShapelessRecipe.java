package dev.lcy0x1.base.recipe;

import com.google.common.collect.Lists;
import com.google.gson.JsonObject;
import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.item.crafting.ShapelessRecipe;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class AbstractShapelessRecipe<T extends AbstractShapelessRecipe<T>> extends ShapelessRecipe {

	public AbstractShapelessRecipe(ResourceLocation rl, String group, ItemStack result, NonNullList<Ingredient> ingredients) {
		super(rl, group, result, ingredients);
	}

	public List<ItemStack> getJEIResult() {
		return Lists.newArrayList(getResultItem());
	}

	@FunctionalInterface
	public interface RecipeFactory<T extends AbstractShapelessRecipe<T>> {

		T create(ResourceLocation rl, String group, ItemStack result, NonNullList<Ingredient> ingredients);

	}

	public static class Serializer<T extends AbstractShapelessRecipe<T>> extends ShapelessRecipe.Serializer {

		private final RecipeFactory<T> factory;

		public Serializer(RecipeFactory<T> factory) {
			this.factory = factory;
		}

		public T fromJson(ResourceLocation id, JsonObject obj) {
			ShapelessRecipe r = super.fromJson(id, obj);
			return factory.create(r.getId(), r.getGroup(), r.getResultItem(), r.getIngredients());
		}

		public T fromNetwork(ResourceLocation id, PacketBuffer obj) {
			ShapelessRecipe r = super.fromNetwork(id, obj);
			if (r == null) {
				return null;
			}
			return factory.create(r.getId(), r.getGroup(), r.getResultItem(), r.getIngredients());
		}

	}

}
