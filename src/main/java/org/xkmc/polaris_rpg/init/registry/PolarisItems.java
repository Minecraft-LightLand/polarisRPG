package org.xkmc.polaris_rpg.init.registry;

import com.tterrag.registrate.providers.DataGenContext;
import com.tterrag.registrate.providers.RegistrateItemModelProvider;
import com.tterrag.registrate.util.entry.ItemEntry;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.generators.ItemModelBuilder;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.common.util.NonNullLazy;
import org.xkmc.polaris_rpg.content.archer.feature.FeatureList;
import org.xkmc.polaris_rpg.content.archer.feature.bow.DefaultShootFeature;
import org.xkmc.polaris_rpg.content.archer.item.GenericArrowItem;
import org.xkmc.polaris_rpg.content.archer.item.GenericBowItem;
import org.xkmc.polaris_rpg.init.PolarisRPG;

import java.util.Locale;
import java.util.function.Consumer;

import static org.xkmc.polaris_rpg.init.PolarisRPG.REGISTRATE;

@SuppressWarnings({"rawtype", "unchecked", "unused"})
public class PolarisItems {

	public enum SimpleItem {
		;

		public final ItemEntry<Item> entry;

		SimpleItem() {
			entry = PolarisRPG.REGISTRATE.item(name().toLowerCase(Locale.ROOT), Item::new)
					.defaultModel().defaultLang().register();
		}

		public static void register() {
		}

	}

	public static final ItemEntry<GenericBowItem> STARTER_BOW;
	public static final ItemEntry<GenericArrowItem> STARTER_ARROW;

	static {
		STARTER_BOW = genBow("starter_bow", 600, 0, 0, FeatureList::end);
		STARTER_ARROW = genArrow("starter_arrow", 0, 0, true, FeatureList::end);
	}


	public static ItemEntry<GenericBowItem> genBow(String id, int durability, float damage, int punch, Consumer<FeatureList> consumer) {
		return genBow(id, durability, damage, punch, 20, 3.0f, 20, 0.15f, consumer);
	}

	public static ItemEntry<GenericBowItem> genBow(String id, int durability, float damage, int punch, int pull_time, float speed, Consumer<FeatureList> consumer) {
		return genBow(id, durability, damage, punch, pull_time, speed, pull_time, 0.15f, consumer);
	}

	public static ItemEntry<GenericBowItem> genBow(String id, int durability, float damage, int punch, int pull_time, float speed, int fov_time, float fov, Consumer<FeatureList> consumer) {
		FeatureList features = new FeatureList().add(DefaultShootFeature.INSTANCE);
		consumer.accept(features);
		return REGISTRATE.item(id, p -> new GenericBowItem(p.stacksTo(1).durability(durability),
						new GenericBowItem.BowConfig(damage, punch, pull_time, speed, fov_time, fov, features)))
				.model(PolarisItems::createBowModel).defaultLang().register();
	}

	public static ItemEntry<GenericArrowItem> genArrow(String id, float damage, int punch, boolean is_inf, Consumer<FeatureList> consumer) {
		NonNullLazy<FeatureList> f = NonNullLazy.of(() -> {
			FeatureList features = new FeatureList();
			consumer.accept(features);
			return features;
		});
		return REGISTRATE.item(id, p -> new GenericArrowItem(p, new GenericArrowItem.ArrowConfig(damage, punch, is_inf, f)))
				.defaultModel().defaultLang().register();
	}

	private static final float[] BOW_PULL_VALS = {0, 0.65f, 0.9f};

	public static <T extends GenericBowItem> void createBowModel(DataGenContext<Item, T> ctx, RegistrateItemModelProvider pvd) {
		ItemModelBuilder builder = pvd.withExistingParent(ctx.getName(), "minecraft:bow");
		builder.texture("layer0", "item/" + ctx.getName());
		for (int i = 0; i < 3; i++) {
			String name = ctx.getName() + "_pulling_" + i;
			ItemModelBuilder ret = pvd.getBuilder(name).parent(new ModelFile.UncheckedModelFile("minecraft:item/bow_pulling_" + i));
			ret.texture("layer0", "item/" + name);
			ItemModelBuilder.OverrideBuilder override = builder.override();
			override.predicate(new ResourceLocation("pulling"), 1);
			if (BOW_PULL_VALS[i] > 0)
				override.predicate(new ResourceLocation("pull"), BOW_PULL_VALS[i]);
			override.model(new ModelFile.UncheckedModelFile(PolarisRPG.MODID + ":item/" + name));
		}
	}

	public static void register() {
	}

}
