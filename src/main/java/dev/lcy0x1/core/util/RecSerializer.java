package dev.lcy0x1.core.util;

import com.google.gson.JsonObject;
import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistryEntry;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class RecSerializer<R extends IRecipe<I>, I extends IInventory> extends ForgeRegistryEntry<IRecipeSerializer<?>> implements IRecipeSerializer<R> {

	public final Class<R> cls;

	public RecSerializer(Class<R> cls) {
		this.cls = cls;
	}

	@Override
	public R fromJson(ResourceLocation id, JsonObject json) {
		return Serializer.from(json, cls,
				ExceptionHandler.get(() -> cls.getConstructor(ResourceLocation.class).newInstance(id)));

	}

	@Override
	public R fromNetwork(ResourceLocation id, PacketBuffer buf) {
		return Serializer.from(buf, cls,
				ExceptionHandler.get(() -> cls.getConstructor(ResourceLocation.class).newInstance(id)));
	}

	@Override
	public void toNetwork(PacketBuffer buf, R recipe) {
		Serializer.to(buf, recipe);
	}

}
