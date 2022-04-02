package dev.lcy0x1.base.block;

import dev.lcy0x1.base.block.type.BlockMethod;
import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.block.Block;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class LightLandBlock extends Block {

	public static LightLandBlock newBaseBlock(LightLandBlockProperties p, BlockMethod... impl) {
		return new LightLandBlockImpl(p, impl);
	}

	protected LightLandBlock(Properties props) {
		super(props);
	}

}