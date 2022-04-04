package dev.lcy0x1.base.recipe;

import com.google.gson.JsonObject;
import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.item.crafting.SmithingRecipe;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class AbstractSmithingRecipe<T extends AbstractSmithingRecipe<T>> extends SmithingRecipe {

	public AbstractSmithingRecipe(ResourceLocation rl, Ingredient left, Ingredient right, ItemStack result) {
		super(rl, left, right, result);
	}

	@FunctionalInterface
	public interface RecipeFactory<T extends AbstractSmithingRecipe<T>> {

		T create(ResourceLocation rl, Ingredient left, Ingredient right, ItemStack result);

	}

	public static class Serializer<T extends AbstractSmithingRecipe<T>> extends SmithingRecipe.Serializer {

		private final RecipeFactory<T> factory;

		public Serializer(RecipeFactory<T> factory) {
			this.factory = factory;
		}

		public T fromJson(ResourceLocation id, JsonObject obj) {
			SmithingRecipe r = super.fromJson(id, obj);
			return factory.create(r.getId(), r.base, r.addition, r.getResultItem());
		}

		public T fromNetwork(ResourceLocation id, PacketBuffer obj) {
			SmithingRecipe r = super.fromNetwork(id, obj);
			if (r == null) {
				return null;
			}
			return factory.create(r.getId(), r.base, r.addition, r.getResultItem());
		}

	}

}