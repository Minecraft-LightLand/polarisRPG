package org.xkmc.polaris_rpg.init.data;

import com.tterrag.registrate.builders.BlockBuilder;
import com.tterrag.registrate.builders.ItemBuilder;
import com.tterrag.registrate.providers.ProviderType;
import com.tterrag.registrate.util.nullness.NonNullFunction;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.fluid.Fluid;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.FluidTags;
import net.minecraft.tags.ITag;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.ResourceLocation;
import org.xkmc.polaris_rpg.init.PolarisRPG;

import java.util.Locale;
import java.util.function.Function;

import static org.xkmc.polaris_rpg.init.PolarisRPG.REGISTRATE;

public class PolarisTags {

	public static <T> ITag.INamedTag<T> tag(Function<ResourceLocation, ITag.INamedTag<T>> wrapperFactory, String namespace,
											String path) {
		return wrapperFactory.apply(new ResourceLocation(namespace, path));
	}

	public static <T> ITag.INamedTag<T> forgeTag(Function<ResourceLocation, ITag.INamedTag<T>> wrapperFactory, String path) {
		return tag(wrapperFactory, "forge", path);
	}

	public static ITag.INamedTag<Block> forgeBlockTag(String path) {
		return forgeTag(BlockTags::createOptional, path);
	}

	public static ITag.INamedTag<Item> forgeItemTag(String path) {
		return forgeTag(ItemTags::createOptional, path);
	}

	public static ITag.INamedTag<Fluid> forgeFluidTag(String path) {
		return forgeTag(FluidTags::createOptional, path);
	}

	public static <T extends Block, P> NonNullFunction<BlockBuilder<T, P>, ItemBuilder<BlockItem, BlockBuilder<T, P>>> tagBlockAndItem(
			String path) {
		return b -> b.tag(forgeBlockTag(path))
				.item()
				.tag(forgeItemTag(path));
	}

	public enum NameSpace {

		MOD(PolarisRPG.MODID, false, true),
		FORGE("forge"),
		TIC("tconstruct");

		public final String id;
		public final boolean optionalDefault;
		public final boolean alwaysDatagenDefault;

		NameSpace(String id) {
			this(id, true, false);
		}

		NameSpace(String id, boolean optionalDefault, boolean alwaysDatagenDefault) {
			this.id = id;
			this.optionalDefault = optionalDefault;
			this.alwaysDatagenDefault = alwaysDatagenDefault;
		}

	}

	public enum AllBlockTags {
		;

		public final ITag.INamedTag<Block> tag;

		AllBlockTags() {
			this(NameSpace.MOD);
		}

		AllBlockTags(NameSpace namespace) {
			this(namespace, namespace.optionalDefault, namespace.alwaysDatagenDefault);
		}

		AllBlockTags(NameSpace namespace, String path) {
			this(namespace, path, namespace.optionalDefault, namespace.alwaysDatagenDefault);
		}

		AllBlockTags(NameSpace namespace, boolean optional, boolean alwaysDatagen) {
			this(namespace, null, optional, alwaysDatagen);
		}

		AllBlockTags(NameSpace namespace, String path, boolean optional, boolean alwaysDatagen) {
			ResourceLocation id = new ResourceLocation(namespace.id, path == null ? name().toLowerCase(Locale.ROOT) : path);
			if (optional) {
				tag = BlockTags.createOptional(id);
			} else {
				tag = BlockTags.bind(id.toString());
			}
			if (alwaysDatagen) {
				REGISTRATE.addDataGenerator(ProviderType.BLOCK_TAGS, prov -> prov.tag(tag));
			}
		}

		public boolean matches(Block block) {
			return tag.contains(block.getBlock());
		}

		public boolean matches(BlockState state) {
			return matches(state.getBlock());
		}

		public void add(Block... values) {
			REGISTRATE.addDataGenerator(ProviderType.BLOCK_TAGS, prov -> prov.tag(tag)
					.add(values));
		}

		public void includeIn(ITag.INamedTag<Block> parent) {
			REGISTRATE.addDataGenerator(ProviderType.BLOCK_TAGS, prov -> prov.tag(parent)
					.addTag(tag));
		}

		public void includeIn(AllBlockTags parent) {
			includeIn(parent.tag);
		}

		public void includeAll(ITag.INamedTag<Block> child) {
			REGISTRATE.addDataGenerator(ProviderType.BLOCK_TAGS, prov -> prov.tag(tag)
					.addTag(child));
		}

	}

	public enum AllItemTags {
		BACKPACKS,
		DIMENSIONAL_STORAGES
		;

		public final ITag.INamedTag<Item> tag;

		AllItemTags() {
			this(NameSpace.MOD);
		}

		AllItemTags(NameSpace namespace) {
			this(namespace, namespace.optionalDefault, namespace.alwaysDatagenDefault);
		}

		AllItemTags(NameSpace namespace, String path) {
			this(namespace, path, namespace.optionalDefault, namespace.alwaysDatagenDefault);
		}

		AllItemTags(NameSpace namespace, boolean optional, boolean alwaysDatagen) {
			this(namespace, null, optional, alwaysDatagen);
		}

		AllItemTags(NameSpace namespace, String path, boolean optional, boolean alwaysDatagen) {
			ResourceLocation id = new ResourceLocation(namespace.id, path == null ? name().toLowerCase(Locale.ROOT) : path);
			if (optional) {
				tag = ItemTags.createOptional(id);
			} else {
				tag = ItemTags.bind(id.toString());
			}
			if (alwaysDatagen) {
				REGISTRATE.addDataGenerator(ProviderType.ITEM_TAGS, prov -> prov.tag(tag));
			}
		}

		public boolean matches(ItemStack stack) {
			return tag.contains(stack.getItem());
		}

		public void add(Item... values) {
			REGISTRATE.addDataGenerator(ProviderType.ITEM_TAGS, prov -> prov.tag(tag)
					.add(values));
		}

		public void includeIn(ITag.INamedTag<Item> parent) {
			REGISTRATE.addDataGenerator(ProviderType.ITEM_TAGS, prov -> prov.tag(parent)
					.addTag(tag));
		}

		public void includeIn(AllItemTags parent) {
			includeIn(parent.tag);
		}

		public void includeAll(ITag.INamedTag<Item> child) {
			REGISTRATE.addDataGenerator(ProviderType.ITEM_TAGS, prov -> prov.tag(tag)
					.addTag(child));
		}

	}

	public enum AllFluidTags {
		;

		public final ITag.INamedTag<Fluid> tag;

		AllFluidTags() {
			this(NameSpace.MOD);
		}

		AllFluidTags(NameSpace namespace) {
			this(namespace, namespace.optionalDefault, namespace.alwaysDatagenDefault);
		}

		AllFluidTags(NameSpace namespace, String path) {
			this(namespace, path, namespace.optionalDefault, namespace.alwaysDatagenDefault);
		}

		AllFluidTags(NameSpace namespace, boolean optional, boolean alwaysDatagen) {
			this(namespace, null, optional, alwaysDatagen);
		}

		AllFluidTags(NameSpace namespace, String path, boolean optional, boolean alwaysDatagen) {
			ResourceLocation id = new ResourceLocation(namespace.id, path == null ? name().toLowerCase(Locale.ROOT) : path);
			if (optional) {
				tag = FluidTags.createOptional(id);
			} else {
				tag = FluidTags.bind(id.toString());
			}
			if (alwaysDatagen) {
				REGISTRATE.addDataGenerator(ProviderType.FLUID_TAGS, prov -> prov.tag(tag));
			}
		}

		public boolean matches(Fluid fluid) {
			return fluid != null && fluid.is(tag);
		}

		public void add(Fluid... values) {
			REGISTRATE.addDataGenerator(ProviderType.FLUID_TAGS, prov -> prov.tag(tag)
					.add(values));
		}

		public void includeIn(ITag.INamedTag<Fluid> parent) {
			REGISTRATE.addDataGenerator(ProviderType.FLUID_TAGS, prov -> prov.tag(parent)
					.addTag(tag));
		}

		public void includeIn(AllFluidTags parent) {
			includeIn(parent.tag);
		}

		public void includeAll(ITag.INamedTag<Fluid> child) {
			REGISTRATE.addDataGenerator(ProviderType.FLUID_TAGS, prov -> prov.tag(tag)
					.addTag(child));
		}

		private static void loadClass() {
		}

	}

	public static void register() {
		AllFluidTags.loadClass();
	}

}
