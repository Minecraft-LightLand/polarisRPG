package dev.lcy0x1.base.block.mult;

import dev.lcy0x1.base.block.type.MultipleBlockMethod;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public interface OnReplacedBlockMethod extends MultipleBlockMethod {
	void onReplaced(BlockState state, World worldIn, BlockPos pos, BlockState newState, boolean isMoving);
}