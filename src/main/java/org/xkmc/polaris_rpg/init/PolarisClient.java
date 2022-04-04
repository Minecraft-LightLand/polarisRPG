package org.xkmc.polaris_rpg.init;

import com.tterrag.registrate.util.entry.ItemEntry;
import net.minecraft.item.ItemModelsProperties;
import net.minecraft.util.ResourceLocation;
import org.xkmc.polaris_rpg.content.archer.item.GenericBowItem;
import org.xkmc.polaris_rpg.content.backpack.BackpackItem;
import org.xkmc.polaris_rpg.init.registry.PolarisItems;

import java.util.ArrayList;
import java.util.List;

public class PolarisClient {

	public static final List<GenericBowItem> BOW_LIKE = new ArrayList<>();

	public static void registerItemProperties() {
		for (GenericBowItem bow : BOW_LIKE) {
			ItemModelsProperties.register(bow, new ResourceLocation("pull"), (stack, level, entity) -> entity == null || entity.getUseItem() != stack ? 0.0F : bow.getPullForTime(entity, stack.getUseDuration() - entity.getUseItemRemainingTicks()));
			ItemModelsProperties.register(bow, new ResourceLocation("pulling"), (stack, level, entity) -> entity != null && entity.isUsingItem() && entity.getUseItem() == stack ? 1.0F : 0.0F);
		}
		for (ItemEntry<BackpackItem> entry : PolarisItems.BACKPACKS) {
			ItemModelsProperties.register(entry.get(), new ResourceLocation("open"), BackpackItem::isOpened);
		}
		ItemModelsProperties.register(PolarisItems.ENDER_BACKPACK.get(), new ResourceLocation("open"), (stack, level, entity) -> 0);
	}

}
