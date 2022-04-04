package org.xkmc.polaris_rpg.init.registry;

import com.tterrag.registrate.providers.DataGenContext;
import com.tterrag.registrate.providers.RegistrateItemModelProvider;
import com.tterrag.registrate.util.entry.ItemEntry;
import net.minecraft.item.DyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.generators.ItemModelBuilder;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.common.util.NonNullLazy;
import org.xkmc.polaris_rpg.content.archer.feature.FeatureList;
import org.xkmc.polaris_rpg.content.archer.arrow.EnderArrowFeature;
import org.xkmc.polaris_rpg.content.archer.arrow.ExplodeArrowFeature;
import org.xkmc.polaris_rpg.content.archer.arrow.NoFallArrowFeature;
import org.xkmc.polaris_rpg.content.archer.bow.DefaultShootFeature;
import org.xkmc.polaris_rpg.content.archer.bow.EnderShootFeature;
import org.xkmc.polaris_rpg.content.archer.bow.GlowTargetAimFeature;
import org.xkmc.polaris_rpg.content.archer.bow.WindBowFeature;
import org.xkmc.polaris_rpg.content.archer.GenericArrowItem;
import org.xkmc.polaris_rpg.content.archer.GenericBowItem;
import org.xkmc.polaris_rpg.content.backpack.BackpackItem;
import org.xkmc.polaris_rpg.content.backpack.EnderBackpackItem;
import org.xkmc.polaris_rpg.content.backpack.WorldChestItem;
import org.xkmc.polaris_rpg.init.PolarisRPG;
import org.xkmc.polaris_rpg.init.data.PolarisTags;

import java.util.function.Consumer;
import java.util.function.Supplier;

import static org.xkmc.polaris_rpg.init.PolarisRPG.REGISTRATE;

@SuppressWarnings({"rawtype", "unchecked", "unused"})
public class PolarisItems {

	public static class Tab extends ItemGroup {

		private final Supplier<ItemEntry> icon;

		public Tab(String id, Supplier<ItemEntry> icon) {
			super(PolarisRPG.MODID + "." + id);
			this.icon = icon;
		}

		@Override
		public ItemStack makeIcon() {
			return icon.get().asStack();
		}
	}

	public static final Tab TAB_MAIN = new Tab("material", () -> PolarisItems.ENDER_BACKPACK);
	//public static final Tab TAB_PROF = new Tab("profession", () -> PolarisItems.STARTER_BOW);
	//public static final Tab TAB_QUEST = new Tab("generated", () -> PolarisItems.GEN_ITEM[0][0]);

	static {
		REGISTRATE.itemGroup(() -> TAB_MAIN);
	}

	public static final ItemEntry<BackpackItem>[] BACKPACKS;
	public static final ItemEntry<WorldChestItem>[] DIMENSIONAL_STORAGE;
	public static final ItemEntry<EnderBackpackItem> ENDER_BACKPACK;
	public static final ItemEntry<GenericBowItem> STARTER_BOW, IRON_BOW, MAGNIFY_BOW, ENDER_AIM_BOW, WIND_BOW;
	public static final ItemEntry<GenericArrowItem> STARTER_ARROW, IRON_ARROW, NO_FALL_ARROW, ENDER_ARROW, TNT_2_ARROW;

	static {
		{
			BACKPACKS = new ItemEntry[16];
			for (int i = 0; i < 16; i++) {
				DyeColor color = DyeColor.values()[i];
				BACKPACKS[i] = REGISTRATE.item("backpack_" + color.getName(), p -> new BackpackItem(color, p.stacksTo(1)))
						.model(PolarisItems::createBackpackModel).tag(PolarisTags.AllItemTags.BACKPACKS.tag)
						.color(() -> () -> (stack, val) -> val == 0 ? -1 : ((BackpackItem) stack.getItem()).color.getMaterialColor().col)
						.defaultLang().register();
			}
			DIMENSIONAL_STORAGE = new ItemEntry[16];
			for (int i = 0; i < 16; i++) {
				DyeColor color = DyeColor.values()[i];
				DIMENSIONAL_STORAGE[i] = REGISTRATE.item("dimensional_storage_" + color.getName(), p -> new WorldChestItem(color, p.stacksTo(1)))
						.model(PolarisItems::createWorldChestModel).tag(PolarisTags.AllItemTags.DIMENSIONAL_STORAGES.tag)
						.color(() -> () -> (stack, val) -> val == 0 ? -1 : ((WorldChestItem) stack.getItem()).color.getMaterialColor().col)
						.defaultLang().register();
			}
			ENDER_BACKPACK = REGISTRATE.item("ender_backpack", EnderBackpackItem::new).model(PolarisItems::createEnderBackpackModel).defaultLang().register();
		}
		{
			STARTER_BOW = genBow("starter_bow", 600, 0, 0, FeatureList::end);
			IRON_BOW = genBow("iron_bow", 1200, 1, 0, 40, 3.9f, FeatureList::end);
			MAGNIFY_BOW = genBow("magnify_bow", 600, 0, 0, 20, 3.0f, 60, 0.9f, e -> e.add(new GlowTargetAimFeature(128)));
			ENDER_AIM_BOW = genBow("ender_aim_bow", 8, -1, 0, e -> e.add(new EnderShootFeature(128)));
			WIND_BOW = genBow("wind_bow", 600, 0, 1, 10, 3.9f, e -> e
					.add(new NoFallArrowFeature(40))
					.add(new WindBowFeature()));

			STARTER_ARROW = genArrow("starter_arrow", 0, 0, true, FeatureList::end);
			IRON_ARROW = genArrow("iron_arrow", 1, 1, false, FeatureList::end);
			NO_FALL_ARROW = genArrow("no_fall_arrow", 0, 0, false, e -> e.add(new NoFallArrowFeature(40)));
			ENDER_ARROW = genArrow("ender_arrow", -1, 0, false, e -> e.add(new EnderArrowFeature()));
			TNT_2_ARROW = genArrow("tnt_arrow_lv2", 0, 0, false, e -> e.add(new ExplodeArrowFeature(4)));
		}
	}


	private static void createBackpackModel(DataGenContext<Item, BackpackItem> ctx, RegistrateItemModelProvider pvd) {
		ItemModelBuilder builder = pvd.withExistingParent(ctx.getName(), "polaris_rpg:backpack");
		builder.override().predicate(new ResourceLocation("open"), 1).model(
				new ModelFile.UncheckedModelFile(PolarisRPG.MODID + ":item/backpack_open"));
	}

	private static void createWorldChestModel(DataGenContext<Item, WorldChestItem> ctx, RegistrateItemModelProvider pvd) {
		ItemModelBuilder builder = pvd.withExistingParent(ctx.getName(), "polaris_rpg:dimensional_storage");
	}

	private static void createEnderBackpackModel(DataGenContext<Item, EnderBackpackItem> ctx, RegistrateItemModelProvider pvd) {
		pvd.withExistingParent("ender_backpack_open", "generated")
				.texture("layer0", "item/ender_backpack_open");
		ItemModelBuilder builder = pvd.withExistingParent("ender_backpack", "generated");
		builder.texture("layer0", "item/ender_backpack");
		builder.override().predicate(new ResourceLocation("open"), 1).model(
				new ModelFile.UncheckedModelFile(PolarisRPG.MODID + ":item/ender_backpack_open"));
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
