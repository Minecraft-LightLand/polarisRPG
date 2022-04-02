package org.xkmc.polaris_rpg.init.registry;

import com.tterrag.registrate.providers.DataGenContext;
import com.tterrag.registrate.providers.RegistrateBlockstateProvider;
import dev.lcy0x1.base.block.LightLandBlock;
import net.minecraft.block.Block;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.generators.ModelFile;
import org.xkmc.polaris_rpg.init.PolarisRPG;

public class PolarisBlocks {

	static {
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
