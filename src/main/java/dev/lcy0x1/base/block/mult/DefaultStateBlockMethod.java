package dev.lcy0x1.base.block.mult;

import dev.lcy0x1.base.block.type.MultipleBlockMethod;
import net.minecraft.block.BlockState;

public interface DefaultStateBlockMethod extends MultipleBlockMethod {
	BlockState getDefaultState(BlockState state);
}
