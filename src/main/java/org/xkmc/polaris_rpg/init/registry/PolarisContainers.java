package org.xkmc.polaris_rpg.init.registry;

import com.tterrag.registrate.util.entry.ContainerEntry;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.util.ResourceLocation;
import org.xkmc.polaris_rpg.content.backpack.BackpackContainer;
import org.xkmc.polaris_rpg.content.backpack.BackpackScreen;
import org.xkmc.polaris_rpg.content.backpack.WorldChestContainer;
import org.xkmc.polaris_rpg.content.backpack.WorldChestScreen;

import static org.xkmc.polaris_rpg.init.PolarisRPG.REGISTRATE;

public class PolarisContainers {

	public static final ContainerEntry<BackpackContainer> MT_BACKPACK = REGISTRATE.container("backpack",
			BackpackContainer::fromNetwork, () -> BackpackScreen::new).lang(PolarisContainers::contLang).register();
	public static final ContainerEntry<WorldChestContainer> MT_WORLD_CHEST = REGISTRATE.container("dimensional_storage",
			WorldChestContainer::fromNetwork, () -> WorldChestScreen::new).lang(PolarisContainers::contLang).register();

	public static void register() {
	}

	private static String contLang(ContainerType<?> type) {
		ResourceLocation id = type.getRegistryName();
		return "container." + id.getNamespace() + "." + id.getPath();
	}

}
