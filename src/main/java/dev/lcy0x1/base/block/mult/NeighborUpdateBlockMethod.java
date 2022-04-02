package dev.lcy0x1.base.block.mult;

import dev.lcy0x1.base.block.type.MultipleBlockMethod;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public interface NeighborUpdateBlockMethod extends MultipleBlockMethod {

	void neighborChanged(Block self, BlockState state, World world, BlockPos pos, Block nei_block, BlockPos nei_pos, boolean moving);

}
