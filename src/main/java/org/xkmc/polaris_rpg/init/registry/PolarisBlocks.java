package org.xkmc.polaris_rpg.init.registry;

import com.tterrag.registrate.providers.DataGenContext;
import com.tterrag.registrate.providers.RegistrateBlockstateProvider;
import com.tterrag.registrate.util.entry.BlockEntry;
import com.tterrag.registrate.util.entry.TileEntityEntry;
import dev.lcy0x1.base.block.BlockProxy;
import dev.lcy0x1.base.block.LightLandBlock;
import dev.lcy0x1.base.block.LightLandBlockProperties;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.item.DyeColor;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.generators.ConfiguredModel;
import net.minecraftforge.client.model.generators.ModelFile;
import org.xkmc.polaris_rpg.content.backpack.WorldChestBlock;
import org.xkmc.polaris_rpg.content.backpack.WorldChestBlockEntity;
import org.xkmc.polaris_rpg.content.ritual.RitualCore;
import org.xkmc.polaris_rpg.content.ritual.RitualRenderer;
import org.xkmc.polaris_rpg.content.ritual.RitualSide;
import org.xkmc.polaris_rpg.init.PolarisRPG;

import static org.xkmc.polaris_rpg.init.PolarisRPG.REGISTRATE;

public class PolarisBlocks {

	public static final BlockEntry<LightLandBlock> WORLD_CHEST, RITUAL_CORE, RITUAL_SIDE;

	public static final TileEntityEntry<WorldChestBlockEntity> TE_WORLD_CHEST;
	public static final TileEntityEntry<RitualSide.TE> TE_RITUAL_SIDE;
	public static final TileEntityEntry<RitualCore.TE> TE_RITUAL_CORE;

	static {
		{
			LightLandBlockProperties CHEST = LightLandBlockProperties.copy(Blocks.ENDER_CHEST);
			WORLD_CHEST = REGISTRATE.block("dimensional_storage", p -> LightLandBlock.newBaseBlock(
							CHEST, WorldChestBlock.INSTANCE, WorldChestBlock.TILE_ENTITY_SUPPLIER_BUILDER
					)).blockstate((ctx, pvd) -> {
						for (DyeColor color : DyeColor.values()) {
							pvd.models().cubeAll("dimensional_storage_" + color.getName(), new ResourceLocation(PolarisRPG.MODID,
									"block/dimensional_storage_" + color.getName()));
						}
						pvd.getVariantBuilder(ctx.getEntry()).forAllStates(state ->
								ConfiguredModel.builder().modelFile(new ModelFile.UncheckedModelFile(new ResourceLocation(PolarisRPG.MODID,
												"block/dimensional_storage_" + state.getValue(WorldChestBlock.COLOR).getName())))
										.build());
					}).loot((table, block) -> table.dropOther(block, Blocks.ENDER_CHEST))
					.defaultLang().register();
			TE_WORLD_CHEST = REGISTRATE.tileEntity("dimensional_storage", WorldChestBlockEntity::new)
					.validBlock(WORLD_CHEST).register();
		}
		{
			LightLandBlockProperties PEDESTAL = LightLandBlockProperties.copy(Blocks.STONE).make(e -> e
					.noOcclusion().lightLevel(bs -> bs.getValue(BlockStateProperties.LIT) ? 15 : 7)
					.isRedstoneConductor((a, b, c) -> false));

			RITUAL_CORE = REGISTRATE.block("ritual_core",
							(p) -> LightLandBlock.newBaseBlock(PEDESTAL, RitualCore.ACTIVATE, RitualCore.CLICK,
									BlockProxy.TRIGGER, RitualCore.TILE_ENTITY_SUPPLIER_BUILDER))
					.blockstate((a, b) -> {
					}).defaultLoot().defaultLang().simpleItem().register();
			RITUAL_SIDE = REGISTRATE.block("ritual_side",
							(p) -> LightLandBlock.newBaseBlock(PEDESTAL, RitualCore.CLICK, RitualSide.TILE_ENTITY_SUPPLIER_BUILDER))
					.blockstate((a, b) -> {
					}).defaultLoot().defaultLang().simpleItem().register();

			TE_RITUAL_CORE = REGISTRATE.tileEntity("ritual_core", RitualCore.TE::new)
					.validBlock(RITUAL_CORE).renderer(() -> RitualRenderer::new).register();

			TE_RITUAL_SIDE = REGISTRATE.tileEntity("ritual_side", RitualSide.TE::new)
					.validBlock(RITUAL_SIDE).renderer(() -> RitualRenderer::new).register();

		}
	}

	private static ResourceLocation blockTex(String str) {
		return new ResourceLocation(PolarisRPG.MODID, "block/" + str);
	}

	private static ModelFile four_side(DataGenContext<Block, LightLandBlock> ctx, RegistrateBlockstateProvider pvd, String base, String front, String sufx) {
		return pvd.models().cube(ctx.getName() + sufx,
						blockTex(base + "_bottom"),
						blockTex(base + "_top"),
						blockTex(base + front),
						blockTex(base + "_side"),
						blockTex(base + "_side"),
						blockTex(base + "_side"))
				.texture("particle", blockTex(base + "_top"));
	}

	private static ModelFile six_side(DataGenContext<Block, LightLandBlock> ctx, RegistrateBlockstateProvider pvd, String base, String sufx, String north, String south, String east, String west) {
		return pvd.models().cube(ctx.getName() + sufx,
						blockTex(base + "_bottom"),
						blockTex(base + "_top"),
						blockTex(base + north),
						blockTex(base + south),
						blockTex(base + east),
						blockTex(base + west))
				.texture("particle", blockTex(base + "_top"));
	}

	public static void register() {
	}

}
